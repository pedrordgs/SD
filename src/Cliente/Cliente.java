package Cliente;


import java.io.*;
import java.net.Socket;

import Runnable.Download;
import Runnable.Upload;
import Runnable.FstUploadRunnable;
import Runnable.SndUploadRunnable;
import Runnable.Notifications;


public class Cliente {
    private static final String defaultUploadPath= System.getProperty("user.dir")+File.separator+"src"+File.separator+"Cliente"+File.separator+"upload";


    private static String uploadCommand(String line){
        String[] args = line.split("\\?");
        if (args.length < 4) return null;
        String titulo = args[1];
        String interprete = args[2];
        String nomeFich = titulo + " - " + interprete + ".mp3";
        File m = new File(defaultUploadPath + File.separator + nomeFich);
        if (!m.exists()) return "Musica nao existe";
        int tam = (int) m.length();
        StringBuilder result= new StringBuilder();
        result.append("upload?").append(tam);
        for(int i=1;i<args.length;i++){
            String s=args[i];
            result.append("?").append(s);
        }
        return result.toString();
    }


    private static void procura(Socket s, int qtd){
        try {
            int count;
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            if (qtd<0) return;
            pw.println("OK");
            pw.flush();
            for(count=0;count<qtd;count++){
                String musica = br.readLine();
                pw.println("OK");
                pw.flush();
                System.out.println(musica);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static String command (String line){
        String command = line.split("\\?")[0];
        String result=null;
        switch (command){
            case "autentica":
                result=line;
                break;
            case "regista":
                result=line;
                break;
            case "upload":
                result=uploadCommand(line);
                if (result!=null && !result.startsWith("upload")) {
                    System.out.println(result);
                    result = null;
                }
                break;
            case "download":
                result=line;
                break;
            case "procura":
                result=line;
                break;
        }
        return result;
    }

    private static void commandProcura(String line){
        try {
            int qtd;
            Socket s = new Socket("localhost", 12345);
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            pw.println(line);
            pw.flush();
            qtd = Integer.parseInt(br.readLine());
            procura(s, qtd);
            System.out.println(br.readLine());
            s.shutdownOutput();
            s.shutdownInput();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void commandUpload (String line){
        Thread t = new Thread(new Upload(line));
        t.start();
    }

    private static void commandDownload (String line){
        Thread t = new Thread(new Download(line));
        t.start();
    }

    private static String commandDefault (String line){
        String r=null;
        try {
            Socket s = new Socket("localhost", 12345);
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            pw.println(line);
            pw.flush();
            r = br.readLine();
            System.out.println(r);
            s.shutdownOutput();
            s.shutdownInput();
            s.close();
            return r;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }

    public static void dealCommand (String line){
        String command = line.split("\\?")[0];
        switch (command){
            case "procura":
                commandProcura(line);
                break;
            case "upload":
                commandUpload(line);
                break;
            case "download":
                commandDownload(line);
                break;
            default:
                commandDefault(line);
                break;
        }
    }

    public static void main(String[] args){
        Thread not = new Thread(new Notifications());
        not.start();
        String mode = Menus.mode();
        String line="quit";
        String l = null;
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            if (mode.equals("developer")){
                try {
                    line = stdin.readLine();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if (mode.equals("test")){
                testMain();
                break;
            }
            if (mode.equals("user")){
                line = Menus.iniciaMenus();
                if (!line.startsWith("regista") && !line.equals("quit")) {
                    String c = Menus.autentica();
                    c = commandDefault(c);
                    if (!c.equals("Dados Corretos")) line = null;
                }
            }
            if (line != null) {
                if (line.equals("quit")) break;
                    l = command(line);
                }
                if (l!=null)
                    dealCommand(l);
            }
        not.interrupt();
    }


    public static void testMain(){
        Thread a = new Thread(new FstUploadRunnable());
        Thread b = new Thread(new SndUploadRunnable());

        a.start();
        b.start();

        try {
            a.join();
            b.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}