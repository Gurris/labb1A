import java.io.*;
import java.net.Socket;

/**
 * Created by Gurris on 2016-09-13.
 */
public class ClientEntity {

    private String nickname = null;
    private Socket sock = null;
    private PrintWriter out = null;
    private BufferedReader in;
    private final Object readLock = new Object();
    private final Object writeLock = new Object();

    public ClientEntity(Socket sock, PrintWriter out){ // client need nickname, socket and read from and write to methods
        this.sock = sock;
        this.out = out;
        this.nickname = "Guest";
    }
    public void setNickname(String newNickname){
        nickname = newNickname;
    }

    public String getNickname(){
        return nickname;
    }

    public boolean writeTo(String message){
        synchronized (writeLock){
            try{
                System.out.println("sending mesage");
                out.println(message);
                return true;
            }catch(Exception e){
                System.out.println("Could not write message to client. Did client close connection?");
                return false;
            }
        }
    }

    public String readFrom(){
        synchronized (readLock){
            try{
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                return in.readLine();
            }catch(Exception e){
                System.out.println("Could not recive message from client. Did client close connection?");
                return null;
            }
        }
    }
    public void closeReadSocket(){
        try{
            if(out != null){
                out.close();
            }
        }catch (Exception e){
            System.out.println("could not close read socket");
        }
    }
    public void closeWriteSocket(){
        try{
            if(in != null){
                in.close();
            }
        }catch (Exception e){
            System.out.println("could not write socket");
        }
    }
    public void closeSocket(){
        try{
            if(sock != null){
                sock.close();
            }
        }catch (Exception e){
            System.out.println("could not close socket");
        }
    }
}
