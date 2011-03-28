package biomatec.client.function;

import java.util.ArrayList;

import biomatec.javaBeans.Gene;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class WikiGenes extends Composite{
	
	private HorizontalPanel wikiGenePanel = new HorizontalPanel();
	private FlexTable genes = new FlexTable();
	private HTML wikiGeneDetail = new HTML();
	
		
	public WikiGenes(final ArrayList<Gene> selectedGenes){
				
		//Compose the view
		wikiGenePanel.add(genes);
		wikiGenePanel.add(wikiGeneDetail);
		
		//Create the genes table
		genes.setText(0, 0, "GENE");
		genes.getRowFormatter().setStyleName(0, "resultsTable-headerRow");
		for(int i = 0; i < selectedGenes.size(); i++) {
			genes.setText(i+1, 0, selectedGenes.get(i).getSymbol());
			genes.getRowFormatter().setStyleName(i+1, "resultsTable-dataRow");
		}
		genes.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				int row = genes.getCellForEvent(event).getRowIndex()-1;
				if(row >= 0){
					int unifeatureKey = selectedGenes.get(row).getUnifeatureKey();
					wikiGeneDetail.setHTML("<iframe height=\"500px\" width=\"800px\" src=\"http://www.wikigenes.org/e/gene/e/"+unifeatureKey+".html\"></iframe>");
				}
			}
		});
		
		//Set sizes
		genes.setWidth("100px");
		wikiGenePanel.setSize("1000px", "500px");
		
		//Initialize the Widget
		this.initWidget(wikiGenePanel);
		this.setStyleName("view-general");
	}

}
