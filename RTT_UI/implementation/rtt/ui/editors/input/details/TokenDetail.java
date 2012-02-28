package rtt.ui.editors.input.details;

import java.util.ArrayList;
import java.util.List;

import rtt.core.archive.output.Attribute;
import rtt.core.archive.output.Token;
import rtt.ui.content.output.TokenContent;

public class TokenDetail implements IDetail {

	public static final String A_COUNT = "rtt.token.attribute.count";
	public static final String FULL_NAME = "rtt.token.fullName";

	private List<StringDetail> details;
	private List<AttributeDetail> attributes;
	private Token token;

	public TokenDetail(TokenContent tokenContent) {
		attributes = new ArrayList<AttributeDetail>();
		details = new ArrayList<StringDetail>();

		setToken(tokenContent.getToken());
	}

	public void setToken(Token token) {
		if (token != null && !token.equals(this.token)) {

			details.clear();
			attributes.clear();

			int aCount = 0;
			if (token.getAttributes() != null) {
				for (Attribute attribute : token.getAttributes().getAttribute()) {
					attributes.add(new AttributeDetail(attribute));
					aCount++;
				}
			}

			details.add(new StringDetail(FULL_NAME, "Class: "
					+ token.getFullName()));

			details.add(new StringDetail(A_COUNT, "Attribute count: " + aCount));
			
			this.token = token;
		}
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
