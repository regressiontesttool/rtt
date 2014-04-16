package rtt.ui.ecore.templates;

import java.util.*;
import org.eclipse.emf.codegen.ecore.genmodel.*;
import rtt.ui.ecore.util.GeneratorUtil;
import rtt.ui.ecore.EcoreAnnotation;
import org.eclipse.emf.codegen.util.ImportManager;

public class ParserClassAspect
{
  protected static String nl;
  public static synchronized ParserClassAspect create(String lineSeparator)
  {
    nl = lineSeparator;
    ParserClassAspect result = new ParserClassAspect();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = ";";
  protected final String TEXT_3 = NL + NL + "/**" + NL + " * This aspect was created by the Regression Test Tool (RTT)." + NL + " * Any changes will be erased if regenerated." + NL + " **/" + NL + "public aspect ParserAspect {";
  protected final String TEXT_4 = NL + "\t// ----- parser annotations: ";
  protected final String TEXT_5 = " ----------" + NL + "\tdeclare @type: ";
  protected final String TEXT_6 = " : @Parser;" + NL + "\t";
  protected final String TEXT_7 = NL + "\tdeclare @method: * ";
  protected final String TEXT_8 = ".";
  protected final String TEXT_9 = "(";
  protected final String TEXT_10 = ") : @Parser.Initialize;";
  protected final String TEXT_11 = NL;
  protected final String TEXT_12 = NL + "\tdeclare @method: * ";
  protected final String TEXT_13 = ".";
  protected final String TEXT_14 = "(): @Parser.AST;";
  protected final String TEXT_15 = NL + "\t";
  protected final String TEXT_16 = NL + "}";
  protected final String TEXT_17 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
	
	Object[] args = (Object[]) argument;
	
	GenModel genModel = (GenModel) args[0];
	List<GenClass> parserClasses = GeneratorUtil.getAnnotatedClasses(genModel, EcoreAnnotation.PARSER);
	
	String packageName = (String) args[1];
	genModel.setImportManager(new ImportManager(packageName));
	
	if (packageName != null && !packageName.isEmpty()) {
    stringBuffer.append(TEXT_1);
    stringBuffer.append(packageName );
    stringBuffer.append(TEXT_2);
    	}
	
	genModel.markImportLocation(stringBuffer);
	genModel.addImport("rtt.annotations.Parser"); 

    stringBuffer.append(TEXT_3);
    	for (GenClass parserClass : parserClasses) { 
	String className = parserClass.getName(); 
    stringBuffer.append(TEXT_4);
    stringBuffer.append(className );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(className);
    stringBuffer.append(TEXT_6);
      GenOperation initOperation = GeneratorUtil.getInitOperation(parserClass);
    if (initOperation != null) {
    stringBuffer.append(TEXT_7);
    stringBuffer.append(className);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(initOperation.getName());
    stringBuffer.append(TEXT_9);
    stringBuffer.append(initOperation.getParameterTypes(","));
    stringBuffer.append(TEXT_10);
     } 
    stringBuffer.append(TEXT_11);
     GenFeature astFeature = GeneratorUtil.getAstFeature(parserClass);
   if (astFeature != null) {
    stringBuffer.append(TEXT_12);
    stringBuffer.append(className);
    stringBuffer.append(TEXT_13);
    stringBuffer.append(astFeature.getGetAccessor());
    stringBuffer.append(TEXT_14);
     } 
    stringBuffer.append(TEXT_15);
    }
    stringBuffer.append(TEXT_16);
    genModel.emitSortedImports(); 
    stringBuffer.append(TEXT_17);
    return stringBuffer.toString();
  }
}
