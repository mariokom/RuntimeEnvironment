import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;


public class TestHelpers {

	public static UriInfo getListUri(final String pa) {
		
		UriInfo uri = new UriInfo() {
			private String path = "/" + pa;
			public String toString() {
				return "http://localhost:8383/todo-service"  + path;
			}
			
			@Override
			public UriBuilder getRequestUriBuilder() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public URI getRequestUri() {
				try {
					return new URI(this.toString());
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			public MultivaluedMap<String, String> getQueryParameters(boolean arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public MultivaluedMap<String, String> getQueryParameters() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<PathSegment> getPathSegments(boolean arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<PathSegment> getPathSegments() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public MultivaluedMap<String, String> getPathParameters(boolean arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public MultivaluedMap<String, String> getPathParameters() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getPath(boolean arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getPath() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<String> getMatchedURIs(boolean arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<String> getMatchedURIs() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<Object> getMatchedResources() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public UriBuilder getBaseUriBuilder() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public URI getBaseUri() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public UriBuilder getAbsolutePathBuilder() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public URI getAbsolutePath() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		return uri;
	}

	public static UriInfo getTodoServiceUri() {
		UriInfo uri = new UriInfo() {
			@Override
			public String toString(){
				return "http://localhost:8383/todo-service/";
			}
			@Override
			public UriBuilder getRequestUriBuilder() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public URI getRequestUri() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public MultivaluedMap<String, String> getQueryParameters(boolean arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public MultivaluedMap<String, String> getQueryParameters() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<PathSegment> getPathSegments(boolean arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<PathSegment> getPathSegments() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public MultivaluedMap<String, String> getPathParameters(boolean arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public MultivaluedMap<String, String> getPathParameters() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getPath(boolean arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getPath() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<String> getMatchedURIs(boolean arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<String> getMatchedURIs() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<Object> getMatchedResources() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public UriBuilder getBaseUriBuilder() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public URI getBaseUri() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public UriBuilder getAbsolutePathBuilder() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public URI getAbsolutePath() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		return uri;
	}

	public static UriInfo getListUri() {
				return getListUri("");
	}

}
