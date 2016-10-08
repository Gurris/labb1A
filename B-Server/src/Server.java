import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server {

    public static final int PORT = 3790;

    private ServerSocket servSocket;
    private ArrayList<ClientEntity> clients;
    private PrintWriter sout;

    public Server(){
        servSocket = null;
        clients = new ArrayList<>();
        sout = null;
    }

    public void start(){
        try{
            servSocket = new ServerSocket(PORT);

            while(true){
                System.out.println("Server is listning...");
                Socket sock = servSocket.accept();
                System.out.println("Connection from " + sock.getInetAddress());
                //Create outStream
                sout = new PrintWriter(sock.getOutputStream(), true);
                ClientEntity ce = new ClientEntity(sock, sout);
                ce.writeTo("Welcome!");
                synchronized (clients){
                    clients.add(ce);
                }

                //create clientHandler thread
                ClientHandler cl = new ClientHandler(this, ce);
                cl.start();
            }

        } catch (Exception e){
            System.out.println("Could not create clientHandler: "+ e);
        } finally {
            try {
                if(servSocket != null) servSocket.close();
            }catch (Exception e){
                System.out.println("Could not close server socket");
            }
        }
    }

    public void broadcast(String message, ClientEntity CE){
        synchronized (clients) {
            try {
                for (ClientEntity client : clients) {
                    if (!client.equals(CE)) { // send to all clients except sender
                        client.writeTo(message);
                    }
                }
            } catch (Exception e) {
                System.out.println("Could not write to client");
            }
        }
    }

    public String listClientNick(){
        String userList = new String();
        synchronized (clients){
            try{
                for(ClientEntity client : clients){
                    System.out.println(client.getNickname());
                    userList += client.getNickname() + " ";
                }
                return userList;
            }catch (Exception e){
                System.out.println("Error listing client nicknames");
                return null;
            }
        }
    }

    public void removeClient(ClientEntity CE){
        synchronized (clients){
            try{
                System.out.println("Removing client: "+CE.getNickname());
                clients.remove(clients.indexOf(CE));
            }catch (Exception e){
                System.out.println("Could not remove client entity");
            }
        }
    }

    public void setNickname(ClientEntity CE, String nick){
        boolean okNick = true;
        synchronized (clients){
            try{
                for(ClientEntity client : clients){
                    if(client.getNickname().equalsIgnoreCase(nick)){
                        CE.writeTo("Server: Nickname already Taken");
                        okNick = false;
                    }
                }
                if(okNick){
                    CE.setNickname(nick);
                }
            }catch (Exception e){
            }
        }
    }

}