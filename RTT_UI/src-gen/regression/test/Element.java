/**
 */
package regression.test;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link regression.test.Element#getAddress <em>Address</em>}</li>
 *   <li>{@link regression.test.Element#isInformational <em>Informational</em>}</li>
 *   <li>{@link regression.test.Element#getName <em>Name</em>}</li>
 *   <li>{@link regression.test.Element#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see regression.test.TestPackage#getElement()
 * @model kind="class"
 *        extendedMetaData="name='element' kind='empty'"
 * @generated
 */
public class Element extends EObjectImpl implements EObject {
	/**
	 * The default value of the '{@link #getAddress() <em>Address</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAddress()
	 * @generated
	 * @ordered
	 */
	protected static final String ADDRESS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAddress() <em>Address</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAddress()
	 * @generated
	 * @ordered
	 */
	protected String address = ADDRESS_EDEFAULT;

	/**
	 * The default value of the '{@link #isInformational() <em>Informational</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isInformational()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INFORMATIONAL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isInformational() <em>Informational</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isInformational()
	 * @generated
	 * @ordered
	 */
	protected boolean informational = INFORMATIONAL_EDEFAULT;

	/**
	 * This is true if the Informational attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean informationalESet;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final Type TYPE_EDEFAULT = Type.ELEMENT;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected Type type = TYPE_EDEFAULT;

	/**
	 * This is true if the Type attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean typeESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Element() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestPackage.Literals.ELEMENT;
	}

	/**
	 * Returns the value of the '<em><b>Address</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Address</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Address</em>' attribute.
	 * @see #setAddress(String)
	 * @see regression.test.TestPackage#getElement_Address()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='address'"
	 * @generated
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the value of the '{@link regression.test.Element#getAddress <em>Address</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Address</em>' attribute.
	 * @see #getAddress()
	 * @generated
	 */
	public void setAddress(String newAddress) {
		String oldAddress = address;
		address = newAddress;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.ELEMENT__ADDRESS, oldAddress, address));
	}

	/**
	 * Returns the value of the '<em><b>Informational</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Informational</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Informational</em>' attribute.
	 * @see #isSetInformational()
	 * @see #unsetInformational()
	 * @see #setInformational(boolean)
	 * @see regression.test.TestPackage#getElement_Informational()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='informational'"
	 * @generated
	 */
	public boolean isInformational() {
		return informational;
	}

	/**
	 * Sets the value of the '{@link regression.test.Element#isInformational <em>Informational</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Informational</em>' attribute.
	 * @see #isSetInformational()
	 * @see #unsetInformational()
	 * @see #isInformational()
	 * @generated
	 */
	public void setInformational(boolean newInformational) {
		boolean oldInformational = informational;
		informational = newInformational;
		boolean oldInformationalESet = informationalESet;
		informationalESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.ELEMENT__INFORMATIONAL, oldInformational, informational, !oldInformationalESet));
	}

	/**
	 * Unsets the value of the '{@link regression.test.Element#isInformational <em>Informational</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetInformational()
	 * @see #isInformational()
	 * @see #setInformational(boolean)
	 * @generated
	 */
	public void unsetInformational() {
		boolean oldInformational = informational;
		boolean oldInformationalESet = informationalESet;
		informational = INFORMATIONAL_EDEFAULT;
		informationalESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TestPackage.ELEMENT__INFORMATIONAL, oldInformational, INFORMATIONAL_EDEFAULT, oldInformationalESet));
	}

	/**
	 * Returns whether the value of the '{@link regression.test.Element#isInformational <em>Informational</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Informational</em>' attribute is set.
	 * @see #unsetInformational()
	 * @see #isInformational()
	 * @see #setInformational(boolean)
	 * @generated
	 */
	public boolean isSetInformational() {
		return informationalESet;
	}

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see regression.test.TestPackage#getElement_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the '{@link regression.test.Element#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.ELEMENT__NAME, oldName, name));
	}

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link regression.test.Type}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see regression.test.Type
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #setType(Type)
	 * @see regression.test.TestPackage#getElement_Type()
	 * @model unsettable="true" required="true"
	 *        extendedMetaData="kind='attribute' name='type'"
	 * @generated
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Sets the value of the '{@link regression.test.Element#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see regression.test.Type
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #getType()
	 * @generated
	 */
	public void setType(Type newType) {
		Type oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		boolean oldTypeESet = typeESet;
		typeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.ELEMENT__TYPE, oldType, type, !oldTypeESet));
	}

	/**
	 * Unsets the value of the '{@link regression.test.Element#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetType()
	 * @see #getType()
	 * @see #setType(Type)
	 * @generated
	 */
	public void unsetType() {
		Type oldType = type;
		boolean oldTypeESet = typeESet;
		type = TYPE_EDEFAULT;
		typeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TestPackage.ELEMENT__TYPE, oldType, TYPE_EDEFAULT, oldTypeESet));
	}

	/**
	 * Returns whether the value of the '{@link regression.test.Element#getType <em>Type</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Type</em>' attribute is set.
	 * @see #unsetType()
	 * @see #getType()
	 * @see #setType(Type)
	 * @generated
	 */
	public boolean isSetType() {
		return typeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TestPackage.ELEMENT__ADDRESS:
				return getAddress();
			case TestPackage.ELEMENT__INFORMATIONAL:
				return isInformational();
			case TestPackage.ELEMENT__NAME:
				return getName();
			case TestPackage.ELEMENT__TYPE:
				return getType();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case TestPackage.ELEMENT__ADDRESS:
				setAddress((String)newValue);
				return;
			case TestPackage.ELEMENT__INFORMATIONAL:
				setInformational((Boolean)newValue);
				return;
			case TestPackage.ELEMENT__NAME:
				setName((String)newValue);
				return;
			case TestPackage.ELEMENT__TYPE:
				setType((Type)newValue);
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
			case TestPackage.ELEMENT__ADDRESS:
				setAddress(ADDRESS_EDEFAULT);
				return;
			case TestPackage.ELEMENT__INFORMATIONAL:
				unsetInformational();
				return;
			case TestPackage.ELEMENT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case TestPackage.ELEMENT__TYPE:
				unsetType();
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
			case TestPackage.ELEMENT__ADDRESS:
				return ADDRESS_EDEFAULT == null ? address != null : !ADDRESS_EDEFAULT.equals(address);
			case TestPackage.ELEMENT__INFORMATIONAL:
				return isSetInformational();
			case TestPackage.ELEMENT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case TestPackage.ELEMENT__TYPE:
				return isSetType();
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
		result.append(" (address: ");
		result.append(address);
		result.append(", informational: ");
		if (informationalESet) result.append(informational); else result.append("<unset>");
		result.append(", name: ");
		result.append(name);
		result.append(", type: ");
		if (typeESet) result.append(type); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} // Element
