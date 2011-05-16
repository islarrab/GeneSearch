package biomatec.client.function;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import biomatec.client.GeneSearchService;
import biomatec.client.GeneSearchServiceAsync;
import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Function;
import biomatec.javaBeans.Gene;
import biomatec.javaBeans.SelectedGenesData;
import biomatec.server.HTTPRequest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.CheckBox;

public class FunctionView extends Composite {

	private Function function;
	private ArrayList<Gene> selectedGenes;
	private Dataset dataset;
	private SelectedGenesData selectedGenesData;
	private ArrayList<String> parameterNames;
	private String parsedUrl = null; // String parsed for common parameters (ufk, dsk, etc.)
	private String reparsedUrl = null; // String parsed from 'parsedUrl' for captured parameters

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
	private String columnTypes; // used only for heatmaps

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

	private void parseURL() {
		String newUrl = function.getUrl();

		//Parse for data
		newUrl = newUrl.replace("<data>", selectedGenesData.getData());
		//Parse for columns type
		newUrl = newUrl.replace("<columns>", selectedGenesData.getColumnsType());

		this.parsedUrl = newUrl;

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
					//TODO el resto de los return types para single gene
					case 'I':
						h.setHTML("<img height=\"500px\" width=\"800px\" src=\""+url+"\"></iframe>");
						vp2.add(h);
						break;
					case 'H': 
						h.setHTML("<iframe height=\"500px\" width=\"800px\" src=\""+url+"\"></iframe>");
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

		form.setEncoding(FormPanel.ENCODING_URLENCODED);
		form.setMethod(FormPanel.METHOD_POST);
		
		Hidden data = new Hidden();
		data.setValue(this.selectedGenesData.getData());
		data.getElement().setAttribute("name", "data");
		Hidden columns = new Hidden();
		columns.setValue(this.selectedGenesData.getColumnsType());
		columns.getElement().setAttribute("name", "columns");
		formPanel.add(data);
		formPanel.add(columns);
		formPanel.add(sidebar);
		for (int i=0; i<parameterNames.size(); i++) {
			Label name = new Label(parameterNames.get(i));
			TextBox paramTextBox = new TextBox();
			paramTextBox.getElement().setAttribute("name", parameterNames.get(i).toLowerCase());
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
				form.submit();

				/*
				switch(function.getReturnType()) {
				// TODO el resto de los return types para multiGene
				case 'I': // Image
					Image image = new Image(reparsedUrl);
					vp2.add(image);
					break;
				case 'H': // HTML
					System.out.println(reparsedUrl);
					h.setHTML("<iframe height=\"500px\" width=\"800px\" src=\""+reparsedUrl+"\"></iframe>");
					vp2.add(h);
					break;
				case 'P': // PDF
					h.setHTML("<embed src=\""+reparsedUrl+"\" height=\"500px\" width=\"800px\">");
				default: // the default action is treating the result as HTML 
					h.setHTML("<iframe height=\"500px\" width=\"800px\" src=\""+reparsedUrl+"\"></iframe>");
					vp2.add(h);
					break;
				}
				 */
			}
		});

		
		
		/*
		// Add an event handler to the form.
		form.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
				// This event is fired just before the form is submitted. We can take
				// this opportunity to perform validation.
			}
		});
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// When the form submission is successfully completed, this event is
				// fired. Assuming the service returned a response of type text/html,
				// we can get the result text here (see the FormPanel documentation for
				// further explanation).
				System.out.println("lol"+event.getResults());
				h.setHTML("<div>"+event.getResults()+"</div>");
				vp2.add(h);
			}
		});
		*/

		form.setWidget(formPanel);
	}
	private void parseUrlForParameters() {
		String newUrl = parsedUrl;
		for (int i=0; i<sidebar.getRowCount()-1; i++) {
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

	/**
	 * used only for heatmaps
	 * @param columnTypes string with the type of each column returned from the
	 * heatmaps webservice
	 */
	private void setColumnTypes(String columnTypes) {
		this.columnTypes = columnTypes;
	}

	/**
	 * used only for heatmaps
	 * @return columnTypes
	 */
	private String getColumnTypes() {
		return columnTypes;
	}

}
