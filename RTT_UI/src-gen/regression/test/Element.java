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
 *   <li>{@link regression.test.Element#getGeneratorName <em>Generator Name</em>}</li>
 *   <li>{@link regression.test.Element#getGeneratorType <em>Generator Type</em>}</li>
 *   <li>{@link regression.test.Element#isInformational <em>Informational</em>}</li>
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
	 * The default value of the '{@link #getGeneratorName() <em>Generator Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneratorName()
	 * @generated
	 * @ordered
	 */
	protected static final String GENERATOR_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getGeneratorName() <em>Generator Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneratorName()
	 * @generated
	 * @ordered
	 */
	protected String generatorName = GENERATOR_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getGeneratorType() <em>Generator Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneratorType()
	 * @generated
	 * @ordered
	 */
	protected static final Type GENERATOR_TYPE_EDEFAULT = Type.OBJECT;

	/**
	 * The cached value of the '{@link #getGeneratorType() <em>Generator Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneratorType()
	 * @generated
	 * @ordered
	 */
	protected Type generatorType = GENERATOR_TYPE_EDEFAULT;

	/**
	 * This is true if the Generator Type attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean generatorTypeESet;

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
	 * Returns the value of the '<em><b>Generator Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Generator Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generator Name</em>' attribute.
	 * @see #setGeneratorName(String)
	 * @see regression.test.TestPackage#getElement_GeneratorName()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='generatorName'"
	 * @generated
	 */
	public String getGeneratorName() {
		return generatorName;
	}

	/**
	 * Sets the value of the '{@link regression.test.Element#getGeneratorName <em>Generator Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generator Name</em>' attribute.
	 * @see #getGeneratorName()
	 * @generated
	 */
	public void setGeneratorName(String newGeneratorName) {
		String oldGeneratorName = generatorName;
		generatorName = newGeneratorName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.ELEMENT__GENERATOR_NAME, oldGeneratorName, generatorName));
	}

	/**
	 * Returns the value of the '<em><b>Generator Type</b></em>' attribute.
	 * The literals are from the enumeration {@link regression.test.Type}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Generator Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generator Type</em>' attribute.
	 * @see regression.test.Type
	 * @see #isSetGeneratorType()
	 * @see #unsetGeneratorType()
	 * @see #setGeneratorType(Type)
	 * @see regression.test.TestPackage#getElement_GeneratorType()
	 * @model unsettable="true" required="true"
	 *        extendedMetaData="kind='attribute' name='generatorType'"
	 * @generated
	 */
	public Type getGeneratorType() {
		return generatorType;
	}

	/**
	 * Sets the value of the '{@link regression.test.Element#getGeneratorType <em>Generator Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generator Type</em>' attribute.
	 * @see regression.test.Type
	 * @see #isSetGeneratorType()
	 * @see #unsetGeneratorType()
	 * @see #getGeneratorType()
	 * @generated
	 */
	public void setGeneratorType(Type newGeneratorType) {
		Type oldGeneratorType = generatorType;
		generatorType = newGeneratorType == null ? GENERATOR_TYPE_EDEFAULT : newGeneratorType;
		boolean oldGeneratorTypeESet = generatorTypeESet;
		generatorTypeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.ELEMENT__GENERATOR_TYPE, oldGeneratorType, generatorType, !oldGeneratorTypeESet));
	}

	/**
	 * Unsets the value of the '{@link regression.test.Element#getGeneratorType <em>Generator Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetGeneratorType()
	 * @see #getGeneratorType()
	 * @see #setGeneratorType(Type)
	 * @generated
	 */
	public void unsetGeneratorType() {
		Type oldGeneratorType = generatorType;
		boolean oldGeneratorTypeESet = generatorTypeESet;
		generatorType = GENERATOR_TYPE_EDEFAULT;
		generatorTypeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TestPackage.ELEMENT__GENERATOR_TYPE, oldGeneratorType, GENERATOR_TYPE_EDEFAULT, oldGeneratorTypeESet));
	}

	/**
	 * Returns whether the value of the '{@link regression.test.Element#getGeneratorType <em>Generator Type</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Generator Type</em>' attribute is set.
	 * @see #unsetGeneratorType()
	 * @see #getGeneratorType()
	 * @see #setGeneratorType(Type)
	 * @generated
	 */
	public boolean isSetGeneratorType() {
		return generatorTypeESet;
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TestPackage.ELEMENT__ADDRESS:
				return getAddress();
			case TestPackage.ELEMENT__GENERATOR_NAME:
				return getGeneratorName();
			case TestPackage.ELEMENT__GENERATOR_TYPE:
				return getGeneratorType();
			case TestPackage.ELEMENT__INFORMATIONAL:
				return isInformational();
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
			case TestPackage.ELEMENT__GENERATOR_NAME:
				setGeneratorName((String)newValue);
				return;
			case TestPackage.ELEMENT__GENERATOR_TYPE:
				setGeneratorType((Type)newValue);
				return;
			case TestPackage.ELEMENT__INFORMATIONAL:
				setInformational((Boolean)newValue);
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
			case TestPackage.ELEMENT__GENERATOR_NAME:
				setGeneratorName(GENERATOR_NAME_EDEFAULT);
				return;
			case TestPackage.ELEMENT__GENERATOR_TYPE:
				unsetGeneratorType();
				return;
			case TestPackage.ELEMENT__INFORMATIONAL:
				unsetInformational();
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
			case TestPackage.ELEMENT__GENERATOR_NAME:
				return GENERATOR_NAME_EDEFAULT == null ? generatorName != null : !GENERATOR_NAME_EDEFAULT.equals(generatorName);
			case TestPackage.ELEMENT__GENERATOR_TYPE:
				return isSetGeneratorType();
			case TestPackage.ELEMENT__INFORMATIONAL:
				return isSetInformational();
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
		result.append(", generatorName: ");
		result.append(generatorName);
		result.append(", generatorType: ");
		if (generatorTypeESet) result.append(generatorType); else result.append("<unset>");
		result.append(", informational: ");
		if (informationalESet) result.append(informational); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} // Element
