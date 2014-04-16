package rtt.ui.launching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchesListener2;

import rtt.ui.RttPluginUI;

public class RTTLaunchesListener implements ILaunchesListener2 {
	
	private List<ILaunch> launches = null;
	
	public RTTLaunchesListener() {
		this.launches = new ArrayList<ILaunch>();
	}

	@Override
	public void launchesRemoved(ILaunch[] launches) {
		System.out.println("Launches removed: " + Arrays.toString(launches));
		
		for (ILaunch launch : launches) {
			if (this.launches.contains(launch)) {
				this.launches.remove(launch);
			}
		}		
	}

	@Override
	public void launchesAdded(ILaunch[] launches) {
		System.out.println("Launches added: " + Arrays.toString(launches));
		
		for (ILaunch launch : launches) {
			if (!this.launches.contains(launch)) {
				this.launches.add(launch);
			}
		}
	}

	@Override
	public void launchesChanged(ILaunch[] launches) {
		// TODO Auto-generated method stub

	}

	@Override
	public void launchesTerminated(ILaunch[] launches) {
		System.out.println("Launches terminated: " + Arrays.toString(launches));
		
		for (ILaunch terminatedLaunch : launches) {
			if (this.launches.contains(terminatedLaunch)) {
				System.out.println("Found launch.");				
				RttPluginUI.getProjectDirectory().needToReload();
			}
		}
	}

}
