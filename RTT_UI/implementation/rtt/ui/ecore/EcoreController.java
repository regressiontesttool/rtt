package rtt.ui.ecore;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * A controller for ecore related operations.
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 */
public class EcoreController {
	
	private Map<String, Boolean> options;
	private EditingDomain domain;

	public EcoreController(final EditingDomain editingDomain) {
		this.domain = editingDomain;
		options = new HashMap<String, Boolean>();
	}
	
	public void addToOptions(String key, boolean value) {
		if (key != null && !key.equals("")) {
			options.put(key, value);
		}
	}
	
	/**
	 * Returns a entry of the options. 
	 * Note: This method returns null, if the given
	 * key cannot be found. 
	 * @param key
	 * @return
	 */
	public Boolean getOption(String key) {
		if (key != null && !key.equals("")) {
			return options.get(key);
		}
		
		return null;
	}
	
	private Command chainCommands(
			final Command command, final Command newCommand) {
		
		if (command != null) {
			return command.chain(newCommand);
		}
		
		return newCommand;
	}
	
	private boolean executeCommand(final Command command) {
		if (command != null && command.canExecute()) {
			domain.getCommandStack().execute(command);
			return true;
		}
		
		return false;
	}

	/**
	 * Adds a {@link EcoreAnnotation} to the given {@link EModelElement}.
	 * @param element
	 * @param annotation
	 * @return returns {@code true}, if the annotation could be successfully set
	 */
	public final boolean addAnnotation(final EModelElement element, 
			final EcoreAnnotation annotation) {
		
		if (annotation == null) {
			throw new IllegalArgumentException("The annotation must not be null.");
		}
		
		if (element == null) {
			throw new IllegalArgumentException("The element must not be null");
		}
		
		if (annotation.isPresentAt(element)) {
			return true;
		}
		
		EAnnotation targetAnnotation = annotation.createEAnnotation();
		
		Command command = AddCommand.create(
				domain, element, null, targetAnnotation);
		
		return executeCommand(command);
	}
	
	/**
	 * Removes a {@link EcoreAnnotation} from the given 
	 * {@link EModelElement}, if present.
	 * 
	 * @param element the object from which the annotation should be removed
	 * @param annotation the annotation which should be removed
	 * @return 
	 */
	public final boolean removeAnnotation(final EModelElement element, 
			final EcoreAnnotation annotation) {
		
		Command command = null;
		
		EAnnotation targetAnnotation = annotation.getEAnnotation(element);
		if (targetAnnotation != null) {
			command = chainCommands(command, RemoveCommand.create(domain, targetAnnotation));
		}
		
		if (annotation == EcoreAnnotation.NODE
				|| annotation == EcoreAnnotation.PARSER) {
			EClass classObject = (EClass) element;
			command = removeAnnotationsFromChildren(classObject, annotation, command);
		}	

		return executeCommand(command);
	}
	
	private final Command removeAnnotationsFromChildren(final EClass classObject, 
			EcoreAnnotation topAnnotation, final Command command) {
		Command resultCommand = command;
		
		for (EStructuralFeature feature : classObject.getEStructuralFeatures()) {
			EcoreAnnotation annotation = EcoreAnnotation.search(feature);
			if (annotation != null && AnnotationRelation.areRelated(topAnnotation, annotation)) {
				resultCommand = chainCommands(resultCommand, 
						RemoveCommand.create(domain, annotation.getEAnnotation(feature)));
			}
		}
		
		for (EOperation operation : classObject.getEOperations()) {
			EcoreAnnotation annotation = EcoreAnnotation.search(operation);
			if (annotation != null) {
				resultCommand = chainCommands(resultCommand, 
						RemoveCommand.create(domain, annotation.getEAnnotation(operation)));
			}
		}
		
		return resultCommand;
	}

	/**
	 * <p>Sets a node related annotation 
	 * (e.g. {@link EcoreAnnotation#NODE_COMPARE}) to the given 
	 * {@link EModelElement}. Before adding the new annotation
	 * previous node annotations will be removed.</p>
	 * 
	 * <p>Note: Do not use this method to add an other annotation
	 *  than a node annotation, use instead 
	 * {@link #addAnnotation(EModelElement, EcoreAnnotation)}.</p>
	 * 
	 * @param element
	 * @param nodeAnnotation
	 * @return a boolean indicating if any changes were made
	 */
	public final boolean setRelatedAnnotation(final EModelElement element, 
			final EcoreAnnotation annotation) {
		
		if (element == null || annotation == null) {
			throw new IllegalArgumentException("Attribute or annotation was null.");
		}
		
		if (annotation.canSetTo(element) == false) {
			return false;
		}
		
		if (checkParentAnnotation(element, annotation) == false) {
			return false;
		}
		
		Command command = null;
		
		EAnnotation oldAnnotation = checkAnnotations(element, 
				AnnotationRelation.findRelation(annotation).relatedAnnotations);
		
		if (oldAnnotation != null) {
			command = chainCommands(command, 
					RemoveCommand.create(domain, oldAnnotation));
		}
		
		EAnnotation eAnnotation = annotation.createEAnnotation();
		command = chainCommands(command, 
				AddCommand.create(domain, element, null, eAnnotation));
		
		return executeCommand(command);
	}
	
	private boolean checkParentAnnotation(EModelElement element, EcoreAnnotation annotation) {
		EcoreAnnotation topAnnotation = AnnotationRelation.findRelation(annotation).topAnnotation;
		
		if (element instanceof EStructuralFeature) {
			EStructuralFeature feature = (EStructuralFeature) element;
			return topAnnotation.isPresentAt(feature.getEContainingClass());
		}
		
		if (element instanceof EOperation) {
			EOperation operation = (EOperation) element;
			return topAnnotation.isPresentAt(operation.getEContainingClass());
		}
		
		return false;
	}

	private EAnnotation checkAnnotations(final EModelElement element, 
			EcoreAnnotation[] annotations) {
		
		EAnnotation annotation = null;
		for (EcoreAnnotation rttAnnotation : annotations) {
			annotation = rttAnnotation.getEAnnotation(element);
			if (annotation != null) {
				return annotation;
			}
		}
		
		return null;
		
	}

	/**
	 * Returns all {@link Resource}s of the current {@link EditingDomain}.
	 * @return a {@link EList} with all {@link Resource}s.
	 */
	public final EList<Resource> getResources() {
		return domain.getResourceSet().getResources();
	}

	/**
	 * Returns the {@link Resource} from the current {@link EditingDomain}
	 * with the given index.
	 * @param index the index within the {@link ResourceSet}.
	 * @return a {@link Resource} object.
	 */
	public final Resource getResource(final int index) {
		return getResources().get(index);
	}

	public final boolean isSaveNeeded() {
		return ((BasicCommandStack) domain.getCommandStack()).isSaveNeeded();
	}
}
