package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import edu.seg2105.client.common.ChatIF;
import server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  ChatIF serverUI;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }
  
  

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	String message = String.valueOf(msg);
	String[] mSplit = message.split(" ");
	
	
	if (mSplit[0].equals("#login")) {
		if (client.getInfo("id") == null) {
			try {
				client.setInfo("id", mSplit[1]);
				System.out.println("Welcome " + mSplit[1]);
				this.sendToAllClients("Welcome " + mSplit[1]);
			}
			catch (Exception e) {
				System.out.println("Enter a valid id");
			}
		}
		else {
			try {
				client.sendToClient("Already logged in, closing connection");
				client.close();
			}
			catch (Exception e){
				System.out.println(e.getMessage());
			}
		}
	}
	else {
		System.out.println("Message received: " + msg + " from " + client.getInfo("id"));
	    this.sendToAllClients(client.getInfo("id") + ": " + msg);
	}
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  
  public void clientDisconnected(ConnectionToClient client) {
	  try {
		  client.sendToClient("Welcome!");
	  }
	  catch(Exception e) {
		  System.out.println(e.getMessage());
	  }
  }
  
  public void clientConnected(ConnectionToClient client) {
	  try {
		  client.sendToClient("Goodbye!");
	  }
	  catch(Exception e) {
		  System.out.println(e.getMessage());
	  }
  }
}
//End of EchoServer class
