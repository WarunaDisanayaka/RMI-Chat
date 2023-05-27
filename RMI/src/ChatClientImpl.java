import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {

    private String name;
    private List clientList;

    private TextArea chatArea;

    protected ChatClientImpl(String n,List l,TextArea ta) throws RemoteException {
            this.name=n;
            this.clientList=l;
            this.chatArea=ta;
    }

    protected ChatClientImpl(int port) throws RemoteException {
        super(port);
    }

    protected ChatClientImpl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    @Override
    public String getName() throws RemoteException {
        return this.name;
    }

    @Override
    public void joined(String name) throws RemoteException {
            this.clientList.add(name);
    }

    @Override
    public void left(String name) throws RemoteException {
            this.clientList.remove(name);
    }

    @Override
    public void showMessage(String from, String message)  {
            this.chatArea.append("Message from "+from+":"+message+"\r\n");
    }
}
