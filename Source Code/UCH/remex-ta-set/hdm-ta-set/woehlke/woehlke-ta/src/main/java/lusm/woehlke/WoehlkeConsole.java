package lusm.woehlke;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import lusm.whoelke.electricityoutlet.ElectricityOutletHandler;

public class WoehlkeConsole {
ElectricityOutletHandler my_woehlke;

public WoehlkeConsole(ElectricityOutletHandler handler){
	my_woehlke = handler;
}
	
	public void  control() {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		
		StringTokenizer string_tokenizer = null;
		boolean stop = false;
		while (!stop){
		System.out.print(">");	
		try {
			string_tokenizer = new StringTokenizer(input.readLine() 	);
		} catch (IOException e) {
				e.printStackTrace();
		}
		 		
		String param_1 = string_tokenizer.nextToken();
		
		if (param_1 == "quit") {
			System.out.println("Stopping program");
			break;}
		
		String param_2 = "";
		if (!singleCommand(param_1 )){
		param_2 = string_tokenizer.nextToken();
		}
		
		
		if (param_1.equals( "get")) {
			
							if (param_2 == "temp") { 
					double result = my_woehlke.getTemperature();
					System.out.println(result);
				} else {
					boolean result = false;
					if (param_2.equals("1") ) result = my_woehlke.getSocket1();
					else if (param_2.equals( "2")) result = my_woehlke.getSocket2();
					else if (param_2.equals( "3")) result = my_woehlke.getSocket3();
					System.out.println(param_2 +": " + result);
				}
				
		}  else if (param_1.equals("host")) {
			my_woehlke.setHost(param_2);
			System.out.println("Server: " + my_woehlke.getHost() + ":" + my_woehlke.getPort() );
		} else if (param_1.equals("port")) {
			my_woehlke.setPort(Integer.parseInt( param_2) );
			System.out.println("Server: " + my_woehlke.getHost() + ":" + my_woehlke.getPort() );
		} else if (param_1.equals("test")){
my_woehlke.testProgram();
		} else if (  param_1.equals("status")){
			System.out.println("checking status");
			my_woehlke.update();
		} else{
			boolean setting = Boolean.parseBoolean(param_2);
			if (param_1.equals( "1")) my_woehlke.setSocket1(setting);
			else if (param_1.equals("2")) my_woehlke.setSocket2(setting);
			else if (param_1.equals("3")) {
			System.out.println("wanted");	
				my_woehlke.setSocket3(setting);
						}
		}
		}
		
	}

	private boolean singleCommand(String param_1) {
		boolean single_command = false;
		String [] single_commands = {"test", "status"};
		
		for (String command : single_commands){
			single_command = param_1.equals(command);
			if (single_command){
				return true;
			}
		}
		
		return false;
	}
}
