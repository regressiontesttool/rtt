package rtt.ui.core.internal.treeItem;

import java.util.List;

import rtt.ui.core.ITreeItem;
import rtt.ui.core.archive.ITestCase;
import rtt.ui.core.archive.ITestSuite;

/**
 * This class provides the tree item functionality for a test suite.
 * 
 * @author C.Oelsner <C.Oelsner@web.>
 */
public class TestSuiteTreeItem extends AbstractTreeItem {
	
	private ITestSuite testSuite;
	
	/**
	 * Creates a new tree item for a test suite.
	 * @param parent the parent tree item
	 * @param testSuite the test suite
	 */
	public TestSuiteTreeItem(ITreeItem parent, ITestSuite testSuite) {
		super(parent);
		setName(testSuite.getName());
		setIcon(TreeItemIcon.TESTSUITE);
		this.testSuite = testSuite;
	}
	
	@Override
	public boolean isEmpty() {
		return testSuite.getTestCases().isEmpty();
	}
	
	@Override
	public Object[] getChildren() {
		List<ITestCase> cases = testSuite.getTestCases();
		if (cases != null && cases.size() > 0) {
			Object[] result = new TestCaseTreeItem[cases.size()];
			for (int i = 0; i < cases.size(); i++) {
				result[i] = new TestCaseTreeItem(this, cases.get(i));
			}
			
			return result;
		}
		
		return new String[] { "No testcases found." };	
	}
	
	@Override
	public Object getObject(Class<?> clazz) {
		if (clazz == ITestSuite.class) {
			return testSuite;
		}
		
		return parent.getObject(clazz);
	}

	public ITestSuite getTestSuite() {
		return testSuite;
	}

}
