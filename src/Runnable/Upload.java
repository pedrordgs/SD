package Runnable;

import java.io.*;
import java.net.Socket;

public class Upload implements Runnable{
    private String line;
    private static final int MAXSIZE=512000;
    private static final String defaultUploadPath= System.getProperty("user.dir")+ File.separator+"src"+File.separator+"Cliente"+File.separator+"upload";

    public Upload(String line) {
        this.line = line;
    }

    public void run() {
        try {
            Socket s = new Socket("localhost", 12345);
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            pw.println(line);
            pw.flush();
            if (!br.readLine().equals("OK")) return;
            String [] args = line.split("\\?");
            if (args.length<4) return;
            String titulo=args[2];
            String interprete=args[3];
            String nomeFich = titulo + " - " + interprete + ".mp3";
            File m = new File (defaultUploadPath+File.separator+nomeFich);
            int tam = (int) m.length();
            byte[] bytes = new byte[MAXSIZE];
            InputStream input = new FileInputStream(m);
            OutputStream output = s.getOutputStream();
            int count;
            int tcount=0;
            while(tcount<tam){
                count=input.read(bytes,0,MAXSIZE);
                tcount+=count;
                output.write(bytes,0,count);
            }
            output.flush();
            input.close();
            System.out.println(br.readLine() + " -> "+nomeFich);
            s.shutdownOutput();
            s.shutdownInput();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
