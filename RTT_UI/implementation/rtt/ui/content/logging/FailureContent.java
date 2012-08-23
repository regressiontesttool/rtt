package rtt.ui.content.logging;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.compare.CompareUI;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.MatchOptions;
import org.eclipse.emf.compare.match.engine.IMatchScope;
import org.eclipse.emf.compare.match.engine.IMatchScopeProvider;
import org.eclipse.emf.compare.match.filter.IResourceFilter;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.ui.editor.ModelCompareEditorInput;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchPage;

import regression.test.Attribute;
import regression.test.TestPackage;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.logging.Failure;
import rtt.core.archive.logging.FailureType;
import rtt.core.archive.logging.Result;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.data.history.OutputDataManager;
import rtt.core.manager.data.history.OutputDataManager.OutputDataType;
import rtt.ui.content.IClickableContent;
import rtt.ui.content.IColumnableContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.model.RttProject;

public class FailureContent extends AbstractContent implements IClickableContent, IColumnableContent {
	
	private static class MyMatchScope implements IMatchScope {

		@Override
		public boolean isInScope(EObject eObject) {
			if (eObject instanceof Attribute) {
				Attribute attribute = (Attribute) eObject;
				if (attribute.isInformational()) {

//					System.out.println("isInScope:" + eObject);
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean isInScope(Resource resource) {
			return true;
		}
		
	}
	
	private static class MyScopeProvider implements IMatchScopeProvider {

		@Override
		public IMatchScope getLeftScope() {
			return new MyMatchScope();
		}

		@Override
		public IMatchScope getRightScope() {
			return new MyMatchScope();
		}

		@Override
		public IMatchScope getAncestorScope() {
			return new MyMatchScope();
		}

		@Override
		public void applyResourceFilter(IResourceFilter filter) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private String suiteName;
	private String caseName;
	
	private OutputDataManager refManager;
	private OutputDataManager testManager;
	private Integer testVersion;
	private Integer refVersion;
	
	private Failure failure;
	
	public FailureContent(TestResultContent parent, Failure failure) {
		super(parent);
		
		this.failure = failure;
		
		Result result = parent.getTestresult();
		
		suiteName = result.getTestsuite();
		caseName = result.getTestcase();
		refVersion = result.getRefVersion();
		testVersion = result.getTestVersion();
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
	
	private InputStream getInputStream(OutputDataManager manager, Integer version) {
		switch (failure.getType()) {
		case LEXER:
			return manager.getLexerOutputStream(version);
			
		case PARSER:
			return manager.getParserOutputStream(version);
		}

		throw new RuntimeException("Failure type of test run unknown: " + failure.getType().toString());
	}

	@Override
	public void doDoubleClick(IWorkbenchPage currentPage) {
		RttProject project = this.getProject();
		
		if (refManager == null || testManager == null) {
			ArchiveLoader loader = project.getLoader();
			Configuration activeConfig = project.getActiveConfiguration();
			
			refManager = new OutputDataManager(loader, suiteName, caseName, activeConfig, OutputDataType.REFERENCE);
			testManager = new OutputDataManager(loader, suiteName, caseName, activeConfig, OutputDataType.TEST);
		}				
		
		final ResourceSet resourceSet1 = new ResourceSetImpl();
		final ResourceSet resourceSet2 = new ResourceSetImpl();
		
		resourceSet1.getPackageRegistry().put(TestPackage.eNS_URI, TestPackage.eINSTANCE);
		resourceSet2.getPackageRegistry().put(TestPackage.eNS_URI, TestPackage.eINSTANCE);
		
		try {
			InputStream refInput = getInputStream(refManager, refVersion);
			EObject referenceModel = ModelUtils.load(refInput, "reference_data.rtt", resourceSet1);
			
			InputStream testInput = getInputStream(testManager, testVersion);
			EObject resultModel = ModelUtils.load(testInput, "test_data.rtt", resourceSet2);
			
			Map<String, Object> options = new HashMap<String, Object>();
			options.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, new MyScopeProvider());
			
			final MatchModel match = MatchService.doMatch(resultModel, referenceModel, options);
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
