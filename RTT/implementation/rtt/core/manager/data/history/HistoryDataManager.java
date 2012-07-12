package rtt.core.manager.data.history;

import java.util.Calendar;
import java.util.List;

import rtt.core.archive.history.History;
import rtt.core.archive.history.Version;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.fetching.HistoryFileFetching;
import rtt.core.manager.data.AbstractDataManager;

public abstract class HistoryDataManager<T> extends AbstractDataManager<History> implements IHistoryManager {

	protected HistoryFileFetching strategy;
	protected int versionCount = 0;

	public HistoryDataManager(ArchiveLoader loader, HistoryFileFetching strategy) {
		super(loader, strategy);
		this.strategy = strategy;
		try {
			load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		versionCount = data.getVersion().size();
	}

	@Override
	public final History doLoad() {
		History history;
		try {
			history = unmarshall(History.class);
		} catch (Exception e) {
			history = new History();
		}

		return history;
	}
	
	@Override
	public final History getHistory() {
		return data;
	}

	public final Version getVersion(Integer versionNr) {
		List<Version> versions = data.getVersion();

		if (versions != null && versions.isEmpty() == false) {
			for (Version version : versions) {
				if (version.getNr() == versionNr) {
					return version;
				}
			}
		}

		return null;
	}

	@Override
	public final void doSave(History data) {
		marshall(History.class, data);
	}

	public boolean setData(T newData, boolean force) {
		Integer versionNr = versionCount;
		T oldData = loadData(versionNr);

		if (force || (dataEqual(oldData, newData) == false)) {
			versionNr++;
			Version version = new Version();
			version.setNr(versionNr);
			version.setDate(Calendar.getInstance());

			data.getVersion().add(version);
			versionCount = versionNr;

			// CHRISTIAN wirlich schon speichern ?
			saveData(newData, versionNr);

			return true;
		}

		return false;
	}

	public boolean setData(T newData) {
		return setData(newData, false);
	}
	
	public abstract T loadData(Integer version);

	public abstract void saveData(T newData, Integer version);

	public abstract boolean dataEqual(T oldData, T newData);

}
