/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package regression.test;

import org.eclipse.emf.ecore.EObject;

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
 * @model extendedMetaData="name='attribute' kind='empty'"
 * @generated
 */
public interface Attribute extends EObject {
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
	boolean isInformational();

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
	void setInformational(boolean value);

	/**
	 * Unsets the value of the '{@link regression.test.Attribute#isInformational <em>Informational</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetInformational()
	 * @see #isInformational()
	 * @see #setInformational(boolean)
	 * @generated
	 */
	void unsetInformational();

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
	boolean isSetInformational();

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
	String getName();

	/**
	 * Sets the value of the '{@link regression.test.Attribute#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

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
	String getValue();

	/**
	 * Sets the value of the '{@link regression.test.Attribute#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

} // Attribute
