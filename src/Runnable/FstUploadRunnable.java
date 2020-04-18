package Runnable;

import Cliente.Cliente;

import java.io.*;


public class FstUploadRunnable implements Runnable{

    private static final String path = System.getProperty("user.dir")+ File.separator+"src"+File.separator+"TXT"+File.separator+"fstuploads.txt";

    public void run() {
        String line;
        try {
            BufferedReader stdin = new BufferedReader(new FileReader(path));
            while ((line = stdin.readLine()) != null) {
                if (line.equals("quit")) System.exit(0);
                String l = Cliente.command(line);
                if (l != null)
                    Cliente.dealCommand(l);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
