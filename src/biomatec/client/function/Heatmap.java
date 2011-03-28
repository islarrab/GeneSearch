package biomatec.client.function;

import java.io.IOException;
import java.util.ArrayList;

import biomatec.client.GeneSearchService;
import biomatec.client.GeneSearchServiceAsync;
import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Gene;
import biomatec.javaBeans.Double;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Heatmap extends Composite {

	private GeneSearchServiceAsync geneSearchSvc = GWT.create(GeneSearchService.class);

	// Vertical panel that will be sent at the end
	private DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
	private VerticalPanel heatmaps = new VerticalPanel();
	
	private ArrayList<Gene> selectedGenes;
	
	public Heatmap(ArrayList<Gene> selectedGenes, Dataset dataset) {
		dock.addNorth(generateWidgetHeader(), 100);
		
		this.selectedGenes = selectedGenes;
		
		AsyncCallback<ArrayList<ArrayList<Double>>> callback = new AsyncCallback<ArrayList<ArrayList<Double>>>() {
			@Override
			public void onFailure(Throwable caught) {
				return;
			}

			@Override
			public void onSuccess(ArrayList<ArrayList<Double>> results) {
				generateHeatmaps(results);
			}
		};

		try {
			geneSearchSvc.generateHeatMap(selectedGenes, dataset, callback);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.initWidget(heatmaps);
		this.setStyleName("view-general");
	}

	private Widget generateWidgetHeader() {
		HorizontalPanel hp = new HorizontalPanel();
		Button btn = new Button("Remove View");
		Label lb = new Label("Heatmaps view");
		
		btn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
			}
		});
		
		hp.add(btn);
		hp.add(lb);
		return hp;
	}

	private void generateHeatmaps(ArrayList<ArrayList<Double>> results) {
		heatmaps.setBorderWidth(5);
		for(int i = 0; i < results.size(); i++){
			HorizontalPanel heatmap = new HorizontalPanel();
			heatmap.add(new Label(this.selectedGenes.get(i).getSymbol()));
			//heatmap.setBorderWidth(3);
			for(int j = 0; j < results.get(i).size(); j++){
				HorizontalPanel section = new HorizontalPanel();
				section.setBorderWidth(0);
				section.setSize("4px", "20px");

				double x = results.get(i).get(j).getDouble();
				if(x >= 0 && x < .1)
					section.setStyleName("heatmap0");
				else if(x >= .1 && x < .2)
					section.setStyleName("heatmap1");
				else if(x >= .2 && x < .3)
					section.setStyleName("heatmap2");
				else if(x >= .3 && x < .4)
					section.setStyleName("heatmap3");
				else if(x >= .4 && x < .5)
					section.setStyleName("heatmap4");
				else if(x >= .5 && x < .6)
					section.setStyleName("heatmap5");
				else if(x >= .6 && x < .7)
					section.setStyleName("heatmap6");
				else if(x >= .7 && x < .8)
					section.setStyleName("heatmap7");
				else if(x >= .8 && x < .9)
					section.setStyleName("heatmap8");
				else if(x >= .9 && x < 1)
					section.setStyleName("heatmap9");
				else if(x == 1)
					section.setStyleName("heatmap10");

				heatmap.add(section);
			}
			heatmaps.add(heatmap);
		}
	}
}
