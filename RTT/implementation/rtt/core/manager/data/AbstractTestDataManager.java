package rtt.core.manager.data;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.history.History;
import rtt.core.archive.history.Version;
import rtt.core.archive.output.LexerOutput;
import rtt.core.archive.output.ParserOutput;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.LoaderUtils;
import rtt.core.loader.fetching.SimpleFileFetching;
import rtt.core.testing.generation.LexerExecuter;
import rtt.core.testing.generation.ParserExecuter;

public abstract class AbstractTestDataManager extends DataManager<History> implements IHistoryManager {
	
	public enum TestDataType {

		REFERENCE("ref"),
		TEST("test");
			
		private String path;
			
		private TestDataType(String path) {
			this.path = path;
		}
			
		public String getPath() {
			return path;
		}
	}

	protected class Reference {
		public Reference(LexerOutput lexOut, ParserOutput parOut, boolean isNew) {
			this.lexOut = lexOut;
			this.parOut = parOut;
			this.isNew = isNew;
		}

		protected Integer refVersion;
		protected LexerOutput lexOut;
		protected ParserOutput parOut;
		protected boolean isNew = false;
	}

	public class GenerationInfo {
		public boolean somethingDone = false;
		public boolean lexerReplaced = false;
		public boolean parserReplaced = false;
		public boolean lexerVersioned = false;
		public boolean parserVersioned = false;
	}

	protected Map<Integer, Reference> dataMap;
	protected LexerOutputManager lexManager;
	protected ParserOutputManager parManager;
	protected Integer lastNr;
	protected String path;
	protected String suiteName;
	protected String caseName;

	public AbstractTestDataManager(ArchiveLoader loader, String suiteName,
			String caseName, Configuration config, String type) {
		super(loader);

		this.suiteName = suiteName;
		this.caseName = caseName;

		path = LoaderUtils.getPath(suiteName, caseName, config.getName(), type);
		setFetchingStrategy(new SimpleFileFetching("history.xml", path));

		lexManager = new LexerOutputManager(loader, LoaderUtils.getPath(path,
				"lexer"));
		parManager = new ParserOutputManager(loader, LoaderUtils.getPath(path,
				"parser"));

		try {
			this.load();
		} catch (Exception e) {
			e.printStackTrace();
		}

		dataMap = new HashMap<Integer, Reference>();

		generateMap(loader);
	}

	private void generateMap(ArchiveLoader loader) {
		List<Version> versionList = data.getVersion();
		if (versionList != null && versionList.isEmpty() == false) {
			for (Version version : versionList) {
				LexerOutput oldLex = lexManager.getData(version.getNr());
				ParserOutput oldPar = parManager.getData(version.getNr());

				Reference ref = new Reference(oldLex, oldPar, false);
				ref.refVersion = version.getReference();

				dataMap.put(version.getNr(), ref);
				lastNr = version.getNr();
			}
		}
	}

	@Override
	public final void doSave(History data) {
		for (Entry<Integer, Reference> entry : dataMap.entrySet()) {
			Reference ref = entry.getValue();

			if (ref.isNew) {
				if (ref.lexOut != null) {
					lexManager.setData(ref.lexOut, entry.getKey());
				}

				if (ref.parOut != null) {
					parManager.setData(ref.parOut, entry.getKey());
				}
			}
		}

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
	public final Integer getCurrentNr() {
		return data.getCurrent();
	}
	
	@Override
	public final History getHistory() {
		return data;
	}
	
	@Override
	public String getSuiteName() {
		return suiteName;
	}
	
	@Override
	public String getCaseName() {
		return caseName;
	}

	public LexerOutput getLexerOutput() {
		return getLexerOutput(data.getCurrent());
	}

	public LexerOutput getLexerOutput(Integer version) {
		Reference ref = dataMap.get(version);

		if (ref != null) {
			return ref.lexOut;
		}

		return null;
	}
	
	public InputStream getLexerOutputStream(Integer version) {
		return lexManager.getStreamData(version);
	}
	
	public InputStream getParserOutputStream(Integer version) {
		return parManager.getStreamData(version);
	}

	public ParserOutput getParserOutput() {
		return getParserOutput(data.getCurrent());
	}

	public ParserOutput getParserOutput(Integer version) {
		Reference ref = dataMap.get(version);

		if (ref != null) {
			return ref.parOut;
		}

		return null;
	}

	@Override
	protected History getEmptyData() {
		return new History();
	}

	public abstract GenerationInfo createData(LexerExecuter lexer, ParserExecuter parser) throws Exception;
	public abstract boolean isUpToDate();
}
