package rtt.ui.editors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import rtt.ui.editors.input.IDetailInput;
import rtt.ui.editors.input.details.AttributeDetail;
import rtt.ui.editors.input.details.IDetail;
import rtt.ui.editors.input.details.StringDetail;

public class RTTDetailsPage implements IDetailsPage {
	
	ScrolledForm form;
	FormToolkit toolkit;
	IDetailInput input;
	Map<String, Label> labels;
	TableViewer attributeViewer;
	
	public RTTDetailsPage(IDetailInput input) {
		this.input = input;
		labels = new HashMap<String, Label>();
	}
	
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
		System.out.println("DetailsPage: SetFormInput - " + input);
		
		return false;
	}

	@Override
	public void setFocus() {
		
	}

	@Override
	public boolean isStale() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh() {
		System.out.println("DetailsPage: refresh");		
	}

	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sSelection = (IStructuredSelection) selection;
			Object o = sSelection.getFirstElement();

			setData(input.getDetail(o));						
		}
	}

	private void setData(IDetail detail) {
		List<StringDetail> details = detail.getStringDetails();
		for (StringDetail stringDetail : details) {
			Label label = labels.get(stringDetail.getKey());
			if (label != null) {
				label.setText(stringDetail.getValue());
			}
		}
		
		attributeViewer.setInput(detail.getAttributes());		
	}

	@Override
	public void createContents(Composite parent) {
		TableWrapLayout layout = new TableWrapLayout();
		layout.topMargin = 5;
		layout.leftMargin = 5;
		layout.rightMargin = 2;
		layout.bottomMargin = 2;
		parent.setLayout(layout);
		
		// DETAIL SECTION -------------------------------------
		Section detailSection = toolkit.createSection(parent, 
				Section.DESCRIPTION | Section.TITLE_BAR
		);
		detailSection.setText("Details");
		detailSection.setDescription("This is an overview of the selected item.");
		detailSection.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL));
		
		Composite detailClient = toolkit.createComposite(detailSection, SWT.WRAP);
		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 1;
		detailClient.setLayout(gLayout);
		for (String key : input.getKeys()) {
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			Label label = toolkit.createLabel(detailClient, "");
			label.setLayoutData(gd);
			
			labels.put(key, label);
		}
		
		toolkit.paintBordersFor(detailClient);
		detailSection.setClient(detailClient);
		
		// ATTRIBUTE SECTION -----------------------------------
		Section attributeSection = toolkit.createSection(parent, 
				Section.DESCRIPTION | Section.TITLE_BAR
		);
		attributeSection.setText("Attributes");
		attributeSection.setDescription("These are all attributes of the selected item.");
		attributeSection.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL));
		
		Composite attributeClient = toolkit.createComposite(attributeSection, SWT.WRAP);
		gLayout = new GridLayout();
//		gLayout.marginWidth = gLayout.marginHeight = 0;
		gLayout.numColumns = 1;
		attributeClient.setLayout(gLayout);
		
		Table table = toolkit.createTable(attributeClient, SWT.BORDER | SWT.SINGLE);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 200;
		gd.widthHint = 200;
		table.setLayoutData(gd);
		
		toolkit.paintBordersFor(attributeClient);
		attributeSection.setClient(attributeClient);
		
		// VIEWER INIT -------------------------------------------
		attributeViewer = new TableViewer(table);
		attributeViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		String[] headerNames = input.getHeaderNames();
		int index = 0;
		
		for (String string : headerNames) {
			TableViewerColumn viewerColumn = new TableViewerColumn(attributeViewer, SWT.LEFT);
			viewerColumn.getColumn().setText(string);
			viewerColumn.getColumn().setWidth(80);
			
			final int currentIndex = index;
			
			viewerColumn.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					if (element instanceof AttributeDetail) {
						return ((AttributeDetail) element).getText(currentIndex);
					}
					
					throw new IllegalArgumentException("Wrong element in Attribute viewer");
				}
			});
			index++;
		}
		
		SectionPart detailSectionPart = new SectionPart(detailSection);
		Section section = detailSectionPart.getSection();
		SectionPart attributeSectionPart = new SectionPart(attributeSection);		
	}
}
