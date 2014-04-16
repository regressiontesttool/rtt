/**
 */
package regression.test;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parser Output Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * type of parser output
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link regression.test.ParserOutputType#getTree <em>Tree</em>}</li>
 * </ul>
 * </p>
 *
 * @see regression.test.TestPackage#getParserOutputType()
 * @model kind="class"
 *        extendedMetaData="name='parserOutput_._type' kind='elementOnly'"
 * @generated
 */
public class ParserOutputType extends EObjectImpl implements EObject {
	/**
	 * The cached value of the '{@link #getTree() <em>Tree</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTree()
	 * @generated
	 * @ordered
	 */
	protected EList<Tree> tree;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ParserOutputType() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestPackage.Literals.PARSER_OUTPUT_TYPE;
	}

	/**
	 * Returns the value of the '<em><b>Tree</b></em>' containment reference list.
	 * The list contents are of type {@link regression.test.Tree}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tree</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tree</em>' containment reference list.
	 * @see regression.test.TestPackage#getParserOutputType_Tree()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='tree' namespace='##targetNamespace'"
	 * @generated
	 */
	public EList<Tree> getTree() {
		if (tree == null) {
			tree = new EObjectContainmentEList<Tree>(Tree.class, this, TestPackage.PARSER_OUTPUT_TYPE__TREE);
		}
		return tree;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TestPackage.PARSER_OUTPUT_TYPE__TREE:
				return ((InternalEList<?>)getTree()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TestPackage.PARSER_OUTPUT_TYPE__TREE:
				return getTree();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case TestPackage.PARSER_OUTPUT_TYPE__TREE:
				getTree().clear();
				getTree().addAll((Collection<? extends Tree>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case TestPackage.PARSER_OUTPUT_TYPE__TREE:
				getTree().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case TestPackage.PARSER_OUTPUT_TYPE__TREE:
				return tree != null && !tree.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // ParserOutputType
