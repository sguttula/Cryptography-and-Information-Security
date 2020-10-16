/*
********************************************************************************************************************************************

                                                      CS 5780 - Advanced Information Security

                                                                Project 2

                                                          Secure Election System

********************************************************************************************************************************************
*/

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.lang.*;
import java.util.*;

import javax.crypto.Cipher;

//Server Class
class voterServer extends Thread {

    static DatagramSocket ds;
    byte[] buffer = new byte[2048];
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

    public void run() {

        int i = 0, aux = 0;
        String clientIP = "";

        try {

            ds = new DatagramSocket(Voter.portNumber);

            System.out.println("\n\nServer running.");

            while(true) {

                ds.receive(packet);

                // Convert the contents to a string, and display them
                String msg = new String(buffer, 0, packet.getLength());

                String[] arr = msg.split(",");

                if(arr[0].equals("Details")) {

                    Voter.name = arr[1];
                    Voter.voterID = Long.parseLong(arr[2]);

                }
                else
                    System.out.println(msg);

                // Reset the length of the packet before reusing it.
                packet.setLength(buffer.length);

            }

        }
        catch (BindException be) {}
        catch (IOException ioe) {}

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

public class Voter {

    public static String name;
    public static int portNumber, CLAportNumber, CTFportNumber;
    public static long voterID;
    public static voterServer server1 = new voterServer();

    public static void clearScreen() {

        System.out.println("\033[H\033[2J");

    }

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

    public static void main(String[] args) throws Exception {

        String userInput;
        Scanner sc = new Scanner(System.in);
        byte[] message;
        InetAddress address = InetAddress.getByName("localhost");
        int aux;

        clearScreen();

        System.out.print("Enter port number to start the server: ");
        portNumber = sc.nextInt();

        print("Starting server on port " + portNumber + " . . . ");

        File file = new File("serverDetails.txt");

        BufferedReader reader = new BufferedReader(new FileReader(file));

        CLAportNumber = Integer.parseInt(reader.readLine());
        CTFportNumber = Integer.parseInt(reader.readLine());

        reader.close();

        server1.start();

        sleepFunction(2000);

        sc.nextLine();
        
        MyRSA rs=new MyRSA();
        KeyPair keyPair = rs.buildKeyPair();
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        String outFile = "voter_public";
        FileOutputStream out = new FileOutputStream(outFile + ".txt",true);
        out.write(pubKey.getEncoded());
        out.close();
        
        
        
        Path path = Paths.get("CLA_Public.txt");
        byte[] bytes = Files.readAllBytes(path);

        /* Generate public key. */
        X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey public_CLA = kf.generatePublic(ks);
        
        Path path1 = Paths.get("CTF_Public.txt");
        byte[] bytes1 = Files.readAllBytes(path1);

        /* Generate public key. */
        //X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
        //KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey public_CTF = kf.generatePublic(ks);
        
        
        System.out.print("\n\nEnter your name: ");
        name = sc.nextLine();
        
       // byte[] nam=sc.nextLine().getBytes();
        userInput = "Generate," + portNumber + ",";// + rs.encrypt(pub, name);
        
        message = userInput.getBytes();
     //   System.out.println("msg len: "+message.length);
       
        String encString="Generate," + portNumber + ","+name;
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, public_CLA);
        byte [] enc= cipher.doFinal(encString.getBytes("UTF-8"));
   //     System.out.println(enc);
       // byte[] result = concat(message, enc);
        
        DatagramPacket packet = new DatagramPacket(enc, enc.length, address, CLAportNumber);
        server1.ds.send(packet);
        while(true) {

            sleepFunction(1000);

            System.out.print("\n\nEnter option number:\n1.View Voter ID\n2.Vote\n3.View Voters Info\n4.Exit\n>>> ");

            userInput = "";
            userInput = sc.nextLine();

            if(userInput.equals("4")){

                server1.stopServer();

                sleepFunction(1000);

                System.exit(1);

            }
            else if(userInput.equals("1")) {

                userInput = "View," + portNumber;
               // message = userInput.getBytes();
                cipher.init(Cipher.ENCRYPT_MODE, public_CLA);
                byte [] view= cipher.doFinal(userInput.getBytes("UTF-8"));
                
                packet = new DatagramPacket(view, view.length, address, CLAportNumber);

                server1.ds.send(packet);

                System.out.println("Voter Name: " + name + "\nVoter ID: " + voterID);

            }
            else if(userInput.equals("2")) {

                int k = 0;

                do {

                    if(k != 0)
                        System.out.println("\nInvalid Voter Number. Please enter a Candidate number between 1 and 5.\n");

                    System.out.print("\nEnter Candidate Number:\n1.Democratic Party\n2.Republican Party\n>>> ");

                    aux = Integer.parseInt(sc.nextLine());
                    k++;

                } while(aux <= 0 && aux >= 6);

                userInput = "Vote," + portNumber + "," + voterID + "," + aux;
                message = userInput.getBytes();

                packet = new DatagramPacket(message, message.length, address, CTFportNumber);

                server1.ds.send(packet);

            }
            else if(userInput.equals("3")) {

                userInput = "VotersInfo," + portNumber;
                message = userInput.getBytes();
            //    cipher.init(Cipher.ENCRYPT_MODE, public_CTF);
                // byte [] voterInfo= cipher.doFinal(userInput.getBytes("UTF-8"));
                // byte [] voterInfo= "chandu".getBytes("UTF-8");
              //  System.out.println(new String(voterInfo));
                packet = new DatagramPacket(message, message.length, address, CTFportNumber);

                
                server1.ds.send(packet);

            }
            else {

                System.out.println("\n\nYou have entered an incorrect option.\nPlease try again with a valid option number.");

            }

        }

    }
    public static byte[] concat(byte[]...arrays)
    {
        // Determine the length of the result array
        int totalLength = 0;
        for (int i = 0; i < arrays.length; i++)
        {
            totalLength += arrays[i].length;
        }

        // create the result array
        byte[] result = new byte[totalLength];

        // copy the source arrays into the result array
        int currentIndex = 0;
        for (int i = 0; i < arrays.length; i++)
        {
            System.arraycopy(arrays[i], 0, result, currentIndex, arrays[i].length);
            currentIndex += arrays[i].length;
        }

        return result;
    }

}