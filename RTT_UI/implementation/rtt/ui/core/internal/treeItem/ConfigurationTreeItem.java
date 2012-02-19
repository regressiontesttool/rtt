package rtt.ui.core.internal.treeItem;

import rtt.ui.core.ITreeItem;
import rtt.ui.core.archive.IConfiguration;

public class ConfigurationTreeItem extends AbstractTreeItem {
	
	private IConfiguration config;

	public ConfigurationTreeItem(ITreeItem parent, IConfiguration config) {
		super(parent);
		
		String name = config.getName();
		if (config.isDefault()) {
			name += " (default)";
		}
		
		if (config.isActive()) {
			name += " (active)";
		}
		
		setName(name);
		setIcon(TreeItemIcon.CONFIG);
		this.config = config;
	}
	
	public IConfiguration getConfiguration() {
		return config;
	}
	
	@Override
	public Object getObject(Class<?> clazz) {
		if (clazz == IConfiguration.class) {
			return config;
		}
		
		return parent.getObject(clazz);
	}

	@Override
	public Object[] getChildren() {
		return new Object[] {
			new SimpleTreeItem(this, "Lexer: " + config.getLexerClass().getName(), TreeItemIcon.LEXER),
			new SimpleTreeItem(this, "Parser: " + config.getParserClass().getName(), TreeItemIcon.PARSER),
			new ClassPathTreeItem(this, config.getClasspathEntries())
		};
	}
}
