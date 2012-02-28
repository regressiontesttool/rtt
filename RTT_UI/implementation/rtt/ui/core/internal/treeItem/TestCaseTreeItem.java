package rtt.ui.core.internal.treeItem;

import org.eclipse.ui.IWorkbenchPage;

import rtt.ui.core.ITreeItem;
import rtt.ui.core.archive.ITestCase;
import rtt.ui.editors.IEditorCommand;

public class TestCaseTreeItem extends AbstractTreeItem {

	private ITestCase testCase;
	private IEditorCommand command;
	
	public class EditorCommand implements IEditorCommand {
		private ITestCase testCase;
		
		public EditorCommand(ITestCase testCase) {
			this.testCase = testCase;
		}
		
		public void open(IWorkbenchPage page) {
//			try {
//				IEditorPart part = null; //IDE.openEditor(page, new TestCaseEditorInput(testCase), MasterDetailFormEditor.ID, true);
//				if (part != null && part instanceof MasterDetailFormEditor) {
//					((MasterDetailFormEditor) part).setActivePage(ParserPage.ID);
//				}
//				
//			} catch (PartInitException e) {
//				ErrorDialog.openError(page.getActivePart().getSite().getShell(), 
//						"Error", "Could not open editor", 
//						new Status(Status.ERROR, RttPluginUI.PLUGIN_ID, e.getMessage(), e)
//				);
//			}
		}
	}

	public TestCaseTreeItem(ITreeItem parent, ITestCase testCase) {
		super(parent);
		String name = testCase.getName();
		if (testCase.getLexerOutput() == null || testCase.getParserOutput() == null) {
			name += " (not generated)";
		} else {
			name += " (Ver. " + testCase.getVersion() + ")";
			command = new EditorCommand(testCase);
		}		
		
		setCommand(command);
		
		setName(name);
		setIcon(TreeItemIcon.TESTCASE);
		
		this.testCase = testCase;
	}

	@Override
	public Object[] getChildren() {
		
		Object[] result = new ITreeItem[3];
		result[0] = new SimpleTreeItem(this, "Input", TreeItemIcon.INPUT);
		result[1] = new LexerOutputTreeItem(this, testCase.getLexerOutput(), command);
		result[2] = new ParserOutputTreeItem(this, testCase.getParserOutput(), command);
		
//		result[1] = new SimpleTreeItem(this, lexerOutput, TreeItemIcon.LEXER_OUTPUT, new TreeItemEditorCommand("Lexer", "Lexer tooltip", new LexerTreeItem(testCase.getLexerOutput())));
		
//		result[2] = new SimpleTreeItem(this, parserOutput, TreeItemIcon.PARSER_OUTPUT);

		return result;
	}
	
	public ITestCase getTestCase() {
		return testCase;
	}
	
	@Override
	public Object getObject(Class<?> clazz) {
		if (clazz == ITestCase.class) {
			return testCase;
		}
		
		return parent.getObject(clazz);
	}

}
