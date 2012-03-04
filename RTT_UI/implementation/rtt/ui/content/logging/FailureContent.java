package rtt.ui.content.logging;

import java.io.InputStream;
import java.util.Calendar;

import org.eclipse.compare.CompareUI;
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
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchPage;

import regression.test.TestPackage;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.logging.Failure;
import rtt.core.archive.logging.FailureType;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.data.ReferenceManager;
import rtt.core.manager.data.TestManager;
import rtt.ui.content.IClickableContent;
import rtt.ui.content.IColumnableContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.model.RttProject;

public class FailureContent extends AbstractContent implements IClickableContent, IColumnableContent {
	
	private String suiteName;
	private String caseName;
	
	private ReferenceManager refManager;
	private TestManager testManager;
	private Integer testVersion;
	private Integer refVersion;
	
	private Failure failure;
	
	public FailureContent(TestResultContent parent, Failure failure) {
		super(parent);
		
		this.failure = failure;		
		
		suiteName = parent.getSuiteName();
		caseName = parent.getCaseName();
		refVersion = parent.getRefVersion();
		testVersion = parent.getTestVersion();
	}

	@Override
	public String getText() {
		if (failure.getType() == FailureType.LEXER) {
			return "Lexer results";
		}
		
		if (failure.getType() == FailureType.PARSER) {
			return "Parser results";
		}
		
		return "Unknown error";
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.FAILED;
	}

	@Override
	public void doDoubleClick(IWorkbenchPage currentPage) {
		RttProject project = this.getProject();
		
		if (refManager == null || testManager == null) {
			ArchiveLoader loader = project.getLoader();
			Configuration activeConfig = project.getActiveConfiguration();
			
			refManager = new ReferenceManager(loader, suiteName, caseName, activeConfig);
			testManager = new TestManager(loader, suiteName, caseName, activeConfig);
		}				
		
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
			e.printStackTrace();
		}
	}

	@Override
	public String getText(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return failure.getType().toString();

		case 1:
			return failure.getMsg() + " - " + failure.getPath();

		default:
			return "";
		}
	}

	@Override
	public Image getImage(int columnIndex, LocalResourceManager resourceManager) {
		if (columnIndex == 0) {
			return getImage(resourceManager);
		}
		
		return null;
	}

}
