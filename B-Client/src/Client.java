import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client{
    private static final int SERVER_PORT = 3790;

    public void start(String address) {

        if(address.isEmpty()) {
            System.out.println("No address");
            System.exit(0);
        }

        Socket sock = null;
        PrintWriter out;
        Scanner s = new Scanner(System.in);
        String message;
        boolean alive = true;
        try {
            sock = new Socket(address, SERVER_PORT);
            Listener l = new Listener(sock);
            l.start();

            while(alive) {
                out = new PrintWriter(sock.getOutputStream(), true);
                message = s.nextLine();
                out.println(message);
                if(message.equalsIgnoreCase("/QUIT")) {
                    alive = false;
                }
            }

        } catch (IOException e) {
            System.out.println("something went wrong. Exception: " + e.getMessage());
        } finally {
            try {
                if(sock != null) sock.close();
            } catch (Exception e) {
                System.out.println("Could not close socket");
            }
        }
    }
}