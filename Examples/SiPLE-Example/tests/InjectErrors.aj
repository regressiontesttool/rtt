/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package siple.rtt;

import siple.semantics.*;
import siple.semantics.ast.*;

/**
 * AspectJ specification injecting artificial compiler errors.
 * @author C. Bürger
 */
public aspect InjectErrors {
	// Simulate implementation error ("true" and "false" are of type integer)
	Type around(Constant node):
	execution(Type Constant+.Type()) && target(node) {
		Type result = proceed(node);
		return result == Type.Boolean ? Type.Integer : result;
	}
}
