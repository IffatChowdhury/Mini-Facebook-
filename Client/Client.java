import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.io.*;
import java.text.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

public class Client
{
	static SocialNetworkService stub = null;static Registry registry;
	public static String UserName = null;public static Account stubA = null;
	public static String notice = null;public static PushUpdate push = null;
	public static ArrayList<String> friendsName = new ArrayList<String>(); 
	public static ArrayList<String> friendsName1 = new ArrayList<String>();
	
	public static void graphics_setting(GridBagConstraints gcc, int pgridx, int pgridy, double pweighty, double pweightx, int pipady)
	{
		gcc.gridx = pgridx;gcc.gridy = pgridy;gcc.weighty = pweighty;gcc.weightx = pweightx;
		gcc.anchor = GridBagConstraints.CENTER;gcc.fill = GridBagConstraints.HORIZONTAL;
		gcc.insets = new Insets(5,5,5,5);gcc.ipady = pipady;
	}
	
	public static void NextPage(String notice)
	{
		JPanel container = new JPanel();
		int count = 0, i = 0, j = 1;
		JButton [] b = new JButton [15];  
		try
		{
			stubA = (Account) registry.lookup(UserName);
			friendsName = stubA.getFriendsList();
			count = friendsName.size();
		}
		catch(Exception e){System.err.println("Get List of Friends Exception: " + e.toString());}
		JLabel latestUpdate = null;
		if(notice != null)
		{
			if(notice.equals("accept"))
				latestUpdate = new JLabel("You Have Accept Notification");
			else if(notice.equals("decline"))
				latestUpdate = new JLabel("You Have Decline Notification");
			else if(notice.equals("invite"))
				latestUpdate = new JLabel("You Have Invite Notification");
		}
		else 
			latestUpdate = new JLabel("You Have No New Notification");
		latestUpdate.setForeground(Color.BLUE);
		JLabel listfrndlbl = new JLabel("List of Friends");listfrndlbl.setForeground(Color.BLUE);
		JButton searchfriends = new JButton("Search for Friends");JButton notifications = new JButton("Notifications");JButton viewprofile = new JButton("View Profile");
		JButton postupdates = new JButton("Post");JButton getupdates = new JButton("Get Updates");
		final JFrame frame = new JFrame(UserName + "'s Home-List Of Friends");
		
		GridBagLayout gridbagLayoutSettings = new GridBagLayout();container.setLayout(gridbagLayoutSettings);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);frame.setSize(800, 600);frame.setLocationRelativeTo(null);
		GridBagConstraints gc = new GridBagConstraints();
		graphics_setting(gc, 0, 0, 1.0, 1.0, 0);container.add(listfrndlbl, gc);
		JButton p, v; JLabel namelbl = null;
		
