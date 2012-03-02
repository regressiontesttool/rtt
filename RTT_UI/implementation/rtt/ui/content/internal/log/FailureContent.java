package rtt.ui.content.internal.log;

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
import org.eclipse.ui.IWorkbenchPage;

import regression.test.TestPackage;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.history.Version;
import rtt.core.archive.logging.Failure;
import rtt.core.archive.logging.FailureType;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.data.ReferenceManager;
import rtt.core.manager.data.TestManager;
import rtt.ui.content.AbstractContent;
import rtt.ui.content.ContentIcon;
import rtt.ui.content.IClickableContent;
import rtt.ui.content.IContent;
import rtt.ui.model.RttProject;

public class FailureContent extends AbstractContent implements IContent, IClickableContent {

	private String text;
//	private String path;
//	private String message;
	
	private ReferenceManager refManager;
	private TestManager testManager;
	private Integer testVersion;
	private Integer refVersion;
	
	public FailureContent(TestResultContent parent, Failure failure) {
		super(parent);
		
		this.testVersion = parent.getTestVersion();
		
		if 	(failure.getType() == FailureType.LEXER) {
			this.text = "Lexer results";
		} else if (failure.getType() == FailureType.PARSER){
			this.text = "Parser results";
		} else {
			this.text = "Unknown error";
		}
		
		RttProject project = parent.getProject();
		
		ArchiveLoader loader = project.getLoader();
		String suiteName = parent.getSuiteName();
		String caseName = parent.getCaseName();
		Configuration activeConfig = project.getActiveConfiguration();
		
		refManager = new ReferenceManager(loader, suiteName, caseName, activeConfig);
		testManager = new TestManager(loader, suiteName, caseName, activeConfig);
		
		for (Version version : testManager.getHistory().getVersion()) {
			if (version.getNr() == parent.getTestVersion()) {
				this.testVersion = parent.getTestVersion();
				this.refVersion = version.getReference();
			}
		}
		
//		this.message = failure.getMsg();
//		this.path = failure.getPath();
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.FAILED;
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
			e.printStackTrace();
		}
	}

}
