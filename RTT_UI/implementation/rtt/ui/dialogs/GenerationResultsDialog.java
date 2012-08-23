package rtt.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import rtt.core.utils.GenerationInformation;
import rtt.core.utils.GenerationInformation.GenerationResult;
import rtt.core.utils.GenerationInformation.GenerationType;

public class GenerationResultsDialog extends TitleAreaDialog {

	class ResultsColumnLabelProvider extends ColumnLabelProvider {

		final static int SUITE_COLUMN = 0;
		final static int CASE_COLUMN = 1;
		final static int COMMENT_COLUMN = 2;

		private int column = 0;

		public ResultsColumnLabelProvider(int column) {
			this.column = column;
		}

		@Override
		public String getText(Object element) {

			if (element instanceof GenerationResult) {
				GenerationResult genResult = (GenerationResult) element;
				switch (column) {
				case SUITE_COLUMN:
					return genResult.suiteName;

				case CASE_COLUMN:
					return genResult.caseName;

				case COMMENT_COLUMN:
					return genResult.getMessage();
				}
			}

			return super.getText(element);
		}
	}

	private GenerationInformation results;
	private Table table;
	private String message;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param results
	 */
	public GenerationResultsDialog(Shell parentShell, GenerationInformation results) {
		super(parentShell);
		setBlockOnOpen(false);

		this.results = results;
		if (results.hasErrors()) {
			message = "Some " + results.getType().text + " could not be generated. Check archive log for further description.";
		} else {
			message = "These are the results of the generation process.";
		}
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		super.setTitle("Generation results");
		super.setMessage(message, results.hasErrors() ? IMessageProvider.ERROR
				: IMessageProvider.INFORMATION);

		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 5;
		gridLayout.marginWidth = 10;
		gridLayout.marginHeight = 10;
		gridLayout.makeColumnsEqualWidth = true;
		gridLayout.horizontalSpacing = 3;

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		TableColumnLayout tcl_composite = new TableColumnLayout();
		composite.setLayout(tcl_composite);

		TableViewer tableViewer = new TableViewer(composite, SWT.BORDER
				| SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());

		TableViewerColumn suiteViewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn suiteColumn = suiteViewerColumn.getColumn();
		tcl_composite.setColumnData(suiteColumn, new ColumnPixelData(80, true,
				true));
		suiteColumn.setText("Test suite");
		suiteViewerColumn.setLabelProvider(new ResultsColumnLabelProvider(
				ResultsColumnLabelProvider.SUITE_COLUMN));

		TableViewerColumn caseViewerColumn = new TableViewerColumn(tableViewer,
				SWT.NONE);
		TableColumn caseColumn = caseViewerColumn.getColumn();
		tcl_composite.setColumnData(caseColumn, new ColumnPixelData(80, true,
				true));
		caseColumn.setText("Test case");
		caseViewerColumn.setLabelProvider(new ResultsColumnLabelProvider(
				ResultsColumnLabelProvider.CASE_COLUMN));

		TableViewerColumn commentViewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn commentColumn = commentViewerColumn.getColumn();
		tcl_composite.setColumnData(commentColumn, new ColumnWeightData(1, 300,
				true));
		commentColumn.setText("Comment");
		commentViewerColumn.setLabelProvider(new ResultsColumnLabelProvider(
				ResultsColumnLabelProvider.COMMENT_COLUMN));

		boolean showAll = (results.getType() == GenerationType.REFERENCE_DATA);
		tableViewer.setInput(results.getResults(showAll));

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(550, 400);
	}

}
