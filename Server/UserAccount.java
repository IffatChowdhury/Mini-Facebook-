import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.io.*;

public class UserAccount implements Account, Serializable {
	private static final long serialVersionUID = 45873L;
	private String username;
	private String password;
	private String profession;
	private String city;
	private String company;
	private String college;
	private String graduatingYear;
	private ArrayList<Account> pendingList;
	private ArrayList<Account> friendsList;
	private ArrayList<String> notifications;  //"invite&invitor", "accept&responder", "decline&responder"
	private HashMap<Date, String> posts;
	private SocialNetworkServant server;
	
	private String callback = null;
	private String ip = null;
	
	public int register(String name, String ip) throws RemoteException {
		if(name == null) 
			return -1;
		this.callback = name;
		this.ip = ip;
		System.out.println("register callback object from " + this.callback + "@" + ip);
		return 0;
	}
	
	public void addNotification(Account requester, String notification) {
		
		synchronized(this.notifications) {
			this.notifications.add(notification);
		}
		
		if(notification.startsWith("invite")) {
			synchronized(this.pendingList) {
				this.pendingList.add(requester);
			}
			
			if (this.callback != null) {
				int i = -2;
	            try {
	            	Registry registry = LocateRegistry.getRegistry(this.ip);
	            	CallBack obj = (CallBack) registry.lookup(this.callback);
	            	i = obj.notify("invite");
	            } catch(Exception e) {
	            	e.printStackTrace();
	            }
	            
	            System.out.println(this.callback + " invoke call back object:invite");
	            if (i == -1)
					System.out.println(this.callback + " client callback object error");
			}
		}
		
		this.server.save();
	}
	
	public ArrayList<String> getNotifications(){
		synchronized(this.notifications) {
			return this.notifications;
		}
	}
	
	public void addFriend(Account a) {
		synchronized(this.friendsList) {
			this.friendsList.add(a);
		}
		
		this.server.save();
	}
	
	public int reply(String response, String invitor) throws RemoteException{
		int i = -2;
		String[] splits = response.split("&");
		String message = "invite&" + invitor;
		
		synchronized(this.notifications) {
			if(this.notifications.contains(message) && (splits[0].equals("accept") || splits[0].equals("decline"))) {
				this.notifications.remove(message);
				Iterator<Account> itr = this.pendingList.iterator();
				while(itr.hasNext()) {
					UserAccount tmp = (UserAccount)itr.next();
					if(tmp.getID().equals(invitor)) {
						if(splits[0].equals("accept")) {
							this.friendsList.add(tmp);
							tmp.addFriend(this);
							if (this.callback != null) {
					            Registry registry = LocateRegistry.getRegistry(this.ip);
					            try {
					            	CallBack obj = (CallBack) registry.lookup(invitor);
					            	i = obj.notify("accept");
					            } catch(Exception e) {
					            	e.printStackTrace();
					            }
					            System.out.println(this.callback + " invoke call back object:accept");
							}
						} else {
							if (this.callback != null) {
								Registry registry = LocateRegistry.getRegistry(this.ip);
								try {
									CallBack obj = (CallBack) registry.lookup(invitor);
									i = obj.notify("decline");
								} catch(Exception e) {
									e.printStackTrace();
								}
								System.out.println(this. callback + "invoke call back object:decline");
							}
							tmp.addNotification(this, response);
							itr.remove();
						}
						if (i == -1)
							System.out.println("client callback object error");
						break;
					}
				}
				
				this.server.save();
				return 0;
			} else {
				return -1;
			}
		}
	}

	public boolean verifyPassword(String password) {
		if(this.password.equals(password))
			return true;
		else
			return false;
	}
	
	public boolean checkCity(String city) {
		if(this.city != null && this.city.equals(city))
			return true;
		else
			return false;
	}
	
	public boolean checkCollege(String college) {
		if(this.college != null && this.college.equals(college))
			return true;
		else
			return false;
	}
	
	public UserAccount(String username, String password, SocialNetworkServant s) {
		this.username = username;
		this.password = password;
		this.profession = "";
		this.city = "";
		this.company = "";
		this.college = "";
		this.graduatingYear = "";
		this.friendsList = new ArrayList<Account>();
		this.notifications = new ArrayList<String>();
		this.posts = new HashMap<Date, String>();
		this.pendingList = new ArrayList<Account>();
		this.server = s;
	}
	
	public String getID() {
		return this.username;
	}
	
	private boolean isFriend(String s) {
		synchronized(this.friendsList) {
			Iterator<Account> itr = friendsList.iterator();
			while(itr.hasNext()) {
				UserAccount tmp = (UserAccount)itr.next();
				if (tmp.getID().equals(s))
					return true;
			}
			return false;
		}
	}
	
	private String getProfile() {
		String s = "yes&" + this.username + "&";
		if(this.profession != null)
			s = s + this.profession + "&";
		else
			s = s + "&"; 
		
		if(this.city != null)
			s = s + this.city + "&";
		else 
			s = s + "&";
		
		if(this.company != null)
			s = s + this.company + "&";
		else
			s = s + "&";
		
		if(this.college != null)
			s = s + this.college + "&";
		else
			s = s + "&";
		
		if(this.graduatingYear != null && (!this.graduatingYear.equals("")))
			s = s + this.graduatingYear;
		else
			s = s + "-1";
		return s;
	}
	
	@Override
	public String viewProfile(String requester) throws RemoteException {
		if(this.username.equals(requester) || isFriend(requester)) {			
			return getProfile();
		}
		else {
			return "no";
		}
	}

	@Override
	public int updateProfile(String s) throws RemoteException {
		String[] splits = s.split("&");
		this.profession = splits[0];
		this.city = splits[1];
		this.company = splits[2];
		this.college = splits[3];
		if (splits[4].equals("-1"))
			this.graduatingYear = "";
		else
			this.graduatingYear = splits[4];
		
		this.server.save();
		return 0;
	}

	@Override
	public int postUpdates(String sender, String content) throws RemoteException {
		if(this.username.equals(sender) || isFriend(sender)) {
			Date d = new Date();
			String post = sender + "&" + this.username + "&" + content;
			synchronized(this.posts) {
				this.posts.put(d, post);
			}
			
			this.server.save();
			return 0;
		}
		else 
			return -1; // only the user and its friends are allowed to post 
	}

	public HashMap<Date, String> newsFeed() {
		synchronized(this.posts) {
			return posts;
		}
	}
	
	@Override
	public HashMap<Date, String> getUpdates() throws RemoteException {
		HashMap<Date, String> all = new HashMap<Date, String>();
		all.putAll(newsFeed());
		synchronized(this.friendsList) {
			Iterator<Account> itr = this.friendsList.iterator();
			while(itr.hasNext()) {
				UserAccount tmp = (UserAccount)itr.next();
				all.putAll(tmp.newsFeed());
			}
		}
		
		return all;
	}

	@Override
	public ArrayList<String> getFriendsList() throws RemoteException {
		ArrayList<String> list = new ArrayList<String>();
		synchronized(this.friendsList) {
			Iterator<Account> itr = this.friendsList.iterator();
			while(itr.hasNext()) {
				UserAccount u = (UserAccount)itr.next();
				String name = u.getID();
				list.add(name);
			}
			return list;
		}
	}

	@Override
	public String getUsername() throws RemoteException {
		return this.username;
	}

}
