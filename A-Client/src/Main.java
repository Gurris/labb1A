import java.util.Scanner;

/**
 * Created by Marthin on 2016-09-25.
 */
public class Main {
    public static void main(String[] args) {
        System.out.print("IP address: ");
        Scanner s = new Scanner(System.in);
        Client client = new Client(s.nextLine());
        client.start();
    }
}
