package rtt.ui.content.main;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import rtt.ui.content.IContent;

public class ProjectDirectoryContent extends AbstractContent {
	
	private Map<String, ProjectContent> projectMap;

	public ProjectDirectoryContent(Collection<ProjectContent> projects) {
		super(null);
		projectMap = new TreeMap<String, ProjectContent>();
		childs = null;
		
		for (ProjectContent projectContent : projects) {
			addProject(projectContent);
		}
	}
	
	@Override
	public IContent[] getChildren() {
		Collection<ProjectContent> childs = projectMap.values();
		return childs.toArray(new IContent[childs.size()]);
	}
	
	public void addProject(ProjectContent projectContent) {
		projectMap.put(projectContent.getText(), projectContent);
	}

	@Override
	public String getText() {
		return "ProjectDirectory";
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.PLACEHOLDER;
	}

}
