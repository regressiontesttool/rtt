package rtt.core.manager.data.history;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;

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
import rtt.core.utils.Debug;
import rtt.core.utils.GenerationInformation.GenerationResult;

public class OutputDataManager extends AbstractDataManager<History> implements IHistoryManager {
	
	public enum OutputDataType {

		REFERENCE("ref"),
		TEST("test");
			
		private String path;
			
		private OutputDataType(String path) {
			this.path = path;
		}
			
		public String getPath() {
			return path;
		}
	}

	protected LexerOutputManager lexManager;
	protected ParserOutputManager parManager;
	
	protected String path;
	protected String suiteName;
	protected String caseName;
	
	private InputManager inputManager;

	public OutputDataManager(ArchiveLoader loader, String suiteName,
			String caseName, Configuration config, OutputDataType type) {
		super(loader);

		this.suiteName = suiteName;
		this.caseName = caseName;

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
			Debug.printTrace(e);
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

	public LexerOutput getLexerOutput(Integer version) {
		return lexManager.getData(version);
	}
	
	public ParserOutput getParserOutput(Integer version) {
		return parManager.getData(version);
	}
	
	public InputStream getLexerOutputStream(Integer version) {
		return lexManager.getStreamData(version);
	}
	
	public InputStream getParserOutputStream(Integer version) {
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
	
	public boolean isOutDated(Integer inputVersion) {
		for (Version version : data.getVersion()) {
			if (version.getReference() == inputVersion) {
				return false;
			}
		}
		
		return true;		
	}

	public GenerationResult createData(LexerExecutor lexer, ParserExecutor parser, Integer inputVersion) {
		GenerationResult result = new GenerationResult(suiteName, caseName);

		LexerOutput newLexOut = null;
		ParserOutput newParOut = null;
		
		try {
			Input input = inputManager.getInput(inputVersion);
			
			newLexOut = DataGenerator.generateOutput(input, lexer);
			newParOut = DataGenerator.generateOutput(input, parser);
		} catch (Throwable t) {
			Debug.printTrace(t);
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
			newVersion.setReference(inputVersion);

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
	
	
}
