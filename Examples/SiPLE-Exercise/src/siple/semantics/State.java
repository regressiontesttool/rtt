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
import siple.ast.*;

/**
 * Instances of this class represent <i>SiPLE</i> interpreter states. The
 * interpretation of a <i>SiPLE</i> program starts with the empty state
 * consisting of a single frame and finishes either, with a state consisting
 * of a single frame that contains the program's results or an {@link
 * InterpretationException interpretation exception}, iff the program is
 * erroneous. Since <i>SiPLE</i> is Turing complete, the interpretation might
 * also not terminate at all.
 * @author C. Bürger
 */
public final class State {
	/**
	 * The procedure currently in execution.
	 */
	public Frame currentFrame = new Frame();
	/**
	 * The standard output buffer.
	 */
	public final StringBuilder stdOut = new StringBuilder();
	
	/**
	 * Allocate memory for the given entity in the current frame and
	 * initialize its value.
	 * @param decl The entity to allocate and initialize.
	 * @param value The entities' initialization value; To specify, that the
	 * entity is not initialized during its allocation the parameter has to be
	 * <tt>null</tt>.
	 */
	public void allocate(Declaration decl, Object value) {
		MemoryLocation loc = new MemoryLocation();
		loc.value = value;
		currentFrame.environment.put(decl, loc);
	}
	
	/**
	 * Access an entity, i.e., return its {@link MemoryLocation memory
	 * location}.
	 * @param decl The entity to access.
	 * @return The entities' address.
	 * @throws InterpretationException Thrown, iff the given entity is not
	 * allocated.
	 */
	public MemoryLocation access(Declaration decl)
	throws InterpretationException {
		for (Frame cf = currentFrame; cf != null; cf = cf.closure) {
			MemoryLocation loc = cf.environment.get(decl);
			if (loc != null)
				return loc;
		}
		throw new InterpretationException(
				"Access to unallocated variable ["+ decl.getName() +"].");
	}
	
	/**
	 * Each frame represents the execution environment of a procedure, i.e.,
	 * the current state of a procedure in execution. It consists of:<ul>
	 * <li>(1) The procedure's {@link #implementation} (AST procedure
	 * declaration)</li>
	 * <li>(2) The procedure's {@link #closure} (the frame within which this
	 * frame is instantiated; required for accesses to non local entities)</li>
	 * <li>(3) The procedure's local {@link #environment} (local variable
	 * bindings)</li> 
	 * <li>(4) The procedure's {@link #returnValue return value} (given, iff
	 * the procedure's execution finished)</li></ul>
	 * @author C. Bürger
	 */
	public static final class Frame {
		/** See {@link Frame}. */
		public ProcedureDeclaration implementation = null;
		/** See {@link Frame}. */
		public Frame closure = null;
		/** See {@link Frame}. */
		public Map<Declaration, MemoryLocation> environment =
			new TreeMap<Declaration, MemoryLocation>();
		/** See {@link Frame}. */
		public Object returnValue = null;
	}
	
	/**
	 * Simple wrapper class to represent addressable memory locations.
	 * @author C. Bürger
	 */
	public static final class MemoryLocation {
		/**
		 * The value stored under the address this memory location represents.
		 */
		public Object value;
	}
}
