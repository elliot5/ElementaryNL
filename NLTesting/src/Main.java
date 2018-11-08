import com.elementarynl.ElementaryClient;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Main {


    public static void main(String [] args) {

        ElementaryClient client2 = new ElementaryClient(new InetSocketAddress("localhost", 21926));
        client2.connect(new InetSocketAddress("localhost", 21925));

        ElementaryClient client = new ElementaryClient(new InetSocketAddress("localhost", 21925));
        client2.connect(new InetSocketAddress("localhost", 21926));

        String msg = "Hello?";
        client.send(msg.getBytes(),  client2.getAddress());
    }
}
