package edu.seg2105.edu.server.backend;

import java.io.*;
import java.util.Scanner;

import edu.seg2105.edu.server.backend.*;
import server.ConnectionToClient;
import edu.seg2105.client.common.*;


public class EchoServerConsole implements ChatIF {
	static EchoServer server;
	
	Scanner fromConsole;
	
	public EchoServerConsole(int port) {
		server = new EchoServer(port);
	    fromConsole = new Scanner(System.in); 
	}
	
	public void display(String message) 
	{
		System.out.println("> " + message);
	}
	
	public void serverMessages(String msg) {
		String[] mSplit = msg.split(" ");
		
		if (msg.charAt(0) == '#') {
			if(msg.equals("#quit")) {
				try {
					server.close();
					display("server closed");
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			else if (msg.equals("#stop")) {
				server.stopListening();
				display("stopped listening for connections");
			}
			else if (msg.equals("#close")) {
				server.stopListening();
				for (Thread client : server.getClientConnections()) {
					try
					{
						((ConnectionToClient)client).close();
					}
					catch (Exception ex) {}
				}
				try {
					server.close();
				}
				catch (Exception e){
					System.out.println(e.getMessage());
				}
			}
			else if (mSplit[0].equals("#setport")) {
				if (server.isListening() == false) {
					try {
						server.setPort(Integer.parseInt(mSplit[1]));
					}
					catch (Exception e) {
						display("enter a valid port");
					}
				}
				else {
					display("run #stop first");
				}
			}
			else if (msg.equals("#start")) {
				if (server.isListening() == false) {
					try {
						server.listen();
					}
					catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
			else if (msg.equals("#getport")) {
				display(String.valueOf(server.getPort()));
			}
		}
		else {
			server.sendToAllClients("SERVER MSG: " + msg);
		}
	}
	
	public void accept() {
		try {
			String message;
			
			message = fromConsole.nextLine();
			serverMessages(message);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = 5555; //Set port to 5555
	    }
		
	    EchoServerConsole console = new EchoServerConsole(port);
	    
	    try 
	    {
	      server.listen(); //Start listening for connections
	      console.accept();
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println("ERROR - Could not listen for clients!");
	    }
	 }
}
