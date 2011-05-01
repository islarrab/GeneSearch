package biomatec.client.function;

import java.util.ArrayList;

import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Gene;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.CheckBox;

public class FunctionView extends Composite {
	
	private Function function;
	private ArrayList<Gene> selectedGenes;
	private Dataset dataset;
	private ArrayList<String[]> parameters;
	private String parsedUrl;
	private HorizontalPanel panel = new HorizontalPanel();
	private FlexTable ft = new FlexTable();
	private VerticalPanel vp = new VerticalPanel();
	private VerticalPanel vp2 = new VerticalPanel();
	private HTML h = new HTML();
	private CheckBox sync = new CheckBox("SYNC");
	private Button remove = new Button("Remove");
	
	public FunctionView(Function f, ArrayList<Gene> sg, Dataset ds) {
		this.function = f;
		this.selectedGenes = sg;
		this.dataset = ds;
		this.parameters = new ArrayList<String[]>();
		sync.setValue(true);
		
		//Build the widget
		panel.add(vp);
		panel.add(vp2);
		vp.add(sync);
		vp2.add(remove);
		remove.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				removeFromParent();
			}
		});
		if (function.getFunctionType() == 'S'){
			addSingleGeneView();
		}
		else if (function.getFunctionType() == 'M'){
			parseURL();
		}
		
		//Format the widget
		ft.setWidth("150px");
		panel.setSize("1000px", "500px");
		vp2.setWidth("805px");
		vp2.setCellHorizontalAlignment(remove, VerticalPanel.ALIGN_RIGHT);
		
		//Initialize the widget
		this.initWidget(panel);
		this.setStyleName("view-general");
	}
	
	private void parseURL() {
		String newUrl = function.getUrl();
		
		//Parse for dataset key
		newUrl.replaceAll("<dsk>", dataset.getDatasetKey()+"");
		
		//Parse for unifeature keys
		String ufk = "";
		if (function.getFunctionType() == 'M') {
			ufk = selectedGenes.get(0).getUnifeatureKey()+"";
			for (int i=1; i<selectedGenes.size(); i++) {
				ufk += "+" + selectedGenes.get(i).getUnifeatureKey();
			}
			newUrl.replaceAll("<ufk>", ufk);
		}
		
		this.parsedUrl = newUrl;
	}
	
	private void addSingleGeneView(){
		vp.add(ft);
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
					case 'w': 
						//TODO el resto de los return types para single gene
						h.setHTML("<iframe height=\"500px\" width=\"800px\" src=\""+url+"\"></iframe>");
						vp2.add(h);
						break;
					default: break;
					}
				}
			}
		});
	}
	
	public boolean getSYNCValue(){
		return sync.getValue();
	}
}
