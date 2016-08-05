package de.ableitner.vlcapi.http;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import de.ableitner.vlcapi.exceptions.VlcApiException;
import de.ableitner.vlcapi.helpers.Checker;

public class SimpleHttpClient implements IHttpClient {
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// Attributes
	// ============================================================================================================================================
	// ============================================================================================================================================
	
	private int socketTimeoutInMilliseconds;
	private int connectTimeoutInMilliseconds;
	private int connectionRequestTimeoutInMilliseconds;
	
	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// Constructors
	// ============================================================================================================================================
	// ============================================================================================================================================
	
	public SimpleHttpClient() {
		this(5000);
	}
	
	public SimpleHttpClient(int timeout) {
		this(timeout, timeout, timeout);
	}
	
	public SimpleHttpClient(int socketTimeoutInMilliseconds, int connectTimeoutInMilliseconds, int connectionRequestTimeoutInMilliseconds){
		Checker.checkIntegerGreater(socketTimeoutInMilliseconds, "socketTimeoutInMilliseconds", 1);
		Checker.checkIntegerGreater(connectTimeoutInMilliseconds, "connectTimeoutInMilliseconds", 1);
		Checker.checkIntegerGreater(connectionRequestTimeoutInMilliseconds, "connectionRequestTimeoutInMilliseconds", 1);
		this.socketTimeoutInMilliseconds = socketTimeoutInMilliseconds;
		this.connectTimeoutInMilliseconds = connectTimeoutInMilliseconds;
		this.connectionRequestTimeoutInMilliseconds = connectionRequestTimeoutInMilliseconds;
	}
	
	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// Getters and Setters
	// ============================================================================================================================================
	// ============================================================================================================================================

	// ============================================================================================================================================
	// ============================================================================================================================================
	// override Methods
	// ============================================================================================================================================
	// ============================================================================================================================================
	
	@Override
	public String sendHttpRequest(String url) throws VlcApiException {
		Checker.checkNullAndEmptiness(url, "url");

		String result = null;

		CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(this.createRequestConfiguration()).build();

		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		HttpEntity entity = null;

		try {
			response = client.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode < 200 || statusCode >= 300) {
				throw new VlcApiException("The execution of the http request failed! VLC answered with: " + response.getStatusLine());
			} else {
				entity = response.getEntity();
				if(entity != null){
					result = EntityUtils.toString(entity);	
				}else{
					throw new VlcApiException("The entity's response is null!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new VlcApiException("The execution of the http request failed!");
		} finally {
			try {
				if (entity != null) {
					EntityUtils.consume(entity);
				}
				if (response != null) {
					response.close();
				}
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new VlcApiException("An error occurred while releasing the http connection resources!");
			}
		}
		return result;
	}
	
	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// public Methods
	// ============================================================================================================================================
	// ============================================================================================================================================

	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// private Methods
	// ============================================================================================================================================
	// ============================================================================================================================================
	
	private RequestConfig createRequestConfiguration() {
		RequestConfig requestConfiguration = RequestConfig.copy(RequestConfig.DEFAULT)
                .setSocketTimeout(this.socketTimeoutInMilliseconds)
                .setConnectTimeout(this.connectTimeoutInMilliseconds)
                .setConnectionRequestTimeout(this.connectionRequestTimeoutInMilliseconds)
                .build();
		return requestConfiguration;
	}
	

}
