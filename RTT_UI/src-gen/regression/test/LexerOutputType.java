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
 * A representation of the model object '<em><b>Lexer Output Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * type of lexer output
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link regression.test.LexerOutputType#getToken <em>Token</em>}</li>
 * </ul>
 * </p>
 *
 * @see regression.test.TestPackage#getLexerOutputType()
 * @model kind="class"
 *        extendedMetaData="name='lexerOutput_._type' kind='elementOnly'"
 * @generated
 */
public class LexerOutputType extends EObjectImpl implements EObject {
	/**
	 * The cached value of the '{@link #getToken() <em>Token</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToken()
	 * @generated
	 * @ordered
	 */
	protected EList<Token> token;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LexerOutputType() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestPackage.Literals.LEXER_OUTPUT_TYPE;
	}

	/**
	 * Returns the value of the '<em><b>Token</b></em>' containment reference list.
	 * The list contents are of type {@link regression.test.Token}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Token</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Token</em>' containment reference list.
	 * @see regression.test.TestPackage#getLexerOutputType_Token()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='token' namespace='##targetNamespace'"
	 * @generated
	 */
	public EList<Token> getToken() {
		if (token == null) {
			token = new EObjectContainmentEList<Token>(Token.class, this, TestPackage.LEXER_OUTPUT_TYPE__TOKEN);
		}
		return token;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TestPackage.LEXER_OUTPUT_TYPE__TOKEN:
				return ((InternalEList<?>)getToken()).basicRemove(otherEnd, msgs);
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
			case TestPackage.LEXER_OUTPUT_TYPE__TOKEN:
				return getToken();
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
			case TestPackage.LEXER_OUTPUT_TYPE__TOKEN:
				getToken().clear();
				getToken().addAll((Collection<? extends Token>)newValue);
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
			case TestPackage.LEXER_OUTPUT_TYPE__TOKEN:
				getToken().clear();
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
			case TestPackage.LEXER_OUTPUT_TYPE__TOKEN:
				return token != null && !token.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // LexerOutputType
