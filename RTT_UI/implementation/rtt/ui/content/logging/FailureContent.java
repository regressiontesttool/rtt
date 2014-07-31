package rtt.ui.content.logging;

import java.io.InputStream;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.editor.ComparisonScopeEditorInput;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.ui.IWorkbenchPage;

import rtt.core.archive.Archive;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.logging.Failure;
import rtt.core.archive.logging.Result;
import rtt.core.exceptions.RTTException;
import rtt.core.manager.Manager;
import rtt.core.manager.data.history.OutputDataManager;
import rtt.core.manager.data.history.OutputDataManager.OutputDataType;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.ecore.util.Messages;
import rtt.ui.model.RttProject;
import rtt.ui.utils.RttLog;
import rtt.ui.utils.RttPluginUtil;

public class FailureContent extends AbstractLogContent {
	
	private static final String ERROR_MESSAGE = 
			"content.failure.error.message";
	
	private String suiteName;
	private String caseName;
	
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
		return "Parser Results";
	}

	@Override
	public ContentIcon getIcon() {
		return ContentIcon.FAILED;
	}
	
	private InputStream getInputStream(OutputDataManager manager, Integer version) {
		return manager.getOutputDataInputStream(version);
	}

	@Override
	public void doDoubleClick(IWorkbenchPage currentPage) {
		RttProject project = this.getProject();
		Configuration activeConfig = project.getActiveConfiguration();
		
		try {
			Manager manager = project.getManager();
			if (manager != null) {
				Archive archive = manager.getArchive();
				
				final ResourceSet left = new ResourceSetImpl();
				final ResourceSet right = new ResourceSetImpl();
				
				OutputDataManager refManager = new OutputDataManager(archive.getLoader(), suiteName, caseName, activeConfig, OutputDataType.REFERENCE);
				OutputDataManager testManager = new OutputDataManager(archive.getLoader(), suiteName, caseName, activeConfig, OutputDataType.TEST);		
				
				RttPluginUtil.loadResource(right, URI.createURI("reference_data.rtt"), getInputStream(refManager, refVersion));				
				RttPluginUtil.loadResource(left, URI.createURI("test_data.rtt"), getInputStream(testManager, testVersion));
				
				EMFCompare comparator = EMFCompare.builder().build();
				IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
				
				ICompareEditingDomain domain = EMFCompareEditingDomain.create(left, right, null);
				AdapterFactory adapterFactory = RttPluginUtil.createFactory();
				
				@SuppressWarnings("restriction")
				CompareEditorInput input = new ComparisonScopeEditorInput(
						new CompareConfiguration(), domain, adapterFactory, comparator, scope);
				
				manager.close();
				
				CompareUI.openCompareEditorOnPage(input, currentPage);
			}
			
		} catch (RTTException e) {
			Messages.openError(currentPage.getActivePart().getSite().getShell(), ERROR_MESSAGE);
			RttLog.log(e);
		}
	}

	@Override
	public int compareTo(AbstractLogContent o) {
		if (o instanceof CommentContent) {
			return -1;
		}
		
		return 0;
	}

	@Override
	public String getMessage() {
		return failure.getMsg() + " - " + failure.getPath();
	}

	@Override
	public String getTitle() {
		return "Parser Failure";
	}

}
