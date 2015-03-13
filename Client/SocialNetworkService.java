//package service;

import java.util.*;
import java.rmi.*;

public interface SocialNetworkService extends Remote {
	// success: 0, account have been taken: -1, two passwords are inconsistent: -2
	// after successfully creating an account, the client get the UserAccount reference by querying the rmiregistry with the username in backend
	//public int createAccount(String username, String password1, String password2, CallBack object) throws RemoteException; 
	public int createAccount(String username, String password1, String password2) throws RemoteException;
		
	// success: 0, fail: -1 and also need to invoke getAccount after login successfully
	//public int loginAccount(String username, String password, CallBack object) throws RemoteException;
	public int loginAccount(String username, String password) throws RemoteException;
	
	//public Account getAccount(String username) throws RemoteException;
	
	// argument format: username&livingcity&college
	// return a list of username
	public ArrayList<String> searchForFriends(String s) throws RemoteException; 
	
	// the Callback object is defined by the client side
	// 0 "Server has received the invitation"  -1 "exception"
	// public int inviteFriend(String requester, String responser, Callback object) throws RemoteException; 
	// we implement the version without callback object first
	public int inviteFriend(String requester, String responser) throws RemoteException;
}