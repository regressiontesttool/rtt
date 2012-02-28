/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package regression.test;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Token</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link regression.test.Token#getAttributes <em>Attributes</em>}</li>
 *   <li>{@link regression.test.Token#isIsEof <em>Is Eof</em>}</li>
 * </ul>
 * </p>
 *
 * @see regression.test.TestPackage#getToken()
 * @model extendedMetaData="name='token' kind='elementOnly'"
 * @generated
 */
public interface Token extends Classable {
	/**
	 * Returns the value of the '<em><b>Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attributes</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attributes</em>' containment reference.
	 * @see #setAttributes(AttributeList)
	 * @see regression.test.TestPackage#getToken_Attributes()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='attributes' namespace='##targetNamespace'"
	 * @generated
	 */
	AttributeList getAttributes();

	/**
	 * Sets the value of the '{@link regression.test.Token#getAttributes <em>Attributes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attributes</em>' containment reference.
	 * @see #getAttributes()
	 * @generated
	 */
	void setAttributes(AttributeList value);

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
	boolean isIsEof();

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
	void setIsEof(boolean value);

	/**
	 * Unsets the value of the '{@link regression.test.Token#isIsEof <em>Is Eof</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetIsEof()
	 * @see #isIsEof()
	 * @see #setIsEof(boolean)
	 * @generated
	 */
	void unsetIsEof();

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
	boolean isSetIsEof();

} // Token
