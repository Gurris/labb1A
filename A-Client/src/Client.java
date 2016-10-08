import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static final int SERVER_PORT = 10017;
    private static final int MAXBUF = 1024;
    private DatagramSocket sock;
    private InetAddress serverAddr;
    private String serverName;
    private DatagramPacket sendPack;
    private DatagramPacket receivedPack;
    private byte[] sendData;
    private byte[] receivedData;
    private boolean isRunning;

    public Client(String serverName) {
        this.serverName = serverName;
    }
    private boolean init() {
        System.out.println("Starting client...");
        sendData = new byte[MAXBUF];
        receivedData = new byte[MAXBUF];
        receivedPack = new DatagramPacket(receivedData, receivedData.length);
        sock = null;
        try {
            serverAddr = InetAddress.getByName(serverName);
            sock = new DatagramSocket();
            sendPack = new DatagramPacket(sendData, sendData.length, serverAddr, SERVER_PORT);
            sock.setSoTimeout(5000);
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (SocketException e) {
            System.out.println(e.getMessage());
            return false;
        }
        isRunning = true;
        return true;
    }

    public void start() {
        //*
        if(init()) {
            Scanner input = new Scanner(System.in);
            while (isRunning) {
                System.out.print(">");
                sendMessage(input.nextLine());
                System.out.println(readMessage());
            }
            close();
        }
        //*/
        /*
        if(init()) {
            Scanner input = new Scanner(System.in);
            System.out.println("spam");
            while (isRunning) {
                try{
                    sendMessage("HEJ");
                    Thread.sleep(100);
                }catch (Exception e){

                }

            }
            close();
        }
        */
    }
    private void sendMessage(String message){
            sendData = message.getBytes();
            sendPack.setData(sendData,0, sendData.length);
        try {
            sock.send(sendPack);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    private String readMessage() {
        try {
            sock.receive(receivedPack);
        } catch (SocketTimeoutException e) {
            isRunning = false;
            return "Server not responding.";
        } catch (IOException e) {
            return "Receiving error.";
        }
        return new String(receivedPack.getData(), receivedPack.getOffset(), receivedPack.getLength());
    }
    private void close(){
        if(sock != null) {
            sock.close();
        }
        System.out.println("client shutdown.");
        System.exit(-1);
    }
}
