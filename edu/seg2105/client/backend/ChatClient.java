// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  private String id;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String id, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.id = id;
    this.clientUI = clientUI;
    System.out.println(id + " has logged on");
    openConnection();
    sendToServer("#login " + id);
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      if (message.charAt(0) == '#') {
    	  String[] mSplit = message.split(" ");
    	  if (message.equals("#quit")) {
    		  quit();
    	  }
    	  else if (message.equals("#logoff")) {
    		  closeConnection();
    	  }
    	  else if (mSplit[0].equals("#sethost")) {
    		  if (!isConnected()) {
    			  try {
    				  setHost(mSplit[1]);
    			  }
    			  catch (Exception e) {
    				  clientUI.display("enter a valid host");
    			  }
    		  }
    		  else {
    			  clientUI.display("Logout first");
    		  }
    	  }
    	  else if (mSplit[0].equals("#setport")) {
    		  if (!isConnected()) {
    			  try {
    				  setPort(Integer.getInteger(mSplit[1]));
    			  }
    			  catch (Exception e) {
    				  clientUI.display("enter a valid port");
    			  }
    		  }
    		  else {
    			  clientUI.display("Logout first");
    		  }
    	  }
    	  else if (message.equals("#login")) {
    		  if (!isConnected()) {
    			  openConnection();
    		  }
    		  else {
    			  clientUI.display("logout first");
    		  }
    	  }
    	  else if (message == "#gethost") {
    		  clientUI.display(getHost());
    	  }
    	  else if (message == "#getPort") {
    		  clientUI.display(String.valueOf(getPort()));
    	  }
      }
      else {
    	sendToServer(message);
      }
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  public void connectionClosed() {
	  clientUI.display("Server connection closed");
  }
  
  public void connectionException(Exception exec) {
	  clientUI.display("Connection closed due to: " + exec.getMessage());
  }
}
//End of ChatClient class
