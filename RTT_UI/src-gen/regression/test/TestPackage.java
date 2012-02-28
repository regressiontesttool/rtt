/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package regression.test;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see regression.test.TestFactory
 * @model kind="package"
 * @generated
 */
public interface TestPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "test";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "regression.test.tool";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "test";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TestPackage eINSTANCE = regression.test.impl.TestPackageImpl.init();

	/**
	 * The meta object id for the '{@link regression.test.impl.AttributeImpl <em>Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.impl.AttributeImpl
	 * @see regression.test.impl.TestPackageImpl#getAttribute()
	 * @generated
	 */
	int ATTRIBUTE = 0;

	/**
	 * The feature id for the '<em><b>Informational</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__INFORMATIONAL = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__NAME = 1;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__VALUE = 2;

	/**
	 * The number of structural features of the '<em>Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link regression.test.impl.AttributeListImpl <em>Attribute List</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.impl.AttributeListImpl
	 * @see regression.test.impl.TestPackageImpl#getAttributeList()
	 * @generated
	 */
	int ATTRIBUTE_LIST = 1;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_LIST__GROUP = 0;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_LIST__ATTRIBUTE = 1;

	/**
	 * The number of structural features of the '<em>Attribute List</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_LIST_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link regression.test.impl.ChildrenListImpl <em>Children List</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.impl.ChildrenListImpl
	 * @see regression.test.impl.TestPackageImpl#getChildrenList()
	 * @generated
	 */
	int CHILDREN_LIST = 2;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHILDREN_LIST__GROUP = 0;

	/**
	 * The feature id for the '<em><b>Node</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHILDREN_LIST__NODE = 1;

	/**
	 * The number of structural features of the '<em>Children List</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHILDREN_LIST_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link regression.test.impl.ClassableImpl <em>Classable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.impl.ClassableImpl
	 * @see regression.test.impl.TestPackageImpl#getClassable()
	 * @generated
	 */
	int CLASSABLE = 3;

	/**
	 * The feature id for the '<em><b>Full Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSABLE__FULL_NAME = 0;

	/**
	 * The feature id for the '<em><b>Simple Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSABLE__SIMPLE_NAME = 1;

	/**
	 * The number of structural features of the '<em>Classable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSABLE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link regression.test.impl.DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.impl.DocumentRootImpl
	 * @see regression.test.impl.TestPackageImpl#getDocumentRoot()
	 * @generated
	 */
	int DOCUMENT_ROOT = 4;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Lexer Output</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__LEXER_OUTPUT = 3;

	/**
	 * The feature id for the '<em><b>Parser Output</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__PARSER_OUTPUT = 4;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link regression.test.impl.LexerOutputTypeImpl <em>Lexer Output Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.impl.LexerOutputTypeImpl
	 * @see regression.test.impl.TestPackageImpl#getLexerOutputType()
	 * @generated
	 */
	int LEXER_OUTPUT_TYPE = 5;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEXER_OUTPUT_TYPE__GROUP = 0;

	/**
	 * The feature id for the '<em><b>Token</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEXER_OUTPUT_TYPE__TOKEN = 1;

	/**
	 * The number of structural features of the '<em>Lexer Output Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LEXER_OUTPUT_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link regression.test.impl.NodeImpl <em>Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.impl.NodeImpl
	 * @see regression.test.impl.TestPackageImpl#getNode()
	 * @generated
	 */
	int NODE = 6;

	/**
	 * The feature id for the '<em><b>Full Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__FULL_NAME = CLASSABLE__FULL_NAME;

	/**
	 * The feature id for the '<em><b>Simple Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__SIMPLE_NAME = CLASSABLE__SIMPLE_NAME;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__ATTRIBUTES = CLASSABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__CHILDREN = CLASSABLE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Is Null</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__IS_NULL = CLASSABLE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_FEATURE_COUNT = CLASSABLE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link regression.test.impl.ParserOutputTypeImpl <em>Parser Output Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.impl.ParserOutputTypeImpl
	 * @see regression.test.impl.TestPackageImpl#getParserOutputType()
	 * @generated
	 */
	int PARSER_OUTPUT_TYPE = 7;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARSER_OUTPUT_TYPE__GROUP = 0;

	/**
	 * The feature id for the '<em><b>Tree</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARSER_OUTPUT_TYPE__TREE = 1;

	/**
	 * The number of structural features of the '<em>Parser Output Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARSER_OUTPUT_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link regression.test.impl.TokenImpl <em>Token</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.impl.TokenImpl
	 * @see regression.test.impl.TestPackageImpl#getToken()
	 * @generated
	 */
	int TOKEN = 8;

	/**
	 * The feature id for the '<em><b>Full Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOKEN__FULL_NAME = CLASSABLE__FULL_NAME;

	/**
	 * The feature id for the '<em><b>Simple Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOKEN__SIMPLE_NAME = CLASSABLE__SIMPLE_NAME;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOKEN__ATTRIBUTES = CLASSABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Eof</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOKEN__IS_EOF = CLASSABLE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Token</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOKEN_FEATURE_COUNT = CLASSABLE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link regression.test.impl.TreeImpl <em>Tree</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.impl.TreeImpl
	 * @see regression.test.impl.TestPackageImpl#getTree()
	 * @generated
	 */
	int TREE = 9;

	/**
	 * The feature id for the '<em><b>Full Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TREE__FULL_NAME = CLASSABLE__FULL_NAME;

	/**
	 * The feature id for the '<em><b>Simple Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TREE__SIMPLE_NAME = CLASSABLE__SIMPLE_NAME;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TREE__GROUP = CLASSABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Node</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TREE__NODE = CLASSABLE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Tree</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TREE_FEATURE_COUNT = CLASSABLE_FEATURE_COUNT + 2;


	/**
	 * Returns the meta object for class '{@link regression.test.Attribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute</em>'.
	 * @see regression.test.Attribute
	 * @generated
	 */
	EClass getAttribute();

	/**
	 * Returns the meta object for the attribute '{@link regression.test.Attribute#isInformational <em>Informational</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Informational</em>'.
	 * @see regression.test.Attribute#isInformational()
	 * @see #getAttribute()
	 * @generated
	 */
	EAttribute getAttribute_Informational();

	/**
	 * Returns the meta object for the attribute '{@link regression.test.Attribute#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see regression.test.Attribute#getName()
	 * @see #getAttribute()
	 * @generated
	 */
	EAttribute getAttribute_Name();

	/**
	 * Returns the meta object for the attribute '{@link regression.test.Attribute#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see regression.test.Attribute#getValue()
	 * @see #getAttribute()
	 * @generated
	 */
	EAttribute getAttribute_Value();

	/**
	 * Returns the meta object for class '{@link regression.test.AttributeList <em>Attribute List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute List</em>'.
	 * @see regression.test.AttributeList
	 * @generated
	 */
	EClass getAttributeList();

	/**
	 * Returns the meta object for the attribute list '{@link regression.test.AttributeList#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see regression.test.AttributeList#getGroup()
	 * @see #getAttributeList()
	 * @generated
	 */
	EAttribute getAttributeList_Group();

	/**
	 * Returns the meta object for the containment reference list '{@link regression.test.AttributeList#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attribute</em>'.
	 * @see regression.test.AttributeList#getAttribute()
	 * @see #getAttributeList()
	 * @generated
	 */
	EReference getAttributeList_Attribute();

	/**
	 * Returns the meta object for class '{@link regression.test.ChildrenList <em>Children List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Children List</em>'.
	 * @see regression.test.ChildrenList
	 * @generated
	 */
	EClass getChildrenList();

	/**
	 * Returns the meta object for the attribute list '{@link regression.test.ChildrenList#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see regression.test.ChildrenList#getGroup()
	 * @see #getChildrenList()
	 * @generated
	 */
	EAttribute getChildrenList_Group();

	/**
	 * Returns the meta object for the containment reference list '{@link regression.test.ChildrenList#getNode <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Node</em>'.
	 * @see regression.test.ChildrenList#getNode()
	 * @see #getChildrenList()
	 * @generated
	 */
	EReference getChildrenList_Node();

	/**
	 * Returns the meta object for class '{@link regression.test.Classable <em>Classable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Classable</em>'.
	 * @see regression.test.Classable
	 * @generated
	 */
	EClass getClassable();

	/**
	 * Returns the meta object for the attribute '{@link regression.test.Classable#getFullName <em>Full Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Full Name</em>'.
	 * @see regression.test.Classable#getFullName()
	 * @see #getClassable()
	 * @generated
	 */
	EAttribute getClassable_FullName();

	/**
	 * Returns the meta object for the attribute '{@link regression.test.Classable#getSimpleName <em>Simple Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Simple Name</em>'.
	 * @see regression.test.Classable#getSimpleName()
	 * @see #getClassable()
	 * @generated
	 */
	EAttribute getClassable_SimpleName();

	/**
	 * Returns the meta object for class '{@link regression.test.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see regression.test.DocumentRoot
	 * @generated
	 */
	EClass getDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link regression.test.DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see regression.test.DocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link regression.test.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see regression.test.DocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link regression.test.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see regression.test.DocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link regression.test.DocumentRoot#getLexerOutput <em>Lexer Output</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Lexer Output</em>'.
	 * @see regression.test.DocumentRoot#getLexerOutput()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_LexerOutput();

	/**
	 * Returns the meta object for the containment reference '{@link regression.test.DocumentRoot#getParserOutput <em>Parser Output</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Parser Output</em>'.
	 * @see regression.test.DocumentRoot#getParserOutput()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_ParserOutput();

	/**
	 * Returns the meta object for class '{@link regression.test.LexerOutputType <em>Lexer Output Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Lexer Output Type</em>'.
	 * @see regression.test.LexerOutputType
	 * @generated
	 */
	EClass getLexerOutputType();

	/**
	 * Returns the meta object for the attribute list '{@link regression.test.LexerOutputType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see regression.test.LexerOutputType#getGroup()
	 * @see #getLexerOutputType()
	 * @generated
	 */
	EAttribute getLexerOutputType_Group();

	/**
	 * Returns the meta object for the containment reference list '{@link regression.test.LexerOutputType#getToken <em>Token</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Token</em>'.
	 * @see regression.test.LexerOutputType#getToken()
	 * @see #getLexerOutputType()
	 * @generated
	 */
	EReference getLexerOutputType_Token();

	/**
	 * Returns the meta object for class '{@link regression.test.Node <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node</em>'.
	 * @see regression.test.Node
	 * @generated
	 */
	EClass getNode();

	/**
	 * Returns the meta object for the containment reference '{@link regression.test.Node#getAttributes <em>Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Attributes</em>'.
	 * @see regression.test.Node#getAttributes()
	 * @see #getNode()
	 * @generated
	 */
	EReference getNode_Attributes();

	/**
	 * Returns the meta object for the containment reference '{@link regression.test.Node#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Children</em>'.
	 * @see regression.test.Node#getChildren()
	 * @see #getNode()
	 * @generated
	 */
	EReference getNode_Children();

	/**
	 * Returns the meta object for the attribute '{@link regression.test.Node#isIsNull <em>Is Null</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Null</em>'.
	 * @see regression.test.Node#isIsNull()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_IsNull();

	/**
	 * Returns the meta object for class '{@link regression.test.ParserOutputType <em>Parser Output Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parser Output Type</em>'.
	 * @see regression.test.ParserOutputType
	 * @generated
	 */
	EClass getParserOutputType();

	/**
	 * Returns the meta object for the attribute list '{@link regression.test.ParserOutputType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see regression.test.ParserOutputType#getGroup()
	 * @see #getParserOutputType()
	 * @generated
	 */
	EAttribute getParserOutputType_Group();

	/**
	 * Returns the meta object for the containment reference list '{@link regression.test.ParserOutputType#getTree <em>Tree</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Tree</em>'.
	 * @see regression.test.ParserOutputType#getTree()
	 * @see #getParserOutputType()
	 * @generated
	 */
	EReference getParserOutputType_Tree();

	/**
	 * Returns the meta object for class '{@link regression.test.Token <em>Token</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Token</em>'.
	 * @see regression.test.Token
	 * @generated
	 */
	EClass getToken();

	/**
	 * Returns the meta object for the containment reference '{@link regression.test.Token#getAttributes <em>Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Attributes</em>'.
	 * @see regression.test.Token#getAttributes()
	 * @see #getToken()
	 * @generated
	 */
	EReference getToken_Attributes();

	/**
	 * Returns the meta object for the attribute '{@link regression.test.Token#isIsEof <em>Is Eof</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Eof</em>'.
	 * @see regression.test.Token#isIsEof()
	 * @see #getToken()
	 * @generated
	 */
	EAttribute getToken_IsEof();

	/**
	 * Returns the meta object for class '{@link regression.test.Tree <em>Tree</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tree</em>'.
	 * @see regression.test.Tree
	 * @generated
	 */
	EClass getTree();

	/**
	 * Returns the meta object for the attribute list '{@link regression.test.Tree#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see regression.test.Tree#getGroup()
	 * @see #getTree()
	 * @generated
	 */
	EAttribute getTree_Group();

	/**
	 * Returns the meta object for the containment reference list '{@link regression.test.Tree#getNode <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Node</em>'.
	 * @see regression.test.Tree#getNode()
	 * @see #getTree()
	 * @generated
	 */
	EReference getTree_Node();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TestFactory getTestFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link regression.test.impl.AttributeImpl <em>Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.impl.AttributeImpl
		 * @see regression.test.impl.TestPackageImpl#getAttribute()
		 * @generated
		 */
		EClass ATTRIBUTE = eINSTANCE.getAttribute();

		/**
		 * The meta object literal for the '<em><b>Informational</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE__INFORMATIONAL = eINSTANCE.getAttribute_Informational();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE__NAME = eINSTANCE.getAttribute_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE__VALUE = eINSTANCE.getAttribute_Value();

		/**
		 * The meta object literal for the '{@link regression.test.impl.AttributeListImpl <em>Attribute List</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.impl.AttributeListImpl
		 * @see regression.test.impl.TestPackageImpl#getAttributeList()
		 * @generated
		 */
		EClass ATTRIBUTE_LIST = eINSTANCE.getAttributeList();

		/**
		 * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE_LIST__GROUP = eINSTANCE.getAttributeList_Group();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ATTRIBUTE_LIST__ATTRIBUTE = eINSTANCE.getAttributeList_Attribute();

		/**
		 * The meta object literal for the '{@link regression.test.impl.ChildrenListImpl <em>Children List</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.impl.ChildrenListImpl
		 * @see regression.test.impl.TestPackageImpl#getChildrenList()
		 * @generated
		 */
		EClass CHILDREN_LIST = eINSTANCE.getChildrenList();

		/**
		 * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHILDREN_LIST__GROUP = eINSTANCE.getChildrenList_Group();

		/**
		 * The meta object literal for the '<em><b>Node</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHILDREN_LIST__NODE = eINSTANCE.getChildrenList_Node();

		/**
		 * The meta object literal for the '{@link regression.test.impl.ClassableImpl <em>Classable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.impl.ClassableImpl
		 * @see regression.test.impl.TestPackageImpl#getClassable()
		 * @generated
		 */
		EClass CLASSABLE = eINSTANCE.getClassable();

		/**
		 * The meta object literal for the '<em><b>Full Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CLASSABLE__FULL_NAME = eINSTANCE.getClassable_FullName();

		/**
		 * The meta object literal for the '<em><b>Simple Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CLASSABLE__SIMPLE_NAME = eINSTANCE.getClassable_SimpleName();

		/**
		 * The meta object literal for the '{@link regression.test.impl.DocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.impl.DocumentRootImpl
		 * @see regression.test.impl.TestPackageImpl#getDocumentRoot()
		 * @generated
		 */
		EClass DOCUMENT_ROOT = eINSTANCE.getDocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENT_ROOT__MIXED = eINSTANCE.getDocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getDocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getDocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Lexer Output</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__LEXER_OUTPUT = eINSTANCE.getDocumentRoot_LexerOutput();

		/**
		 * The meta object literal for the '<em><b>Parser Output</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__PARSER_OUTPUT = eINSTANCE.getDocumentRoot_ParserOutput();

		/**
		 * The meta object literal for the '{@link regression.test.impl.LexerOutputTypeImpl <em>Lexer Output Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.impl.LexerOutputTypeImpl
		 * @see regression.test.impl.TestPackageImpl#getLexerOutputType()
		 * @generated
		 */
		EClass LEXER_OUTPUT_TYPE = eINSTANCE.getLexerOutputType();

		/**
		 * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LEXER_OUTPUT_TYPE__GROUP = eINSTANCE.getLexerOutputType_Group();

		/**
		 * The meta object literal for the '<em><b>Token</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LEXER_OUTPUT_TYPE__TOKEN = eINSTANCE.getLexerOutputType_Token();

		/**
		 * The meta object literal for the '{@link regression.test.impl.NodeImpl <em>Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.impl.NodeImpl
		 * @see regression.test.impl.TestPackageImpl#getNode()
		 * @generated
		 */
		EClass NODE = eINSTANCE.getNode();

		/**
		 * The meta object literal for the '<em><b>Attributes</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE__ATTRIBUTES = eINSTANCE.getNode_Attributes();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE__CHILDREN = eINSTANCE.getNode_Children();

		/**
		 * The meta object literal for the '<em><b>Is Null</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__IS_NULL = eINSTANCE.getNode_IsNull();

		/**
		 * The meta object literal for the '{@link regression.test.impl.ParserOutputTypeImpl <em>Parser Output Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.impl.ParserOutputTypeImpl
		 * @see regression.test.impl.TestPackageImpl#getParserOutputType()
		 * @generated
		 */
		EClass PARSER_OUTPUT_TYPE = eINSTANCE.getParserOutputType();

		/**
		 * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARSER_OUTPUT_TYPE__GROUP = eINSTANCE.getParserOutputType_Group();

		/**
		 * The meta object literal for the '<em><b>Tree</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARSER_OUTPUT_TYPE__TREE = eINSTANCE.getParserOutputType_Tree();

		/**
		 * The meta object literal for the '{@link regression.test.impl.TokenImpl <em>Token</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.impl.TokenImpl
		 * @see regression.test.impl.TestPackageImpl#getToken()
		 * @generated
		 */
		EClass TOKEN = eINSTANCE.getToken();

		/**
		 * The meta object literal for the '<em><b>Attributes</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TOKEN__ATTRIBUTES = eINSTANCE.getToken_Attributes();

		/**
		 * The meta object literal for the '<em><b>Is Eof</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TOKEN__IS_EOF = eINSTANCE.getToken_IsEof();

		/**
		 * The meta object literal for the '{@link regression.test.impl.TreeImpl <em>Tree</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.impl.TreeImpl
		 * @see regression.test.impl.TestPackageImpl#getTree()
		 * @generated
		 */
		EClass TREE = eINSTANCE.getTree();

		/**
		 * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TREE__GROUP = eINSTANCE.getTree_Group();

		/**
		 * The meta object literal for the '<em><b>Node</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TREE__NODE = eINSTANCE.getTree_Node();

	}

} //TestPackage
