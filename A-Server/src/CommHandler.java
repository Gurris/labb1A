import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;


public class CommHandler {
    private DatagramSocket sock;
    private Client newClient;
    private Client currentClient;
    private Queue<Client> clientQueue;

    public CommHandler(int port) {
        try {
            this.sock = new DatagramSocket(port);
        } catch (SocketException e) {
            System.out.println(e.getMessage());
            closeSock();
        }
        currentClient=null;
        clientQueue = new LinkedList<>();
    }

    public void sendMessage(String message){
        byte[] data = message.getBytes();
        DatagramPacket sendPack = new DatagramPacket(data, data.length, currentClient.getAddr(), currentClient.getPort());
        try {
            sock.send(sendPack);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void sendMessage(String message, Client client){
        byte[] data = message.getBytes();
        DatagramPacket sendPack = new DatagramPacket(data, data.length, client.getAddr(), client.getPort());
        try {
            sock.send(sendPack);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public String readMessage() {
        byte[] receivedData = new byte[1024];
        DatagramPacket receivedPack = new DatagramPacket(receivedData, receivedData.length);
        int limit = 10;
        long start = System.currentTimeMillis();
        try{
            sock.setSoTimeout(1000);
        }catch (Exception e){
            System.out.println("Broken socket");
            return null;
        }
        while(true){
            long end = System.currentTimeMillis();
            if(((end-start)/1000) > limit)
                break;
            try{
                sock.receive(receivedPack);
                newClient = new Client(receivedPack.getAddress(), receivedPack.getPort());
                if (currentClient == null) {
                    currentClient = newClient;
                }
                if (authClient()) {
                    return new String(receivedPack.getData(), receivedPack.getOffset(), receivedPack.getLength());
                } else {
                    addClientToQueue(newClient);
                }
            }catch (Exception e){
            }

        }
        return "TIMEOUT";
    }

    private void addClientToQueue(Client client){
        boolean contains = false;
        if (clientQueue.size() < 1) {
            clientQueue.add(client);
            sendMessage("BUSY, ADDED TO THE QUEUE",client);
        } else {
            for (Client c : clientQueue) {
                if (c.getAddr().equals(client.getAddr()) && c.getPort() == client.getPort()) {
                    contains = true;
                    sendMessage("ALREADY IN THE QUEUE",client);
                }
            }
            if (!contains) {
                clientQueue.add(client);
                sendMessage("BUSY, ADDED TO THE QUEUE",client);
            }
        }
    }

    private boolean authClient() {
        if(currentClient.getPort() == newClient.getPort() && currentClient.getAddr().equals(newClient.getAddr())) {
            return true;
        }
        return false;
    }
    public void closeSock() {
        if(sock != null) {
            sock.close();
        }
    }
    public void removeCurrentClient(){
        if(currentClient != null){
            sendMessage("Disconnected from server", currentClient);
        }
        this.currentClient = null;
        if(clientQueue.size()>0){
            currentClient = clientQueue.poll();
            sendMessage("Your turn", currentClient);
        }
    }
}
