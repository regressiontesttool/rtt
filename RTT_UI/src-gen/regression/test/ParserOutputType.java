/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package regression.test;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parser Output Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * type of parser output
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link regression.test.ParserOutputType#getGroup <em>Group</em>}</li>
 *   <li>{@link regression.test.ParserOutputType#getTree <em>Tree</em>}</li>
 * </ul>
 * </p>
 *
 * @see regression.test.TestPackage#getParserOutputType()
 * @model extendedMetaData="name='parserOutput_._type' kind='elementOnly'"
 * @generated
 */
public interface ParserOutputType extends EObject {
	/**
	 * Returns the value of the '<em><b>Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' attribute list.
	 * @see regression.test.TestPackage#getParserOutputType_Group()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:0'"
	 * @generated
	 */
	FeatureMap getGroup();

	/**
	 * Returns the value of the '<em><b>Tree</b></em>' containment reference list.
	 * The list contents are of type {@link regression.test.Tree}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tree</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tree</em>' containment reference list.
	 * @see regression.test.TestPackage#getParserOutputType_Tree()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='tree' namespace='##targetNamespace' group='#group:0'"
	 * @generated
	 */
	EList<Tree> getTree();

} // ParserOutputType
