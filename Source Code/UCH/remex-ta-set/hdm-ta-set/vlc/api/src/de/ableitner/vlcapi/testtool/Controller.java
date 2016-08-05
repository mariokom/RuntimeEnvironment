package de.ableitner.vlcapi.testtool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

import de.ableitner.vlcapi.IVlcHttpApi;
import de.ableitner.vlcapi.VlcHttpApi;
import de.ableitner.vlcapi.http.SimpleHttpClient;
import de.ableitner.vlcapi.response.ResponseHandler;
import de.ableitner.vlcapi.url.RequestUrlCreatorAdapter;

public class Controller {
	
	private Scanner scanner;
	private boolean terminate;
	private IVlcHttpApi vlc;
	private RequestUrlCreatorAdapter ruca;
	
	
	public Controller(){
		this.scanner = new Scanner(System.in);
		this.terminate = false;
		this.ruca = new RequestUrlCreatorAdapter("12345", "127.0.0.1", 8080);
		this.vlc = new VlcHttpApi(this.ruca, new SimpleHttpClient(), new ResponseHandler());
	}
	
	
	public void start(){
		while(!this.terminate && this.scanner.hasNextLine()){
			String line = this.scanner.nextLine();
			ArrayList<String> lineItems = this.splitLine(line);
			boolean inputConsumed = false;
			inputConsumed = this.handleCommand(lineItems);
			if(inputConsumed == false){
				System.out.println("Unknown input! Type in \"help\" for available commands!");
			}
			System.out.println(line);
			
		}
	}
	
	private ArrayList<String> splitLine(String line){
		ArrayList<String> result = new ArrayList<String>();
		String[]tmp = line.split(" ");
		for(int i = 0; i < tmp.length; i++){
			result.add(tmp[i]);
		}
		return result;
	}
	
	private boolean handleCommand(ArrayList<String> lineItems){
		boolean consumed = true;
		String command = lineItems.get(0);
		switch(command){
		case ControllerCommands.HELP:
			this.printHelp();
			break;
		case ControllerCommands.EXIT:
			this.terminate = true;
			break;
		case ControllerCommands.CURRENT_SETTINGS:
			this.printCurrentSettings();
			break;
		case ControllerCommands.SET_IP:
			break;	
		case ControllerCommands.SET_PORT:
			break;	
		case ControllerCommands.SET_PASSWORD:
			break;	
		case ControllerCommands.SET_TIMEOUT:
			break;
		case ControllerCommands.TEST:
			this.executeMethod(lineItems);
			break;
		default:
			consumed = false;
		}
		return consumed;
	}
	
	private void printHelp(){
		// TODO
		this.printVlcApiHelp();
	}
	
	private void printVlcApiHelp(){
		Method[] methods = IVlcHttpApi.class.getMethods();
		for(int i = 0; i < methods.length; i++){
			Method tmp = methods[i];
			java.lang.reflect.Parameter[] parameters = tmp.getParameters();
			String line = i + 1 + " " + tmp.getName();
			for(int n = 0; n < parameters.length; n++){
				java.lang.reflect.Parameter parameter = parameters[n];
				line += " " + parameter.getType() + " " + parameter.getName();
			}
			System.out.println(line);
		}
	}
	
	private void printCurrentSettings(){
		System.out.println("IP-Address: " + this.ruca.getIpAddress());
		System.out.println("Port: " + this.ruca.getPortNumber());
		System.out.println("Password: " + this.ruca.getPassword());
	}
	
	private void executeMethod(ArrayList<String> lineItems){
		String methodSelection = lineItems.get(1);
		if(methodSelection != null){
			Method method = null;
			Class[] parameterTypes = null;
			Object[] parameters = new Object[lineItems.size() - 2];
			if(this.isInteger(methodSelection)){
				// TODO error handling
				Method[] methods = IVlcHttpApi.class.getMethods();
				method = methods[Integer.valueOf(methodSelection) - 1];
				parameterTypes = method.getParameterTypes();
				
				
			}else{
				// TODO handle call by method name
			}
			for(int i = 2; i < lineItems.size(); i++){
				parameters[i - 2] = this.castInput(parameterTypes[i - 2], lineItems.get(2));
				//parameters[i - 2] = lineItems.get(i);
			}
			try {
				System.out.println(parameters);
				method.invoke(this.vlc, parameters);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private boolean isInteger(String potentialNumber){
		boolean isNumber = true;
		try{
			Integer.valueOf(potentialNumber);	
		}catch(NumberFormatException e){
			isNumber = false;
		}
		return isNumber;
	}
	
	private Object castInput(Class parameterType, String parameterValue){
		System.out.println("parameterType: " + parameterType.getSimpleName());
		System.out.println("parameterValue: " + parameterValue);
		Object result = null;
		if(parameterType.getSimpleName().equals("int")){
			result = Integer.valueOf(parameterValue);
		}else if(parameterType.getSimpleName().equals("String")){
			result = parameterValue;
		}
		return result;
	}
}
