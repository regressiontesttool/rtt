/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package regression.test;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link regression.test.Node#getAttributes <em>Attributes</em>}</li>
 *   <li>{@link regression.test.Node#getChildren <em>Children</em>}</li>
 *   <li>{@link regression.test.Node#isIsNull <em>Is Null</em>}</li>
 * </ul>
 * </p>
 *
 * @see regression.test.TestPackage#getNode()
 * @model extendedMetaData="name='node' kind='elementOnly'"
 * @generated
 */
public interface Node extends Classable {
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
	 * @see regression.test.TestPackage#getNode_Attributes()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='attributes' namespace='##targetNamespace'"
	 * @generated
	 */
	AttributeList getAttributes();

	/**
	 * Sets the value of the '{@link regression.test.Node#getAttributes <em>Attributes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attributes</em>' containment reference.
	 * @see #getAttributes()
	 * @generated
	 */
	void setAttributes(AttributeList value);

	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference.
	 * @see #setChildren(ChildrenList)
	 * @see regression.test.TestPackage#getNode_Children()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='children' namespace='##targetNamespace'"
	 * @generated
	 */
	ChildrenList getChildren();

	/**
	 * Sets the value of the '{@link regression.test.Node#getChildren <em>Children</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Children</em>' containment reference.
	 * @see #getChildren()
	 * @generated
	 */
	void setChildren(ChildrenList value);

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
	boolean isIsNull();

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
	void setIsNull(boolean value);

	/**
	 * Unsets the value of the '{@link regression.test.Node#isIsNull <em>Is Null</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetIsNull()
	 * @see #isIsNull()
	 * @see #setIsNull(boolean)
	 * @generated
	 */
	void unsetIsNull();

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
	boolean isSetIsNull();

} // Node
