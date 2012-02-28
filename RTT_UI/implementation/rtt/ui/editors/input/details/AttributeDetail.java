package rtt.ui.editors.input.details;

import java.util.ArrayList;
import java.util.List;

import rtt.core.archive.output.Attribute;

public class AttributeDetail {
	
	private List<String> details;
	
	public AttributeDetail(Attribute attribute) {
		details = new ArrayList<String>();
		
		if (attribute != null) {
			details.add(attribute.getName());
			details.add(attribute.getValue());
			details.add(attribute.isInformational() ? "INFO" : "COMPARE");
		}		
	}

	public String getText(int currentIndex) {
		return details.get(currentIndex);
	}

}
