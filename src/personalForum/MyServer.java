package personalForum;

import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        boolean turnLive = true;
        try{
            serverSocket = new ServerSocket(8282);
            System.out.println("Server On.");
            while (true) {
                socket = serverSocket.accept();
                CommandPad commandPad = new CommandPad(socket);
                commandPad.start();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
