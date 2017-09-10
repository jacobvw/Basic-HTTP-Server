import java.net.*;
import java.io.*;
import java.time.*;

public class HttpServerSession extends Thread {
  private Socket client;
  private String[] httpRequestHeader;
  private BufferedReader reader;
  private BufferedOutputStream output;

  public HttpServerSession(Socket cli) {
    client = cli;
    httpRequestHeader = new String[2];
  }

  public void run() {
    try {
      System.out.println("Client Connected - " + client.getInetAddress());

      // setup reader and writer streams
      reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
      output = new BufferedOutputStream(client.getOutputStream());

      // read the first line of the request header
      httpRequestHeader[0] = reader.readLine();

      httpRequestHeader[1] = reader.readLine();
      // split it into the 3 parts
      String httpParts[] = httpRequestHeader[0].split(" ");

      if(httpParts.length == 3) {
        if(httpParts[0].compareTo("GET") == 0) {

          // if no file name has been given, set to index.html by default
          if(httpParts[1].substring(1).equals("")) httpParts[1] = "/index.html";

            File file = new File("htdocs/" + httpParts[1].substring(1));
            if(file.exists()) {
              // get localtime
              //LocalTime time = LocalTime.now();

              System.out.println(httpParts[1] + " 200 OK");
              println(output, "HTTP/1.1 200 OK");
              //println(output, "Date: " +
              println(output, "");

              // send the requested file to the client
              FileInputStream stream = new FileInputStream(file);
              byte[] fileBytes = new byte[(int)file.length()];
              stream.read(fileBytes);
              for(int i=0; i<fileBytes.length; i++) {
                output.write(fileBytes[i]);
                // uncomment the following line to simulate a slow connection
                //sleep(1);
              }

            } else {
              System.err.println(httpParts[1] + " 404 Not Found");
              println(output, "HTTP/1.1 404 Not Found");
              println(output, "");
            }

            // flush the output to the client
            output.flush();
          }
        } else {
          System.err.println("Http request invalid. Does not contain 3 pieces");
        }

      client.close();

    } catch(Exception ex) { System.err.println("Error - " + ex); }
  }

  private void println(BufferedOutputStream bos, String s) throws IOException {
    String news = s + "\r\n";
    byte[] array = news.getBytes();
    for(int i=0; i<array.length; i++) bos.write(array[i]);
  }

}
