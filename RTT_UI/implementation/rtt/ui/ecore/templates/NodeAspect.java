package rtt.ui.ecore.templates;

import org.eclipse.emf.codegen.ecore.genmodel.*;
import java.util.*;
import rtt.ui.ecore.util.GeneratorUtil;
import rtt.ui.ecore.EcoreAnnotation;
import org.eclipse.emf.codegen.util.ImportManager;

public class NodeAspect
{
  protected static String nl;
  public static synchronized NodeAspect create(String lineSeparator)
  {
    nl = lineSeparator;
    NodeAspect result = new NodeAspect();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = ";";
  protected final String TEXT_3 = NL + NL + "/**" + NL + " * This aspect was created by the Regression Test Tool (RTT)." + NL + " * Any changes will be erased if regenerated." + NL + " **/" + NL + "public aspect NodeAspect {";
  protected final String TEXT_4 = NL + "\t// ----- node annotations: ";
  protected final String TEXT_5 = " ----------" + NL + "\tdeclare @type: ";
  protected final String TEXT_6 = " : @Node; ";
  protected final String TEXT_7 = NL + "\tdeclare @method: * ";
  protected final String TEXT_8 = ".";
  protected final String TEXT_9 = "() : @Node.Informational;";
  protected final String TEXT_10 = NL + "\tdeclare @method: * ";
  protected final String TEXT_11 = ".";
  protected final String TEXT_12 = " : @Node.Informational;";
  protected final String TEXT_13 = NL + "\tdeclare @method: * ";
  protected final String TEXT_14 = ".";
  protected final String TEXT_15 = "() : @Node.Compare;";
  protected final String TEXT_16 = NL + "\tdeclare @method: * ";
  protected final String TEXT_17 = ".";
  protected final String TEXT_18 = " : @Node.Compare;";
  protected final String TEXT_19 = NL + "\tdeclare @method: * ";
  protected final String TEXT_20 = ".";
  protected final String TEXT_21 = "() : @Node.Child;";
  protected final String TEXT_22 = NL + "\tdeclare @method: * ";
  protected final String TEXT_23 = ".";
  protected final String TEXT_24 = " : @Node.Child;";
  protected final String TEXT_25 = NL + "\t";
  protected final String TEXT_26 = NL + "}";
  protected final String TEXT_27 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
	
	Object[] args = (Object[]) argument;
	
	GenModel genModel = (GenModel) args[0];
	List<GenClass> nodeClasses = GeneratorUtil.getAnnotatedClasses(genModel, EcoreAnnotation.NODE);
	
	String packageName = (String) args[1];
	
	ImportManager manager = new ImportManager(packageName);
	genModel.setImportManager(manager);
	
	if (packageName != null && !packageName.isEmpty()) {
    stringBuffer.append(TEXT_1);
    stringBuffer.append(packageName );
    stringBuffer.append(TEXT_2);
    	}
	genModel.markImportLocation(stringBuffer);
	genModel.addImport("rtt.annotations.Parser.Node"); 

    stringBuffer.append(TEXT_3);
    	for (GenClass nodeClass : nodeClasses) {
	manager.addImport(nodeClass.getQualifiedInterfaceName());
	
	String className = nodeClass.getName(); 
    stringBuffer.append(TEXT_4);
    stringBuffer.append(className );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(className );
    stringBuffer.append(TEXT_6);
    	
	
	for (GenFeature genFeature : GeneratorUtil.getNodeFeatures(nodeClass, EcoreAnnotation.NODE_INFORMATIONAL)) { 
    stringBuffer.append(TEXT_7);
    stringBuffer.append(className );
    stringBuffer.append(TEXT_8);
    stringBuffer.append(genFeature.getGetAccessor() );
    stringBuffer.append(TEXT_9);
    
	}	
	for (GenOperation genOperation : GeneratorUtil.getOperations(nodeClass, EcoreAnnotation.NODE_INFORMATIONAL)) { 
    stringBuffer.append(TEXT_10);
    stringBuffer.append(className );
    stringBuffer.append(TEXT_11);
    stringBuffer.append(genOperation.getFormattedName());
    stringBuffer.append(TEXT_12);
    	
	}	
	for (GenFeature genFeature : GeneratorUtil.getNodeFeatures(nodeClass, EcoreAnnotation.NODE_COMPARE)) { 
    stringBuffer.append(TEXT_13);
    stringBuffer.append(className );
    stringBuffer.append(TEXT_14);
    stringBuffer.append(genFeature.getGetAccessor() );
    stringBuffer.append(TEXT_15);
    
	}	
	for (GenOperation genOperation : GeneratorUtil.getOperations(nodeClass, EcoreAnnotation.NODE_COMPARE)) { 
    stringBuffer.append(TEXT_16);
    stringBuffer.append(className );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(genOperation.getFormattedName());
    stringBuffer.append(TEXT_18);
    	
	}	
	for (GenFeature genFeature : GeneratorUtil.getNodeFeatures(nodeClass, EcoreAnnotation.NODE_CHILDREN)) { 
    stringBuffer.append(TEXT_19);
    stringBuffer.append(className );
    stringBuffer.append(TEXT_20);
    stringBuffer.append(genFeature.getGetAccessor() );
    stringBuffer.append(TEXT_21);
    
	}	
	for (GenOperation genOperation : GeneratorUtil.getOperations(nodeClass, EcoreAnnotation.NODE_CHILDREN)) { 
    stringBuffer.append(TEXT_22);
    stringBuffer.append(className );
    stringBuffer.append(TEXT_23);
    stringBuffer.append(genOperation.getFormattedName());
    stringBuffer.append(TEXT_24);
    	
	} 
    stringBuffer.append(TEXT_25);
     } 
    stringBuffer.append(TEXT_26);
    
genModel.emitSortedImports();

    stringBuffer.append(TEXT_27);
    return stringBuffer.toString();
  }
}
