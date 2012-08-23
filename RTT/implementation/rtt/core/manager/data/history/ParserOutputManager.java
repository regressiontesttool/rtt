package rtt.core.manager.data.history;

import java.io.InputStream;
import java.util.List;

import rtt.core.archive.output.ParserOutput;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.fetching.OutputDataFetching;
import rtt.core.loader.fetching.OutputDataFetching.OutputType;
import rtt.core.manager.data.AbstractDataManager;
import rtt.core.testing.compare.ParserOutputCompare;
import rtt.core.testing.compare.results.ParserTestFailure;

public class ParserOutputManager extends AbstractDataManager<ParserOutput> {

	OutputDataFetching fetching;

	public ParserOutputManager(ArchiveLoader loader, String path) {
		super(loader);

		fetching = new OutputDataFetching(path, OutputType.PARSER);
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
				"Use getData(Version) for loading ParserOutput");
	}

	@Override
	protected void doSave(ParserOutput data) {
		throw new RuntimeException(
				"Use setData(Version) for loading ParserOutput");
	}

	public static boolean dataEqual(ParserOutput oldData, ParserOutput newData) {
		if (oldData == newData) {
			return true;
		}
		
		if (oldData.getTree() == newData.getTree()) {
			return true;
		}

		// CHRISTIAN todo
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


// Tree oldT = oldData.getTree().get(0);
// Tree newT = newData.getTree().get(0);
//
// int oldNodeSize = oldT.getNode().size();
// int newNodeSize = newT.getNode().size();
//
// int size = oldNodeSize > newNodeSize ? oldNodeSize : newNodeSize;
//
// for(int i = 0; i < size; i++) {
// Node oldNode = oldT.getNode().get(i);
// Node newNode = newT.getNode().get(i);
//
// if (oldNode.getClassName().equals(newNode.getClassName()) == false) {
// return false;
// }
// }

