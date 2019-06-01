import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

public class RMIClient {

	public static void main(String[] args) {
		if(args.length != 2)
		{
			System.out.println("Usage: Classname ServerName ServiceName");
			System.exit(1);
		}
		String mServer = args[0];
		String mServName = args[1];
		
		System.out.println("Remote Method Invocate to " + mServer + ", Service name : " + mServName);
		
		try{
			BlackJack bj = (BlackJack)Naming.lookup("rmi://"+mServer+"/"+mServName);
			bj.play();
		}
		catch(MalformedURLException mue)
		{
			System.out.println("MalformedURLException: " + mue);
		}
		catch(RemoteException re)
		{
			System.out.println("RemoteException: " + re);
		}
		catch(NotBoundException nbe)
		{
			System.out.println("NotBoundException: " + nbe);
		}
		catch(java.lang.ArithmeticException ae)
		{
			System.out.println("java.lang.ArithmeticException " + ae);
		}
	}

}
