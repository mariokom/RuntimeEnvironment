import java.util.HashMap;

import org.openurc.uch.TAFatalException;

import de.hdm.woehlke.configurationmanager.WoehlkeConfigurationManagerTAFacade;


public class MyTest {

	public static void main(String[] args){
		WoehlkeConfigurationManagerTAFacade wcmtaf = new WoehlkeConfigurationManagerTAFacade();
		try {
			System.out.print("init...");
			wcmtaf.init(new TestITAListener(), new HashMap<String,String>(), new HashMap<String,String>());
			System.out.println("done");
		} catch (TAFatalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
