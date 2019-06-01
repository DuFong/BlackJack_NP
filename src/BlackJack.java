import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BlackJack extends Remote{
	public void play() throws RemoteException;
}
