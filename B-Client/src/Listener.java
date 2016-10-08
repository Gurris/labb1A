import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Listener extends Thread {

    private Socket sock = null;
    private BufferedReader sin = null;
    private boolean alive = true;

    public Listener(Socket sock) {
        this.sock = sock;
    }

    public void run() {
        try{
            while(alive) {
                sin = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                String message = sin.readLine();
                if(message == null){
                    alive = false;
                }
                System.out.println(message);
            }
        } catch (IOException e) {
            System.out.println("Could not read server message. " + e.getMessage());
        } finally {
            try {
                if(sock != null) sock.close();
            } catch (IOException e){
                System.out.println("Could not close socket." + e.getMessage());
            }
            System.exit(0);
        }
    }
}