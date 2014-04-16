package rtt.ui.ecore.editor.util.selectionAdapter;

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;

import rtt.ui.ecore.EcoreAnnotation;
import rtt.ui.ecore.EcoreController;
import rtt.ui.ecore.editor.EcoreEditor;
import rtt.ui.ecore.util.Messages;

/**
 * <p>This {@linkplain SelectionListener} is used to add annotations to 
 * {@linkplain EModelElement}s. It takes the current selected object from
 * the {@link EcoreEditor#getLeftTreeViewer()} and checks if adding is 
 * appropriate. If true the the given {@linkplain EcoreAnnotation} will 
 * be added. </p>
 * <p>Afterwards the current selection of the 
 * {@link EcoreEditor#getRightTreeViewer()} will be set to the 
 * altered {@linkplain EModelElement}.</p>
 * 
 * <p>Following checks will be made:<ul>
 * <li>{@link #validateContainment(EReference)}</li>
 * <li>{@link #validateParent(EModelElement)}</li>
 * <li>{@link #validateChildren(EModelElement, EcoreAnnotation)}</li>
 * </ul></p>
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 */
public class AddAnnotationSelectionAdapter extends ViewerSourceSelectionAdapter {
	
	protected static final String PARENT_MESSAGE = 
			"rtt.ecore.editor.selection.addAnnotation.parent"; //$NON-NLS-1$
	protected static final String CHILDREN_MESSAGE = 
			"rtt.ecore.editor.selection.addAnnotation.child"; //$NON-NLS-1$
	protected static final String NONCONTAINMENT_MESSAGE = 
			"rtt.ecore.editor.selection.addAnnotation.noncontainment";//$NON-NLS-1$
	
	protected static final String RELATED_TITLE = 
			"rtt.ecore.editor.selection.addAnnotation.related.title";	
	protected static final String RELATED_MESSAGE = 
			"rtt.ecore.editor.selection.addAnnotation.related.message";
	
	protected static final String DOUBLE_ANNOTATION_TITLE = 
			"rtt.ecore.editor.selection.addAnnotation.double.title";
	protected static final String DOUBLE_ANNOTAITON_MESSAGE = 
			"rtt.ecore.editor.selection.addAnnotation.double.message";
	
	protected static final String ASK_PARENT_OPTION = "askParent"; //$NON-NLS-1$
	protected static final String ASK_CHILDREN_OPTION = "askChildren"; //$NON-NLS-1$
	protected static final String ASK_NONCONTAINMENT_OPTION = "askNonContainment"; //$NON-NLS-1$
	
	
	
	private EcoreEditor editor;
	private EcoreController controller;
	private EcoreAnnotation annotation;
	
	public AddAnnotationSelectionAdapter(EcoreEditor editor, 
			EcoreAnnotation annotation) {
		super(editor.getLeftTreeViewer());
		
		this.editor = editor;
		this.controller = editor.getController();
		this.annotation = annotation;
	}
	
	private Shell getShell() {
		return editor.getSite().getShell();
	}
	
	@Override
	public void handleObject(Object object) {
		if (object instanceof EModelElement) {
			EModelElement element = (EModelElement) object;
			if (addAnnotation(element)) {
				editor.getRightTreeViewer().setSelection(
						new StructuredSelection(annotation.getEAnnotation(element)), true);
			}
		}
	}

	/**
	 * <p>This method tries to add an {@link EcoreAnnotation} to 
	 * the given {@link EModelElement}. If the annotation is already
	 * present, {@code true} will be returned. <br />
	 * If not, the {@link EModelElement} will be checked if the annotation
	 * can be applied by the rules for annotation adding:
	 * <ul>
	 * <li>{@link #validateContainment(EModelElement)}</li>
	 * <li>{@link #validateParent(EModelElement, EcoreAnnotation)}</li>
	 * <li>{@link #validateChildren(EModelElement, EcoreAnnotation)}</li>
	 * </ul></p>
	 * 
	 * @param element the {@link EModelElement}
	 * @return {@code true}, if annotation already present or successfully added
	 */
	public boolean addAnnotation(EModelElement element) {
		if (element == null) {
			throw new IllegalArgumentException("The given element must not be null.");
		}
		
		if (annotation.isPresentAt(element)) {
			return true;
		}
		
		switch (annotation) {
		case NODE:
		case PARSER:
			return setAnnotation(element);
			
		case PARSER_AST:
		case PARSER_INIT:
			return setParserRelatedAnnotation(element);
			
		case NODE_INFORMATIONAL:
		case NODE_COMPARE:
		case NODE_CHILDREN:
			return setNodeRelatedAnnotation(element);

		default:
			throw new IllegalStateException("Annotation not recognized.");
		}
	}

