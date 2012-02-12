package rtt.core.manager.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import rtt.core.archive.history.History;
import rtt.core.archive.input.Input;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.LoaderUtils;
import rtt.core.loader.fetching.HistoryFileFetching;
import rtt.core.utils.DebugLog;

public class InputManager extends HistoryDataManager<Input> {
	
	public static class FileFetching extends HistoryFileFetching {
		
		public FileFetching(String suiteName, String caseName) {
			super(LoaderUtils.getPath(".", suiteName, caseName, "input"));
		}

		@Override
		public String getFileName(Integer version) {
			return "input." + version + ".src";
		}
	}
	
	private String suiteName;
	private String caseName;

	public InputManager(ArchiveLoader loader, String suiteName, String caseName) {
		super(loader, new FileFetching(suiteName, caseName));
		
		this.suiteName = suiteName;
		this.caseName = caseName;
	}
	
	@Override
	public Input loadData(Integer version) {
		Input input = new Input();
		
		InputStream in = getInputStream(strategy.getFileName(version), strategy.getFolders());
		
		if (in != null) {
			input.setValue(getContent(in));
			try {
				in.close();
			} catch (IOException e) {
				DebugLog.printTrace(e);
			}
			
			return input;
		}
		
		return new Input();	
	}
	
	@Override
	public void saveData(Input data, Integer version) {
		try {
			OutputStream os = getOutputStream(strategy.getFileName(version), strategy.getFolders());
			os.write(data.getValue().getBytes());
			
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean dataEqual(Input oldData, Input newData) {
		if (oldData == null || oldData.getValue() == null) {
			return false;
		}
		
		if (newData == null || newData.getValue() == null) {
			return false;
		}
		
		return oldData.getValue().equals(newData.getValue());
	}

	public static String getContent(InputStream input) {
		if (input != null) {
			StringBuffer fileData = new StringBuffer(1000);
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			try {
				char[] buf = new char[1024];

				int numRead = 0;
				while ((numRead = reader.read(buf)) != -1) {
					String readData = String.valueOf(buf, 0, numRead);
					fileData.append(readData);
					buf = new char[1024];
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return fileData.toString();
		}	
		
		return "";
	}

	@Override
	protected History getEmptyData() {
		return new History();
	}

	@Override
	public String getSuiteName() {
		return suiteName;
	}

	@Override
	public String getCaseName() {
		return caseName;
	}
}
