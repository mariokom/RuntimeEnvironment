import java.util.List;
import java.util.Map;

import org.openurc.uch.IProfile;
import org.openurc.uch.ITDM;
import org.openurc.uch.ITDMListener;
import org.openurc.uch.IUCHStore;
import org.openurc.uch.TAException;
import org.openurc.uch.TAFatalException;
import org.openurc.uch.UCHException;
import org.openurc.uch.UCHNotImplementedException;
import org.woehlke.tdm.Discovery;
import org.woehlke.tdm.WoehlkeTDM;

import de.hdm.woehlke.api.WoehlkeConstants;
import de.hdm.woehlke.configurationmanager.WoehlkeConfigurationManagerTAFacade;
import edu.wisc.trace.uch.util.Constants;
import static org.junit.Assert.assertEquals;
public class TestTDMListener implements ITDMListener {

	WoehlkeConfigurationManagerTAFacade configurationManager;


	
	TestTDMListener(WoehlkeTDM woehlkeTDM, WoehlkeConfigurationManagerTAFacade configurationManager){	
		this.configurationManager = configurationManager;
	}

	@Override
	public void addUriServiceContexts(ITDM arg0, String arg1, List<Map<String, IProfile>> arg2) throws UCHException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Map<String, IProfile>> getContexts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocument(String arg0, String arg1, Map<String, IProfile> arg2) throws UCHException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIpAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IUCHStore getLocalUCHStore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<List<Map<String, List<String>>>> getResources(String arg0, List<Map<String, List<String>>> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getUCHProps() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isImplemented(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeTDM(ITDM arg0) throws UCHNotImplementedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeUriServiceContexts(ITDM arg0, String arg1, List<Map<String, IProfile>> arg2) throws UCHException {
		// TODO Auto-generated method stub

	}

	@Override
	public String startUriService(ITDM arg0, String arg1, int arg2, boolean arg3, String arg4, boolean arg5,
			List<Map<String, IProfile>> arg6) throws UCHException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stopUriService(ITDM arg0, String arg1) throws UCHException {
		// TODO Auto-generated method stub

	}

	@Override
	public void targetContextsAdded(String arg0, List<Map<String, IProfile>> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void targetContextsRemoved(String arg0, List<Map<String, IProfile>> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void targetDiscarded(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public String targetDiscovered(ITDM arg0, Map<String, Object> targetProps, Map<String, String> arg2,
			List<Map<String, IProfile>> arg3) {
		try {
			 if (targetProps.get(Constants.PROPERTY_DEVICE_TYPE) == WoehlkeConstants.PROPERTY_DEVICE_TYPE_VALUE_WOEHLKE_CONFIGURATION_MANAGER){
			configurationManager.registerTarget("id", targetProps, arg3 );
			 } else if (targetProps.get(Constants.PROPERTY_DEVICE_TYPE) == WoehlkeConstants.PROPERTY_DEVICE_TYPE_VALUE_WOEHLKE_Web_Steckdose){
				 
				 assertEquals("127.0.0.1", targetProps.get(WoehlkeConstants.IpAddress )  );
			 }
		} catch (TAException | TAFatalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Map<String, String>> uploadResources(List<Map<String, List<String>>> arg0, List<String> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
