/**
 */
package regression.test;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link regression.test.Node#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link regression.test.Node#getNode <em>Node</em>}</li>
 *   <li>{@link regression.test.Node#isIsNull <em>Is Null</em>}</li>
 *   <li>{@link regression.test.Node#getMethod <em>Method</em>}</li>
 * </ul>
 * </p>
 *
 * @see regression.test.TestPackage#getNode()
 * @model kind="class"
 *        extendedMetaData="name='node' kind='elementOnly'"
 * @generated
 */
public class Node extends Classable {
	/**
	 * The cached value of the '{@link #getAttribute() <em>Attribute</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttribute()
	 * @generated
	 * @ordered
	 */
	protected EList<Attribute> attribute;

	/**
	 * The cached value of the '{@link #getNode() <em>Node</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNode()
	 * @generated
	 * @ordered
	 */
	protected EList<Node> node;

	/**
	 * The default value of the '{@link #isIsNull() <em>Is Null</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsNull()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_NULL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsNull() <em>Is Null</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsNull()
	 * @generated
	 * @ordered
	 */
	protected boolean isNull = IS_NULL_EDEFAULT;

	/**
	 * This is true if the Is Null attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean isNullESet;

	/**
	 * The default value of the '{@link #getMethod() <em>Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMethod()
	 * @generated
	 * @ordered
	 */
	protected static final String METHOD_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMethod() <em>Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMethod()
	 * @generated
	 * @ordered
	 */
	protected String method = METHOD_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Node() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestPackage.Literals.NODE;
	}

	/**
	 * Returns the value of the '<em><b>Attribute</b></em>' containment reference list.
	 * The list contents are of type {@link regression.test.Attribute}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attribute</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute</em>' containment reference list.
	 * @see regression.test.TestPackage#getNode_Attribute()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='attribute' namespace='##targetNamespace'"
	 * @generated
	 */
	public EList<Attribute> getAttribute() {
		if (attribute == null) {
			attribute = new EObjectContainmentEList<Attribute>(Attribute.class, this, TestPackage.NODE__ATTRIBUTE);
		}
		return attribute;
	}

	/**
	 * Returns the value of the '<em><b>Node</b></em>' containment reference list.
	 * The list contents are of type {@link regression.test.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Node</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Node</em>' containment reference list.
	 * @see regression.test.TestPackage#getNode_Node()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='node' namespace='##targetNamespace'"
	 * @generated
	 */
	public EList<Node> getNode() {
		if (node == null) {
			node = new EObjectContainmentEList<Node>(Node.class, this, TestPackage.NODE__NODE);
		}
		return node;
	}

	/**
	 * Returns the value of the '<em><b>Is Null</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Null</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Null</em>' attribute.
	 * @see #isSetIsNull()
	 * @see #unsetIsNull()
	 * @see #setIsNull(boolean)
	 * @see regression.test.TestPackage#getNode_IsNull()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='isNull'"
	 * @generated
	 */
	public boolean isIsNull() {
		return isNull;
	}

	/**
	 * Sets the value of the '{@link regression.test.Node#isIsNull <em>Is Null</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Null</em>' attribute.
	 * @see #isSetIsNull()
	 * @see #unsetIsNull()
	 * @see #isIsNull()
	 * @generated
	 */
	public void setIsNull(boolean newIsNull) {
		boolean oldIsNull = isNull;
		isNull = newIsNull;
		boolean oldIsNullESet = isNullESet;
		isNullESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.NODE__IS_NULL, oldIsNull, isNull, !oldIsNullESet));
	}

	/**
	 * Unsets the value of the '{@link regression.test.Node#isIsNull <em>Is Null</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetIsNull()
	 * @see #isIsNull()
	 * @see #setIsNull(boolean)
	 * @generated
	 */
	public void unsetIsNull() {
		boolean oldIsNull = isNull;
		boolean oldIsNullESet = isNullESet;
		isNull = IS_NULL_EDEFAULT;
		isNullESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TestPackage.NODE__IS_NULL, oldIsNull, IS_NULL_EDEFAULT, oldIsNullESet));
	}

	/**
	 * Returns whether the value of the '{@link regression.test.Node#isIsNull <em>Is Null</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Is Null</em>' attribute is set.
	 * @see #unsetIsNull()
	 * @see #isIsNull()
	 * @see #setIsNull(boolean)
	 * @generated
	 */
	public boolean isSetIsNull() {
		return isNullESet;
	}

	/**
	 * Returns the value of the '<em><b>Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Method</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Method</em>' attribute.
	 * @see #setMethod(String)
	 * @see regression.test.TestPackage#getNode_Method()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='method'"
	 * @generated
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Sets the value of the '{@link regression.test.Node#getMethod <em>Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Method</em>' attribute.
	 * @see #getMethod()
	 * @generated
	 */
	public void setMethod(String newMethod) {
		String oldMethod = method;
		method = newMethod;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.NODE__METHOD, oldMethod, method));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TestPackage.NODE__ATTRIBUTE:
				return ((InternalEList<?>)getAttribute()).basicRemove(otherEnd, msgs);
			case TestPackage.NODE__NODE:
				return ((InternalEList<?>)getNode()).basicRemove(otherEnd, msgs);
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
			case TestPackage.NODE__ATTRIBUTE:
				return getAttribute();
			case TestPackage.NODE__NODE:
				return getNode();
			case TestPackage.NODE__IS_NULL:
				return isIsNull();
			case TestPackage.NODE__METHOD:
				return getMethod();
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
			case TestPackage.NODE__ATTRIBUTE:
				getAttribute().clear();
				getAttribute().addAll((Collection<? extends Attribute>)newValue);
				return;
			case TestPackage.NODE__NODE:
				getNode().clear();
				getNode().addAll((Collection<? extends Node>)newValue);
				return;
			case TestPackage.NODE__IS_NULL:
				setIsNull((Boolean)newValue);
				return;
			case TestPackage.NODE__METHOD:
				setMethod((String)newValue);
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
			case TestPackage.NODE__ATTRIBUTE:
				getAttribute().clear();
				return;
			case TestPackage.NODE__NODE:
				getNode().clear();
				return;
			case TestPackage.NODE__IS_NULL:
				unsetIsNull();
				return;
			case TestPackage.NODE__METHOD:
				setMethod(METHOD_EDEFAULT);
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
			case TestPackage.NODE__ATTRIBUTE:
				return attribute != null && !attribute.isEmpty();
			case TestPackage.NODE__NODE:
				return node != null && !node.isEmpty();
			case TestPackage.NODE__IS_NULL:
				return isSetIsNull();
			case TestPackage.NODE__METHOD:
				return METHOD_EDEFAULT == null ? method != null : !METHOD_EDEFAULT.equals(method);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (isNull: ");
		if (isNullESet) result.append(isNull); else result.append("<unset>");
		result.append(", method: ");
		result.append(method);
		result.append(')');
		return result.toString();
	}

} // Node
