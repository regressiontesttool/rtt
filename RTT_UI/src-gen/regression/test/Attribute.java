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
 * A representation of the model object '<em><b>Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link regression.test.Attribute#isInformational <em>Informational</em>}</li>
 *   <li>{@link regression.test.Attribute#getName <em>Name</em>}</li>
 *   <li>{@link regression.test.Attribute#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see regression.test.TestPackage#getAttribute()
 * @model kind="class"
 *        extendedMetaData="name='attribute' kind='empty'"
 * @generated
 */
public class Attribute extends EObjectImpl implements EObject {
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
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected String value = VALUE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Attribute() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestPackage.Literals.ATTRIBUTE;
	}

	/**
	 * Returns the value of the '<em><b>Informational</b></em>' attribute.
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
	 * @see regression.test.TestPackage#getAttribute_Informational()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='informational'"
	 * @generated
	 */
	public boolean isInformational() {
		return informational;
	}

	/**
	 * Sets the value of the '{@link regression.test.Attribute#isInformational <em>Informational</em>}' attribute.
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
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.ATTRIBUTE__INFORMATIONAL, oldInformational, informational, !oldInformationalESet));
	}

	/**
	 * Unsets the value of the '{@link regression.test.Attribute#isInformational <em>Informational</em>}' attribute.
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, TestPackage.ATTRIBUTE__INFORMATIONAL, oldInformational, INFORMATIONAL_EDEFAULT, oldInformationalESet));
	}

	/**
	 * Returns whether the value of the '{@link regression.test.Attribute#isInformational <em>Informational</em>}' attribute is set.
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
	 * @see regression.test.TestPackage#getAttribute_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the '{@link regression.test.Attribute#getName <em>Name</em>}' attribute.
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
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.ATTRIBUTE__NAME, oldName, name));
	}

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see regression.test.TestPackage#getAttribute_Value()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='value'"
	 * @generated
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the '{@link regression.test.Attribute#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	public void setValue(String newValue) {
		String oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.ATTRIBUTE__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TestPackage.ATTRIBUTE__INFORMATIONAL:
				return isInformational();
			case TestPackage.ATTRIBUTE__NAME:
				return getName();
			case TestPackage.ATTRIBUTE__VALUE:
				return getValue();
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
			case TestPackage.ATTRIBUTE__INFORMATIONAL:
				setInformational((Boolean)newValue);
				return;
			case TestPackage.ATTRIBUTE__NAME:
				setName((String)newValue);
				return;
			case TestPackage.ATTRIBUTE__VALUE:
				setValue((String)newValue);
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
			case TestPackage.ATTRIBUTE__INFORMATIONAL:
				unsetInformational();
				return;
			case TestPackage.ATTRIBUTE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case TestPackage.ATTRIBUTE__VALUE:
				setValue(VALUE_EDEFAULT);
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
			case TestPackage.ATTRIBUTE__INFORMATIONAL:
				return isSetInformational();
			case TestPackage.ATTRIBUTE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case TestPackage.ATTRIBUTE__VALUE:
				return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
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
		result.append(" (informational: ");
		if (informationalESet) result.append(informational); else result.append("<unset>");
		result.append(", name: ");
		result.append(name);
		result.append(", value: ");
		result.append(value);
		result.append(')');
		return result.toString();
	}

} // Attribute
