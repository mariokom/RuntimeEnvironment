package de.ableitner.vlcapi.url;

import java.util.HashMap;

public class RequestUrlCreator {
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// Attributes
	// ============================================================================================================================================
	// ============================================================================================================================================
		
	private String password;
	private String ipAddress;
	private int portNumber;
	// TODO put this in constructor
	private Boolean authentificationViaURL = true;
	
	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// Constructors
	// ============================================================================================================================================
	// ============================================================================================================================================
		
	public RequestUrlCreator(String password, String ipAddress, int portNumber) {
		this.password = password;
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
	}
	
	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// Getters and Setters
	// ============================================================================================================================================
	// ============================================================================================================================================		
	
	// TODO check
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return this.password;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public String getIpAddress() {
		return this.ipAddress;
	}
	
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	
	public int getPortNumber() {
		return this.portNumber;
	}
	
	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// override Methods
	// ============================================================================================================================================
	// ============================================================================================================================================

	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// public Methods
	// ============================================================================================================================================
	// ============================================================================================================================================

	public String createPlaylistRequestUrl() {
		return this.createRequestUrl(RequestTypeEnum.PLAYLIST, null, null, null);
	}

	public String createBrowseRequestUrl() {
		return this.createRequestUrl(RequestTypeEnum.BROWSE, null, null, null);
	}

	public String createVlmRequestUrl() {
		return this.createRequestUrl(RequestTypeEnum.VLM, null, null, null);
	}

	public String createVlmCmdRequestUrl(String cmd) {
		if (cmd == null) {
			throw new NullPointerException("The parameter cmd must not be null!");
		}
		if (cmd.isEmpty()) {
			throw new RuntimeException("The parameter cmd must not be empty!");
		}
		return this.createRequestUrl(RequestTypeEnum.VLM_CMD, null, cmd, null);
	}

	public String createStatusRequestUrl() {
		return this.createRequestUrl(RequestTypeEnum.STATUS, null, null, null);
	}

	public String createStatusRequestUrl(CommandEnum command) {
		this.checkParameters(command, null);
		return this.createRequestUrl(RequestTypeEnum.STATUS, command, null, null);
	}

	public String createStatusRequestUrl(CommandEnum command, HashMap<String, String> parameters) {
		this.checkParameters(command, parameters);
		return this.createRequestUrl(RequestTypeEnum.STATUS, command, null, parameters);
	}
	
	
	
	
	// ============================================================================================================================================
	// ============================================================================================================================================
	// private Methods
	// ============================================================================================================================================
	// ============================================================================================================================================
			
	private String createRequestUrl(RequestTypeEnum requestType, CommandEnum command, String cmd, HashMap<String, String> parameters) {
		String url = "http://";
		if (this.authentificationViaURL) {
			url += ":" + this.password + "@";
		}
		url += this.ipAddress + ":" + this.portNumber + "/requests/" + requestType.getValue();

		if (requestType == RequestTypeEnum.STATUS && command != null) {
			url += "?command=" + command.getValue();
			if (parameters != null && parameters.isEmpty() == false) {
				url += this.createParameters(parameters);
			}
		} else if (requestType == RequestTypeEnum.VLM_CMD) {
			url += "?command=" + cmd;
		}

		return url;
	}

	private void checkParameters(CommandEnum command, HashMap<String, String> parameters) {
		if (command == null) {
			throw new NullPointerException("The parameter command must not be null!");
		}
		if ((command.getNumberOfRequiredParameters() > 0) && parameters == null) {
			throw new NullPointerException(
					"The Command " + command.getValue() + " needs at least " + command.getNumberOfRequiredParameters() + " parameter(s)!");
		} else if (parameters != null) {
			if (parameters.size() < command.getNumberOfRequiredParameters()) {
				throw new RuntimeException("The Command " + command.getValue() + " needs at least "
						+ command.getNumberOfRequiredParameters() + " parameter(s)!");
			}
			if (parameters.size() > command.getNumberOfAllowedParameters()) {
				throw new RuntimeException("The Command " + command.getValue() + " can be called with maximal "
						+ command.getNumberOfRequiredParameters() + " parameter(s)!");
			}
			for (String key : parameters.keySet()) {
				if (command.getValidParameterNames().contains(key) == false) {
					throw new RuntimeException("The Command " + command.getValue() + " was called with the parameter name " + key
							+ ", which is invalid for this command!");
				}
			}
		}
	}

	private String createParameters(HashMap<String, String> parameters) {
		String parametersForUrl = "";
		String value = null;
		for (String key : parameters.keySet()) {
			value = parameters.get(key);
			parametersForUrl += "&" + key + "=" + value;
		}
		return parametersForUrl;
	}
}