	private boolean setAnnotation(EModelElement element) {
		boolean hasNodeAnnotation = EcoreAnnotation.NODE.isPresentAt(element);
		boolean hasParserAnnotation = EcoreAnnotation.PARSER.isPresentAt(element);
		
		if ((annotation == EcoreAnnotation.NODE && hasParserAnnotation)
				|| (annotation == EcoreAnnotation.PARSER && hasNodeAnnotation)) {
			MessageDialog.openInformation(getShell(), 
					Messages.getString(DOUBLE_ANNOTATION_TITLE),
					Messages.getString(DOUBLE_ANNOTAITON_MESSAGE));
		}
		
		return controller.addAnnotation(element, annotation);
	}

	private boolean setParserRelatedAnnotation(EModelElement element) {

		boolean doSet = true;

		// check if selected element has info, compare or children annotation
		// if true, then inform user that this is not recommended
		if (EcoreAnnotation.NODE_INFORMATIONAL.isPresentAt(element)
				|| EcoreAnnotation.NODE_COMPARE.isPresentAt(element)
				|| EcoreAnnotation.NODE_CHILDREN.isPresentAt(element)) {
			
			EcoreAnnotation nodeAnnotation = EcoreAnnotation.search(element);
			
			doSet = MessageDialog.openQuestion(getShell(),
					Messages.getString(RELATED_TITLE),
					Messages.getString(RELATED_MESSAGE, 
						nodeAnnotation.getToken(), annotation.getToken()));
		}
		
		if (doSet && validateContainment(element)
				&& validateParent(element, EcoreAnnotation.PARSER)) {
			
			if (annotation == EcoreAnnotation.PARSER_AST) {
				doSet = validateChildren(element, EcoreAnnotation.NODE);
			}
			
			if (doSet) {
				return controller.setRelatedAnnotation(element, annotation);
			}		
		}
		
		return false;
	}
	
	private boolean setNodeRelatedAnnotation(EModelElement element) {
		
		Boolean doSet = true;

		// check if selected element has ast or init annotation
		// if true, then ask the user if proceed 
		if (EcoreAnnotation.PARSER_AST.isPresentAt(element)
				|| EcoreAnnotation.PARSER_INIT.isPresentAt(element)) {
			
			EcoreAnnotation parserAnnotation = EcoreAnnotation.search(element);
			
			doSet = MessageDialog.openQuestion(getShell(),
					Messages.getString(RELATED_TITLE),
					Messages.getString(RELATED_MESSAGE,
							parserAnnotation.getToken(), annotation.getToken()));
		}
		
		if (doSet && validateContainment(element)
				&& validateParent(element, EcoreAnnotation.NODE)) {
			
			if (annotation == EcoreAnnotation.NODE_CHILDREN) {
				doSet = validateChildren(element, EcoreAnnotation.NODE);					
			}
			
			if (doSet) {
				return controller.setRelatedAnnotation(element, annotation);
			}
		}
		
		return false;
	}

	/**
	 * <p> Checks if the given {@link EModelElement} is an non-containment 
	 * {@link EReference}. If true and an {@link EcoreAnnotation#NODE_CHILDREN}
	 * or {@link EcoreAnnotation#PARSER_AST} should be added, a confirmation
	 * dialog will be presented and infers the return value of this method.</p>
	 * <p> Any other {@link EModelElement} or a containment-reference 
	 * will return {@code true}. </p>
	 * 
	 * @param element ideally an non-containment {@link EReference}
	 * @return {@code false}, if non-containment reference and 
	 * 	no proceeding confirmation of the user
	 */
	private boolean validateContainment(EModelElement element) {
		
		// if element is reference and non-containment, then do a check
		if (element instanceof EReference 
				&& !((EReference) element).isContainment()) {
			
			if (annotation == EcoreAnnotation.NODE_CHILDREN
					|| annotation == EcoreAnnotation.PARSER_AST) {
				
				// check options or ask user
				Boolean doSet = controller.getOption(ASK_NONCONTAINMENT_OPTION);
				if (doSet == null) {
					MessageDialogWithToggle dialog = ToggleDialogCreator.createQuestion(
							editor.getSite().getShell(), 
							Messages.getString(NONCONTAINMENT_MESSAGE, annotation.getToken()));
					int returnCode = dialog.getReturnCode();
					doSet = returnCode == IDialogConstants.YES_ID;

					if (returnCode != IDialogConstants.CANCEL_ID
							&& dialog.getToggleState() == true) {
						controller.addToOptions(ASK_NONCONTAINMENT_OPTION, doSet);
					}
				}
				
				return doSet;
			}		
		}
		
		return true;
	}

