/**
 */
package regression.test;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

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
public class TestPackage extends EPackageImpl {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNAME = "test";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNS_URI = "regression.test.tool";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String eNS_PREFIX = "test";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final TestPackage eINSTANCE = regression.test.TestPackage.init();

	/**
	 * The meta object id for the '{@link regression.test.Attribute <em>Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.Attribute
	 * @see regression.test.TestPackage#getAttribute()
	 * @generated
	 */
	public static final int ATTRIBUTE = 0;

	/**
	 * The feature id for the '<em><b>Informational</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ATTRIBUTE__INFORMATIONAL = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ATTRIBUTE__NAME = 1;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ATTRIBUTE__VALUE = 2;

	/**
	 * The number of structural features of the '<em>Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int ATTRIBUTE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link regression.test.Classable <em>Classable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.Classable
	 * @see regression.test.TestPackage#getClassable()
	 * @generated
	 */
	public static final int CLASSABLE = 1;

	/**
	 * The feature id for the '<em><b>Full Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int CLASSABLE__FULL_NAME = 0;

	/**
	 * The feature id for the '<em><b>Simple Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int CLASSABLE__SIMPLE_NAME = 1;

	/**
	 * The number of structural features of the '<em>Classable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int CLASSABLE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link regression.test.DocumentRoot <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.DocumentRoot
	 * @see regression.test.TestPackage#getDocumentRoot()
	 * @generated
	 */
	public static final int DOCUMENT_ROOT = 2;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Lexer Output</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DOCUMENT_ROOT__LEXER_OUTPUT = 3;

	/**
	 * The feature id for the '<em><b>Parser Output</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DOCUMENT_ROOT__PARSER_OUTPUT = 4;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int DOCUMENT_ROOT_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link regression.test.LexerOutputType <em>Lexer Output Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.LexerOutputType
	 * @see regression.test.TestPackage#getLexerOutputType()
	 * @generated
	 */
	public static final int LEXER_OUTPUT_TYPE = 3;

	/**
	 * The feature id for the '<em><b>Token</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int LEXER_OUTPUT_TYPE__TOKEN = 0;

	/**
	 * The number of structural features of the '<em>Lexer Output Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int LEXER_OUTPUT_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link regression.test.Node <em>Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.Node
	 * @see regression.test.TestPackage#getNode()
	 * @generated
	 */
	public static final int NODE = 4;

	/**
	 * The feature id for the '<em><b>Full Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NODE__FULL_NAME = CLASSABLE__FULL_NAME;

	/**
	 * The feature id for the '<em><b>Simple Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NODE__SIMPLE_NAME = CLASSABLE__SIMPLE_NAME;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NODE__ATTRIBUTE = CLASSABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Node</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NODE__NODE = CLASSABLE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Is Null</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NODE__IS_NULL = CLASSABLE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NODE__METHOD = CLASSABLE_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int NODE_FEATURE_COUNT = CLASSABLE_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link regression.test.ParserOutputType <em>Parser Output Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.ParserOutputType
	 * @see regression.test.TestPackage#getParserOutputType()
	 * @generated
	 */
	public static final int PARSER_OUTPUT_TYPE = 5;

	/**
	 * The feature id for the '<em><b>Tree</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int PARSER_OUTPUT_TYPE__TREE = 0;

	/**
	 * The number of structural features of the '<em>Parser Output Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int PARSER_OUTPUT_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link regression.test.Token <em>Token</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.Token
	 * @see regression.test.TestPackage#getToken()
	 * @generated
	 */
	public static final int TOKEN = 6;

	/**
	 * The feature id for the '<em><b>Full Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TOKEN__FULL_NAME = CLASSABLE__FULL_NAME;

	/**
	 * The feature id for the '<em><b>Simple Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TOKEN__SIMPLE_NAME = CLASSABLE__SIMPLE_NAME;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TOKEN__ATTRIBUTE = CLASSABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Eof</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TOKEN__IS_EOF = CLASSABLE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Token</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TOKEN_FEATURE_COUNT = CLASSABLE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link regression.test.Tree <em>Tree</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see regression.test.Tree
	 * @see regression.test.TestPackage#getTree()
	 * @generated
	 */
	public static final int TREE = 7;

