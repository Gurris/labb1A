import java.net.InetAddress;

public class Client {
    private InetAddress addr;
    private int port;
    public Client(InetAddress addr, int port) {
        this.addr = addr;
        this.port = port;
    }
    public Client() {
        this.addr = null;
        this.port = 0;
    }
    public InetAddress getAddr() {
        return addr;
    }
    public void setAddr(InetAddress addr) {
        addr = addr;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }

}
