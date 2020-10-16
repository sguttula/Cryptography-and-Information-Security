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
class CTFServer extends Thread {

    static DatagramSocket ds;
    byte[] buffer = new byte[256];
    byte[] message;
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

    public void run() {

        int i = 0, aux = 0;
        String clientIP = "";

        try {

            ds = new DatagramSocket(CTF.portNumber);

            System.out.println("\n\nServer running.");
            MyRSA rs=new MyRSA();
            KeyPair keyPair = rs.buildKeyPair();
            PublicKey pubKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            String outFile = "CTF_Public";
            FileOutputStream out = new FileOutputStream(outFile + ".txt");
            out.write(pubKey.getEncoded());
            out.close();
            
            
            while(true) {
                packet = new DatagramPacket(buffer, buffer.length);
                ds.receive(packet);

                // Convert the contents to a string, and display them
                String msg = new String(buffer, 0, packet.getLength());
             //   System.out.println(new String(buffer));
               // byte[] by=rs.decrypt(privateKey, buffer);
                
                //System.out.println(new String(by));
                //String msg=new String(by);
                String arr[] = msg.split(",");
                
                //String arr[] = msg.split(",");

                InetAddress address = InetAddress.getByName("localhost");

                if(arr[0].equals("new")) {

                    CTF.voterName.add(arr[1]);
                    CTF.voterIDNumber.add(Long.parseLong(arr[2]));
                    CTF.votingStatus.add(false);

                    System.out.println("New Voter Registered!");

                }
                else if(arr[0].equals("VotersInfo")) {

                    message = "Voter Name\t\t\tVoting Status\n---------------------------------------------------".getBytes();

                    packet = new DatagramPacket(message, message.length, address, Integer.parseInt(arr[1]));
                    
                    
                   
                    
                    ds.send(packet);

                    for (i = 0; i < CTF.voterName.size(); i++) {  

                        String status;

                        if(CTF.votingStatus.get(i) == false)
                            status = "Pending";
                        else
                            status = "Voted";             
                        
                        msg = CTF.voterName.get(i) + "\t\t\t\t" + status;

                        message = msg.getBytes();

                        packet = new DatagramPacket(message, message.length, address, Integer.parseInt(arr[1]));

                        ds.send(packet);      
                    
                    }

                }
                else if(arr[0].equals("Vote")) {

                    long voterID = Long.parseLong(arr[2]);

                    if(CTF.votingStatus.get(CTF.voterIDNumber.indexOf(voterID)) == false) {

                        CTF.votingStatus.set(CTF.voterIDNumber.indexOf(voterID), true);

                        CTF.candidates[Integer.parseInt(arr[3]) - 1]++;

                        message = "Vote Recorded Successfully".getBytes();

                        packet = new DatagramPacket(message, message.length, address, Integer.parseInt(arr[1]));

                        ds.send(packet);

                    }
                    else {

                        message = "Cannot change your vote. Previous vote already registered.".getBytes();

                        packet = new DatagramPacket(message, message.length, address, Integer.parseInt(arr[1]));

                        ds.send(packet);

                    }

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

public class CTF {

    public static ArrayList<String> voterName = new ArrayList<String>();
    public static ArrayList<Long> voterIDNumber = new ArrayList<Long>();
    public static ArrayList<Boolean> votingStatus = new ArrayList<Boolean>();
    public static int[] candidates = {0, 0, 0, 0, 0};
    public static CTFServer server1 = new CTFServer();
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

    public static void main(String[] args) throws UnknownHostException, IOException {

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

        portNumber = CTFportNumber;

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
            else if(msg.equals("results")) {
            	
            	//FileOutputStream out = new FileOutputStream("voter_public" + ".txt",true);
                //out.write("");
                //out.close();
                PrintWriter writer = new PrintWriter("voter_public.txt");
                writer.print("");
                writer.close();

                System.out.println("\033[H\033[2J");

                System.out.println("Closing Voting . . . ");

                sleepFunction(1000);

                System.out.println("Counting Results . . . ");

                sleepFunction(1000);

                System.out.print("\n\n\nResults: ");

                for (i = 0; i < 5; i++) {

                    System.out.println("\nCandidate " + (i + 1) + " : " + candidates[i]);

                }

                System.out.print("\n\n");

                server1.stopServer();

                System.exit(1);

            }

        }

    }

}