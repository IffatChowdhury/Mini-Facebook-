import java.io.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;

public class PushUpdate implements CallBack
{
	public String statusMessage = null;
	public String usrname = null;
	
	public PushUpdate(String name)
	{
		usrname =  name;
	}
	
	public int notify(String notice)
	{
		statusMessage = notice;
		return 0;
	}
}
