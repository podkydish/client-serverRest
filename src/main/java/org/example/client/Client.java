package org.example.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.GroupTypeAdapter;
import ru.maipomogator.hibernate.entities.Group;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public static void main(String args[]) throws IOException {


        InetAddress address= InetAddress.getLocalHost();
        Socket socket=null;
        String line;
        BufferedReader clientInput=null;
        PrintWriter clientOutput=null;
        InputStream serverInput;
        ObjectInputStream objectInputStream = null;

        try {
            socket=new Socket(address, 4004); // You can use static final constant PORT_NUM
            clientInput= new BufferedReader(new InputStreamReader(System.in));



             serverInput = socket.getInputStream();
             objectInputStream = new ObjectInputStream(serverInput);

            clientOutput= new PrintWriter(socket.getOutputStream());
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        System.out.println("Client Address : "+address);
        System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

        String response;
        String json;
        Gson gson;
        try{
            line=clientInput.readLine();
            while(line.compareTo("QUIT")!=0){
                clientOutput.println(line);
                clientOutput.flush();
                assert objectInputStream != null;
               // response=objectInputStream.readObject().toString();
                //List<Group> groups = (List<Group>) objectInputStream.readObject();
                 json = (String) objectInputStream.readObject();
                gson = new GsonBuilder()
                        .registerTypeAdapter(Group.class, new GroupTypeAdapter())
                        .create();
                Group group = gson.fromJson(json, Group.class);
                System.out.println("Server Response : "+/*response*/ group.getName());
                line=clientInput.readLine();
            }






        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("Socket read Error");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally{

            //serverInput.close();
            clientOutput.close();
            clientInput.close();
            socket.close();
            System.out.println("Connection Closed");

        }

    }
}