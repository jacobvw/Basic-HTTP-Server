import java.net.*;
import java.io.*;
import java.util.*;

public class HttpServer {
    
    public static void main(String[] args) {
        System.out.println("Web Server Starting");
        HttpServer webServer = new HttpServer();
        webServer.startServer();
    }
    
    public void startServer() {
        
        try {
            ServerSocket socket = new ServerSocket(8080);
            while(true) {
                Socket client = socket.accept();
                HttpServerSession thread = new HttpServerSession(client);
                thread.start();
            }
        } catch(Exception ex) { System.out.println(ex); }
    }
    
}
