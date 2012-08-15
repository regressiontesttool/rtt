package rtt.core.manager.data.history;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Calendar;

import rtt.core.archive.history.History;
import rtt.core.archive.history.Version;
import rtt.core.archive.input.Input;
import rtt.core.exceptions.RTTException;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.LoaderUtils;
import rtt.core.loader.fetching.SimpleFileFetching;
import rtt.core.manager.data.AbstractDataManager;
import rtt.core.utils.Debug;

public class InputManager extends AbstractDataManager<History> implements
		IHistoryManager {

	private String suiteName;
	private String caseName;
	private int versionCount;

	public InputManager(ArchiveLoader loader, String suiteName, String caseName) {
		super(loader, new SimpleFileFetching("history.xml",
				LoaderUtils.getPath(".", suiteName, caseName, "input")));

		this.suiteName = suiteName;
		this.caseName = caseName;

		try {
			load();
		} catch (RTTException e) {
			Debug.printTrace(e);
		}

		versionCount = data.getVersion().size();
	}

	@Override
	public String getSuiteName() {
		return suiteName;
	}

	@Override
	public String getCaseName() {
		return caseName;
	}

	@Override
	protected History getEmptyData() {
		return new History();
	}

	@Override
	protected final History doLoad() {
		History history;
		try {
			history = unmarshall(History.class);
		} catch (Exception e) {
			history = new History();
		}

		return history;
	}

	@Override
	public final void doSave(History data) {
		marshall(History.class, data);
	}

	@Override
	public final History getHistory() {
		return data;
	}

	// public final Version getVersion(Integer versionNr) {
	// List<Version> versions = data.getVersion();
	//
	// if (versions != null && versions.isEmpty() == false) {
	// for (Version version : versions) {
	// if (version.getNr() == versionNr) {
	// return version;
	// }
	// }
	// }
	//
	// return null;
	// }

	public boolean addInput(Input newData, boolean force) {
		Integer versionNr = versionCount;
		Input oldData = getInput(versionNr);

		if (force || (dataEqual(oldData, newData) == false)) {
			versionNr++;
			Version version = new Version();
			version.setNr(versionNr);
			version.setDate(Calendar.getInstance());

			data.getVersion().add(version);
			versionCount = versionNr;

			// CHRISTIAN wirklich schon speichern ?
			try {
				OutputStream os = loader.getOutputStream("input." + versionNr
						+ ".src", strategy.getFolders());
				os.write(newData.getValue().getBytes());

				os.flush();
				os.close();
			} catch (Exception e) {
				Debug.printTrace(e);
			}

			return true;
		}

		return false;
	}

	public Input getInput(Integer version) {
		Input input = new Input();

		InputStream in = loader.getInputStream("input." + version + ".src",
				strategy.getFolders());

		if (in != null) {
			input.setValue(getContent(in));
			try {
				in.close();
			} catch (IOException e) {
				Debug.printTrace(e);
			}

			return input;
		}

		return new Input();
	}

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
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));
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
					Debug.printTrace(e);
				}
			}
			return fileData.toString();
		}

		return "";
	}
}