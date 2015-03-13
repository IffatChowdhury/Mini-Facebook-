import java.io.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;


public class SocialNetworkServant implements SocialNetworkService, Serializable {
	
	private static final long serialVersionUID = 38746L;
	private HashMap<String, Account> users = null;
	
	public SocialNetworkServant() {
		users = new HashMap<String, Account>();
	}
	
	public void save() {
		try {

			FileOutputStream f_out = new FileOutputStream ("myobject.data");
			ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
			obj_out.writeObject (this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, Account> getMap() {
		return this.users;
	}
	
	public static SocialNetworkServant restore() {
		try {
        	FileInputStream f_in = new FileInputStream ("myobject.data");
        	ObjectInputStream obj_in = new ObjectInputStream (f_in);
        	SocialNetworkServant obj = (SocialNetworkServant)obj_in.readObject ();
        	
        	HashMap<String, Account> map = obj.getMap();
        	Set<String> k = map.keySet();
        	Iterator<String> itr = k.iterator();
        	while(itr.hasNext()) {
        		String tmp = (String)itr.next();
        		UserAccount u = (UserAccount)map.get(tmp);
        		Account stub = (Account)UnicastRemoteObject.exportObject(u, 0);
				Registry registry = LocateRegistry.getRegistry();
	            registry.rebind(tmp, stub);
        	}
        	return obj;
        } catch(FileNotFoundException e) {
        	return new SocialNetworkServant();
        } catch(Exception e) {
        	e.printStackTrace();
        	return null;
        }
	}

	@Override
	public int createAccount(String username, String password1, String password2) throws RemoteException {
		if (!(password1.equals(password2)))
			return -2; // the passwords are inconsistent
		
		synchronized(users) {
			if (users.containsKey(username)) {
				return -1; // the username has been taken
			} else {
				Account newuser = new UserAccount(username, password1, this);
				Account stub = (Account)UnicastRemoteObject.exportObject(newuser, 0);
				Registry registry = LocateRegistry.getRegistry();
	            registry.rebind(username, stub);
				users.put(username, newuser);
				save();
				return 0;
			}
		}
	}

	@Override
	public int loginAccount(String username, String password) throws RemoteException {
		UserAccount account = null;
		synchronized(users) {
			account = (UserAccount)users.get(username);
		}
		if(account == null) {
			return -1; // the user doesn't exist 
		} else if (account.verifyPassword(password)) {
			return 0;
		}
		return -1; // the password is wrong
	}
	
	private Account getAccount(String username) {
		Account account = null;
		synchronized(users) {
			account = users.get(username);
		}
		return account;
	}
	
	private ArrayList<Account> searchByUsername(String username) {
		ArrayList<Account> list = new ArrayList<Account>();
		Account a = null;
		synchronized(users) {
			a = getAccount(username);
		}
		if(a != null)
			list.add(a);
		return list;
	}
	
	private ArrayList<Account> searchByCity(String city) {
		ArrayList<Account> list = new ArrayList<Account>();
		Collection<Account> l = null;
		synchronized(users) {
			l = users.values();
		}
		if (l != null) {
			Iterator<Account> itr = l.iterator();
			while(itr.hasNext()) {
				UserAccount tmp = (UserAccount)itr.next();
				if(tmp.checkCity(city)) {
					list.add(tmp);
				}
			}
		}
		return list;
	}
	
	private ArrayList<Account> searchByCollege(String college) {
		ArrayList<Account> list = new ArrayList<Account>();
		Collection<Account> l = null;
		synchronized(users) {
			l = users.values();
		}
		if (l != null) {
			Iterator<Account> itr = l.iterator();
			while(itr.hasNext()) {
				UserAccount tmp = (UserAccount)itr.next();
				if(tmp.checkCollege(college)) {
					list.add(tmp);
				}
			}
		}
		return list;
	}

	@Override
	public ArrayList<String> searchForFriends(String s) throws RemoteException {
		ArrayList<Account> list = null;
		ArrayList<String> candidates = new ArrayList<String>();
		String[] splits = s.split("&");
			
		switch(splits.length) {
			case 0: 
				break;  // no search key words
			case 1:
				list = searchByUsername(splits[0]);
				break;
			case 2:
				list = searchByCity(splits[1]);
				if(!splits[0].equals("")) {
					Account one = getAccount(splits[0]);
					if(one != null && list.contains(one)) {
						list.clear();
						list.add(one);
					}
					if(one != null && (!list.contains(one))) {
						list.clear();
					}
				}
				break;
			case 3:
				list = searchByCollege(splits[2]);
				if(!splits[0].equals("")) {
					Account one = getAccount(splits[0]);
					if(one != null && list.contains(one)) {
						list.clear();
						list.add(one);
					}
					if(one != null && (!list.contains(one))) {
						list.clear();
					}
				}
				
				if(!splits[1].equals("")) {
					Iterator<Account> itr = list.iterator();
					while(itr.hasNext()) {
						UserAccount tmp = (UserAccount)itr.next();
						if(!(tmp.checkCity(splits[1]))) {
							itr.remove();
						}
					}
				}
				break;
			default:
				break;
		}
		
		if(list == null || list.isEmpty()) 
			return null;
		else {
			Iterator<Account> itr = list.iterator();
			while(itr.hasNext()) {
				UserAccount tmp = (UserAccount)itr.next();
				candidates.add(tmp.getID());
			}
			
			return candidates;	
		}
	}
	
	//public int inviteFriend(String requester, String responder, Callback object) throws RemoteException {
	public int inviteFriend(String requester, String responder) throws RemoteException {
		UserAccount one = null;
		UserAccount two = null;
		synchronized(users) {
			one = (UserAccount)getAccount(requester);
			two = (UserAccount)getAccount(responder);
		}
		
		if(one != null && two != null) {
			String message = "invite&" + requester;
			two.addNotification(one, message);
			return 0;
		} else {
			return -1; // one of the party doesn't exist
		}
	}
	
	public static void main(String[] args) {
		try{
			String name = "socialnetworkservant";
			//SocialNetworkService servant = new SocialNetworkServant();
			SocialNetworkService servant = restore();
			SocialNetworkService stub = (SocialNetworkService)UnicastRemoteObject.exportObject(servant, 0);
			Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("social network servant is ready.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
