package personalForum;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;



class Board {
    Socket socket;
    DataOutputStream dOut;
    DataInputStream dIn;
    static List<Article> forum = new ArrayList<>();

    Board(Socket socket) {
        try {
            this.socket = socket;
            dOut = new DataOutputStream(socket.getOutputStream());
            dIn = new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void outIntro() {
        try {
            dOut.writeUTF("-=-=-=-=-=-=-=-=-=-=-=");
            dOut.writeUTF("-=-=-=-=-Forum-=-=-=-=");
            dOut.writeUTF("-=-=-=-=-=-=-=-=-=-=-=");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void outUi() {
        try {
            dOut.writeUTF("==================================================");
            dOut.writeUTF("No|    TITLE     |    AUTHOR    |DATE  ");
            dOut.writeUTF("-----------------=============---------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void outCommand() {
        try {
            dOut.writeUTF("1.Intro 2.Write 3.Read 4.Delete 0.End ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void outList() {
        int articleNum = 1;
        outUi();
        try {
            for (Article a : forum) {
                dOut.writeUTF(articleNum++ + "\t" + a.getPrint());
            }
            outCommand();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void write() {
        forum.add(new Article(socket));
        outCommand();
    }

    void read() {
        outList();
        try {
            dOut.writeUTF("CHOOSE THE ARTICLE. \n >> ");
            String inputNum = dIn.readUTF();
            int selector = Integer.parseInt(inputNum);
            outList();
            dOut.writeUTF(inputNum + "\t" + forum.get(--selector).getDetail());
            outCommand();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void delete() {
        outList();
        try {
            dOut.writeUTF("CHOOSE THE ARTICLE TO DELETE \n >>");
            String inputNum = dIn.readUTF();
            int intNum = Integer.parseInt(inputNum);
            forum.remove(intNum - 1);
            dOut.writeUTF("DELETE COMPLETE");
            outCommand();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DELETE ERROR");
        }
    }
    
    void FileDown() {
        BufferedOutputStream bs = null;
        try {
            bs = new BufferedOutputStream(new FileOutputStream("C:\\Users\\USER\\IdeaProjects\\sample.txt"));
            String str = forum.toString();
            bs.write(str.getBytes()); //Byte형으로만 넣을 수 있음

            dOut.writeUTF("DOWNLOAD COMPLETE");

        } catch (Exception e) {
            e.getStackTrace();
            // TODO: handle exception
        }
    }



}


class Article {
    DataOutputStream dOut;
    DataInputStream dIn;
    String artTitle, artContents, artAuthor, releaseDate;


    Article(Socket socket) {
        try {
            dOut = new DataOutputStream(socket.getOutputStream());
            dIn = new DataInputStream(socket.getInputStream());


            dOut.writeUTF("PUT IN TITLE ");
                //HAND_WRITE
            dOut.writeUTF("PUT IN CONTENTS ");
            dOut.writeUTF("@ is STOP");
            String stackLine = "";
            boolean notYet = true;
            while (notYet) {
                String lastLine = dIn.readUTF();
                if (lastLine.equals("@")) {
                    this.artContents = stackLine;
                    stackLine = "";
                    notYet = false;
                } else {
                    stackLine += (lastLine + "\n");
                }
                lastLine = null;
            }
            dOut.writeUTF("PUT IN AUTHOR ");
            this.artAuthor = dIn.readUTF();


            LocalDateTime now = LocalDateTime.now();
            releaseDate = now.format(DateTimeFormatter.ofPattern("[yy.MM.dd/HH:mm:ss]"));
            System.out.println(releaseDate +"new ARTICLE :: " + artTitle + "IS WRITTEN BY " + artAuthor );
            dOut.writeUTF("-=-=-=-=DONE-=-=-=-=-=");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    String getPrint() {
        return (artTitle + "\t" + artAuthor + "\t" + releaseDate);
    }

    String getDetail() {
        return ("TITLE : " + artTitle + "\n" +
                "AUTHOR :  " + artAuthor + "\n" +
                artContents + "\n" +
                "written at " + releaseDate);
    }



}

