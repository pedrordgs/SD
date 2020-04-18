package Runnable;

import Lib.Musica;
import Lib.Sistema;
import Lib.ThreadsNot;
import Lib.User;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServidorRunnable implements Runnable {
    private Socket socket;
    private Sistema sistema;
    private ThreadsNot tnot;
    private static final int MAXSIZE = 512000;
    private static final String filesPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"Servidor"+File.separator+"files";

    public ServidorRunnable(Socket socket, Sistema sistema,ThreadsNot tnot) {
        this.socket = socket;
        this.sistema = sistema;
        this.tnot=tnot;
    }

    public String autentica (String line){
        String [] args = line.split("\\?");
        String result;
        if (args.length<3) result="400 Bad Request";
        else{
            if (this.sistema.dadosCorretos(args[1],args[2]))
                result="Dados Corretos";
            else result = "Dados Incorretos";
        }
        return result;
    }

    public String regista (String line){
        String [] args = line.split("\\?");
        String result;
        if (args.length<3) result="400 Bad Request";
        else{
            User u = new User (args[1],args[2]);
            this.sistema.addUser(u);
            result = "Utilizador Registado";
        }
        return result;
    }

    public void uploadMusica (String nomeFich,int tam){
        try{
            OutputStream out = new FileOutputStream(filesPath+File.separator+nomeFich);
            int count;
            int tcount=0;
            byte[] bytes = new byte[MAXSIZE];
            InputStream in = this.socket.getInputStream();
            while(tcount<tam){
                this.sistema.dlock();
                count=in.read(bytes,0,MAXSIZE);
                tcount+=count;
                out.write(bytes,0,count);
                this.sistema.dunlock();
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String upload (String line){
        String result=null;
        try {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            String[] args = line.split("\\?");
            if (args.length < 5) result = "400 Bad Request";
            else {
                int tam = Integer.parseInt(args[1]);
                if (tam>0){
                    pw.println("OK");
                    pw.flush();
                }
                String titulo = args[2];
                String interprete = args[3];
                int ano = Integer.parseInt(args[4]);
                List<String> etiquetas = new ArrayList<>(Arrays.asList(args).subList(5, args.length));
                String nomeFich = titulo + " - " + interprete + ".mp3";
                uploadMusica(nomeFich, tam);
                this.sistema.lock();
                Musica m = new Musica(titulo, interprete, ano, etiquetas);
                if (this.sistema.existeMusica(m)) return "Musica já existe";
                int id = this.sistema.addMusica(m);
                this.sistema.unlock();
                result = "Musica adicionada ID: " + id;
                this.tnot.notificarTodos(titulo,interprete,id);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return result;
    }

    public String download(String line){
        String [] args = line.split("\\?");
        try{
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            Musica mu = this.sistema.getMusica(Integer.parseInt(args[1]));
            String musica = mu.getTitulo() + " - " + mu.getInterprete() + ".mp3";
            File m = new File (filesPath+File.separator+musica);
            if (!m.exists()) return "Something went wrong";
            int tam = (int) m.length();
            String info = tam+"?"+musica;
            pw.println(info);
            pw.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            if (!br.readLine().equals("OK")) return "Something went wrong";
            byte [] bytes  = new byte [MAXSIZE];
            InputStream input = new FileInputStream (m);
            OutputStream output = this.socket.getOutputStream();
            int count;
            int tcount=0;
            while (tcount<tam){
                this.sistema.dlock();
                count=input.read(bytes,0,MAXSIZE);
                tcount+=count;
                output.write(bytes,0,count);
                this.sistema.dunlock();
            }
            output.flush();
            input.close();
            this.sistema.download(Integer.parseInt(args[1]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Download feito com sucesso";
    }

    public String procura (String line){
        String [] args = line.split("\\?");
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            List<Musica> r = this.sistema.procura(args[1]);
            int tam = r.size();
            pw.println(tam);
            pw.flush();
            if (!br.readLine().equals("OK")) return "Something went wrong";
            for (Musica m:r){
                pw.println(m.toString());
                pw.flush();
                if (!br.readLine().equals("OK")) return "Something went wrong";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Procura terminada";
    }

    public String executeCommand (String line){
        String command = line.split("\\?")[0];
        String result="404 Command not found";
        System.out.println(command);
        switch (command){
            case "autentica":
                result=autentica(line);
                break;
            case "regista":
                result=regista(line);
                break;
            case "upload":
                result=upload(line);
                break;
            case "download":
                result=download(line);
                break;
            case "procura":
                result=procura(line);
                break;
            case "notificar":
                notificationThread();
                result=null;
                break;
        }
        return result;
    }



    public void notificar (String titulo, String autor, int id){
        try{
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            pw.println("Foi adicionada: " +titulo + " - " + autor + " -> ID: " + id);
            pw.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    /*
    public void notificar (){
        try{
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            pw.println("Foi adicionada uma música ");
            pw.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    */


    public void notificationThread () {
        this.tnot.lock();
        this.tnot.addThread(this);
        this.tnot.unlock();
        System.out.println("Sou notificação");
        while(true);
    }


    public void run (){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            String line;
            String result;
            line = br.readLine();
            if (line != null){
                result=executeCommand(line);
                if (result!=null){
                    pw.println(result);
                    pw.flush();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            this.tnot.lock();
            this.tnot.removeThread(this);
            this.tnot.unlock();
        }
        try {
            this.socket.shutdownOutput();
            this.socket.shutdownInput();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            this.tnot.lock();
            this.tnot.removeThread(this);
            this.tnot.unlock();
        }
    }
}