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
 * A representation of the model object '<em><b>Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link regression.test.Node#getGeneratorName <em>Generator Name</em>}</li>
 *   <li>{@link regression.test.Node#getGeneratorType <em>Generator Type</em>}</li>
 *   <li>{@link regression.test.Node#isInformational <em>Informational</em>}</li>
 *   <li>{@link regression.test.Node#isIsNull <em>Is Null</em>}</li>
 * </ul>
 * </p>
 *
 * @see regression.test.TestPackage#getNode()
 * @model kind="class"
 *        extendedMetaData="name='node' kind='empty'"
 * @generated
 */
public class Node extends EObjectImpl implements EObject {
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
	protected static final GeneratorType GENERATOR_TYPE_EDEFAULT = GeneratorType.FIELD;

	/**
	 * The cached value of the '{@link #getGeneratorType() <em>Generator Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneratorType()
	 * @generated
	 * @ordered
	 */
	protected GeneratorType generatorType = GENERATOR_TYPE_EDEFAULT;

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
	 * Returns the value of the '<em><b>Generator Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Generator Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generator Name</em>' attribute.
	 * @see #setGeneratorName(String)
	 * @see regression.test.TestPackage#getNode_GeneratorName()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='generatorName'"
	 * @generated
	 */
	public String getGeneratorName() {
		return generatorName;
	}

	/**
	 * Sets the value of the '{@link regression.test.Node#getGeneratorName <em>Generator Name</em>}' attribute.
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
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.NODE__GENERATOR_NAME, oldGeneratorName, generatorName));
	}

	/**
	 * Returns the value of the '<em><b>Generator Type</b></em>' attribute.
	 * The literals are from the enumeration {@link regression.test.GeneratorType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Generator Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generator Type</em>' attribute.
	 * @see regression.test.GeneratorType
	 * @see #isSetGeneratorType()
	 * @see #unsetGeneratorType()
	 * @see #setGeneratorType(GeneratorType)
	 * @see regression.test.TestPackage#getNode_GeneratorType()
	 * @model unsettable="true" required="true"
	 *        extendedMetaData="kind='attribute' name='generatorType'"
	 * @generated
	 */
	public GeneratorType getGeneratorType() {
		return generatorType;
	}

	/**
	 * Sets the value of the '{@link regression.test.Node#getGeneratorType <em>Generator Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generator Type</em>' attribute.
	 * @see regression.test.GeneratorType
	 * @see #isSetGeneratorType()
	 * @see #unsetGeneratorType()
	 * @see #getGeneratorType()
	 * @generated
	 */
	public void setGeneratorType(GeneratorType newGeneratorType) {
		GeneratorType oldGeneratorType = generatorType;
		generatorType = newGeneratorType == null ? GENERATOR_TYPE_EDEFAULT : newGeneratorType;
		boolean oldGeneratorTypeESet = generatorTypeESet;
		generatorTypeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.NODE__GENERATOR_TYPE, oldGeneratorType, generatorType, !oldGeneratorTypeESet));
	}

	/**
	 * Unsets the value of the '{@link regression.test.Node#getGeneratorType <em>Generator Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetGeneratorType()
	 * @see #getGeneratorType()
	 * @see #setGeneratorType(GeneratorType)
	 * @generated
	 */
	public void unsetGeneratorType() {
		GeneratorType oldGeneratorType = generatorType;
		boolean oldGeneratorTypeESet = generatorTypeESet;
		generatorType = GENERATOR_TYPE_EDEFAULT;
		generatorTypeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TestPackage.NODE__GENERATOR_TYPE, oldGeneratorType, GENERATOR_TYPE_EDEFAULT, oldGeneratorTypeESet));
	}

	/**
	 * Returns whether the value of the '{@link regression.test.Node#getGeneratorType <em>Generator Type</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Generator Type</em>' attribute is set.
	 * @see #unsetGeneratorType()
	 * @see #getGeneratorType()
	 * @see #setGeneratorType(GeneratorType)
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
	 * @see regression.test.TestPackage#getNode_Informational()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='informational'"
	 * @generated
	 */
	public boolean isInformational() {
		return informational;
	}

	/**
	 * Sets the value of the '{@link regression.test.Node#isInformational <em>Informational</em>}' attribute.
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
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.NODE__INFORMATIONAL, oldInformational, informational, !oldInformationalESet));
	}

	/**
	 * Unsets the value of the '{@link regression.test.Node#isInformational <em>Informational</em>}' attribute.
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, TestPackage.NODE__INFORMATIONAL, oldInformational, INFORMATIONAL_EDEFAULT, oldInformationalESet));
	}

	/**
	 * Returns whether the value of the '{@link regression.test.Node#isInformational <em>Informational</em>}' attribute is set.
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TestPackage.NODE__GENERATOR_NAME:
				return getGeneratorName();
			case TestPackage.NODE__GENERATOR_TYPE:
				return getGeneratorType();
			case TestPackage.NODE__INFORMATIONAL:
				return isInformational();
			case TestPackage.NODE__IS_NULL:
				return isIsNull();
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
			case TestPackage.NODE__GENERATOR_NAME:
				setGeneratorName((String)newValue);
				return;
			case TestPackage.NODE__GENERATOR_TYPE:
				setGeneratorType((GeneratorType)newValue);
				return;
			case TestPackage.NODE__INFORMATIONAL:
				setInformational((Boolean)newValue);
				return;
			case TestPackage.NODE__IS_NULL:
				setIsNull((Boolean)newValue);
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
			case TestPackage.NODE__GENERATOR_NAME:
				setGeneratorName(GENERATOR_NAME_EDEFAULT);
				return;
			case TestPackage.NODE__GENERATOR_TYPE:
				unsetGeneratorType();
				return;
			case TestPackage.NODE__INFORMATIONAL:
				unsetInformational();
				return;
			case TestPackage.NODE__IS_NULL:
				unsetIsNull();
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
			case TestPackage.NODE__GENERATOR_NAME:
				return GENERATOR_NAME_EDEFAULT == null ? generatorName != null : !GENERATOR_NAME_EDEFAULT.equals(generatorName);
			case TestPackage.NODE__GENERATOR_TYPE:
				return isSetGeneratorType();
			case TestPackage.NODE__INFORMATIONAL:
				return isSetInformational();
			case TestPackage.NODE__IS_NULL:
				return isSetIsNull();
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
		result.append(" (generatorName: ");
		result.append(generatorName);
		result.append(", generatorType: ");
		if (generatorTypeESet) result.append(generatorType); else result.append("<unset>");
		result.append(", informational: ");
		if (informationalESet) result.append(informational); else result.append("<unset>");
		result.append(", isNull: ");
		if (isNullESet) result.append(isNull); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} // Node
