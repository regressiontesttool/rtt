package headliner.treedistance;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import rtt.core.archive.output.Node;
import rtt.core.archive.output.Tree;
import rtt.core.manager.Printer;


/** This defines are tree.  The tree contains labelled nodes.  Labels
 * must be unique.  Sibling order is defined by the order in which
 * edges are added to a parent node.
 * 
 * Populating the tree is done at construction time, which must also
 * specify what type of tree node traversal to number all the nodes.
 * Default is POSTORDER
 *
 * INSERT-LICENCE-INFO
 */
public class BasicTree extends TreeDefinition<Node>{

    //A set of nodes (once set, this is not changed)
	Tree treeStructure;
	List<Node> nodes;
	
    public BasicTree() {
    }

    /** This takes a |e| x 2 array of string, where |e| is the number
     * of edges.
     */
    public BasicTree(Tree tree,
    		Node _root,
		     int ordering) {
	setRoot(_root);
	nodes = new LinkedList<Node>();
	addNode(nodes, getRoot());
	
	treeStructure = tree;
	
	System.out.println("TreeDefinition.init: root: "+getRoot());
	
	orderNodes(ordering);
	
    }

    /** Returns the parent nodes in the tree*/
    public Collection<Node> getNodes() {
    	return nodes;
    }
    private void addNode(List<Node> nodes,Node  curNode)
    {
    	nodes.add(curNode);
    	if (curNode.getNodes() == null || curNode.getNodes().size() == 0)
    		return;
    	
    	for(Node c : curNode.getNodes())
    		addNode(nodes, c);
    	
    }
    
    
    /** Returns the children of the node given as a parameter. */
    public List<Node> getChildren(Node nodeLabel) {
    	if (nodeLabel.getNodes() == null)
    		return new LinkedList<Node>();
    	
	return nodeLabel.getNodes();
    }

    public String toString() {
    	return Printer.PrintNode(getRoot());
    }
}