		for(i = 0,j = 1; i < count; i++,j++)
		{
			try
			{
				namelbl = new JLabel(friendsName.get(i));
			}
			catch(Exception e){System.err.println("Get User Name Exception: " + e.toString());}
			final int ii = i;
			graphics_setting(gc, 0, j, 1.0, 1.0, 0);container.add(namelbl, gc);
			graphics_setting(gc, 1, j, 1.0, 1.0, 0);p = new JButton("Post"); container.add(p, gc);
			
			p.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae2)			//post
				{
					frame.setVisible(false); 
					frame.dispose();
					postupdates_action(ii);
				}
													});
			graphics_setting(gc, 2, j, 1.0, 1.0, 0);
			v = new JButton("View"); container.add(v, gc);
			
			v.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae2)			//view profile
				{
					frame.setVisible(false); 
					frame.dispose();
					viewprofile_action(ii);
				}
													});
		}
		graphics_setting(gc, 0, j, 1.0, 1.0, 0);gc.gridwidth = 2;container.add(latestUpdate, gc);
		graphics_setting(gc, 0, j+1, 1.0, 1.0, 0);container.add(searchfriends, gc);
		graphics_setting(gc, 2, j+1, 1.0, 1.0, 0);gc.gridwidth = 1;container.add(notifications, gc);
		graphics_setting(gc, 0, j+2, 1.0, 1.0, 0);gc.gridwidth = 1;container.add(viewprofile, gc);
		graphics_setting(gc, 1, j+2, 1.0, 1.0, 0);gc.gridwidth = 1;container.add(postupdates, gc);
		graphics_setting(gc, 2, j+2, 1.0, 1.0, 0);gc.gridwidth = 1;container.add(getupdates, gc);
		frame.add(container);frame.setVisible(true);
		
		searchfriends.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)					//search for friends
			{
				frame.setVisible(false); 
				frame.dispose();
				searchfriends_action();
			}
												});
		notifications.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)					//notifications
			{
				frame.setVisible(false); 
				frame.dispose();
				notifications_action();
			}
												});
		viewprofile.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)					//view profile
			{
				frame.setVisible(false); 
				frame.dispose();
				viewprofile_action(-1);
			}
												});
		postupdates.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)					//post updates
			{
				frame.setVisible(false); 
				frame.dispose();
				postupdates_action(-1);
			}
												});
		getupdates.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)					//get updates
			{
				frame.setVisible(false); 
				frame.dispose();
				getupdates_action();
			}
												});
	}
	
	public static void searchfriends_action()							//search friends
	{
		JPanel container = new JPanel();final JFrame frame = new JFrame(UserName + "'s Home-Search Friends");
		JLabel namelbl = new JLabel("Search By Name");final JTextField nametxt = new JTextField(15);
		JLabel citylbl = new JLabel("Search by City");final JTextField citytxt = new JTextField(15);
		JLabel collegelbl = new JLabel("Search by College");final JTextField collegetxt = new JTextField(15);
		JButton exit = new JButton("Exit");JButton Ok = new JButton("Ok");
		
		GridBagLayout gridbagLayoutSettings = new GridBagLayout();container.setLayout(gridbagLayoutSettings);
		frame.setSize(500, 300);frame.setLocationRelativeTo(null);
		GridBagConstraints gc = new GridBagConstraints();
		graphics_setting(gc, 0, 0, 1.0, 1.0, 0);container.add(namelbl, gc);
		graphics_setting(gc, 1, 0, 1.0, 1.0, 0);container.add(nametxt, gc);
		graphics_setting(gc, 0, 1, 1.0, 1.0, 0);container.add(citylbl, gc);
		graphics_setting(gc, 1, 1, 1.0, 1.0, 0);container.add(citytxt, gc);
		graphics_setting(gc, 0, 2, 1.0, 1.0, 0);container.add(collegelbl, gc);
		graphics_setting(gc, 1, 2, 1.0, 1.0, 0);container.add(collegetxt, gc);
		graphics_setting(gc, 0, 3, 1.0, 1.0, 0);gc.gridwidth = 1;container.add(exit, gc);
		graphics_setting(gc, 1, 3, 1.0, 1.0, 0);container.add(Ok, gc);
		frame.add(container);frame.setVisible(true);
		
		exit.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae)
				{
					frame.setVisible(false);
					notice = push.statusMessage;
					NextPage(notice);
				}
												   });
													 
		Ok.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae)
		{
			JLabel namelbl1 = null;
			int count = 0;
			String message = nametxt.getText() + "&" + citytxt.getText() + "&" + collegetxt.getText();
			nametxt.setText("");citytxt.setText("");collegetxt.setText("");
			try
			{
				friendsName1 = stub.searchForFriends(message);
				count = friendsName1.size();
			}
			catch(NullPointerException e){System.out.println("");}
			catch(Exception e){System.err.println("Search Friends List Exception: " + e.toString());}
			JPanel container1 = new JPanel();
			int j = 0;int i = 0;
			final JFrame frame1 = new JFrame(UserName + "'s Home-Search Friends");JButton Ok1 = new JButton("Ok");
			JButton in; String frndname = null;String friendName = null;
			
			GridBagLayout gridbagLayoutSettings1 = new GridBagLayout();container1.setLayout(gridbagLayoutSettings1);
			frame1.setSize(400, 300);frame1.setLocationRelativeTo(null);
			GridBagConstraints gc1 = new GridBagConstraints();
			
			if(count == 0)
			{
				namelbl1 = new JLabel("You have no such Friends");
				graphics_setting(gc1, 0, 0, 1.0, 1.0, 0);container1.add(namelbl1, gc1);
				j = 1;
			}
			else
			{
				for(i = 0, j = 0; i < count; i++, j++)
				{
					namelbl1 = new JLabel(friendsName1.get(i));
					friendName = friendsName1.get(i);
					final String FName = friendName;
				
					graphics_setting(gc1, 0, j, 1.0, 1.0, 0);container1.add(namelbl1, gc1);
					graphics_setting(gc1, 1, j, 1.0, 1.0, 0);in = new JButton("Invite");container1.add(in, gc1);
				
					in.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent ae)
						{
							int r = 0;
							try
							{
								r = stub.inviteFriend(UserName,FName);
								frame1.setVisible(false);
								notice = push.statusMessage;
								NextPage(notice);
							}
							catch(Exception e){System.err.println("Invite Exception: " + e.toString());}
						}
															});
				}
			}
			graphics_setting(gc1, 1, j, 1.0, 1.0, 0);container1.add(Ok1, gc1);
			frame1.add(container1);frame1.setVisible(true);
			
			Ok1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae)
				{
					frame1.setVisible(false);
				}
												     });
		}
												});
	}
	
	public static void notifications_action()									//notifications
	{
		ArrayList<String> allNotifications = new ArrayList<String>(); 
		JPanel container = new JPanel();
		int count = 0, i = 0, j = 1;
		try
		{
			stubA = (Account) registry.lookup(UserName);
			allNotifications = stubA.getNotifications();
			count = allNotifications.size();
		}
		catch(NullPointerException e){System.out.println("");}
		catch(Exception e){System.err.println("Get Notifications Exception: " + e.toString());}
		String [] parsedNotifications = new String[2];
		final JFrame frame = new JFrame(UserName + "'s Home-Notifications");
		JLabel notilbl = new JLabel("Notifications");notilbl.setForeground(Color.BLUE);JButton Ok = new JButton("Ok");
		
		GridBagLayout gridbagLayoutSettings = new GridBagLayout();container.setLayout(gridbagLayoutSettings);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);frame.setSize(500, 300);frame.setLocationRelativeTo(null);
		GridBagConstraints gc = new GridBagConstraints();graphics_setting(gc, 0, 0, 1.0, 1.0, 0);container.add(notilbl, gc);
		JButton ac = new JButton("Accept");JButton dec = new JButton("Decline"); 
		
		for(i = 0,j = 1; i < count; i++,j++)
		{
			parsedNotifications = allNotifications.get(i).split("&");
			JLabel namelbl;
			final String parsedNoti1index = parsedNotifications[1];
			
			if(parsedNotifications[0].equals("invite"))
			{
				namelbl = new JLabel(parsedNotifications[1] + " invites you");
				graphics_setting(gc, 0, j, 1.0, 1.0, 0);container.add(namelbl, gc);
				graphics_setting(gc, 1, j, 1.0, 1.0, 0);container.add(ac, gc);
				graphics_setting(gc, 2, j, 1.0, 1.0, 0);container.add(dec, gc);
			}
			
			else if(parsedNotifications[0].equals("accept"))
			{
				namelbl = new JLabel(parsedNotifications[1] + " accepts your request");
				graphics_setting(gc, 0, j, 1.0, 1.0, 0);container.add(namelbl, gc);
			}
			
			else if(parsedNotifications[0].equals("decline"))
			{
				namelbl = new JLabel(parsedNotifications[1] + " declines your request");
				graphics_setting(gc, 0, j, 1.0, 1.0, 0);container.add(namelbl, gc);
			}
			
			ac.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae)
				{
					try
					{
						stubA = (Account) registry.lookup(UserName);
						//System.out.println(UserName + "  accepted");
						stubA.reply("accept&" + UserName, parsedNoti1index);
						frame.setVisible(false);
						notice = push.statusMessage;
						NextPage(notice);
					}
					catch(Exception e){System.err.println("Accept Reply Exception: " + e.toString());}
				}
													});
													
			dec.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae)
				{
					try
					{
						stubA = (Account) registry.lookup(UserName);
						//System.out.println(UserName + "  declined");
						stubA.reply("decline&" + UserName, parsedNoti1index);
						frame.setVisible(false);
						notice = push.statusMessage;
						NextPage(notice);
					}
					catch(Exception e){System.err.println("Decline Reply Exception: " + e.toString());}
				}
													 });
		}
		graphics_setting(gc, 2, j, 1.0, 1.0, 0);container.add(Ok, gc);
		frame.add(container);frame.setVisible(true);
		
		Ok.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae)
		{
			frame.setVisible(false);
			notice = push.statusMessage;
			NextPage(notice);
		}
												});
	}
	
	public static void viewprofile_action(int index)							//view profile
	{
		String profileMessage = null;
		String [] parsedProfile = new String[7];
		int flag = 0;
		for(int i = 0; i < 7; i++)
			parsedProfile[i] = null;
		try
		{
			if(index == -1)
			{
				stubA = (Account) registry.lookup(UserName);
				profileMessage = stubA.viewProfile(UserName);
			}
			else
			{
				stubA = (Account) registry.lookup(friendsName.get(index));
				profileMessage = stubA.viewProfile(UserName);
			}
		}
		catch(Exception e){System.err.println("Get Profile Exception: " + e.toString());}
		if(profileMessage == "no") flag = 1;
		else if(profileMessage != "") parsedProfile = profileMessage.split("&");
		
		JPanel container4 = new JPanel();GridBagLayout gridbagLayoutSettings4 = new GridBagLayout();container4.setLayout(gridbagLayoutSettings4);
		JLabel usrnamelbl = new JLabel("User Name",JLabel.LEFT);final JTextField usrnametxt = new JTextField(15);
		usrnametxt.setText(parsedProfile[1]);usrnametxt.setEditable(false);
		JLabel professionlbl = new JLabel("Profession",JLabel.LEFT);final JTextField professiontxt = new JTextField(15);
		professiontxt.setText(parsedProfile[2]);professiontxt.setEditable(false);
		JLabel citylbl = new JLabel("Living City",JLabel.LEFT);final JTextField citytxt = new JTextField(15);
		citytxt.setText(parsedProfile[3]);citytxt.setEditable(false);
		JLabel companylbl = new JLabel("Company",JLabel.LEFT);final JTextField companytxt = new JTextField(15);
		companytxt.setText(parsedProfile[4]);companytxt.setEditable(false);
		JLabel collegelbl = new JLabel("College Name",JLabel.LEFT);final JTextField collegetxt = new JTextField(15);
		collegetxt.setText(parsedProfile[5]);collegetxt.setEditable(false);
		JLabel graduatelbl = new JLabel("Graduate Year",JLabel.LEFT);final JTextField graduatetxt = new JTextField(15);
		if(parsedProfile[6].equals("-1")) graduatetxt.setText("");
		else graduatetxt.setText(parsedProfile[6]);
		graduatetxt.setEditable(false);
		
		JButton Edit = new JButton("Edit");JButton Ok = new JButton("Ok");
		final JFrame frame4 = new JFrame(UserName + "'s Home-View Profile");
		frame4.setSize(400, 250);frame4.setLocationRelativeTo(null);GridBagConstraints gc4 = new GridBagConstraints();
		graphics_setting(gc4, 0, 0, 1.0, 1.0, 0);container4.add(usrnamelbl, gc4);
		graphics_setting(gc4, 1, 0, 0.0, 0.0, 0);container4.add(usrnametxt, gc4);
		graphics_setting(gc4, 0, 1, 1.0, 1.0, 0);container4.add(professionlbl, gc4);
		graphics_setting(gc4, 1, 1, 0.0, 0.0, 0);container4.add(professiontxt, gc4);
		graphics_setting(gc4, 0, 2, 1.0, 1.0, 0);container4.add(citylbl, gc4);
		graphics_setting(gc4, 1, 2, 0.0, 0.0, 0);container4.add(citytxt, gc4);
		graphics_setting(gc4, 0, 3, 1.0, 1.0, 0);container4.add(collegelbl, gc4);
		graphics_setting(gc4, 1, 3, 0.0, 0.0, 0);container4.add(collegetxt, gc4);
		graphics_setting(gc4, 0, 4, 1.0, 1.0, 0);container4.add(companylbl, gc4);
		graphics_setting(gc4, 1, 4, 0.0, 0.0, 0);container4.add(companytxt, gc4);
		graphics_setting(gc4, 0, 5, 1.0, 1.0, 0);container4.add(graduatelbl, gc4);
		graphics_setting(gc4, 1, 5, 0.0, 0.0, 0);container4.add(graduatetxt, gc4);
		graphics_setting(gc4, 0, 6, 1.0, 1.0, 0);
		if(index == -1)
			container4.add(Edit, gc4);
		graphics_setting(gc4, 1, 6, 1.0, 1.0, 0);container4.add(Ok, gc4);
		frame4.add(container4);
		if(flag == 0)
			frame4.setVisible(true);
		else 
			JOptionPane.showMessageDialog(null,"You are not Authorized to See","Error",JOptionPane.ERROR_MESSAGE);
		final int ii = index;flag = 0;
		
		Ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				String message = null;
				if(ii == -1)
				{
					message = professiontxt.getText() + "&" + citytxt.getText() + "&" + companytxt.getText() + "&" + collegetxt.getText() + "&";
					if(graduatetxt.getText().equals(null) || graduatetxt.getText().equals(""))
						message = message + "-1";
					else message = message + graduatetxt.getText();
					int rr = 0;
					try
					{
						stubA = (Account) registry.lookup(UserName);
						rr = stubA.updateProfile(message);
						if(rr == -1)
							JOptionPane.showMessageDialog(null,"Update Failed","Error",JOptionPane.ERROR_MESSAGE);
					}
					catch(Exception e){System.err.println("Update Profile Exception: " + e.toString());}
				}
				frame4.setVisible(false);
				notice = push.statusMessage;
				NextPage(notice);
			}
												});
												
		Edit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				if(ii == -1)
				{
					citytxt.setEditable(true);collegetxt.setEditable(true);
					professiontxt.setEditable(true);graduatetxt.setEditable(true);companytxt.setEditable(true);
				}
			}
													});
	}
	
	public static void postupdates_action(int index)							//post Updates
	{
		JPanel container5 = new JPanel();
		GridBagLayout gridbagLayoutSettings5 = new GridBagLayout();container5.setLayout(gridbagLayoutSettings5);
		JLabel updatelbl = new JLabel("Enter Your Post :", JLabel.LEFT);
		final JTextField updatetxt = new JTextField(15);
		JButton Reset = new JButton("Reset");JButton Ok = new JButton("Ok");
		final JFrame frame5 = new JFrame(UserName + "'s Home-Post Updates");
		
		frame5.setSize(400, 200);frame5.setLocationRelativeTo(null);
		GridBagConstraints gc5 = new GridBagConstraints();
		graphics_setting(gc5, 0, 0, 1.0, 1.0, 0);container5.add(updatelbl, gc5);
		graphics_setting(gc5, 1, 0, 0.0, 0.0, 0);container5.add(updatetxt, gc5);
		graphics_setting(gc5, 0, 1, 1.0, 1.0, 0);gc5.gridwidth = 1;container5.add(Reset, gc5);
		graphics_setting(gc5, 1, 1, 1.0, 1.0, 0);container5.add(Ok, gc5);
		frame5.add(container5);frame5.setVisible(true);
		final int ii = index;
		
		Ok.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae)
		{
			String message = updatetxt.getText();
			updatetxt.setText("");
			try
			{
				if (ii == -1)
				{
					if( message.length() != 0 )
					{
						stubA = (Account) registry.lookup(UserName);
						stubA.postUpdates(UserName,message);
					}
				}
				else
				{
					if( message.length() != 0 )
					{
						stubA = (Account) registry.lookup(friendsName.get(ii));
						stubA.postUpdates(UserName, message);
					}
				}
				frame5.setVisible(false);
				notice = push.statusMessage;
				NextPage(notice);
			}
			catch(Exception e){System.err.println("Post Updates Exception: " + e.toString());}
		}
												});
												
		Reset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				updatetxt.setText("");
			}
													});
	}
	
	public static void getupdates_action()										//get updates
	{
		int count = 0;
		Map<Date,String> sortedMap = null;
		String[] parsedUpdates = new String[3]; 
		for(int i = 0; i < 3; i++)
			parsedUpdates[i] = null;
		Iterator itr;Date[] keys = new Date[count];String[] values = new String[count];
		try
		{
			stubA = (Account) registry.lookup(UserName);
			HashMap<Date, String> allUpdates = stubA.getUpdates();
			sortedMap = new TreeMap<Date, String>(allUpdates);
			count = sortedMap.size();keys = new Date[count];values = new String[count];
			int index = 0;
			for (Map.Entry<Date, String> mapEntry : sortedMap.entrySet()) 
			{
				keys[index] = mapEntry.getKey();
				values[index] = mapEntry.getValue();
				index++;
			}
		}
		catch(NullPointerException e){System.out.println("");}
		catch(Exception e){System.err.println("Get Updates Exception: " + e.toString());}
		JPanel container = new JPanel();
		int i = 0, j = 1;
		final JFrame frame = new JFrame(UserName + "'s Home-Get Updates");
		JLabel updatelbl = new JLabel("Friends Updates");updatelbl.setForeground(Color.BLUE);JButton Ok = new JButton("Ok");
		
		GridBagLayout gridbagLayoutSettings = new GridBagLayout();container.setLayout(gridbagLayoutSettings);
		frame.setSize(800, 600);frame.setLocationRelativeTo(null);
		GridBagConstraints gc = new GridBagConstraints();
		graphics_setting(gc, 0, 0, 1.0, 1.0, 0);container.add(updatelbl, gc);
		
		j = 1;
		for(int k = count-1; k > -1; k--)
		{
			parsedUpdates = values[k].split("&");
			graphics_setting(gc, 0, j, 1.0, 1.0, 0);
			SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");String d = sdf.format(keys[k]);
			container.add(new JLabel(d), gc);
			if(parsedUpdates[0].equals(parsedUpdates[1]))
			{
				graphics_setting(gc, 1, j, 1.0, 1.0, 0);container.add(new JLabel(parsedUpdates[0]), gc);
			}
			else
			{
				graphics_setting(gc, 1, j, 1.0, 1.0, 0);container.add(new JLabel(parsedUpdates[0] + " wrote on " + parsedUpdates[1] + "'s wall"), gc);
			}
			graphics_setting(gc, 0, j+1, 1.0, 1.0, 0);
			JLabel status = new JLabel(parsedUpdates[2]);
			status.setForeground(Color.BLUE);
			container.add(status, gc);
			j = j + 2;
		}
		graphics_setting(gc, 3, j, 1.0, 1.0, 0);gc.gridwidth = 1;container.add(Ok, gc);
		frame.add(container);frame.setVisible(true);
		
		Ok.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae)
		{
			frame.setVisible(false);
			notice = push.statusMessage;
			NextPage(notice);
		}
												});
	}
	
	public static void LoginForm(String ip)
	{
		JPanel container = new JPanel();
		final String ipAddress = ip;
		GridBagLayout gridbagLayoutSettings = new GridBagLayout();container.setLayout(gridbagLayoutSettings);
		JLabel usrnamelbl = new JLabel("Username : ", JLabel.LEFT);final JTextField usrnametxt = new JTextField(15);
		JLabel passlbl = new JLabel("Password : "   , JLabel.LEFT);final JTextField passtxt = new JPasswordField(15);
		JButton SUBMIT = new JButton("Submit");JButton NewAccount = new JButton("Sign Up");
		try
		{
			final JFrame frame = new JFrame("My Facebook-Login");
			
			frame.setSize(400, 200);frame.setLocationRelativeTo(null);
			GridBagConstraints gc = new GridBagConstraints();
			graphics_setting(gc, 0, 0, 1.0, 1.0, 0);container.add(usrnamelbl, gc);
			graphics_setting(gc, 1, 0, 0.0, 0.0, 0);container.add(usrnametxt, gc);
			graphics_setting(gc, 0, 1, 1.0, 1.0, 0);container.add(passlbl, gc);
			graphics_setting(gc, 1, 1, 0.0, 0.0, 0);container.add(passtxt, gc);
			graphics_setting(gc, 0, 2, 1.0, 1.0, 0);gc.gridwidth = 1;container.add(NewAccount, gc);
			graphics_setting(gc, 1, 2, 1.0, 1.0, 0);container.add(SUBMIT, gc);
			frame.add(container);frame.setVisible(true);
			
			SUBMIT.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae)
				{
					String n = usrnametxt.getText();String p = passtxt.getText();
					usrnametxt.setText("");passtxt.setText("");
					int loginResponse = 0;
					try
					{
						loginResponse = stub.loginAccount(n,p);
					}
					catch (Exception e){System.err.println("Login Account Exception: " + e.toString());}
					if(loginResponse == 0)
					{
						try
						{
							stubA = (Account) registry.lookup(n);
							UserName = n;
							push = new PushUpdate(UserName);
						}
						catch(Exception e){System.err.println("Get Account Exception: " + e.toString());}
						try
						{
							String name = UserName;
							//System.out.println("ip add" + ipAddress);
							CallBack stubC = (CallBack)UnicastRemoteObject.exportObject(push, 0);
							Registry registryC = LocateRegistry.getRegistry();
							registryC.rebind(name, stubC);
							stubA.register(name, ipAddress);
							frame.setVisible(false);
							notice = push.statusMessage;
							NextPage(notice);
						}
						catch(Exception e){System.err.println("Login Callback Exception: "); e.printStackTrace();}
					}
					else
					{
						JOptionPane.showMessageDialog(null,"Incorrect login or password","Error",JOptionPane.ERROR_MESSAGE);
					}
				}
														});
														
			NewAccount.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae)
				{
					JPanel container1 = new JPanel();
					GridBagLayout gridbagLayoutSettings1 = new GridBagLayout();container1.setLayout(gridbagLayoutSettings1);
					JLabel usrnamelbl1 = new JLabel("Enter Username : ", JLabel.LEFT);final JTextField usrnametxt1 = new JTextField(15);
					JLabel passlbl1 = new JLabel("Enter Password : "   , JLabel.LEFT);final JTextField passtxt1 = new JPasswordField(15);
					JLabel passlbl2 = new JLabel("Password Again: "   , JLabel.LEFT);final JTextField passtxt2 = new JPasswordField(15);
					JButton Reset = new JButton("Reset");JButton Create = new JButton("Create");
					final JFrame frame1 = new JFrame("My Facebook-Login");
					
					frame1.setSize(400, 200);frame1.setLocationRelativeTo(null);
					GridBagConstraints gc1 = new GridBagConstraints();
					graphics_setting(gc1, 0, 0, 1.0, 1.0, 0);container1.add(usrnamelbl1, gc1);
					graphics_setting(gc1, 1, 0, 0.0, 0.0, 0);container1.add(usrnametxt1, gc1);
					graphics_setting(gc1, 0, 1, 1.0, 1.0, 0);container1.add(passlbl1, gc1);
					graphics_setting(gc1, 1, 1, 0.0, 0.0, 0);container1.add(passtxt1, gc1);
					graphics_setting(gc1, 0, 2, 1.0, 1.0, 0);container1.add(passlbl2, gc1);
					graphics_setting(gc1, 1, 2, 0.0, 0.0, 0);container1.add(passtxt2, gc1);
					graphics_setting(gc1, 0, 3, 1.0, 1.0, 0);container1.add(Reset, gc1);
					graphics_setting(gc1, 1, 3, 1.0, 1.0, 0);container1.add(Create, gc1);
					frame1.add(container1);frame1.setVisible(true);
					
					Create.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent ae)
						{
							String n1 = usrnametxt1.getText();String p1 = passtxt1.getText();String pp1 = passtxt2.getText();
							usrnametxt1.setText("");passtxt1.setText("");passtxt2.setText("");
							int response1 = 0;
							try
							{
								response1 = stub.createAccount(n1, p1, pp1);
							}
							catch (Exception e){System.err.println("Create Account Exception: " + e.toString());}
							if(response1 == 0)
							{
								try
								{
									stubA = (Account) registry.lookup(n1);
									UserName = n1;
								}
								catch (Exception e){System.out.println("Registry Look Up Exception: " + e.toString());}
								try
								{
									push = new PushUpdate(UserName);
									String name = UserName;
									CallBack stubC = (CallBack)UnicastRemoteObject.exportObject(push, 0);
									Registry registryC = LocateRegistry.getRegistry();
									registryC.rebind(name, stubC);
									System.out.println("user name" + name);
									stubA.register(name, ipAddress);
								}
								catch (Exception e){System.out.println("Callback Register Exception: " + e.toString());}
								frame1.setVisible(false);
								notice = push.statusMessage;
								NextPage(notice);
							}
							else if(response1 == -1)
								JOptionPane.showMessageDialog(null,"Account has been Taken","Error",JOptionPane.ERROR_MESSAGE);
							else if(response1 == -2)
								JOptionPane.showMessageDialog(null,"Password Mismatch","Error",JOptionPane.ERROR_MESSAGE);
						}
																});
																
					Reset.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent ae)
						{
							usrnametxt1.setText("");passtxt1.setText("");passtxt2.setText("");
						}
																});
				}
															});
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
	public static void main(String[] args)
	{
		String host = (args.length < 1) ? null : args[0];
		try
		{
			registry = LocateRegistry.getRegistry(host);
			stub = (SocialNetworkService) registry.lookup("socialnetworkservant");
			LoginForm(host);
		}
		catch (Exception e)
		{
			System.err.println("Client Got Exception: " + e.toString());
            e.printStackTrace();
		}
    }
}
	
	
