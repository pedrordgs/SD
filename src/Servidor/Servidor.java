package Servidor;

import Lib.Sistema;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Lib.ThreadsNot;
import Runnable.ServidorRunnable;

public class Servidor{

    public static void main(String[] args) {
        try {
            Sistema sistema = new Sistema();
            ThreadsNot t = new ThreadsNot();
            ServerSocket ss = new ServerSocket(12345);
            while(true){
                Socket s = ss.accept();
                new Thread(new ServidorRunnable(s,sistema,t)).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}