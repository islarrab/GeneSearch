package biomatec.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.SuggestOracle;

public interface SearchSuggestionService extends RemoteService {

	public static class Util {
		public static SearchSuggestionServiceAsync getInstance() {
			SearchSuggestionServiceAsync instance=(SearchSuggestionServiceAsync) GWT.create(SearchSuggestionService.class);
			ServiceDefTarget target = (ServiceDefTarget) instance;
			target.setServiceEntryPoint("/GWT/suggest");
			return instance;
		}
	}

	public SuggestOracle.Response getSuggestions(SuggestOracle.Request req);

}