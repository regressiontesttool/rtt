package rtt.core.manager.data.history;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.history.History;
import rtt.core.archive.history.Version;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.LexerOutput;
import rtt.core.archive.output.ParserOutput;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.LoaderUtils;
import rtt.core.loader.fetching.SimpleFileFetching;
import rtt.core.manager.data.AbstractDataManager;
import rtt.core.testing.generation.DataGenerator;
import rtt.core.testing.generation.LexerExecutor;
import rtt.core.testing.generation.ParserExecutor;
import rtt.core.utils.GenerationInformation.GenerationResult;
import rtt.core.utils.RTTLogging;

public class OutputDataManager extends AbstractDataManager<History> implements IHistoryManager {
	
	public enum OutputDataType {

		REFERENCE("ref","Reference"),
		TEST("test", "Test");
			
		private String path;
		private String text;
			
		private OutputDataType(String path, String text) {
			this.path = path;
			this.text = text;
		}
			
		public String getPath() {
			return path;
		}
		
		public String getText() {
			return text;
		}
	}

	protected LexerOutputManager lexManager;
	protected ParserOutputManager parManager;
	
	protected String path;
	protected String suiteName;
	protected String caseName;
	
	private InputManager inputManager;
	private OutputDataType type;

	public OutputDataManager(ArchiveLoader loader, String suiteName,
			String caseName, Configuration config, OutputDataType type) {
		super(loader);

		this.suiteName = suiteName;
		this.caseName = caseName;
		this.type = type;

		path = LoaderUtils.getPath(suiteName, caseName, config.getName(), type.getPath());
		setFetchingStrategy(new SimpleFileFetching("history.xml", path));

		inputManager = new InputManager(loader, suiteName, caseName);
		
		lexManager = new LexerOutputManager(loader, LoaderUtils.getPath(path,
				"lexer"));
		parManager = new ParserOutputManager(loader, LoaderUtils.getPath(path,
				"parser"));

		try {
			this.load();
		} catch (Exception e) {
			RTTLogging.trace("Could not load output data history", e);
		}
	}
	
	@Override
	public final void doSave(History data) {
		marshall(History.class, data);
	}

	@Override
	public final History doLoad() {
		History h;
		try {
			h = unmarshall(History.class);
		} catch (Exception e) {
			h = new History();
		}

		return h;
	}
	
	@Override
	public String getSuiteName() {
		return suiteName;
	}
	
	@Override
	public String getCaseName() {
		return caseName;
	}
	
	public OutputDataType getType() {
		return type;
	}

	public LexerOutput getLexerOutput(Integer version) {
		return lexManager.getData(version);
	}
	
	public ParserOutput getParserOutput(Integer version) {
		return parManager.getData(version);
	}
	
	public InputStream getLexerInputStream(Integer version) {
		return lexManager.getStreamData(version);
	}
	
	public InputStream getParserInputStream(Integer version) {
		return parManager.getStreamData(version);
	}
	
	@Override
	public History getHistory() {
		return data;
	}	

	@Override
	protected History getEmptyData() {
		return new History();
	}
	
	public boolean isOutDated(Integer latestInputID) {
		for (Version version : data.getVersion()) {
			if (version.getInputBase().equals(latestInputID)) {
				return false;
			}
		}
		
		return true;		
	}

	public GenerationResult createData(LexerExecutor lexer, ParserExecutor parser, Integer inputVersion, List<String> params) {
		GenerationResult result = new GenerationResult(suiteName, caseName);

		LexerOutput newLexOut = null;
		ParserOutput newParOut = null;
		
		try {
			Input input = inputManager.getInput(inputVersion);
			
			newLexOut = DataGenerator.generateOutput(input, params, lexer);
			newParOut = DataGenerator.generateOutput(input, params, parser);
		} catch (Throwable t) {
			RTTLogging.trace("Could not create output data", t);
			if (t instanceof InvocationTargetException) {
				t = t.getCause();
			}			
			result.exception = t;
			
			return result;
		}

		result.noError = true;

		boolean replace = false;
		int lastVersion = 0;

		if (data.getVersion() == null || data.getVersion().isEmpty()) {
			// previous history data are not available, write new one
			replace = true;
		} else {
			// previous data is available, load and check if data has changed
			lastVersion = data.getVersion().size();
			
			LexerOutput oldLexOut = lexManager.getData(lastVersion);
			ParserOutput oldParOut = parManager.getData(lastVersion);			
			
			if (isOutDated(inputVersion)) {
				// input is newer, than reference data -> replace reference
				replace = true;
			} else {
				
				boolean lexerChanged = !LexerOutputManager.dataEqual(oldLexOut, newLexOut);
				boolean parserChanged = !ParserOutputManager.dataEqual(oldParOut, newParOut); 

				if (lexerChanged || parserChanged) {				
					// data has really changed -> replace
					replace = true;
				}
			}			
		}

		if (replace) {
			Version newVersion = new Version();
			newVersion.setDate(Calendar.getInstance());

			lastVersion++;

			newVersion.setNr(lastVersion);
			newVersion.setInputBase(inputVersion);

			data.getVersion().add(newVersion);

			lexManager.setData(newLexOut, lastVersion);
			parManager.setData(newParOut, lastVersion);
			
			result.hasReplaced = true;
		}

		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OutputDataManager) {
			OutputDataManager manager = (OutputDataManager) obj;
			
			if (!manager.suiteName.equals(this.suiteName)) {
				return false;
			}
			
			if (!manager.caseName.equals(this.caseName)) {
				return false;
			}
			
			if (manager.loader != this.loader) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
	
}
