package com.company;
import javax.print.attribute.standard.RequestingUserName;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Main {

    public static void acceptAndServe(Socket s){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String sName = "NULL";
                try{
                    BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    PrintStream ps = new PrintStream(s.getOutputStream());
                    ps.println("What is your nickname?");
                    String sText = br.readLine();
                    sName = sText;
                    ps.println("Hello " + sName + "\nWelcome to the club buddy");
                    while((sText = br.readLine())!=null){
                        for(Socket other : clientSet){
                            if(other!=s){
                                PrintStream psOther = new PrintStream(other.getOutputStream());
                                psOther.println(sName + " - "+sText);
                            }
                        }
                    }
                    InputStream is = s.getInputStream();
                    OutputStream os = s.getOutputStream();
                    int c;
                    while((c = is.read())!=-1){
                        os.write(c);
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                finally {
                    try {
                        s.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Disconnected from "+s.getInetAddress());
                clientSet.remove(s);
            }
        });
        t.start();
    }

    static HashSet<Socket> clientSet= new HashSet<>();

    public static void main(String[] args) throws IOException, NumberFormatException {
        ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]));
        while(true){
            Socket s = ss.accept();
            clientSet.add(s);
            System.out.println("Connected with "+ s.getInetAddress());
            acceptAndServe(s);
        }

    }
}
