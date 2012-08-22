package rtt.ui.content.configuration;

import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.model.RttProject;

public class ConfigurationDirectory extends AbstractContent {

	public ConfigurationDirectory(ProjectContent parent) {
		super(parent);
		loadContents();
	}
	
	private void loadContents() {
		RttProject project = getProject();
		List<Configuration> configs = project.getConfigurations();
		
		Configuration activeConfig = project.getActiveConfiguration();
		Configuration defaultConfig = project.getDefaultConfiguration();				
		
		if (configs != null) {
			for (Configuration config : configs) {
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
	}
	
	@Override
	public void reload(ReloadInfo info) {
		if (info.contains(Content.CONFIGURATION)) {
			childs.clear();
			loadContents();			
		}
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
