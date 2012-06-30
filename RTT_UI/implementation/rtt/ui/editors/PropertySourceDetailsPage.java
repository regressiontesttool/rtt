package rtt.ui.editors;

import java.util.List;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class PropertySourceDetailsPage implements IDetailsPage {
	
	protected abstract class PropertyColumnLabelProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof IItemPropertyDescriptor) {
				return getPropertyText((IItemPropertyDescriptor) element, null);
			}
			return super.getText(element);
		}

		public abstract String getPropertyText(IItemPropertyDescriptor element, Object object);
	}
	
	public PropertySourceDetailsPage() {
	}

	private FormToolkit toolkit;
	private Table table;
	private TableViewer tableViewer;

	@Override
	public void initialize(IManagedForm form) {
		this.toolkit = form.getToolkit();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void commit(boolean onSave) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean setFormInput(Object input) {
		System.out.println("SetFormInput: " + input);
		return false;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isStale() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sSelection = (IStructuredSelection) selection;
			Object o = sSelection.getFirstElement();
			
			if (o instanceof IItemPropertySource) {
				IItemPropertySource propertySource = (IItemPropertySource) o;
				setData(propertySource.getPropertyDescriptors(null));
			}
		}
	}

	private void setData(List<IItemPropertyDescriptor> propertyDescriptors) {
		if (propertyDescriptors != null) {
			tableViewer.setInput(propertyDescriptors);
		} else {
			tableViewer.setInput(new Object[0]);
		}
	}

	/**
	 * 
	 */
	@Override
	public void createContents(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		//		
		Section section = toolkit.createSection(parent,
				ExpandableComposite.EXPANDED | ExpandableComposite.TITLE_BAR);
		section.setText("Details");
		//
		Composite composite = toolkit.createComposite(section, SWT.NONE);
		toolkit.paintBordersFor(composite);
		section.setClient(composite);
		composite.setLayout(new GridLayout(1, false));
		
		Label explainLabel = toolkit.createLabel(composite, "This table shows some details of the current selected element in the tree.", SWT.WRAP);
		explainLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite_1.setBounds(10, 10, 64, 64);
		TableColumnLayout tclLayout = new TableColumnLayout();
		composite_1.setLayout(tclLayout);
		toolkit.adapt(composite_1);
		toolkit.paintBordersFor(composite_1);
		
		tableViewer = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		toolkit.paintBordersFor(table);
		
		TableViewerColumn idViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnId = idViewerColumn.getColumn();
		tclLayout.setColumnData(tblclmnId, new ColumnPixelData(100, true, true));
		tblclmnId.setText("Id");
		idViewerColumn.setLabelProvider(new PropertyColumnLabelProvider() {
			
			@Override
			public String getPropertyText(IItemPropertyDescriptor element, Object object) {
				return element.getId(object);
			}
		});
		
		TableViewerColumn valueViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnValue = valueViewerColumn.getColumn();
		tclLayout.setColumnData(tblclmnValue, new ColumnWeightData(1, 150, true));
		tblclmnValue.setText("Value");
		valueViewerColumn.setLabelProvider(new PropertyColumnLabelProvider() {
			
			@Override
			public String getPropertyText(IItemPropertyDescriptor element, Object object) {
				IItemPropertySource source = (IItemPropertySource) element.getPropertyValue(object);
				return source.getEditableValue(null).toString();				
			}
		});
		
		TableViewerColumn descriptionViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnDescription = descriptionViewerColumn.getColumn();
		tclLayout.setColumnData(tblclmnDescription, new ColumnWeightData(1, 150, true));
		tblclmnDescription.setText("Description");
		descriptionViewerColumn.setLabelProvider(new PropertyColumnLabelProvider() {
			
			@Override
			public String getPropertyText(IItemPropertyDescriptor element, Object object) {
				return element.getDescription(object);
			}
		});

		prepareViewer(tableViewer);		
	}
	
	private void prepareViewer(StructuredViewer tableViewer) {		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());		
	}
}
