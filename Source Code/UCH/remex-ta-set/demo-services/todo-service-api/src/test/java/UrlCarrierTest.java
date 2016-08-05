import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import de.hdm.todoservice.api.UrlCarrier;


public class UrlCarrierTest {
@Test
public void test() throws JAXBException, MalformedURLException{
	URL originalURL = new URL("http://localhost:8383/test");
	UrlCarrier originalUc = new UrlCarrier();
	originalUc.addUrl(originalURL);
	JAXBContext context = JAXBContext.newInstance( UrlCarrier.class );
	Marshaller m = context.createMarshaller();
	m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
	ByteArrayOutputStream bOut = new ByteArrayOutputStream();
	m.marshal( originalUc, bOut );
	
	ByteArrayInputStream bIn = new ByteArrayInputStream(bOut.toByteArray());
	Unmarshaller u = context.createUnmarshaller();
	UrlCarrier returnUc = (UrlCarrier) u.unmarshal(bIn);
	URL returnUrl = returnUc.createURL(returnUc);
	assertEquals(originalURL, returnUrl);

}
}
