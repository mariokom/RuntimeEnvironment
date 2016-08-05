import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.jetty.server.Server;


public class ServerConsole extends Thread {
	
	private Server server;
	
	public ServerConsole(Server server){
		this.server = server;
	}
	   public void run(){
		   
		   
		   						BufferedReader	reader = new BufferedReader(new InputStreamReader(System.in));
				boolean stop = false;
						while (!stop){
				String line = "";
				try {
					line = reader.readLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
						if (line.equals("stop")){
							
								
									System.out.println("Stoppe Tomcat");
									try {
										server.stop();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										System.exit(0);
									}
							stop = true;
							return;
						}
						System.out.println("Invalid command");
						}
			
		}
	}
