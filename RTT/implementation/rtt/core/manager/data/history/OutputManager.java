package rtt.core.manager.data.history;

import java.io.InputStream;
import java.util.List;

import rtt.core.archive.output.Output;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.fetching.OutputDataFetching;
import rtt.core.manager.data.AbstractDataManager;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.results.TestFailure;

public class OutputManager extends AbstractDataManager<Output> {

	OutputDataFetching fetching;

	public OutputManager(ArchiveLoader loader, String path) {
		super(loader);
		
		fetching = new OutputDataFetching(path);
		
		setFetchingStrategy(fetching);
	}

	public void setData(Output data, Integer version) {
		fetching.setVersion(version);
		marshall(Output.class, data);
	}

	public Output getData(Integer version) {
		fetching.setVersion(version);
		try {
			return unmarshall(Output.class);
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
	protected Output doLoad() {
		throw new RuntimeException(
				"Use getData(Version) for loading output");
	}

	@Override
	protected void doSave(Output data) {
		throw new RuntimeException(
				"Use setData(Version) for loading output");
	}

	public static boolean dataEqual(Output oldData, Output newData) {
		if (oldData == newData) {
			return true;
		}
		
		if (oldData.getAST() == newData.getAST()) {
			return true;
		}

		List<TestFailure> failures = OutputCompare
				.compareOutput(oldData, newData, false);
		if (failures != null && failures.isEmpty() == false) {
			return false;
		}

		return true;
	}

	@Override
	protected Output getEmptyData() {
		return new Output();
	}

}
