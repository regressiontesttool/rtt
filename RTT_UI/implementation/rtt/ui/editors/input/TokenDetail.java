package rtt.ui.editors.input;

import java.util.ArrayList;
import java.util.List;

import rtt.ui.core.archive.IAttribute;
import rtt.ui.core.archive.IToken;
import rtt.ui.core.internal.treeItem.TokenTreeItem;

public class TokenDetail implements IDetail {
	
	public static final String A_COUNT = "rtt.token.attribute.count";
	public static final String CLASSNAME = "rtt.token.classname";
	
	private List<StringDetail> details;
	private List<AttributeDetail> attributes;
	private IToken token;
	
	public TokenDetail(TokenTreeItem o) {
		attributes = new ArrayList<AttributeDetail>();
		details = new ArrayList<StringDetail>();
		
		setToken((IToken) o.getObject(IToken.class));
	}
		
	public void setToken(IToken token) {
		if (token != null && !token.equals(this.token)) {			
			
			details.clear();
			attributes.clear();
			
			if (token != null) {

				details.add(new StringDetail(CLASSNAME, "Class: " + token.getClassName()));
				details.add(new StringDetail(A_COUNT, "Attribute count: " + token.getAttributes().size()));
				
				for (IAttribute attribute : token.getAttributes()) {
					attributes.add(new AttributeDetail(attribute));
				}
			}
		}
		
		this.token = token;
	}

	@Override
	public List<StringDetail> getStringDetails() {		
		return details;
	}

	@Override
	public List<AttributeDetail> getAttributes() {		
		return attributes;
	}
}
