package Runnable;

import java.io.*;
import java.net.Socket;

public class Download implements Runnable {
    private String line;
    private static final int MAXSIZE=512000;
    private static final String defaultdowloadPath= System.getProperty("user.dir")+File.separator+"src"+File.separator+"Cliente"+File.separator+"download";

    public Download(String line) {
        this.line = line;
    }

    public void run() {
        try{
            Socket s = new Socket("localhost", 12345);
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            pw.println(this.line);
            pw.flush();
            String [] info = br.readLine().split("\\?");
            String nomeFich=info[1];
            int tam = Integer.parseInt(info[0]);
            if (tam>0){
                pw.println("OK");
                pw.flush();
            }
            String orginalName=defaultdowloadPath+File.separator+nomeFich.split("\\.")[0];
            String filename = orginalName+".mp3";
            int i=1;
            File f = new File(filename);
            while(f.exists()){
                filename=orginalName+"("+i+").mp3";
                f = new File(filename);
                i++;
            }
            OutputStream out = new FileOutputStream(filename);
            byte[] bytes = new byte[MAXSIZE];
            InputStream in = s.getInputStream();
            int count;
            int tcount=0;
            while(tcount<tam){
                count=in.read(bytes,0,MAXSIZE);
                tcount+=count;
                out.write(bytes,0,count);
            }
            out.flush();
            out.close();
            System.out.println(br.readLine() + ": " +filename);
            s.shutdownOutput();
            s.shutdownInput();
            s.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
