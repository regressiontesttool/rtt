package rtt.core.manager.data.history;

import java.io.InputStream;

import rtt.core.archive.output.LexerOutput;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.fetching.OutputFetching;
import rtt.core.loader.fetching.OutputFetching.OutputType;
import rtt.core.manager.data.AbstractDataManager;
import rtt.core.testing.compare.LexerOutputCompare;
import rtt.core.testing.compare.results.LexerTestFailure;

public class LexerOutputManager extends AbstractDataManager<LexerOutput> {
	
	OutputFetching fetching;
	
	public LexerOutputManager(ArchiveLoader loader, String path) {
		super(loader);		
		fetching = new OutputFetching(path, OutputType.LEXER);
		
		setFetchingStrategy(fetching);
	}

	public LexerOutput getData(Integer version) {
		fetching.setVersion(version);
		try {
			return unmarshall(LexerOutput.class);
		} catch (Exception e) {
			return null;
		}
	}
	
	public InputStream getStreamData(Integer version) {
		fetching.setVersion(version);
		return getInputStream(strategy.getFileName(), strategy.getFolders());
	}
	
	public void setData(LexerOutput data, Integer version) {
		fetching.setVersion(version);
		marshall(LexerOutput.class, data);
	}
	
	@Override
	protected LexerOutput doLoad() {
		throw new RuntimeException("Use getData(Version) for loading LexerOutput");
	}

	@Override
	protected void doSave(LexerOutput data) {
		throw new RuntimeException("Use setData(Version) for loading LexerOutput");
	}
	
	public static boolean dataEqual(LexerOutput oldData, LexerOutput newData) {
		if (oldData == null || oldData.getToken().isEmpty()) {
			return false;
		}
		
		if (newData == null || newData.getToken().isEmpty()) {
			return false;
		}
		
//		CHRISTIAN todo
		LexerTestFailure failure = LexerOutputCompare.compareLexerOutput(newData, oldData, false);
		if (failure != null) {
			return false;
		}
		
		return true;
	}

	@Override
	protected LexerOutput getEmptyData() {
		return new LexerOutput();
	}

}