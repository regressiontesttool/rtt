package rtt.ui.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;

import rtt.core.archive.testsuite.Testsuite;
import rtt.ui.content.IContent;
import rtt.ui.content.main.EmptyContent;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.main.SimpleTypedContent;
import rtt.ui.content.main.SimpleTypedContent.ContentType;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.model.RttProject;
import rtt.ui.utils.TestsuiteComboViewer;
import rtt.ui.viewer.ContentTreeViewer;

public class TestsuiteView extends AbstractProjectView {
	public TestsuiteView() {
	}

	public static final String ID = "rtt.ui.views.TestsuiteView";

	private ContentTreeViewer contentViewer;
	private TestsuiteComboViewer suiteComboViewer;

	@Override
	public void createContentControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		Group runGroup = new Group(parent, SWT.NONE);
		runGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		runGroup.setText("Generate tests");
		runGroup.setLayout(new GridLayout(2, false));

		Label descriptionLabel = new Label(runGroup, SWT.WRAP);
		descriptionLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 2, 1));
		descriptionLabel
				.setText("Select a test suite from the current project and click \"Generate new tests\" to create new reference data.");

		Label tesuiteLabel = new Label(runGroup, SWT.NONE);
		tesuiteLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		tesuiteLabel.setText("Testsuite:");

		suiteComboViewer = new TestsuiteComboViewer(runGroup, SWT.READ_ONLY);
		Combo suiteCombo = suiteComboViewer.getCombo();
		suiteCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		final Button generateButton = new Button(runGroup, SWT.NONE);
		generateButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 2, 1));
		generateButton.setText("Generate new tests ...");

		Group detailsGroup = new Group(parent, SWT.NONE);
		detailsGroup.setLayout(new GridLayout(1, false));
		detailsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		detailsGroup.setText("Overview - Testsuites");

		contentViewer = new ContentTreeViewer(detailsGroup, SWT.BORDER, this
				.getSite().getPage());
		Tree tree = contentViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		MenuManager menuManager = new MenuManager();
		getSite().setSelectionProvider(contentViewer);
		Menu menu = menuManager.createContextMenu(contentViewer.getControl());
		contentViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, contentViewer);
	}

	protected void loadContent(ProjectContent currentContent) {
		List<IContent> childs = new ArrayList<IContent>();

		if (currentContent != null && currentContent.getProject() != null) {
			RttProject project = currentContent.getProject();
			List<Testsuite> suites = project.getArchive().getTestsuites(false);

			if (suites == null || suites.size() == 0) {
				childs.add(new EmptyContent("No test suites found."));
			} else {
				for (Testsuite testsuite : suites) {
					childs.add(new TestsuiteContent(currentContent, testsuite));
				}
			}
		} else {
			childs.add(new EmptyContent("No test suites found."));
		}

		contentViewer.setInput(new SimpleTypedContent(currentContent,
				ContentType.TESTSUITE_DIRECTORY, childs));
		contentViewer.expandAll();
		
		suiteComboViewer.setInput(childs.toArray());
		suiteComboViewer.getCombo().select(0);
	}

	@Override
	public void setFocus() {
		contentViewer.getControl().setFocus();
	}
}
