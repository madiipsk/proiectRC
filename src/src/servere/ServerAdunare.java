package servere;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerAdunare implements AutoCloseable {

    private DatagramSocket socket;

    public ServerAdunare(int port) throws IOException {
        socket = new DatagramSocket(port);
        while (socket != null && !socket.isClosed()) {
            byte[] buffer = new byte[512];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            socket.receive(request);
            InetAddress clientAddress = request.getAddress();
            int clientPort = request.getPort();

            String decoded = new String(buffer, 0, request.getLength());

            System.out.println(decoded);
            String[] components = decoded.split(" ");
            if (components.length == 3 && components[0].equals("aduna")) {
                buffer = Integer.toString(Integer.parseInt(components[1]) + Integer.parseInt(components[2])).getBytes();
                DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                socket.send(response);

            } else if (components.length == 1 && components[0].equals("ports")) {
                buffer = ("Portul 8080 - ocupat\n" +
                        "Portul 8081 - ocupat\n" +
                        "Portul 8082 - liber\n" +
                        "Portul 8083 - liber").getBytes();
                DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                socket.send(response);

            } else if (components.length == 2 && components[0].equals("connect")) {
                if (components[1].equals("8080")){
                buffer = ("Conectat la portul 8080").getBytes();
                System.out.println("Client conectat la 8080");
                DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                socket.send(response);}else{
                    buffer = ("Nu se poate realiza conexiunea").getBytes();
                    DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                    socket.send(response);
                }

            }else if (components.length == 1 && components[0].equals("list")) {
                buffer = ("Port 8080\nPort 8081\n" +
                        "Port 8082\nPort 8083").getBytes();
                DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                socket.send(response);

            } else if (components.length == 1 && components[0].equals("dateC")) {
                buffer = (Integer.toString(clientPort) + clientAddress).getBytes();
                DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                socket.send(response);
                System.out.println("client");
                System.out.println(clientPort);

            } else if (components.length == 3 && components[0].equals("scade")) {
                //trimite la serverul de scaderi
                request.setAddress(InetAddress.getByName("127.0.0.1"));
                request.setPort(8081);
                socket.send(request);
                System.out.println("am trimis la serverul de scaderi");
                //trimite mesaj intermediar clientului
                buffer = "am trimis mai departe la serverul de scaderi".getBytes();
                DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                socket.send(response);

                //primit mesajul de la serverul de scaderi
                socket.receive(request);
                System.out.println("am primit de la serverul de scaderi");
                request.setPort(clientPort);
                request.setAddress(clientAddress);
                socket.send(request);
                System.out.println("am trimis mai departe la client");

            } else if (components.length == 1 && components[0].equals("dateS")) {
                System.out.println("am trimis la server");
                buffer = (Integer.toString(8080) + clientAddress).getBytes();
                DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                socket.send(response);
                System.out.println(8080);

            }
            else if (components.length!=0){
                System.out.println("comanda invalida");
                buffer = "comanda invalida".getBytes();
                DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
                socket.send(response);

            }

        }
    }

    @Override
    public void close() throws Exception {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

}