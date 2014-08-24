/**
 */
package regression.test;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Output Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link regression.test.OutputType#getInitialElement <em>Initial Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see regression.test.TestPackage#getOutputType()
 * @model kind="class"
 *        extendedMetaData="name='output_._type' kind='elementOnly'"
 * @generated
 */
public class OutputType extends EObjectImpl implements EObject {
	/**
	 * The cached value of the '{@link #getInitialElement() <em>Initial Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialElement()
	 * @generated
	 * @ordered
	 */
	protected Element initialElement;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OutputType() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestPackage.Literals.OUTPUT_TYPE;
	}

	/**
	 * Returns the value of the '<em><b>Initial Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initial Element</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initial Element</em>' containment reference.
	 * @see #setInitialElement(Element)
	 * @see regression.test.TestPackage#getOutputType_InitialElement()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='initialElement' namespace='##targetNamespace'"
	 * @generated
	 */
	public Element getInitialElement() {
		return initialElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInitialElement(Element newInitialElement, NotificationChain msgs) {
		Element oldInitialElement = initialElement;
		initialElement = newInitialElement;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TestPackage.OUTPUT_TYPE__INITIAL_ELEMENT, oldInitialElement, newInitialElement);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * Sets the value of the '{@link regression.test.OutputType#getInitialElement <em>Initial Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Initial Element</em>' containment reference.
	 * @see #getInitialElement()
	 * @generated
	 */
	public void setInitialElement(Element newInitialElement) {
		if (newInitialElement != initialElement) {
			NotificationChain msgs = null;
			if (initialElement != null)
				msgs = ((InternalEObject)initialElement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TestPackage.OUTPUT_TYPE__INITIAL_ELEMENT, null, msgs);
			if (newInitialElement != null)
				msgs = ((InternalEObject)newInitialElement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TestPackage.OUTPUT_TYPE__INITIAL_ELEMENT, null, msgs);
			msgs = basicSetInitialElement(newInitialElement, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.OUTPUT_TYPE__INITIAL_ELEMENT, newInitialElement, newInitialElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TestPackage.OUTPUT_TYPE__INITIAL_ELEMENT:
				return basicSetInitialElement(null, msgs);
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
			case TestPackage.OUTPUT_TYPE__INITIAL_ELEMENT:
				return getInitialElement();
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
			case TestPackage.OUTPUT_TYPE__INITIAL_ELEMENT:
				setInitialElement((Element)newValue);
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
			case TestPackage.OUTPUT_TYPE__INITIAL_ELEMENT:
				setInitialElement((Element)null);
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
			case TestPackage.OUTPUT_TYPE__INITIAL_ELEMENT:
				return initialElement != null;
		}
		return super.eIsSet(featureID);
	}

} // OutputType
