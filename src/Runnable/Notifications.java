package Runnable;

import java.io.*;
import java.net.Socket;

public class Notifications implements Runnable {
    public void run() {
        try {
            Socket s = new Socket("localhost", 12345);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            pw.println("notificar");
            pw.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            while(true){
                System.out.println(br.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}