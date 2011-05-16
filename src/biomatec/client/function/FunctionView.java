package biomatec.client.function;

import java.io.IOException;
import java.util.ArrayList;

import biomatec.client.GeneSearchService;
import biomatec.client.GeneSearchServiceAsync;
import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Function;
import biomatec.javaBeans.Gene;
import biomatec.javaBeans.SelectedGenesData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.CheckBox;

public class FunctionView extends Composite {

	private static GeneSearchServiceAsync geneSearchSvc = GWT.create(GeneSearchService.class);

	private Function function;
	private ArrayList<Gene> selectedGenes;
	@SuppressWarnings("unused")
	private Dataset dataset;
	private SelectedGenesData selectedGenesData;
	private ArrayList<String> parameterNames;
	private String reparsedUrl = null; // String parsed from url for captured parameters

	private FormPanel form = new FormPanel();
	private VerticalPanel formPanel = new VerticalPanel();

	private HorizontalPanel panel = new HorizontalPanel();
	private FlexTable ft = new FlexTable();
	private FlexTable sidebar = new FlexTable(); // the left sidebar that contains genes or parameters 
	private VerticalPanel vp = new VerticalPanel();
	private VerticalPanel vp2 = new VerticalPanel();
	private HTML h = new HTML();
	private CheckBox sync = new CheckBox("SYNC");
	private Button remove = new Button("Remove");
	private Label errorLabel = new Label();

	private char type;

	public FunctionView(){}

	public FunctionView(Function f, ArrayList<Gene> sg, Dataset ds, SelectedGenesData selectedGenesData) {
		this.function = f;
		this.selectedGenes = sg;
		this.dataset = ds;
		this.selectedGenesData = selectedGenesData;
		this.parameterNames = new ArrayList<String>();
		sync.setValue(true);

		//Build the widget
		panel.add(vp);
		panel.add(vp2);
		vp.add(sync);
		vp.add(ft);
		vp.add(form);
		vp2.add(remove);
		remove.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				removeFromParent();
			}
		});
		type = function.getFunctionType();
		if (type == 'S'){
			addSingleGeneView();
		}
		else if (type == 'M'){
			//parseURL();
			addMultiGeneView();
		}

		//Format the widget
		ft.setWidth("150px");
		panel.setWidth("1000px");
		vp2.setWidth("800px");
		vp2.setCellHorizontalAlignment(remove, VerticalPanel.ALIGN_RIGHT);

		//Initialize the widget
		this.initWidget(panel);
		this.setStyleName("view-general");
	}


	private void generateParametersArray() {
		String newUrl = function.getUrl();

		int pos1, pos2;
		pos1 = 0;
		pos2 = 0;

		while (pos1 >= 0) {
			pos1 = newUrl.indexOf("%%", pos2+2);
			pos2 = newUrl.indexOf("%%", pos1+2);
			if (pos1 >= 0) {
				parameterNames.add(newUrl.substring(pos1+2, pos2));
			}
		}
	}

	private void addSingleGeneView(){
		ft.setText(0, 0, "GENE");
		ft.getRowFormatter().setStyleName(0, "resultsTable-headerRow");
		for(int i = 0; i < selectedGenes.size(); i++) {
			if (selectedGenes.get(i).getAvailable()){
				ft.setText(i+1, 0, selectedGenes.get(i).getSymbol());
				ft.getRowFormatter().setStyleName(i+1, "resultsTable-dataRow");
			}
		}
		ft.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				int row = ft.getCellForEvent(event).getRowIndex()-1;
				if(row >= 0){
					int unifeatureKey = selectedGenes.get(row).getUnifeatureKey();
					String url = function.getUrl();
					url = url.replace("<ufk>", unifeatureKey+"");
					switch(function.getReturnType()){
					case 'I':
						h.setHTML("<img height=\"500px\" width=\"800px\" src=\""+url+"\"/>");
						vp2.add(h);
						break;
					case 'H': 
						h.setHTML("<iframe height=\"500px\" width=\"800px\" src=\""+url+"\"></iframe>");
						vp2.add(h);
						break;
					case 'P':
						h.setHTML("<embed height=\"500px\" width=\"800px\" src=\""+url+"\"/>");
						vp2.add(h);
						break;
					default: 
						h.setHTML("<iframe height=\"500px\" width=\"800px\" src=\""+url+"\"></iframe>");
						vp2.add(h);
						break;
					}
				}
			}
		});
	}

	private void addMultiGeneView() {
		generateParametersArray();
		Button go = new Button("Go");

		formPanel.add(sidebar);
		for (int i=0; i<parameterNames.size(); i++) {
			Label name = new Label(parameterNames.get(i));
			TextBox paramTextBox = new TextBox();
			paramTextBox.setVisibleLength(10);

			sidebar.setWidget(i, 0, name);
			sidebar.setWidget(i, 1, paramTextBox);
		}
		formPanel.add(go);

		// Click handler for the go button
		go.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				vp2.clear();
				vp2.add(remove);
				vp2.setCellHorizontalAlignment(remove, VerticalPanel.ALIGN_RIGHT);

				parseUrlForParameters();
				form.setAction(reparsedUrl);
				System.out.println(reparsedUrl);

				// Set up the callback object.
				AsyncCallback<String> callback = new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						errorLabel.setText("Error");
					}

					@Override
					public void onSuccess(String results) {
						errorLabel.setText("");
						switch(function.getReturnType()){
						case 'I':
							h.setHTML("<img src=\""+results+"\"/>");
							vp2.add(h);
							break;
						case 'H': 
							h.setHTML("<div>"+results+"</div>");
							vp2.add(h);
							break;
						case 'P':
							h.setHTML("<embed height=\"500px\" width=\"800px\" src=\""+results+"\"/>");
							vp2.add(h);
							break;
						default: 
							h.setHTML("<div>"+results+"</div>");
							vp2.add(h);
							break;
						}
					}
				};

				String data = "data="+selectedGenesData.getData()+"&columns="+selectedGenesData.getColumnsType();
				try {
					geneSearchSvc.doPost(reparsedUrl, data, callback);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		form.setWidget(formPanel);
	}
	private void parseUrlForParameters() {
		String newUrl = function.getUrl();
		for (int i=0; i<sidebar.getRowCount(); i++) {
			String name = sidebar.getText(i, 0);
			TextBox tb = (TextBox)sidebar.getWidget(i, 1);
			String value = tb.getValue();

			newUrl = newUrl.replace("%%"+name+"%%", value);
		}
		reparsedUrl = newUrl;
	}

	public void updateFlexTable(){
		ft.removeAllRows();
		ft.setText(0, 0, "GENE");
		ft.getRowFormatter().setStyleName(0, "resultsTable-headerRow");
		for(int i = 0; i < selectedGenes.size(); i++) {
			if (selectedGenes.get(i).getAvailable()){	
				ft.setText(i+1, 0, selectedGenes.get(i).getSymbol());
				ft.getRowFormatter().setStyleName(i+1, "resultsTable-dataRow");
			}
		}
	}

	public boolean getSYNCValue(){
		return sync.getValue();
	}

	public char getType(){
		return type;
	}
}