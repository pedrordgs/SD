package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Menus {

    public static String mode (){
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("[1] Develop");
            System.out.println("[2] Testing");
            System.out.println("[3] User");
            String c = stdin.readLine();
            switch (c){
                case "1":
                    c="developer";
                    break;
                case "2":
                    c="test";
                    break;
                case "3":
                    c="user";
                    break;
                default:
                    mode();
            }
            return c;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String autentica(){
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        try {
            cls();
            System.out.println("Username:");
            String user=stdin.readLine();
            System.out.println("Password:");
            String pwd=stdin.readLine();
            return "autentica?"+user+"?"+pwd;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static String menuProcurar(){
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        try {
            cls();
            System.out.println("Etiqueta:");
            String etiqueta=stdin.readLine();
            return "procura?"+etiqueta;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static String menuDownload(){
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        try {
            cls();
            System.out.println("ID da musica:");
            String musica=stdin.readLine();
            return "download?"+musica;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static String menuUtilizador(){
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        try {
            cls();
            System.out.println("Username:");
            String user=stdin.readLine();
            System.out.println("Password:");
            String pwd=stdin.readLine();
            return "regista?"+user+"?"+pwd;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static String menuUpload(){
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        try {
            cls();
            System.out.println("Titulo:");
            String titulo=stdin.readLine();
            System.out.println("Interprete:");
            String interprete=stdin.readLine();
            System.out.println("Ano:");
            String ano=stdin.readLine();
            System.out.println("Etiquetas (separadas por ?)");
            String etiquetas=stdin.readLine();
            return "upload?"+titulo+"?"+interprete+"?"+ano+"?"+etiquetas;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static String menuInicial (){
        cls();
        System.out.println("=========> Musicas <========");
        System.out.println("[1] Criar utilizador");
        System.out.println("[2] Upload");
        System.out.println("[3] Procurar");
        System.out.println("[4] Download");
        System.out.println("\n[5]Sair");
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        try {
            return stdin.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static void cls (){
        System.out.print("\n\n");
        System.out.flush();
    }

    public static String iniciaMenus(){
        String i=null;
        while (i==null){
            i = menuInicial();
        }
        String c ="";
        switch (i){
            case "1":
                c=menuUtilizador();
                break;
            case "2":
                c=menuUpload();
                break;
            case "3":
                c=menuProcurar();
                break;
            case "4":
                c=menuDownload();
                break;
            case "5":
                c="quit";
                break;
        }
        return c;
    }
}