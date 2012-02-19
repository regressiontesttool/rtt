package rtt.ui.editors.input;

import org.eclipse.ui.IEditorInput;


public interface IDetailInput extends IEditorInput {
	
	String getEditorID();
	String getEditorTitle();

	String[] getKeys();
	String[] getHeaderNames();
	IDetail getDetail(Object o);
	String getMasterSectionTitle();
	Object getMasterRoot();
	Class<?>[] getDetailClasses();
	
//	void setDetail(IDetail detail);
//	IDetail getDetail();
}