	/**
	 * The feature id for the '<em><b>Full Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TREE__FULL_NAME = CLASSABLE__FULL_NAME;

	/**
	 * The feature id for the '<em><b>Simple Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TREE__SIMPLE_NAME = CLASSABLE__SIMPLE_NAME;

	/**
	 * The feature id for the '<em><b>Node</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TREE__NODE = CLASSABLE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Tree</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	public static final int TREE_FEATURE_COUNT = CLASSABLE_FEATURE_COUNT + 1;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass attributeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass classableEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass documentRootEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lexerOutputTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parserOutputTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tokenEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass treeEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see regression.test.TestPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private TestPackage() {
		super(eNS_URI, TestFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link TestPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static TestPackage init() {
		if (isInited) return (TestPackage)EPackage.Registry.INSTANCE.getEPackage(TestPackage.eNS_URI);

		// Obtain or create and register package
		TestPackage theTestPackage = (TestPackage)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof TestPackage ? EPackage.Registry.INSTANCE.get(eNS_URI) : new TestPackage());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theTestPackage.createPackageContents();

		// Initialize created meta-data
		theTestPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theTestPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(TestPackage.eNS_URI, theTestPackage);
		return theTestPackage;
	}


	/**
	 * Returns the meta object for class '{@link regression.test.Attribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute</em>'.
	 * @see regression.test.Attribute
	 * @generated
	 */
	public EClass getAttribute() {
		return attributeEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link regression.test.Attribute#isInformational <em>Informational</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Informational</em>'.
	 * @see regression.test.Attribute#isInformational()
	 * @see #getAttribute()
	 * @generated
	 */
	public EAttribute getAttribute_Informational() {
		return (EAttribute)attributeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the attribute '{@link regression.test.Attribute#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see regression.test.Attribute#getName()
	 * @see #getAttribute()
	 * @generated
	 */
	public EAttribute getAttribute_Name() {
		return (EAttribute)attributeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for the attribute '{@link regression.test.Attribute#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see regression.test.Attribute#getValue()
	 * @see #getAttribute()
	 * @generated
	 */
	public EAttribute getAttribute_Value() {
		return (EAttribute)attributeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * Returns the meta object for class '{@link regression.test.Classable <em>Classable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Classable</em>'.
	 * @see regression.test.Classable
	 * @generated
	 */
	public EClass getClassable() {
		return classableEClass;
	}

	/**
	 * Returns the meta object for the attribute '{@link regression.test.Classable#getFullName <em>Full Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Full Name</em>'.
	 * @see regression.test.Classable#getFullName()
	 * @see #getClassable()
	 * @generated
	 */
	public EAttribute getClassable_FullName() {
		return (EAttribute)classableEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the attribute '{@link regression.test.Classable#getSimpleName <em>Simple Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Simple Name</em>'.
	 * @see regression.test.Classable#getSimpleName()
	 * @see #getClassable()
	 * @generated
	 */
	public EAttribute getClassable_SimpleName() {
		return (EAttribute)classableEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for class '{@link regression.test.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see regression.test.DocumentRoot
	 * @generated
	 */
	public EClass getDocumentRoot() {
		return documentRootEClass;
	}

	/**
	 * Returns the meta object for the attribute list '{@link regression.test.DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see regression.test.DocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	public EAttribute getDocumentRoot_Mixed() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for the map '{@link regression.test.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see regression.test.DocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	public EReference getDocumentRoot_XMLNSPrefixMap() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for the map '{@link regression.test.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see regression.test.DocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	public EReference getDocumentRoot_XSISchemaLocation() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * Returns the meta object for the containment reference '{@link regression.test.DocumentRoot#getLexerOutput <em>Lexer Output</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Lexer Output</em>'.
	 * @see regression.test.DocumentRoot#getLexerOutput()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	public EReference getDocumentRoot_LexerOutput() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * Returns the meta object for the containment reference '{@link regression.test.DocumentRoot#getParserOutput <em>Parser Output</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Parser Output</em>'.
	 * @see regression.test.DocumentRoot#getParserOutput()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	public EReference getDocumentRoot_ParserOutput() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * Returns the meta object for class '{@link regression.test.LexerOutputType <em>Lexer Output Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Lexer Output Type</em>'.
	 * @see regression.test.LexerOutputType
	 * @generated
	 */
	public EClass getLexerOutputType() {
		return lexerOutputTypeEClass;
	}

	/**
	 * Returns the meta object for the containment reference list '{@link regression.test.LexerOutputType#getToken <em>Token</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Token</em>'.
	 * @see regression.test.LexerOutputType#getToken()
	 * @see #getLexerOutputType()
	 * @generated
	 */
	public EReference getLexerOutputType_Token() {
		return (EReference)lexerOutputTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link regression.test.Node <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node</em>'.
	 * @see regression.test.Node
	 * @generated
	 */
	public EClass getNode() {
		return nodeEClass;
	}

	/**
	 * Returns the meta object for the containment reference list '{@link regression.test.Node#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attribute</em>'.
	 * @see regression.test.Node#getAttribute()
	 * @see #getNode()
	 * @generated
	 */
	public EReference getNode_Attribute() {
		return (EReference)nodeEClass.getEStructuralFeatures().get(0);
	}


	/**
	 * Returns the meta object for the containment reference list '{@link regression.test.Node#getNode <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Node</em>'.
	 * @see regression.test.Node#getNode()
	 * @see #getNode()
	 * @generated
	 */
	public EReference getNode_Node() {
		return (EReference)nodeEClass.getEStructuralFeatures().get(1);
	}


	/**
	 * Returns the meta object for the attribute '{@link regression.test.Node#isIsNull <em>Is Null</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Null</em>'.
	 * @see regression.test.Node#isIsNull()
	 * @see #getNode()
	 * @generated
	 */
	public EAttribute getNode_IsNull() {
		return (EAttribute)nodeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * Returns the meta object for the attribute '{@link regression.test.Node#getMethod <em>Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Method</em>'.
	 * @see regression.test.Node#getMethod()
	 * @see #getNode()
	 * @generated
	 */
	public EAttribute getNode_Method() {
		return (EAttribute)nodeEClass.getEStructuralFeatures().get(3);
	}


	/**
	 * Returns the meta object for class '{@link regression.test.ParserOutputType <em>Parser Output Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parser Output Type</em>'.
	 * @see regression.test.ParserOutputType
	 * @generated
	 */
	public EClass getParserOutputType() {
		return parserOutputTypeEClass;
	}

	/**
	 * Returns the meta object for the containment reference list '{@link regression.test.ParserOutputType#getTree <em>Tree</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Tree</em>'.
	 * @see regression.test.ParserOutputType#getTree()
	 * @see #getParserOutputType()
	 * @generated
	 */
	public EReference getParserOutputType_Tree() {
		return (EReference)parserOutputTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the meta object for class '{@link regression.test.Token <em>Token</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Token</em>'.
	 * @see regression.test.Token
	 * @generated
	 */
	public EClass getToken() {
		return tokenEClass;
	}

	/**
	 * Returns the meta object for the containment reference list '{@link regression.test.Token#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attribute</em>'.
	 * @see regression.test.Token#getAttribute()
	 * @see #getToken()
	 * @generated
	 */
	public EReference getToken_Attribute() {
		return (EReference)tokenEClass.getEStructuralFeatures().get(0);
	}


	/**
	 * Returns the meta object for the attribute '{@link regression.test.Token#isIsEof <em>Is Eof</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Eof</em>'.
	 * @see regression.test.Token#isIsEof()
	 * @see #getToken()
	 * @generated
	 */
	public EAttribute getToken_IsEof() {
		return (EAttribute)tokenEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * Returns the meta object for class '{@link regression.test.Tree <em>Tree</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tree</em>'.
	 * @see regression.test.Tree
	 * @generated
	 */
	public EClass getTree() {
		return treeEClass;
	}

	/**
	 * Returns the meta object for the containment reference list '{@link regression.test.Tree#getNode <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Node</em>'.
	 * @see regression.test.Tree#getNode()
	 * @see #getTree()
	 * @generated
	 */
	public EReference getTree_Node() {
		return (EReference)treeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	public TestFactory getTestFactory() {
		return (TestFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		attributeEClass = createEClass(ATTRIBUTE);
		createEAttribute(attributeEClass, ATTRIBUTE__INFORMATIONAL);
		createEAttribute(attributeEClass, ATTRIBUTE__NAME);
		createEAttribute(attributeEClass, ATTRIBUTE__VALUE);

		classableEClass = createEClass(CLASSABLE);
		createEAttribute(classableEClass, CLASSABLE__FULL_NAME);
		createEAttribute(classableEClass, CLASSABLE__SIMPLE_NAME);

		documentRootEClass = createEClass(DOCUMENT_ROOT);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__LEXER_OUTPUT);
		createEReference(documentRootEClass, DOCUMENT_ROOT__PARSER_OUTPUT);

		lexerOutputTypeEClass = createEClass(LEXER_OUTPUT_TYPE);
		createEReference(lexerOutputTypeEClass, LEXER_OUTPUT_TYPE__TOKEN);

		nodeEClass = createEClass(NODE);
		createEReference(nodeEClass, NODE__ATTRIBUTE);
		createEReference(nodeEClass, NODE__NODE);
		createEAttribute(nodeEClass, NODE__IS_NULL);
		createEAttribute(nodeEClass, NODE__METHOD);

		parserOutputTypeEClass = createEClass(PARSER_OUTPUT_TYPE);
		createEReference(parserOutputTypeEClass, PARSER_OUTPUT_TYPE__TREE);

		tokenEClass = createEClass(TOKEN);
		createEReference(tokenEClass, TOKEN__ATTRIBUTE);
		createEAttribute(tokenEClass, TOKEN__IS_EOF);

		treeEClass = createEClass(TREE);
		createEReference(treeEClass, TREE__NODE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		nodeEClass.getESuperTypes().add(this.getClassable());
		tokenEClass.getESuperTypes().add(this.getClassable());
		treeEClass.getESuperTypes().add(this.getClassable());

		// Initialize classes and features; add operations and parameters
		initEClass(attributeEClass, Attribute.class, "Attribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAttribute_Informational(), theXMLTypePackage.getBoolean(), "informational", null, 0, 1, Attribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAttribute_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, Attribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAttribute_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, Attribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(classableEClass, Classable.class, "Classable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getClassable_FullName(), theXMLTypePackage.getString(), "fullName", null, 0, 1, Classable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getClassable_SimpleName(), theXMLTypePackage.getString(), "simpleName", null, 0, 1, Classable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_LexerOutput(), this.getLexerOutputType(), null, "lexerOutput", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_ParserOutput(), this.getParserOutputType(), null, "parserOutput", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(lexerOutputTypeEClass, LexerOutputType.class, "LexerOutputType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLexerOutputType_Token(), this.getToken(), null, "token", null, 0, -1, LexerOutputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(nodeEClass, Node.class, "Node", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getNode_Attribute(), this.getAttribute(), null, "attribute", null, 0, -1, Node.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getNode_Node(), this.getNode(), null, "node", null, 0, -1, Node.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNode_IsNull(), theXMLTypePackage.getBoolean(), "isNull", "false", 0, 1, Node.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNode_Method(), theXMLTypePackage.getString(), "method", null, 0, 1, Node.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parserOutputTypeEClass, ParserOutputType.class, "ParserOutputType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getParserOutputType_Tree(), this.getTree(), null, "tree", null, 0, -1, ParserOutputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(tokenEClass, Token.class, "Token", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getToken_Attribute(), this.getAttribute(), null, "attribute", null, 0, -1, Token.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getToken_IsEof(), theXMLTypePackage.getBoolean(), "isEof", "false", 0, 1, Token.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(treeEClass, Tree.class, "Tree", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTree_Node(), this.getNode(), null, "node", null, 0, -1, Tree.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";		
		addAnnotation
		  (attributeEClass, 
		   source, 
		   new String[] {
			 "name", "attribute",
			 "kind", "empty"
		   });		
		addAnnotation
		  (getAttribute_Informational(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "informational"
		   });		
		addAnnotation
		  (getAttribute_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
		addAnnotation
		  (getAttribute_Value(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "value"
		   });		
		addAnnotation
		  (classableEClass, 
		   source, 
		   new String[] {
			 "name", "classable",
			 "kind", "empty"
		   });		
		addAnnotation
		  (getClassable_FullName(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "fullName"
		   });		
		addAnnotation
		  (getClassable_SimpleName(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "simpleName"
		   });		
		addAnnotation
		  (documentRootEClass, 
		   source, 
		   new String[] {
			 "name", "",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getDocumentRoot_Mixed(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "name", ":mixed"
		   });		
		addAnnotation
		  (getDocumentRoot_XMLNSPrefixMap(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xmlns:prefix"
		   });		
		addAnnotation
		  (getDocumentRoot_XSISchemaLocation(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xsi:schemaLocation"
		   });		
		addAnnotation
		  (getDocumentRoot_LexerOutput(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "lexerOutput",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_ParserOutput(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "parserOutput",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (lexerOutputTypeEClass, 
		   source, 
		   new String[] {
			 "name", "lexerOutput_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getLexerOutputType_Token(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "token",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (nodeEClass, 
		   source, 
		   new String[] {
			 "name", "node",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getNode_Attribute(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "attribute",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getNode_Node(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "node",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getNode_IsNull(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "isNull"
		   });		
		addAnnotation
		  (getNode_Method(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "method"
		   });			
		addAnnotation
		  (parserOutputTypeEClass, 
		   source, 
		   new String[] {
			 "name", "parserOutput_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getParserOutputType_Tree(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "tree",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (tokenEClass, 
		   source, 
		   new String[] {
			 "name", "token",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getToken_Attribute(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "attribute",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getToken_IsEof(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "isEof"
		   });		
		addAnnotation
		  (treeEClass, 
		   source, 
		   new String[] {
			 "name", "tree",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getTree_Node(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "node",
			 "namespace", "##targetNamespace"
		   });
	}

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
	public interface Literals {
		/**
		 * The meta object literal for the '{@link regression.test.Attribute <em>Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.Attribute
		 * @see regression.test.TestPackage#getAttribute()
		 * @generated
		 */
		public static final EClass ATTRIBUTE = eINSTANCE.getAttribute();

		/**
		 * The meta object literal for the '<em><b>Informational</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute ATTRIBUTE__INFORMATIONAL = eINSTANCE.getAttribute_Informational();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute ATTRIBUTE__NAME = eINSTANCE.getAttribute_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute ATTRIBUTE__VALUE = eINSTANCE.getAttribute_Value();

		/**
		 * The meta object literal for the '{@link regression.test.Classable <em>Classable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.Classable
		 * @see regression.test.TestPackage#getClassable()
		 * @generated
		 */
		public static final EClass CLASSABLE = eINSTANCE.getClassable();

		/**
		 * The meta object literal for the '<em><b>Full Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute CLASSABLE__FULL_NAME = eINSTANCE.getClassable_FullName();

		/**
		 * The meta object literal for the '<em><b>Simple Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute CLASSABLE__SIMPLE_NAME = eINSTANCE.getClassable_SimpleName();

		/**
		 * The meta object literal for the '{@link regression.test.DocumentRoot <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.DocumentRoot
		 * @see regression.test.TestPackage#getDocumentRoot()
		 * @generated
		 */
		public static final EClass DOCUMENT_ROOT = eINSTANCE.getDocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute DOCUMENT_ROOT__MIXED = eINSTANCE.getDocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getDocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getDocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Lexer Output</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference DOCUMENT_ROOT__LEXER_OUTPUT = eINSTANCE.getDocumentRoot_LexerOutput();

		/**
		 * The meta object literal for the '<em><b>Parser Output</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference DOCUMENT_ROOT__PARSER_OUTPUT = eINSTANCE.getDocumentRoot_ParserOutput();

		/**
		 * The meta object literal for the '{@link regression.test.LexerOutputType <em>Lexer Output Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.LexerOutputType
		 * @see regression.test.TestPackage#getLexerOutputType()
		 * @generated
		 */
		public static final EClass LEXER_OUTPUT_TYPE = eINSTANCE.getLexerOutputType();

		/**
		 * The meta object literal for the '<em><b>Token</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference LEXER_OUTPUT_TYPE__TOKEN = eINSTANCE.getLexerOutputType_Token();

		/**
		 * The meta object literal for the '{@link regression.test.Node <em>Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.Node
		 * @see regression.test.TestPackage#getNode()
		 * @generated
		 */
		public static final EClass NODE = eINSTANCE.getNode();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference NODE__ATTRIBUTE = eINSTANCE.getNode_Attribute();

		/**
		 * The meta object literal for the '<em><b>Node</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference NODE__NODE = eINSTANCE.getNode_Node();

		/**
		 * The meta object literal for the '<em><b>Is Null</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute NODE__IS_NULL = eINSTANCE.getNode_IsNull();

		/**
		 * The meta object literal for the '<em><b>Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute NODE__METHOD = eINSTANCE.getNode_Method();

		/**
		 * The meta object literal for the '{@link regression.test.ParserOutputType <em>Parser Output Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.ParserOutputType
		 * @see regression.test.TestPackage#getParserOutputType()
		 * @generated
		 */
		public static final EClass PARSER_OUTPUT_TYPE = eINSTANCE.getParserOutputType();

		/**
		 * The meta object literal for the '<em><b>Tree</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference PARSER_OUTPUT_TYPE__TREE = eINSTANCE.getParserOutputType_Tree();

		/**
		 * The meta object literal for the '{@link regression.test.Token <em>Token</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.Token
		 * @see regression.test.TestPackage#getToken()
		 * @generated
		 */
		public static final EClass TOKEN = eINSTANCE.getToken();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference TOKEN__ATTRIBUTE = eINSTANCE.getToken_Attribute();

		/**
		 * The meta object literal for the '<em><b>Is Eof</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EAttribute TOKEN__IS_EOF = eINSTANCE.getToken_IsEof();

		/**
		 * The meta object literal for the '{@link regression.test.Tree <em>Tree</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see regression.test.Tree
		 * @see regression.test.TestPackage#getTree()
		 * @generated
		 */
		public static final EClass TREE = eINSTANCE.getTree();

		/**
		 * The meta object literal for the '<em><b>Node</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public static final EReference TREE__NODE = eINSTANCE.getTree_Node();

	}

} //TestPackage
