package biomatec.client;

import java.util.ArrayList;

import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Gene;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("search")
public interface GeneSearchService extends RemoteService {
	ArrayList<Gene> geneSearch(String gene) throws IllegalArgumentException;
	ArrayList<Dataset> geneDetailSearch(int unifeatureKey) throws IllegalArgumentException;
}

