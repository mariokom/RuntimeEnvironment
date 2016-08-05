
public class MyTest {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("starteTest");
		try {
		System.out.println("---TodoServiceTest.testDemo");
			new TodoServiceTest().testDemo();
		System.out.println("--- TodoListTest.addItemsTest --");
			new TodoListTest().testAddTodoItems(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
