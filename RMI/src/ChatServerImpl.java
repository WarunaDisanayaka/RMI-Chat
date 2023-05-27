import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {

    private Map<String,ChatClient> clientMap=new HashMap<>();

    protected ChatServerImpl() throws RemoteException {
    }

    protected ChatServerImpl(int port) throws RemoteException {
        super(port);
    }

    protected ChatServerImpl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    @Override
    public String[] login(ChatClient client) throws RemoteException {
        String name=client.getName();
        if (clientMap.containsKey(name)){
            throw new RuntimeException("Name is already used");
        }
        String[] clientNames=list();
        clientMap.put(name,client);
        for (String clientName:clientNames){
            clientMap.get(clientName).joined(name);
        }
        return clientNames;
    }

    @Override
    public String[] list() throws RemoteException {
        return clientMap.keySet().toArray(new String[clientMap.size()]);
    }

    @Override
    public void sendMessage(String from, String to, String message) throws RemoteException {
                clientMap.get(to).showMessage(from,message);
    }

    @Override
    public void sendMessage(String from, String message) throws RemoteException {
            String[] clientNames=list();
            for (String clientName:clientNames){
                sendMessage(from,clientName,message);
            }
    }

    @Override
    public void logout(String name) throws RemoteException {
            clientMap.remove(name);
            String[] clientNames=list();
            for (String clientName:clientNames){
                clientMap.get(clientName).left(name);
            }
    }
}
