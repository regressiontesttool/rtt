package rtt.core.manager.data;

import java.io.InputStream;
import java.util.List;

import rtt.core.archive.output.ParserOutput;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.fetching.OutputFetching;
import rtt.core.loader.fetching.OutputFetching.OutputType;
import rtt.core.testing.compare.ParserOutputCompare;
import rtt.core.testing.compare.results.ParserTestFailure;

public class ParserOutputManager extends DataManager<ParserOutput> {
	
	OutputFetching fetching;
	
	public ParserOutputManager(ArchiveLoader loader, String path) {
		super(loader);
		
		fetching = new OutputFetching(path, OutputType.PARSER);
		setFetchingStrategy(fetching);
	}
	
	public ParserOutput getData(Integer version) {
		fetching.setVersion(version);
		try {
			return unmarshall(ParserOutput.class);
		} catch (Exception e) {
			return null;
		}
	}
	
	public void setData(ParserOutput data, Integer version) {
		fetching.setVersion(version);
		marshall(ParserOutput.class, data);
	}

	@Override
	protected ParserOutput doLoad() {
		throw new RuntimeException("Use getData(Version) for loading ParserOutput");
	}

	@Override
	protected void doSave(ParserOutput data) {
		throw new RuntimeException("Use setData(Version) for loading ParserOutput");
	}
	
	public static boolean dataEqual(ParserOutput oldData, ParserOutput newData) {
		if (oldData == null || oldData.getTree().isEmpty()) {
			return false;
		}
		
		if (newData == null || newData.getTree().isEmpty()) {
			return false;
		}
		
//		CHRISTIAN todo
		List<ParserTestFailure> failures = ParserOutputCompare.compareParserOuput(oldData, newData, false, false);
		if (failures != null && failures.isEmpty() == false) {
			return false;
		}
		
		return true;
	}

	@Override
	protected ParserOutput getEmptyData() {
		return new ParserOutput();
	}

	public InputStream getStreamData(Integer version) {
		fetching.setVersion(version);
		return getInputStream(strategy.getFileName(), strategy.getFolders());
	}
	
}
//	public static class FileFetching extends HistoryFileFetching {
//		
//		public FileFetching(String suiteName, String caseName, String configName, String type) {
//			super(LoaderUtils.getPath(".", suiteName, caseName, configName, type, "parser"));
//		}
//
//		@Override
//		public String getFileName(Integer version) {
//			return "parser." + version + ".xml";
//		}
//	}
//	
//	public ParserOutputManager(ArchiveLoader loader, String suiteName, String caseName,
//			String configName, TestDataType type) {
//		super(loader, new FileFetching(suiteName, caseName, configName, type.getPath()));
//	}
//
//	@Override
//	public ParserOutput loadData(Integer version) {
//		ParserOutput output = (ParserOutput) unmarshall(ParserOutput.class, loader.getInputStream(strategy.getFileName(version), strategy.getFolders()));
//		
//		if (output == null) {
//			output = new ParserOutput();
//		}
//		
//		return output;
//	}
//	
//	@Override
//	public void saveData(ParserOutput newData, Integer version) {
//		marshall(ParserOutput.class, newData, loader.getOutputStream(strategy.getFileName(version), strategy.getFolders()));
//	}
//
	
		
//		Tree oldT = oldData.getTree().get(0);
//		Tree newT = newData.getTree().get(0);
//		
//		int oldNodeSize = oldT.getNode().size();
//		int newNodeSize = newT.getNode().size();
//		
//		int size = oldNodeSize > newNodeSize ? oldNodeSize : newNodeSize;
//		
//		for(int i = 0; i < size; i++) {
//			Node oldNode = oldT.getNode().get(i);
//			Node newNode = newT.getNode().get(i);
//			
//			if (oldNode.getClassName().equals(newNode.getClassName()) == false) {
//				return false;
//			}
//		}
//		
//		return true;
//	}

//	@Override
//	public ParserOutput doLoad() {
//		
//		if (archiveFile.isDirectory()) {
//			archiveFile = new File(archiveFile, path);
//			
//			if (archiveFile.exists()) {
//				return unmarshall(ParserOutput.class, archiveFile);
//			}
//		}
//		
//		return new ParserOutput();
//	}
//	
//	public void setNewData(ParserOutput output) {
//		data = output;
//	}
//
//	@Override
//	public void doSave(ParserOutput data) {
//		marshall(ParserOutput.class, data);
//	}
//
//
//	public boolean equalsTo(ParserOutput newParOut) {
//		// TODO Auto-generated method stub
//		return true;
//	}

//}
