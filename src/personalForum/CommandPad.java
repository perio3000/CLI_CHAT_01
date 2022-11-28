package personalForum;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CommandPad extends Thread {
    protected boolean turnLive = true;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    Board board;

    public CommandPad(Socket socket) {
        try {
            this.socket = socket;
            board = new Board(socket);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //(dataInputStream != null)&&
    @Override
    public void run() {
        board.outIntro();
        board.outCommand();
        turnLive = true;
        while (turnLive) {
            String inputStr = "";
            try {
                inputStr = dataInputStream.readUTF();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                switch (inputStr) {
                    case "0":
                        turnLive = false;
                        dataOutputStream.writeUTF("SHUT DOWN");
                        break;
                    case "1":
                        board.outList();
                        break;
                    case "2":
                        board.write();
                        break;
                    case "3":
                        board.read();
                        break;
                    case "4":
                        board.delete();
                        break;
                    case "5":
                        board.FileDown();
                        board.outList();
                        break;
                    default:
                        dataOutputStream.writeUTF("WRONG COMMAND.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
