import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

// java UDPClient host port
public class QQTDClient {
    public static void main(String[] args) throws IOException {
        String SERVER_ADDRESS = "localhost";
        int TCP_PORT = 17;
        int UDP_PORT = 17;
        // Create TCP socket and connect to server
        Socket tcpSocket = new Socket(SERVER_ADDRESS, TCP_PORT);
        BufferedReader tcpIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
        PrintWriter tcpOut = new PrintWriter(tcpSocket.getOutputStream(), true);

        // Create UDP socket
        DatagramSocket udpSocket = new DatagramSocket();

        // Send TCP request
        tcpOut.println("Hello from TCP client!");

        // Send UDP request
        byte[] buffer = "Hello from UDP client!".getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(SERVER_ADDRESS), UDP_PORT);
        udpSocket.send(packet);

        // Receive TCP response
        String tcpResponse = tcpIn.readLine();
        System.out.println("TCP response: " + tcpResponse);

        // Receive UDP response
        byte[] responseBuffer = new byte[1024];
        packet = new DatagramPacket(responseBuffer, responseBuffer.length);
        udpSocket.receive(packet);
        String udpResponse = new String(packet.getData(), 0, packet.getLength());
        System.out.println("UDP response: " + udpResponse);

        // Close sockets
        tcpSocket.close();
        udpSocket.close();
    }
}