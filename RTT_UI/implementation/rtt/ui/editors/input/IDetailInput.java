package rtt.ui.editors.input;

import org.eclipse.ui.IEditorInput;

import rtt.ui.content.IContent;
import rtt.ui.editors.input.details.IDetail;


public interface IDetailInput extends IEditorInput {
	
	String getEditorID();
	String getEditorTitle();

	String[] getKeys();
	String[] getHeaderNames();
	IDetail getDetail(Object o);
	String getMasterSectionTitle();
	Class<?>[] getDetailClasses();
	
	IContent getRoot();
	
//	void setDetail(IDetail detail);
//	IDetail getDetail();
}
