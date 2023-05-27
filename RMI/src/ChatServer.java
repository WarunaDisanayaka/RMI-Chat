
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface ChatServer extends Remote{

        String[] login(ChatClient client)throws RemoteException;
        String[]  list()throws RemoteException;
        void sendMessage(String from,String to,String message)throws RemoteException;
        void sendMessage(String from,String message) throws RemoteException;

        void logout(String name)throws RemoteException;



}
