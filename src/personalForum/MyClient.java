package personalForum;

import java.net.Socket;

public class MyClient {
    public static void main(String[] args) {
        try {
            boolean turnLive = true;
            Socket socket = new Socket("localhost",8282);

            System.out.println("SEVER IS CONNECTED.");

            Sender sender = new Sender(socket);
            Receiver receiver = new Receiver(socket);

            sender.start();
            receiver.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
