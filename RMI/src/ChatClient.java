import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClient extends Remote {

    String getName()throws RemoteException;
    void joined(String name)throws RemoteException;

    void left(String name)throws RemoteException;

    void showMessage(String from,String message)throws RemoteException;


}
