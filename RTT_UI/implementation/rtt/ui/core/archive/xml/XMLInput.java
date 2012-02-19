package rtt.ui.core.archive.xml;

import rtt.core.archive.input.Input;
import rtt.ui.core.archive.IInput;

public class XMLInput implements IInput {
	
	private Input input;

	public XMLInput(Input input) {
		this.input = input;
	}

	@Override
	public String getValue() {
		return input.getValue();
	}

}
