package biomatec.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * The async counterpart of <code>GeneSearchService</code>.
 */
public interface SearchSuggestionServiceAsync {
	void getSuggestions(SuggestOracle.Request req, AsyncCallback<SuggestOracle.Response> callback) throws IllegalArgumentException;
}