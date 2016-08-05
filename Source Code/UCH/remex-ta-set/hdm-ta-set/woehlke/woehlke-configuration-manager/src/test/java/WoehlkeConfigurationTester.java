import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openurc.uch.ITAListener;
import org.openurc.uch.TAException;
import org.openurc.uch.TAFatalException;

import de.hdm.woehlke.api.WoehlkeConstants;
import de.hdm.woehlke.configurationmanager.WoehlkeConfigurationManagerTAFacade;


public class WoehlkeConfigurationTester {


public final static String ipadr = "1.2.3.4";

@Test
public void registrationTest() throws TAFatalException, TAException{
	
	WoehlkeConfigurationManagerTAFacade wcmf = new WoehlkeConfigurationManagerTAFacade();
	ITAListener taListener = new TestITAListener();
	
	wcmf.init(taListener, new HashMap<String, String>(), new HashMap<String, String>());
	Map targetProps = new HashMap<String, Object>();
	targetProps. put(WoehlkeConstants.WOEHLKE_DISCOVERY_MODULE, new TestDiscoveryModule() );
	wcmf.registerTarget("test", targetProps, null);
wcmf.	openSessionRequest("test", "WoehlkeConfigurationManagerSocket", new HashMap<String, String>(), null);
wcmf.sessionOpened("test", "session", "WoehlkeConfigurationManagerSocket", new HashMap<String, String>(), null);
List<String> paths = new LinkedList<String>();
paths.add("AddNewWebSteckdose");
List<String> operations = new LinkedList<String>();
operations.add("x");
		
List<String> reqValues = new LinkedList<String>();
reqValues.add(ipadr);
		
wcmf.setValuesRequest("session", true, paths, operations, reqValues);
}
}
