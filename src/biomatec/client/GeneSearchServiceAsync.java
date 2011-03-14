package biomatec.client;

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
	void geneDetailSearch(ArrayList<Int> input, AsyncCallback<ArrayList<Dataset>> callback)
		throws IllegalArgumentException;
}