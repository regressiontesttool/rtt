package rtt.core.manager.data.history;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.history.History;
import rtt.core.archive.history.Version;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.Output;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.LoaderUtils;
import rtt.core.loader.fetching.SimpleFileFetching;
import rtt.core.manager.data.AbstractDataManager;
import rtt.core.testing.generation.DataGenerator;
import rtt.core.testing.generation.Executor;
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

	protected OutputManager outputManager;
	
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
		outputManager = new OutputManager(loader, path);

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
	
	public Output getOutputData(Integer version) {
		return outputManager.getData(version);
	}
	
	public InputStream getOutputDataInputStream(Integer version) {
		return outputManager.getStreamData(version);
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

	public GenerationResult createData(Executor parser, Integer inputVersion, List<String> params) {
		GenerationResult result = new GenerationResult(suiteName, caseName);

		Output newOutput = null;
		
		try {
			Input input = inputManager.getInput(inputVersion);
			
			newOutput = DataGenerator.generateOutput(input, params, parser);
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
		int lastVersion = data.getVersion().size();
		
		if (lastVersion == 0) {
			// previous history data are not available, write new one
			replace = true;
		} else {
			// previous data is available, load and check if data has changed
			Output oldOutput = outputManager.getData(lastVersion);
			replace = isOutDated(inputVersion) || !OutputManager.dataEqual(oldOutput, newOutput);
		}

		if (replace) {
			addVersion(newOutput, inputVersion, lastVersion);			
			result.hasReplaced = true;
		}

		return result;
	}

	private void addVersion(Output newOutput, int inputVersion,
			int lastVersion) {
		
		Version newVersion = new Version();
		newVersion.setDate(Calendar.getInstance());

		int versionNr = lastVersion + 1;
		
		newVersion.setNr(versionNr);
		newVersion.setInputBase(inputVersion);

		data.getVersion().add(newVersion);

		outputManager.setData(newOutput, versionNr);
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
