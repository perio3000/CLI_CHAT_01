package personalForum;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Sender extends Thread {
    Socket socket;
    DataOutputStream out;

    public Sender(Socket socket) {
        this.socket = socket;
        try {
            out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while(!socket.isClosed()) {
            try {
                out.writeUTF(scanner.nextLine());
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
}
