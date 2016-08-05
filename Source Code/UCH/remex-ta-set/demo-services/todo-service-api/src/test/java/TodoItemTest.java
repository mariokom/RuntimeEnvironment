import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import de.hdm.todoservice.api.TodoItem;


public class TodoItemTest {

	@Test
	public void testCompare(){
		
		TodoItem firstItem = new TodoItem(new Date(1), "first Item", "description");
		TodoItem seccondItem = new TodoItem(new Date(2), "2nd  Item", "description");
		
		assertTrue( firstItem.compareTo(seccondItem) == -1);
		
	}
	
	@Test
	public void TestEquals(){
		TodoItem thisItem = new TodoItem(new Date(1), "first Item", "description");
		TodoItem thatItem = new TodoItem(new Date(1), "first Item", "description");
		assertEquals(thisItem, thatItem);
		assertEquals(thatItem, thisItem);
		assertEquals(thisItem, thisItem);
		assertFalse(thisItem.equals( null));
	}
	
	@Test
	public void xmlTest() throws Exception{
		TodoItem original = new TodoItem(new Date(), "title", "description");
		
		JAXBContext context = JAXBContext.newInstance( TodoItem.class );
		Marshaller m = context.createMarshaller();
		m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		m.marshal( original, bOut );
		
		ByteArrayInputStream bIn = new ByteArrayInputStream(bOut.toByteArray());
		Unmarshaller u = context.createUnmarshaller();
		TodoItem returnItem = (TodoItem) u.unmarshal(bIn);
		
		assertEquals(original, returnItem);
		
	}
}