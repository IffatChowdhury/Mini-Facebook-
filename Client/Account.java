//package service;

import java.util.*;
import java.rmi.*;

public interface Account extends Remote {
	// not allow to view "no"
	// "yes&username&profession&livingcity&company&college&year"
	
	public String viewProfile(String requester) throws RemoteException; 
	
	// not allowed to update the username because it is a primary key
	// argument format: "profession&city&company&college&year" the year can't be empty(replace the empty with "-1")
	// 0 success -1 errors
	public int updateProfile(String s) throws RemoteException;  
	
	public int postUpdates(String sender, String content) throws RemoteException;
	
	//the return String format: sender&receiver&content
	//when rendering the updates, the client side need to sort them in Date order
	public HashMap<Date, String> getUpdates() throws RemoteException; 
	
	public ArrayList<String> getFriendsList() throws RemoteException;
	
	public String getUsername() throws RemoteException;
	
	// reply the friends invitation
	// argument data format: accept&responder or decline&responder
	public int reply(String response, String invitor) throws RemoteException;
	
	public ArrayList<String> getNotifications() throws RemoteException;
	
	public int register(String obj, String ip) throws RemoteException;
}
