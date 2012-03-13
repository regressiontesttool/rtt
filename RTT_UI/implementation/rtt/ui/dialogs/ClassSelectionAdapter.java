package rtt.ui.dialogs;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;

import rtt.ui.RttLog;

public class ClassSelectionAdapter extends SelectionAdapter {		
	
	private Text text;
	private Shell parentShell;
	
	private IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
	static final IRunnableContext context = PlatformUI.getWorkbench().getProgressService();	
	
	public ClassSelectionAdapter(Shell parentShell, Text text) {
		super();
		this.text = text;
		this.parentShell = parentShell;
	}
	
	public ClassSelectionAdapter(Shell parentShell, Text text, IJavaSearchScope scope) {
		this(parentShell, text);
		this.scope = scope;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		try {
			SelectionDialog selectionDialog = JavaUI.createTypeDialog(parentShell,context,scope,
							IJavaElementSearchConstants.CONSIDER_CLASSES_AND_INTERFACES,
							false);
			
			if (selectionDialog.open() == Dialog.OK) {
				for (Object o : selectionDialog.getResult()) {
					if (o instanceof IType) {
						IType type = (IType) o;
						text.setText(type.getFullyQualifiedName());
					}						
				}
			}
		} catch (JavaModelException exception) {
			RttLog.log(exception);
		}		
	}
}
