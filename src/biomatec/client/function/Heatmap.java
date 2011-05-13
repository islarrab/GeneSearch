package biomatec.client.function;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class Heatmap extends Composite {

	private HorizontalPanel heatmap = new HorizontalPanel();

	public Heatmap(double values[]) {
		generateHeatmap(values);
		
		this.initWidget(heatmap);
		this.setStyleName("view-general");
	}
	
	private void generateHeatmap(double values[]) {
		heatmap.setBorderWidth(3);
		
		for(int i = 0; i < values.length; i++){
			HorizontalPanel section = new HorizontalPanel();
			section.setBorderWidth(0);
			section.setSize("4px", "20px");

			double x = values[i];
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
	}
}
