package rtt.ui.content.history;

import java.util.Calendar;

import org.eclipse.compare.CompareUI;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.ui.editor.ModelCompareEditorInput;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IWorkbenchPage;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.history.Version;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.data.IHistoryManager;
import rtt.core.manager.data.OutputDataManager;
import rtt.core.manager.data.OutputDataManager.OutputDataType;
import rtt.ui.RttPluginUI;
import rtt.ui.RttPluginUtil;
import rtt.ui.content.IClickableContent;
import rtt.ui.content.IContent;
import rtt.ui.model.RttProject;

public class TestVersionContent extends AbstractVersionContent<OutputDataManager> implements IClickableContent {

	private OutputDataManager refManager;
	private int refVersion;
	private int testVersion;

	public TestVersionContent(IContent parent, Version version,
			IHistoryManager manager) {
		super(parent, version, (OutputDataManager) manager);
		
		RttProject project = parent.getProject();
		
		ArchiveLoader loader = project.getLoader();
		String suiteName = manager.getSuiteName();
		String caseName = manager.getCaseName();
		Configuration activeConfig = project.getActiveConfiguration();
		
		refManager = new OutputDataManager(loader, suiteName, caseName, activeConfig, OutputDataType.REFERENCE);
		refVersion = version.getReference();
		testVersion = version.getNr();
		
		calendar = version.getDate();
	}

	@Override
	public String getText() {
		return "Test";
	}

	@Override
	public void doDoubleClick(IWorkbenchPage currentPage) {		
		try {
			ResourceSet refResourceSet = RttPluginUtil.createResourceSet(refManager.getParserOutputStream(refVersion), "reference.rtt");
			Resource refResource = refResourceSet.getResources().get(0);
			
			ResourceSet testResourceSet = RttPluginUtil.createResourceSet(refManager.getParserOutputStream(testVersion), "test.rtt");
			Resource testResource = testResourceSet.getResources().get(0);
			
			final MatchModel match = MatchService.doMatch(refResource.getContents().get(0), testResource.getContents().get(0), null);
			final DiffModel diff = DiffService.doDiff(match, false);
			
			final ComparisonResourceSnapshot snapshot = DiffFactory.eINSTANCE.createComparisonResourceSnapshot();
			snapshot.setDate(Calendar.getInstance().getTime());
			snapshot.setMatch(match);
			snapshot.setDiff(diff);
			
			ModelCompareEditorInput input = new ModelCompareEditorInput(snapshot);
			CompareUI.openCompareEditorOnPage(input, currentPage);
		} catch (Exception e) {
			ErrorDialog.openError(currentPage.getActivePart().getSite()
					.getShell(), "Error", "Could not open editor", new Status(
					Status.ERROR, RttPluginUI.PLUGIN_ID, e.getMessage(), e));
		}
	}

}
