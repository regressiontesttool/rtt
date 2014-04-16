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
 * A representation of the model object '<em><b>Token</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link regression.test.Token#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link regression.test.Token#isIsEof <em>Is Eof</em>}</li>
 * </ul>
 * </p>
 *
 * @see regression.test.TestPackage#getToken()
 * @model kind="class"
 *        extendedMetaData="name='token' kind='elementOnly'"
 * @generated
 */
public class Token extends Classable {
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
	 * The default value of the '{@link #isIsEof() <em>Is Eof</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsEof()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_EOF_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsEof() <em>Is Eof</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsEof()
	 * @generated
	 * @ordered
	 */
	protected boolean isEof = IS_EOF_EDEFAULT;

	/**
	 * This is true if the Is Eof attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean isEofESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Token() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestPackage.Literals.TOKEN;
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
	 * @see regression.test.TestPackage#getToken_Attribute()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='attribute' namespace='##targetNamespace'"
	 * @generated
	 */
	public EList<Attribute> getAttribute() {
		if (attribute == null) {
			attribute = new EObjectContainmentEList<Attribute>(Attribute.class, this, TestPackage.TOKEN__ATTRIBUTE);
		}
		return attribute;
	}

	/**
	 * Returns the value of the '<em><b>Is Eof</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Eof</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Eof</em>' attribute.
	 * @see #isSetIsEof()
	 * @see #unsetIsEof()
	 * @see #setIsEof(boolean)
	 * @see regression.test.TestPackage#getToken_IsEof()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='isEof'"
	 * @generated
	 */
	public boolean isIsEof() {
		return isEof;
	}

	/**
	 * Sets the value of the '{@link regression.test.Token#isIsEof <em>Is Eof</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Eof</em>' attribute.
	 * @see #isSetIsEof()
	 * @see #unsetIsEof()
	 * @see #isIsEof()
	 * @generated
	 */
	public void setIsEof(boolean newIsEof) {
		boolean oldIsEof = isEof;
		isEof = newIsEof;
		boolean oldIsEofESet = isEofESet;
		isEofESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.TOKEN__IS_EOF, oldIsEof, isEof, !oldIsEofESet));
	}

	/**
	 * Unsets the value of the '{@link regression.test.Token#isIsEof <em>Is Eof</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetIsEof()
	 * @see #isIsEof()
	 * @see #setIsEof(boolean)
	 * @generated
	 */
	public void unsetIsEof() {
		boolean oldIsEof = isEof;
		boolean oldIsEofESet = isEofESet;
		isEof = IS_EOF_EDEFAULT;
		isEofESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TestPackage.TOKEN__IS_EOF, oldIsEof, IS_EOF_EDEFAULT, oldIsEofESet));
	}

	/**
	 * Returns whether the value of the '{@link regression.test.Token#isIsEof <em>Is Eof</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Is Eof</em>' attribute is set.
	 * @see #unsetIsEof()
	 * @see #isIsEof()
	 * @see #setIsEof(boolean)
	 * @generated
	 */
	public boolean isSetIsEof() {
		return isEofESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TestPackage.TOKEN__ATTRIBUTE:
				return ((InternalEList<?>)getAttribute()).basicRemove(otherEnd, msgs);
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
			case TestPackage.TOKEN__ATTRIBUTE:
				return getAttribute();
			case TestPackage.TOKEN__IS_EOF:
				return isIsEof();
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
			case TestPackage.TOKEN__ATTRIBUTE:
				getAttribute().clear();
				getAttribute().addAll((Collection<? extends Attribute>)newValue);
				return;
			case TestPackage.TOKEN__IS_EOF:
				setIsEof((Boolean)newValue);
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
			case TestPackage.TOKEN__ATTRIBUTE:
				getAttribute().clear();
				return;
			case TestPackage.TOKEN__IS_EOF:
				unsetIsEof();
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
			case TestPackage.TOKEN__ATTRIBUTE:
				return attribute != null && !attribute.isEmpty();
			case TestPackage.TOKEN__IS_EOF:
				return isSetIsEof();
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
		result.append(" (isEof: ");
		if (isEofESet) result.append(isEof); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} // Token