	/**
	 * <p>Checks if a given {@link EcoreAnnotation} is present at
	 * the parent object of the given {@link EModelElement}.
	 * If the annotation is not present, the user will be
	 * asked if the annotation should be added and 
	 * infers the return value of this method.</p>
	 * 
	 * @param element any {@link EModelElement}
	 * @param annotation an {@link EcoreAnnotation} which the parent should contain
	 * @return {@code true}, if parent has the given annotation
	 */
	private boolean validateParent(EModelElement element, EcoreAnnotation annotation) {
		EObject container = element.eContainer();
		if (container instanceof EModelElement) {
			EModelElement parent = (EModelElement) container;
			
			if (annotation.isPresentAt(parent)) {
				return true;
			}
			
			// if annotation is not present at parent, then
			// check options or ask user			
			Boolean doSet = controller.getOption(ASK_PARENT_OPTION);
			if (doSet == null) {
				MessageDialogWithToggle dialog = ToggleDialogCreator.createQuestion(
						editor.getSite().getShell(), 
						Messages.getString(PARENT_MESSAGE, annotation.getToken()));
				
				int returnCode = dialog.getReturnCode();
				
				doSet = returnCode == IDialogConstants.YES_ID;
				
				if (returnCode != IDialogConstants.CANCEL_ID 
						&& dialog.getToggleState() == true) {
					controller.addToOptions(ASK_PARENT_OPTION, doSet);
				}
			}
			
			if (doSet) {
				return controller.addAnnotation(parent, annotation);
			}
		}
		
		return false;
	}
	
	/**
	 * <p>Checks if the referenced type of the given 
	 * {@link EReference} or {@link EOperation} contains the given
	 * {@link EcoreAnnotation}. <br/> 
	 * If the given annotation is not set, the user will be asked
	 * if the annotation should be added and infers 
	 * the return value of this method.<br/>
	 * <i>Note:</i> Any other {@link EObject} will not be checked
	 * and always return {@code false}.</p>
	 * 
	 * @param element ideally an {@link EReference} or {@link EOperation}
	 * @param annotation an {@link EcoreAnnotation} which the referenced type should contain
	 * @return {@code true}, if the referenced type of the element
	 * 	contains the given annotation
	 */
	private boolean validateChildren(EModelElement element, EcoreAnnotation annotation) {

		EModelElement child = null;
		if (element instanceof EReference) {
			EReference reference = (EReference) element;
			child = reference.getEReferenceType();
		} else if (element instanceof EOperation) {
			EOperation operation = (EOperation) element;
			child = operation.getEType();
		} 
		
		if (child != null) {
			if (annotation.isPresentAt(child)) {
				return true;
			}
			
			// if annotation is not present at child, then
			// check options or ask user						
			Boolean doSet = controller.getOption(ASK_CHILDREN_OPTION);
			if (doSet == null) {
				MessageDialogWithToggle dialog = ToggleDialogCreator.createQuestion(
						editor.getSite().getShell(), 
						Messages.getString(CHILDREN_MESSAGE, annotation.getToken()));

				int returnCode = dialog.getReturnCode();

				doSet = returnCode == IDialogConstants.YES_ID;

				if (returnCode != IDialogConstants.CANCEL_ID 
						&& dialog.getToggleState() == true) {
					controller.addToOptions(ASK_CHILDREN_OPTION, doSet);
				}
			}
			
			if (doSet) {
				return controller.addAnnotation(child, annotation);
			}
		}
		
		return false;
	}
}
