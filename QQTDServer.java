import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class QQTDServer {
    private static final int PORT = 17;
    static ExecutorService exec = Executors.newFixedThreadPool(5);
    public static void main(String[] args) {
        try {
            // Create TCP and UDP sockets
            ServerSocket tcpSocket = new ServerSocket(PORT);
            DatagramSocket udpSocket = new DatagramSocket(PORT);

            // Submit tasks to executor
            exec.submit(() -> handleTCPRequests(tcpSocket));
            exec.submit(() -> handleUDPRequests(udpSocket));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleTCPRequests(ServerSocket tcpSocket) {
        System.out.println("TCP QOTD Server listening on port " + PORT);
        while (true) {
            try {
                Socket clientSocket = tcpSocket.accept();
                System.out.println("New TCP client connected: " + clientSocket.getInetAddress().getHostAddress());

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(getRandomQuote());

                clientSocket.close();
                System.out.println("TCP client disconnected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleUDPRequests(DatagramSocket udpSocket) {
        System.out.println("UDP QOTD Server listening on port " + PORT);
        while (true) {
            try {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                udpSocket.receive(receivePacket);

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                System.out.println("New UDP client connected: " + clientAddress.getHostAddress());

                byte[] sendData = getRandomQuote().getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                udpSocket.send(sendPacket);

                System.out.println("UDP client disconnected");
            }   catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static String getRandomQuote() {
        List<String> quotes = Arrays.asList(
                "The only way to do great work is to love what you do. -Steve Jobs",
                "There’s a shortage of perfect breasts in this world. It would be a pity to damage yours. -The Princess Bride",
                "Believe you can and you're halfway there. -Theodore Roosevelt",
                "The greatest glory in living lies not in never falling, but in rising every time we fall. -Nelson Mandela",
                "There is no confusion like the confusion of a simple mind… -The Great Gatsby (My favorite movie)"
        );

        Random random = new Random();
        return quotes.get(random.nextInt(quotes.size()));
    }
}