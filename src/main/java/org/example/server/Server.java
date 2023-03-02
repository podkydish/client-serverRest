package org.example.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.GroupTypeAdapter;
import ru.maipomogator.hibernate.entities.Group;
import ru.maipomogator.hibernate.factory.Factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
    public static void main(String args[]){


        Socket s;
        ServerSocket ss2=null;
        System.out.println("Server Listening......");
        try{
            ss2 = new ServerSocket(4004); // can also use static final PORT_NUM , when defined
        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("Server error");

        }

        while(true){
            try{
                s= ss2.accept();
                System.out.println("connection Established");
                ServerThread st=new ServerThread(s);
                st.start();

            }

            catch(Exception e){
                e.printStackTrace();
                System.out.println("Connection Error");

            }
        }

    }

}

class ServerThread extends Thread{

    String line=null;
    BufferedReader  is = null;
    OutputStream os=null;
    Socket s;
    ObjectOutputStream objectOutputStream;

    public ServerThread(Socket s){
        this.s=s;
    }

    public void run() {
        try{
            is= new BufferedReader(new InputStreamReader(s.getInputStream()));
            os= s.getOutputStream();
             objectOutputStream = new ObjectOutputStream(os);

        }catch(IOException e){
            System.out.println("IO error in server thread");
        }

        try {
            line=is.readLine();
            while(line.compareTo("QUIT")!=0){
                if(line.compareTo("groups") == 0) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Group.class, new GroupTypeAdapter())
                            .create();
                    List<Group> groupsForButtons;
                    groupsForButtons = Factory.getInstance().getGroupDao()
                            .getAllByCourseAndFaculty(3, 3);
                    Group group = groupsForButtons.get(0);
                    String jsonInString = gson.toJson(group);
                    //JSONObject mJSONObject = new JSONObject(jsonInString);
                    System.out.println(jsonInString);
                    objectOutputStream.writeObject(jsonInString);
                }
                line = is.readLine();
            }
                System.out.println("Response to Client  :  "+line);
                //  }
        } catch (IOException e) {

            line=this.getName(); //reused String line for getting thread name
            System.out.println("IO Error/ Client "+line+" terminated abruptly");
        }
        catch(NullPointerException e){
            line=this.getName(); //reused String line for getting thread name
            System.out.println("Client "+line+" Closed");
        }

        finally{
            try{
                System.out.println("Connection Closing..");
                if (is!=null){
                    is.close();
                    System.out.println(" Socket Input Stream Closed");
                }

                if(os!=null){
                    os.close();
                    System.out.println("Socket Out Closed");
                }
                if (s!=null){
                    s.close();
                    System.out.println("Socket Closed");
                }

            }
            catch(IOException ie){
                System.out.println("Socket Close Error");
            }
        }//end finally
    }
}