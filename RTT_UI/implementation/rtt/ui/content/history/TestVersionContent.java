package rtt.ui.content.history;

import java.io.InputStream;
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
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IWorkbenchPage;

import regression.test.TestPackage;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.history.Version;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.data.OutputDataManager;
import rtt.core.manager.data.IHistoryManager;
import rtt.core.manager.data.OutputDataManager.OutputDataType;
import rtt.ui.RttPluginUI;
import rtt.ui.content.IClickableContent;
import rtt.ui.content.IContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.model.RttProject;

public class TestVersionContent extends AbstractContent implements IClickableContent {

	private Version version;
	private OutputDataManager testManager;
	private OutputDataManager refManager;
	private int refVersion;
	private int testVersion;

	public TestVersionContent(IContent parent, Version version,
			IHistoryManager manager) {
		super(parent);
		this.version = version;
		this.testManager = (OutputDataManager) manager;
		
		RttProject project = parent.getProject();
		
		ArchiveLoader loader = project.getLoader();
		String suiteName = testManager.getSuiteName();
		String caseName = testManager.getCaseName();
		Configuration activeConfig = project.getActiveConfiguration();
		
		refManager = new OutputDataManager(loader, suiteName, caseName, activeConfig, OutputDataType.REFERENCE);
		refVersion = version.getReference();
		testVersion = version.getNr();
	}

	@Override
	public String getText() {
		return "Test (" + version.getNr() + ")";
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.VERSION;
	}

	@Override
	public void doDoubleClick(IWorkbenchPage currentPage) {
		final ResourceSet resourceSet1 = new ResourceSetImpl();
		final ResourceSet resourceSet2 = new ResourceSetImpl();
		
		resourceSet1.getPackageRegistry().put(TestPackage.eNS_URI, TestPackage.eINSTANCE);
		resourceSet2.getPackageRegistry().put(TestPackage.eNS_URI, TestPackage.eINSTANCE);
		
		try {
			InputStream refInput = refManager.getParserOutputStream(refVersion);
			EObject referenceModel = ModelUtils.load(refInput, "reference.rtt", resourceSet1);
			
			InputStream testInput = testManager.getParserOutputStream(testVersion);
			EObject resultModel = ModelUtils.load(testInput, "result.rtt", resourceSet2);
			
			final MatchModel match = MatchService.doMatch(referenceModel, resultModel, null);
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
