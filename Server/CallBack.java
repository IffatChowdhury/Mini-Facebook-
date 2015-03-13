import java.util.*;
import java.rmi.*;

public interface CallBack extends Remote {
	//the message format should be "accept" or "decline" or "invite"
	//0 succeess, -1 otherwise
	public int notify(String notice) throws RemoteException; 
}
