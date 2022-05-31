package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientAdunare {

    public static void main(String[] args) throws UnknownHostException {
        int port = 8080;
        String host = "127.0.0.1";

        InetAddress address = InetAddress.getByName(host);
        try (DatagramSocket socket = new DatagramSocket()) {
            //System.out.println("Conectat la serverul de adunari");
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    String command = scanner.nextLine();
                    byte[] buffer = new byte[512];

                    buffer = command.getBytes();
                    DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, port);
                    socket.send(request);
                    byte[] buffer2 = new byte[512];
                    DatagramPacket response = new DatagramPacket(buffer2, buffer2.length);
                    socket.receive(response);

                    String decoded = new String(buffer2, 0, response.getLength());

                    if (decoded.equals("am trimis mai departe la serverul de scaderi")) {
                        System.out.println(decoded);
                        response = new DatagramPacket(buffer2, buffer2.length);
                        socket.receive(response);
                        decoded = new String(buffer2, 0, response.getLength());
                    }
                    if (decoded.equals("am trimis mai departe la server")) {
                        System.out.println(decoded);
                        response = new DatagramPacket(buffer2, buffer2.length);
                        socket.receive(response);
                        decoded = new String(buffer2, 0, response.getLength());
                    }



                    System.out.println(decoded);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.exit(0);
        }
    }

}