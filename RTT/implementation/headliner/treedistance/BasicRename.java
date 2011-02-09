package headliner.treedistance;

import rtt.archive.Node;
import rtt.managing.Printer;

/* Implements a basic rename operation.  If node labels are the same
 * then the cost is 0, otherwise the cost is 1.
 *
 * INSERT-LICENCE-INFO
 */
public class BasicRename extends TreeEditOperation<Node>{

    public BasicRename() {
	super.opName = "RELABEL";
    }

    @Override
    public double getCost(int aNodeID, int bNodeID, 
			  TreeDefinition<Node> aTree,
			  TreeDefinition<Node> bTree) {
	Node aString = aTree.getLabelForMatching(aNodeID);
	Node bString = bTree.getLabelForMatching(bNodeID);
	
	//strip out informational attributes (those are in paranthesis)
	String tmpARep = Printer.PrintNode(aString);
	tmpARep = tmpARep.replaceAll("\\(.*\\)", "");
	
	String tmpBRep = Printer.PrintNode(bString);
	tmpBRep = tmpBRep.replaceAll("\\(.*\\)", "");
	
	if (tmpARep.equals
	    (tmpBRep)) {
	    return 0;
	}
	return 1;
    }
}