/*
********************************************************************************************************************************************

                                                      CS 5780 - Advanced Information Security

                                                                Project 2

                                                          Secure Election System

********************************************************************************************************************************************
*/

import java.io.*;
import java.net.*;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.lang.*;
import java.util.*;

//Server Class
class CLAServer extends Thread {

    static DatagramSocket ds;
    byte[] buffer = new byte[256];
    byte[] message;
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
   
    public void run() {

        int i = 0, aux = 0;
        String clientIP = "";

        try {

            ds = new DatagramSocket(CLA.portNumber);
            System.out.println("\n\nServer running.");
            MyRSA rs=new MyRSA();
            KeyPair keyPair = rs.buildKeyPair();
            PublicKey pubKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            String outFile = "CLA_Public";
            FileOutputStream out = new FileOutputStream(outFile + ".txt");
            out.write(pubKey.getEncoded());
            out.close();
            while(true) {
                packet = new DatagramPacket(buffer, buffer.length);
                ds.receive(packet);
                // Convert the contents to a string, and display them
             //   String msg = new String(buffer, 0, packet.getLength());

            //    System.out.println(buffer.length);
               // MyRSA rs=new MyRSA();
              //  System.out.println(buffer);
                byte[] by=rs.decrypt(privateKey, buffer);
               // System.out.println(new String(by));
                String msg=new String(by);
                String arr[] = msg.split(",");
                //System.out.println("arr:"+ arr);
                InetAddress address = InetAddress.getByName("localhost");

                if(arr[0].equals("Generate")) {

                    Random random = new Random();
                    long voterID = random.nextInt(900000) + 100000;
                    
                    
                    if(!CLA.voterName.contains(arr[2])) {

                        CLA.voterName.add(arr[2]);

                        if(CLA.voterIDNumber.contains(voterID)) {

                            voterID = random.nextInt(900000) + 100000;

                            CLA.voterIDNumber.add(voterID);

                        }
                        else {
                            CLA.voterIDNumber.add(voterID);
                        }
                        System.out.println("New Voter Registered!");

                        String msg1 = "Voter Name: " + arr[2] + "\nVoter ID: " + String.valueOf(voterID);

                        message = msg1.getBytes();

                        packet = new DatagramPacket(message, message.length, address, Integer.parseInt(arr[1]));

                        ds.send(packet);

                       String msg2 = "new," + arr[2] + "," + String.valueOf(voterID);

                        message = msg2.getBytes();

                        packet = new DatagramPacket(message, message.length, address, CLA.CTFportNumber);

                        ds.send(packet);

                    }
                    else {

                       String msg3 = "Voter already registered!";

                        message = msg3.getBytes();

                        packet = new DatagramPacket(message, message.length, address, Integer.parseInt(arr[1]));

                        ds.send(packet);

                    }

                    String msg4 = "Details," + arr[2] + "," + CLA.voterIDNumber.get(CLA.voterName.indexOf(arr[2]));

                    message = msg4.getBytes();

                    packet = new DatagramPacket(message, message.length, address, Integer.parseInt(arr[1]));

                    ds.send(packet);

                }
                else if(arr[0].equals("VotersInfo")) {

                    for (i = 0; i < CLA.voterIDNumber.size(); i++) {               
                        
                        System.out.println(CLA.voterIDNumber.get(i));         
                    
                    }

                    message = "VotersInfo".getBytes();

                    packet = new DatagramPacket(message, message.length, address, Integer.parseInt(arr[1]));

                    ds.send(packet);

                }

            }

        }
        catch (BindException be) {  } 
        catch(IOException ioe) {  } catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    //To stop the server
    public void stopServer() {

        try {

            ds.close();

            System.out.println("Server stopped.\n\n");

        }
        catch (Exception e) {

            System.out.println("Cannot close server socket.\n\n");

            System.exit(1);

        }

    }

}

public class CLA {

    public static ArrayList<String> voterName = new ArrayList<String>();
    public static ArrayList<Long> voterIDNumber = new ArrayList<Long>();
    public static CLAServer server1 = new CLAServer();
    public static int portNumber, CLAportNumber, CTFportNumber;

    public static void print(String text) {

        System.out.println(text);

    }

    public static void sleepFunction(int time) {

        try {

            Thread.sleep(time);

        }
        catch(Exception e) {

            print("Some error occured\n\nReference : " + e);

        }

    }

    public static void main(String[] args) throws UnknownHostException, IOException  {

        String userInput = "", fileName = "";
        int i = 0, aux = 0, port = 0, fieldsUpdated = 4;
        char temp;
        String array[] = new String[10], msg = "";
        Scanner sc = new Scanner(System.in);
        FileInputStream fin;
        FileOutputStream fout;
        byte[] message;

        System.out.println("\033[H\033[2J");

        File file = new File("serverDetails.txt");

        BufferedReader reader = new BufferedReader(new FileReader(file));

        CLAportNumber = Integer.parseInt(reader.readLine());
        CTFportNumber = Integer.parseInt(reader.readLine());

        reader.close();

        portNumber = CLAportNumber;

        print("Starting server on port " + portNumber + " . . . ");

        sleepFunction(3000);

        server1.start();

        sleepFunction(1000);
        
        while(true) {

            msg = sc.nextLine();

            if(msg.equals("exit")){

                server1.stopServer();

                sleepFunction(1000);

                System.exit(1);

            }

        }

    }

}
