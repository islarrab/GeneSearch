package biomatec.client;

import java.io.IOException;
import java.util.ArrayList;

import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Gene;
import biomatec.javaBeans.Int;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GeneSearchService</code>.
 */
public interface GeneSearchServiceAsync {
	void geneSearch(String input, AsyncCallback<ArrayList<Gene>> callback)
			throws IllegalArgumentException;
	void geneDetailSearch(ArrayList<Gene> selectedGenes, AsyncCallback<ArrayList<Dataset>> callback)
		throws IllegalArgumentException;
	void generateHeatMap(ArrayList<Gene> selectedGenes, Dataset dataset, AsyncCallback<Int> callback)
		throws IOException;
}
