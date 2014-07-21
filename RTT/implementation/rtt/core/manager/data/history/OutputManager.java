package rtt.core.manager.data.history;

import java.io.InputStream;
import java.util.List;

import rtt.core.archive.output.ParserOutput;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.fetching.OutputDataFetching;
import rtt.core.manager.data.AbstractDataManager;
import rtt.core.testing.compare.ParserOutputCompare;
import rtt.core.testing.compare.results.ParserTestFailure;

public class OutputManager extends AbstractDataManager<ParserOutput> {

	OutputDataFetching fetching;

	public OutputManager(ArchiveLoader loader, String path) {
		super(loader);
		
		fetching = new OutputDataFetching(path);
		
		setFetchingStrategy(fetching);
	}

	public void setData(ParserOutput data, Integer version) {
		fetching.setVersion(version);
		marshall(ParserOutput.class, data);
	}

	public ParserOutput getData(Integer version) {
		fetching.setVersion(version);
		try {
			return unmarshall(ParserOutput.class);
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
	protected ParserOutput doLoad() {
		throw new RuntimeException(
				"Use getData(Version) for loading output");
	}

	@Override
	protected void doSave(ParserOutput data) {
		throw new RuntimeException(
				"Use setData(Version) for loading output");
	}

	public static boolean dataEqual(ParserOutput oldData, ParserOutput newData) {
		if (oldData == newData) {
			return true;
		}
		
		if (oldData.getTree() == newData.getTree()) {
			return true;
		}

		List<ParserTestFailure> failures = ParserOutputCompare
				.compareParserOuput(oldData, newData, false, false);
		if (failures != null && failures.isEmpty() == false) {
			return false;
		}

		return true;
	}

	@Override
	protected ParserOutput getEmptyData() {
		return new ParserOutput();
	}

}
