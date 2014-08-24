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
 *   <li>{@link regression.test.OutputType#getAst <em>Ast</em>}</li>
 *   <li>{@link regression.test.OutputType#getExecutor <em>Executor</em>}</li>
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
	 * The cached value of the '{@link #getAst() <em>Ast</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAst()
	 * @generated
	 * @ordered
	 */
	protected Element ast;

	/**
	 * The default value of the '{@link #getExecutor() <em>Executor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecutor()
	 * @generated
	 * @ordered
	 */
	protected static final String EXECUTOR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExecutor() <em>Executor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecutor()
	 * @generated
	 * @ordered
	 */
	protected String executor = EXECUTOR_EDEFAULT;

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
	 * Returns the value of the '<em><b>Ast</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ast</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ast</em>' containment reference.
	 * @see #setAst(Element)
	 * @see regression.test.TestPackage#getOutputType_Ast()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='ast' namespace='##targetNamespace'"
	 * @generated
	 */
	public Element getAst() {
		return ast;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAst(Element newAst, NotificationChain msgs) {
		Element oldAst = ast;
		ast = newAst;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TestPackage.OUTPUT_TYPE__AST, oldAst, newAst);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * Sets the value of the '{@link regression.test.OutputType#getAst <em>Ast</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ast</em>' containment reference.
	 * @see #getAst()
	 * @generated
	 */
	public void setAst(Element newAst) {
		if (newAst != ast) {
			NotificationChain msgs = null;
			if (ast != null)
				msgs = ((InternalEObject)ast).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TestPackage.OUTPUT_TYPE__AST, null, msgs);
			if (newAst != null)
				msgs = ((InternalEObject)newAst).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TestPackage.OUTPUT_TYPE__AST, null, msgs);
			msgs = basicSetAst(newAst, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.OUTPUT_TYPE__AST, newAst, newAst));
	}

	/**
	 * Returns the value of the '<em><b>Executor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Executor</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Executor</em>' attribute.
	 * @see #setExecutor(String)
	 * @see regression.test.TestPackage#getOutputType_Executor()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='executor'"
	 * @generated
	 */
	public String getExecutor() {
		return executor;
	}

	/**
	 * Sets the value of the '{@link regression.test.OutputType#getExecutor <em>Executor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Executor</em>' attribute.
	 * @see #getExecutor()
	 * @generated
	 */
	public void setExecutor(String newExecutor) {
		String oldExecutor = executor;
		executor = newExecutor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestPackage.OUTPUT_TYPE__EXECUTOR, oldExecutor, executor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TestPackage.OUTPUT_TYPE__AST:
				return basicSetAst(null, msgs);
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
			case TestPackage.OUTPUT_TYPE__AST:
				return getAst();
			case TestPackage.OUTPUT_TYPE__EXECUTOR:
				return getExecutor();
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
			case TestPackage.OUTPUT_TYPE__AST:
				setAst((Element)newValue);
				return;
			case TestPackage.OUTPUT_TYPE__EXECUTOR:
				setExecutor((String)newValue);
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
			case TestPackage.OUTPUT_TYPE__AST:
				setAst((Element)null);
				return;
			case TestPackage.OUTPUT_TYPE__EXECUTOR:
				setExecutor(EXECUTOR_EDEFAULT);
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
			case TestPackage.OUTPUT_TYPE__AST:
				return ast != null;
			case TestPackage.OUTPUT_TYPE__EXECUTOR:
				return EXECUTOR_EDEFAULT == null ? executor != null : !EXECUTOR_EDEFAULT.equals(executor);
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
		result.append(" (executor: ");
		result.append(executor);
		result.append(')');
		return result.toString();
	}

} // OutputType
