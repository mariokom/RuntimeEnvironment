package test;

import java.io.IOException;
import java.util.Set;

import cy.ac.ucy.cs.seit.uchCommunication.UchDiscovery;

public class UchDiscoveryTest {

	public static void main(String[] args) throws IOException {
		
		System.out.println("==========================================================================================");
		System.out.println("Testing the UCH discovery functionality");
		System.out.println("==========================================================================================\n");
		
		Set<String> ipSet = UchDiscovery.getUchServerIPs();
		
		System.out.println(ipSet);
		System.out.println();
		
		System.out.println("==========================================================================================");
		System.out.println("End of test.");
		System.out.println("==========================================================================================\n");
	}

}

