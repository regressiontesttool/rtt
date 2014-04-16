package rtt.ui.content.configuration;

import rtt.core.archive.Archive;
import rtt.core.archive.configuration.Configuration;
import rtt.core.manager.Manager;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.content.main.ProjectContent;

public class ConfigurationDirectory extends AbstractContent {

	public ConfigurationDirectory(ProjectContent parent) {
		super(parent);
	}
	
	private void loadContents(Archive archive) {
		
		Configuration activeConfig = getProject().getActiveConfiguration();
		Configuration defaultConfig = archive.getDefaultConfiguration();
		
		for (Configuration config : archive.getConfigurations()) {
			ConfigurationContent content = new ConfigurationContent(this, config);

			// set active, when configs have same names
			if (activeConfig != null) {
				content.setActive(config.getName().equals(
						activeConfig.getName()));
			}

			// set default, when configs have same names
			if (defaultConfig != null) {
				content.setDefault(config.getName().equals(
						defaultConfig.getName()));
			}

			childs.add(content);
		}
		
	}
	
	@Override
	public void reload(ReloadInfo info, Manager manager) {
		childs.clear();
		loadContents(manager.getArchive());
	}

	@Override
	public String getText() {
		return "Configurations";
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.CONFIG;
	}

}
