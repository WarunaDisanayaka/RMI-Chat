import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ChatFrame extends Frame {

    private ChatServer server;
    private ChatClient client;

    private String name;

    private TextArea chatArea = new TextArea(20, 70);

    private JList<String> clientList = new JList<>(new DefaultListModel<>());
    private TextArea entryArea = new TextArea(5, 70);

    private Button sendButton = new Button("Send");
    private Button logoutButton = new Button("Logout");

    public ChatFrame(ChatServer chatServer, String clientName) throws RemoteException {
        super("chat client-" + clientName);
        this.server = chatServer;
        this.name = clientName;

        List<String> clients = new ArrayList<>(clientList.getModel().getSize());
        for (int i = 0; i < clientList.getModel().getSize(); i++) {
            clients.add(clientList.getModel().getElementAt(i));
        }

       do {
           this.client = new ChatClientImpl(clientName, clients, chatArea);
           String[] clientNames = server.login(client);
           for (String clname : clientNames) {
               ((DefaultListModel<String>) clientList.getModel()).addElement(clname);
           }
       }while (false);

        this.setBounds(0, 0, 700, 500);
        this.setupComponents();
        this.setupEvents();
    }

    private void setupComponents() {
        this.setLayout(new BorderLayout());
        this.chatArea.setEditable(false);
        this.add(chatArea, BorderLayout.CENTER);
        this.add(clientList);

        JScrollPane clientScrollPane = new JScrollPane(clientList);
        clientScrollPane.setPreferredSize(new Dimension(200, getHeight()));
        this.add(clientScrollPane, BorderLayout.EAST);

        this.add(entryArea, BorderLayout.SOUTH);

        Panel p = new Panel();
        p.add(sendButton);
        p.add(logoutButton);
        this.add(p, BorderLayout.NORTH);
    }


    private void setupEvents() {
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                logout();
            }
        });

        this.logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        this.sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                List<String> clientNames = clientList.getSelectedValuesList();
                for (String clientName : clientNames) {
                    try {
                        server.sendMessage(name, clientName, entryArea.getText());
                        chatArea.append("Sent message to " + clientName + "\r\n");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        chatArea.append("Failed to send message to: " + clientName + "\r\n");
                    }
                }
                entryArea.setText("");
            }
        });
    }

    private void logout() {
        // Implement the logout functionality
    }

    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        String clientName = args[0];
        String host = args[1];
        String chatRoom = args[2];

        ChatServer chatServer = (ChatServer) Naming.lookup("rmi://" + host + "/" + chatRoom);

        ChatFrame frame = new ChatFrame(chatServer, clientName);
        frame.setVisible(true);
    }
}
