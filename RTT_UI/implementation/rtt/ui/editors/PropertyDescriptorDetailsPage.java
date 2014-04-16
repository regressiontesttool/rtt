package rtt.ui.editors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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

import rtt.ui.utils.RttPluginUtil;

/**
 * This {@link IDetailsPage} displays all properties from an
 * {@link EObject} within a {@link TableViewer}.
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 */
public class PropertyDescriptorDetailsPage implements IDetailsPage {

	private ComposedAdapterFactory adapterFactory;
	private AdapterFactoryItemDelegator itemDelegator;
	
	private FormToolkit toolkit;
	private TableViewer tableViewer;
	private EObject currentObject;

	@Override
	public void initialize(IManagedForm form) {
		this.toolkit = form.getToolkit();
		
		if (this.adapterFactory == null) {
			this.adapterFactory = RttPluginUtil.createFactory();
		}
		
		if (this.itemDelegator == null) {
			this.itemDelegator = new AdapterFactoryItemDelegator(adapterFactory);
		}
	}

	@Override
	public void dispose() {
		if (this.itemDelegator != null) {
			itemDelegator = null;
		}
		
		if (this.adapterFactory != null) {
			adapterFactory.dispose();
			adapterFactory = null;
		}
		
		if (tableViewer != null) {
			tableViewer.getControl().dispose();
			tableViewer = null;
		}
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void commit(boolean onSave) { }

	@Override
	public boolean setFormInput(Object input) {
		return false;
	}

	@Override
	public void setFocus() {
		
	}

	@Override
	public boolean isStale() {
		return false;
	}

	@Override
	public void refresh() {
		
	}

	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sSelection = (IStructuredSelection) selection;
			Object o = sSelection.getFirstElement();
			
			if (o instanceof EObject) {
				currentObject = (EObject) o;				
				tableViewer.setInput(
						itemDelegator.getPropertyDescriptors(currentObject));
			}
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
		
		Label explainLabel = toolkit.createLabel(composite, "This table contains additional details of the selected element.", SWT.WRAP);
		explainLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite_1.setBounds(10, 10, 64, 64);
		TableColumnLayout tclLayout = new TableColumnLayout();
		composite_1.setLayout(tclLayout);
		toolkit.adapt(composite_1);
		toolkit.paintBordersFor(composite_1);
		
		
		// create a table for displaying data
		tableViewer = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		toolkit.paintBordersFor(table);
		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		// column 1: displaying the id/key of the property
		TableViewerColumn idViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn keyColumn = idViewerColumn.getColumn();
		tclLayout.setColumnData(keyColumn, new ColumnPixelData(100, true, true));
		keyColumn.setText("Key");
		
		idViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof IItemPropertyDescriptor) {
					IItemPropertyDescriptor ipd = (IItemPropertyDescriptor) element;
					return ipd.getId(currentObject);
				}
				
				return super.getText(element);
			}
		});		
		
		// column 2: display the value of the property
		TableViewerColumn valueViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn valueColumn = valueViewerColumn.getColumn();
		tclLayout.setColumnData(valueColumn, new ColumnWeightData(1, 150, true));
		valueColumn.setText("Value");
		valueViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {				
				if (element instanceof IItemPropertyDescriptor) {
					IItemPropertyDescriptor ipd = (IItemPropertyDescriptor) element;
					return itemDelegator.getText(ipd.getPropertyValue(currentObject));
				}
				
				return super.getText(element);
			}
		});
	}
}
