/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-clause license which accompanies this distribution.
 *
 * </copyright>
 */
package siple.semantics;

import java.util.*;

/**
 * Instances of this class represent comparable addresses in <i>Dewey</i>
 * notation.<br>
 * <br>
 * If an address <tt>A</tt> and an address <tt>B</tt> are compared
 * (<tt>A.compareTo(B)</tt>) it is decided, whether <tt>A</tt> is
 * <i>smaller</i>, <i>equal</i> or <i>greater</i> than <tt>B</tt> or not.
 * <i>Dewey</i> addresses can be viewed as tree labels, whereas an address
 * specifies an unique tree node. Each <i>Dewey</i> address consists of
 * arbitrary many parts, whereas each part is a natural number. The root
 * node's address has one part with value 1. Any other node's address is the
 * address of its parent node extended with an additional part, whose value is
 * the node's child number. E.g. assume a parent node <tt>P</tt> with address
 * <tt>1.54.8</tt>. Its third child has address <tt>1.54.8.3</tt>.<br>
 * <br>
 * If an address <tt>A</tt> represents a node in the subtree of <tt>B</tt>, we
 * define <tt>A</tt> to be <i>greater</i> than <tt>B</tt>. If <tt>B</tt> is in
 * the subtree of <tt>A</tt>, we define that <tt>A</tt> is <i>smaller</i> than
 * B. If neither, <tt>A</tt> is in the subtree of <tt>B</tt> or <tt>B</tt> is
 * in the subtree of <tt>A</tt>, <tt>A</tt> and <tt>B</tt> must have one
 * nearest ancestor node <tt>ANC</tt>. Let <tt>ANC_CHILD_A</tt> and
 * <tt>ANC_CHILD_B</tt> be child nodes of <tt>ANC</tt>. In this case <tt>A</tt>
 * is <i>smaller</i> than <tt>B</tt>, iff<br>
 *  - the path from <tt>A</tt> to <tt>ANC</tt> contains the node
 *  <tt>ANC_CHILD_A</tt> and<br>
 *  - the path from <tt>B</tt> to <tt>ANC</tt> contains the node
 *  <tt>ANC_CHILD_B</tt> and<br>
 *  - <tt>ANC_CHILD_A</tt> is <i>smaller</i> than <tt>ANC_CHILD_B</tt>
 *    (thus <tt>ANC_CHILD_A.compareTo(ANC_CHILD_B) == 1</tt>).<br>
 * If <tt>A</tt> is <i>smaller</i> than <tt>B</tt>, <tt>B</tt> must be
 * <i>greater</i> than <tt>A</tt>.
 * @author C. Bürger
 */
public class DeweyAddress implements Comparable<DeweyAddress>, Cloneable {
	private List<Integer> address;
	
	/**
	 * Constructs a new NodeAddress, not yet initialized with any <i>Dewey</i>
	 * address.
	 */
	public DeweyAddress() {
		address = new ArrayList<Integer>();
	}
	
	/**
	 * Adds another part to the address. E.g. assume address <tt>1.5</tt> is
	 * represented by <tt>DeweyAddress</tt> <tt>na</tt>.
	 * <tt>na.addAddressPart(23).addAddressPart(2)</tt> results into the
	 * address <tt>1.5.23.2</tt>.
	 * @param part The part to add.
	 * @return This address.
	 */
	public DeweyAddress addAddressPart(int part) {
		address.add(part);
		return this;
	}
	
	/**
	 * Decides if this node is an ancestor of the other node.
	 * @param otherAddress The other node to compare to.
	 * @return True, if the other node is a successor of this node.
	 */
	public boolean isAncestor(DeweyAddress otherAddress) {
		if (address.size() >= otherAddress.address.size())
			return false;
		Iterator<Integer> iter2 = otherAddress.address.iterator();
		for (Integer part1:address) {
			int part2 = iter2.next();
			if (part1 != part2)
				return false;
		}
		return true;
	}
	
	/**
	 * Decides if this node is a successor of the other node. If this node is
	 * a successor, its address must be greater than the other node.
	 * @param otherAddress The other node to compare to.
	 * @return True, if the other node is an ancestor of this node.
	 */
	public boolean isSuccessor(DeweyAddress otherAddress) {
		return otherAddress.isAncestor(this);
	}
	
	/**
	 * Compares this address with another one to decide, whether this address
	 * is <i>smaller</i> than the other one, both addresses are equal or this
	 * address is <i>greater</i> than the other one.
	 * @param otherAddress The address to which compare this one.
	 * @return 1 if this address is <i>smaller</i> than the other one, 0 if
	 * both addresses are <i>equal</i> or -1 if this address is <i>greater</i>
	 * than the other one.
	 */
	public int compareTo(DeweyAddress otherAddress) {
		Iterator<Integer> iter2 = otherAddress.address.iterator();
		for (Integer part1:address) {
			if (!iter2.hasNext())
				return -1;
			int part2 = iter2.next();
			if (part1 < part2)
				return 1;
			if (part1 > part2)
				return -1;
		}
		if (iter2.hasNext())
			return 1;
		return 0;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof DeweyAddress))
			return false;
		return ((DeweyAddress)o).compareTo(this) == 0;
	}
	
	public DeweyAddress clone() {
		DeweyAddress result = new DeweyAddress();
		result.address = new ArrayList<Integer>(address);
		return result;
	}
	
	/**
	 * Returns a human readable <i>Dewey</i> address. E.g. <tt>1.3.2.65</tt>
	 * @return A readable <i>Dewey</i> address.
	 */
	public String toString() {
		StringBuilder result = new StringBuilder(3*address.size());
		for (Integer part:address) {
			result.append(part);
			result.append('.');
		}
		result.deleteCharAt(result.length() - 1);
		return result.toString();
	}
}
