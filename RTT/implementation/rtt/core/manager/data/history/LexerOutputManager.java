package rtt.core.manager.data.history;

import java.io.InputStream;

import rtt.core.archive.output.LexerOutput;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.fetching.OutputDataFetching;
import rtt.core.loader.fetching.OutputDataFetching.OutputType;
import rtt.core.manager.data.AbstractDataManager;
import rtt.core.testing.compare.LexerOutputCompare;
import rtt.core.testing.compare.results.LexerTestFailure;

public class LexerOutputManager extends AbstractDataManager<LexerOutput> {

	OutputDataFetching fetching;

	public LexerOutputManager(ArchiveLoader loader, String path) {
		super(loader);
		
		fetching = new OutputDataFetching(path, OutputType.LEXER);
		setFetchingStrategy(fetching);
	}

	public void setData(LexerOutput data, Integer version) {
		fetching.setVersion(version);
		marshall(LexerOutput.class, data);
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
		return loader.getInputStream(strategy.getFileName(),
				strategy.getFolders());
	}

	@Override
	protected LexerOutput doLoad() {
		throw new RuntimeException(
				"Use getData(Version) for loading LexerOutput");
	}

	@Override
	protected void doSave(LexerOutput data) {
		throw new RuntimeException(
				"Use setData(Version) for loading LexerOutput");
	}

	public static boolean dataEqual(LexerOutput oldData, LexerOutput newData) {
		if (oldData == newData) {
			return true;
		}
		
		if (oldData.getToken() == newData.getToken()) {
			return true;
		}
		
		// CHRISTIAN todo
		LexerTestFailure failure = LexerOutputCompare.compareLexerOutput(
				newData, oldData, false);
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