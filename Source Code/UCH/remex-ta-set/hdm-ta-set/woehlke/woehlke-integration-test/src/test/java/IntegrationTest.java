import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openurc.uch.ITDMListener;
import org.openurc.uch.TAFatalException;
import org.openurc.uch.TDMFatalException;
import org.woehlke.tdm.WoehlkeTDM;

import de.hdm.woehlke.configurationmanager.WoehlkeConfigurationManagerTAFacade;

public class IntegrationTest {


//@Test
public void testIntegration() throws TAFatalException, TDMFatalException{
	WoehlkeTDM	woehlkeTDM = new WoehlkeTDM();
	WoehlkeConfigurationManagerTAFacade configurationManager = new WoehlkeConfigurationManagerTAFacade();
	ITDMListener tdmListener = new TestTDMListener(woehlkeTDM,configurationManager);
	woehlkeTDM.init(tdmListener, (Map) new HashMap<String,String>(), (Map) new HashMap<String,String>(),null);
	configurationManager.init(new DummyTAListener(), new HashMap<String, String>(), new HashMap<String, String>());
	
	woehlkeTDM.discovery.discoverConfigurationManager();
	List<String> paths = new LinkedList<String>();
	List<String> reqValues = new LinkedList<>();
	paths.add("addNewTarget");
	reqValues.add("127.0.0.1");
	configurationManager.setValuesRequest("id", false, paths,null , reqValues);
}
}
