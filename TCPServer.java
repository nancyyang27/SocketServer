// import java.io.*;
// import java.net.*;
// import java.util.concurrent.*;

// public class TCPServer {
//     static ExecutorService exec = null;

//     public static void handleRequest(Socket socket) {
//         exec = Executors.newFixedThreadPool(5);

//         try {
//             System.out.print("INCOMING CLIENT: ");
//             InputStream in = socket.getInputStream();
//             OutputStream out = socket.getOutputStream();
//             int ch = 0;
//             while ((ch = in.read()) != '\n') {
//                 System.out.print((char)ch);
//                 out.write((char)ch);
//             }
//             System.out.println();
//             in.close();
//             out.close();
//         }
//         catch (Exception ex) {
//             ex.printStackTrace();
//         }
//     }
//     public static void main(String... args) throws Exception {
//         // String server = args[0];
//         // int port = Integer.parseInt(args[1]);

//         ServerSocket server = new ServerSocket(17);
//         Socket socket = null;
//         while ((socket = server.accept()) != null) {
//             System.out.println("Accepted client request");
//             final Socket threadSocket = socket;
//             //new Thread( () -> handleRequest(threadSocket) ).start();
//             exec.submit( () -> handleRequest(threadSocket));
//         }
//         server.close();
//     }    
// }

import java.io.*;
import java.net.*;
import java.util.*;

public class UDPServer {
    private static final int PORT = 17;

    public static void main(String[] args) {
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            System.out.println("UDP QOTD Server listening on port " + PORT);

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                System.out.println("New UDP client connected: " + clientAddress.getHostAddress());

                byte[] sendData = getRandomQuote().getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);

                System.out.println("UDP client disconnected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getRandomQuote() {
        List<String> quotes = Arrays.asList(
                "The only way to do great work is to love what you do. -Steve Jobs",
                "I have not failed. I've just found 10,000 ways that won't work. -Thomas Edison",
                "Believe you can and you're halfway there. -Theodore Roosevelt",
                "Happiness is not something ready made. It comes from your own actions. -Dalai Lama",
                "If you look at what you have in life, you'll always have more. -Oprah Winfrey"
        );

        Random random = new Random();
        return quotes.get(random.nextInt(quotes.size()));
    }
}

