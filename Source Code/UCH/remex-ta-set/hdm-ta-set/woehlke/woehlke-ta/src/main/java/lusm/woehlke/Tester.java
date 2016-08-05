package lusm.woehlke;

import lusm.whoelke.electricityoutlet.ElectricityOutletHandler;

public class Tester {

	
	public static void main(String[] args) {
		
System.out.println("Test started");
		ElectricityOutletHandler handler = new ElectricityOutletHandler();
		WoehlkeConsole woehlke_console = new WoehlkeConsole(handler);
		woehlke_console.control();
	}

}
