package com.example.owner.ipv6multicasttest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MainActivity extends Activity {

    private static final int PORT = 9876;
    private static final String MCAST_ADDR = "FF7E:230::1234";

    private static InetAddress GROUP;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            GROUP = InetAddress.getByName(MCAST_ADDR);
            Thread server = server();
            server.start();
            Thread.sleep(3000);
            Thread client = client();
            client.start();
            client.join();
        } catch (Exception e) {

        }
    }
    private static Thread client() {
        return new Thread(new Runnable() {
            public void run() {
                MulticastSocket multicastSocket = null;
                try {
                    multicastSocket = new MulticastSocket(PORT);
                    multicastSocket.joinGroup(GROUP);
                    while (true) {
                        try {
                            byte[] receiveData = new byte[256];
                            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                            multicastSocket.receive(receivePacket);
                            Log.d("ipv6multicasttest", "Client received from : " + receivePacket.getAddress() + ", " + new String(receivePacket.getData()));

                        } catch (Exception e) {
                         }
                    }
                } catch (Exception e) {
                    } finally {
                    multicastSocket.close();
                }
            }
        });
    }

    private static Thread server() {
        return new Thread(new Runnable() {
            public void run() {
                DatagramSocket serverSocket = null;
                try {
                    serverSocket = new DatagramSocket();
                    try {
                        while (true) {
                            byte[] sendData = new byte[256];
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, GROUP, PORT);
                            serverSocket.send(sendPacket);
                            }
                    } catch (Exception e) {
                       }
                } catch (Exception e) {
                      }
            }
        });
    }

}