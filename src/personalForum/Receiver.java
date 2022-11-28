package personalForum;

import java.io.DataInputStream;
import java.net.Socket;

public class Receiver extends Thread {
    Socket socket;
    DataInputStream in;


    public Receiver(Socket socket) {
        this.socket = socket;
        try {
            in = new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void run() {

        while (!socket.isClosed()) {
            try {
                String inputStr = in.readUTF();
                if (inputStr.equals("SHUT DOWN")){
                    socket.close();
                } else {
                    System.out.println(inputStr);
                }

            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        System.out.println("Receiver Closed");
    }
}
