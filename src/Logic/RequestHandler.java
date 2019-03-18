package Logic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

public class RequestHandler implements Runnable {

    /**
     * Socket connected to client passed by Proxy server
     */
    Socket clientSocket;

    /**
     * Read data client sends to proxy
     */
    BufferedReader proxyToClientBr;

    /**
     * Send data from proxy to client
     */
    BufferedWriter proxyToClientBw;

    /**
     * Thread that is used to transmit data read from client to server when using HTTPS
     * Reference to this is required so it can be closed once completed.
     */
    private Thread httpsClientToServer;

    /**
     * Creates a ReuqestHandler object capable of servicing HTTP(S) GET requests
     *
     * @param clientSocket socket connected to the client
     */
    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            this.clientSocket.setSoTimeout(2000);
            proxyToClientBr = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            proxyToClientBw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads and examines the requestString and calls the appropriate method based
     * on the request type.
     */
    @Override
    public void run() {

        // Get Request from client
        String requestString;
        try {
            requestString = proxyToClientBr.readLine();
        } catch (IOException e) {
            //e.printStackTrace();
            //System.err.println("Error reading request from client");
            return;
        }

        // Parse out URL
        //System.out.println("Reuest Received " + requestString);

        // Get the Request type
        String request = requestString.substring(0, requestString.indexOf(' '));

        // remove request type and space
        String urlString = requestString.substring(requestString.indexOf(' ') + 1);

        // Remove everything past next space
        urlString = urlString.substring(0, urlString.indexOf(' '));

        // Prepend http:// if necessary to create correct URL
        if (!urlString.substring(0, 4).equals("http")) {
            String temp = "http://";
            urlString = temp + urlString;
        }

        // Check if site is blocked
        //System.out.println("site requested : " + urlString);
        if (Backend.checkValidityOfURL(urlString)) {
            blockedSiteRequested();
            return;
        }

        //Check request type
        if (request.equals("CONNECT")) {
            //System.out.println("HTTPS Request for : " + urlString + "\n");
            handleHTTPSRequest(urlString);
        } else
            sendNonCachedToClient(urlString);

    }

    /**
     * Handles HTTPS requests between client and remote server
     *
     * @param urlString desired file to be transmitted over https
     */
    private void handleHTTPSRequest(String urlString) {
        // Extract the URL and port of remote
        String url = urlString.substring(7);
        String pieces[] = url.split(":");
        url = pieces[0];
        int port = Integer.valueOf(pieces[1]);

        try {
            // Only first line of HTTPS request has been read at this point (CONNECT *)
            // Read (and throw away) the rest of the initial data on the stream
            for (int i = 0; i < 5; i++) {
                proxyToClientBr.readLine();
            }

            // Get actual IP associated with this URL through DNS
            InetAddress address = InetAddress.getByName(url);

            // Open a socket to the remote server
            Socket proxyToServerSocket = new Socket(address, port);
            proxyToServerSocket.setSoTimeout(5000);

            // Send Connection established to the client
            String line = "HTTP/1.0 200 Connection established\r\n" +
                    "Proxy-Agent: ProxyServer/1.0\r\n" +
                    "\r\n";
            proxyToClientBw.write(line);
            proxyToClientBw.flush();


            // Client and Remote will both start sending data to proxy at this point
            // Proxy needs to asynchronously read data from each party and send it to the other party


            //Create a Buffered Writer betwen proxy and remote
            BufferedWriter proxyToServerBW = new BufferedWriter(new OutputStreamWriter(proxyToServerSocket.getOutputStream()));

            // Create Buffered Reader from proxy and remote
            BufferedReader proxyToServerBR = new BufferedReader(new InputStreamReader(proxyToServerSocket.getInputStream()));


            // Create a new thread to listen to client and transmit to server
            ClientToServerHttpsTransmit clientToServerHttps =
                    new ClientToServerHttpsTransmit(clientSocket.getInputStream(), proxyToServerSocket.getOutputStream());

            httpsClientToServer = new Thread(clientToServerHttps);
            httpsClientToServer.start();


            // Listen to remote server and relay to client
            try {
                byte[] buffer = new byte[4096];
                int read;
                do {
                    read = proxyToServerSocket.getInputStream().read(buffer);
                    if (read > 0) {
                        clientSocket.getOutputStream().write(buffer, 0, read);
                        if (proxyToServerSocket.getInputStream().available() < 1) {
                            clientSocket.getOutputStream().flush();
                        }
                    }
                } while (read >= 0);
            } catch (SocketTimeoutException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }


            // Close Down Resources
            if (proxyToServerSocket != null) {
                proxyToServerSocket.close();
            }

            if (proxyToServerBR != null) {
                proxyToServerBR.close();
            }

            if (proxyToServerBW != null) {
                proxyToServerBW.close();
            }

            if (proxyToClientBw != null) {
                proxyToClientBw.close();
            }


        } catch (SocketTimeoutException e) {
            String line = "HTTP/1.0 504 Timeout Occured after 10s\n" +
                    "User-Agent: ProxyServer/1.0\n" +
                    "\r\n";
            try {
                proxyToClientBw.write(line);
                proxyToClientBw.flush();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            //System.out.println("Error on HTTPS : " + urlString);
            e.printStackTrace();
        }
    }

    /**
     * Sends the contents of the file specified by the urlString to the client
     *
     * @param urlString URL ofthe file requested
     */
    private void sendNonCachedToClient(String urlString) {

        try {

            // Compute a logical file name as per schema
            // This allows the files on stored on disk to resemble that of the URL it was taken from
            int fileExtensionIndex = urlString.lastIndexOf(".");
            String fileExtension;

            // Get the type of file
            fileExtension = urlString.substring(fileExtensionIndex, urlString.length());

            // Get the initial file name
            String fileName = urlString.substring(0, fileExtensionIndex);


            // Trim off http://www. as no need for it in file name
            fileName = fileName.substring(fileName.indexOf('.') + 1);

            // Remove any illegal characters from file name
            fileName = fileName.replace("/", "__");
            fileName = fileName.replace('.', '_');

            // Trailing / result in index.html of that directory being fetched
            if (fileExtension.contains("/")) {
                fileExtension = fileExtension.replace("/", "__");
                fileExtension = fileExtension.replace('.', '_');
                fileExtension += ".html";
            }

            fileName = fileName + fileExtension;


            // Check if file is an image
            if ((fileExtension.contains(".png")) || fileExtension.contains(".jpg") ||
                    fileExtension.contains(".jpeg") || fileExtension.contains(".gif")) {
                // Create the URL
                URL remoteURL = new URL(urlString);
                BufferedImage image = ImageIO.read(remoteURL);

                if (image != null) {
                    // Send response code to client
                    String line = "HTTP/1.0 200 OK\n" +
                            "Proxy-agent: ProxyServer/1.0\n" +
                            "\r\n";
                    proxyToClientBw.write(line);
                    proxyToClientBw.flush();

                    // Send them the image data
                    ImageIO.write(image, fileExtension.substring(1), clientSocket.getOutputStream());

                    // No image received from remote server
                } else {
                    //System.out.println("Sending 404 to client as image wasn't received from server"
                    //      + fileName);
                    String error = "HTTP/1.0 404 NOT FOUND\n" +
                            "Proxy-agent: ProxyServer/1.0\n" +
                            "\r\n";
                    proxyToClientBw.write(error);
                    proxyToClientBw.flush();
                    return;
                }
            }

            // File is a text file
            else {

                // Create the URL
                URL remoteURL = new URL(urlString);
                // Create a connection to remote server
                HttpURLConnection proxyToServerCon = (HttpURLConnection) remoteURL.openConnection();
                proxyToServerCon.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                proxyToServerCon.setRequestProperty("Content-Language", "en-US");
                proxyToServerCon.setUseCaches(false);
                proxyToServerCon.setDoOutput(true);

                // Create Buffered Reader from remote Server
                BufferedReader proxyToServerBR = new BufferedReader(new InputStreamReader(proxyToServerCon.getInputStream()));


                // Send success code to client
                String line = "HTTP/1.0 200 OK\n" +
                        "Proxy-agent: ProxyServer/1.0\n" +
                        "\r\n";
                proxyToClientBw.write(line);


                // Read from input stream between proxy and remote server
                while ((line = proxyToServerBR.readLine()) != null) {
                    // Send on data to client
                    proxyToClientBw.write(line);
                }

                // Ensure all data is sent by this point
                proxyToClientBw.flush();

                // Close Down Resources
                if (proxyToServerBR != null) {
                    proxyToServerBR.close();
                }
            }
            if (proxyToClientBw != null) {
                proxyToClientBw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Listen to data from client and transmits it to server.
     * This is done on a separate thread as must be done
     * asynchronously to reading data from server and transmitting
     * that data to the client.
     */
    class ClientToServerHttpsTransmit implements Runnable {

        InputStream proxyToClientIS;
        OutputStream proxyToServerOS;

        /**
         * Creates Object to Listen to Client and Transmit that data to the server
         *
         * @param proxyToClientIS Stream that proxy uses to receive data from client
         * @param proxyToServerOS Stream that proxy uses to transmit data to remote server
         */
        public ClientToServerHttpsTransmit(InputStream proxyToClientIS, OutputStream proxyToServerOS) {
            this.proxyToClientIS = proxyToClientIS;
            this.proxyToServerOS = proxyToServerOS;
        }

        @Override
        public void run() {
            try {
                // Read byte by byte from client and send directly to server
                byte[] buffer = new byte[4096];
                int read;
                do {
                    read = proxyToClientIS.read(buffer);
                    if (read > 0) {
                        proxyToServerOS.write(buffer, 0, read);
                        if (proxyToClientIS.available() < 1) {
                            proxyToServerOS.flush();
                        }
                    }
                } while (read >= 0);
            } catch (SocketTimeoutException ste) {
                // TODO: handle exception
            } catch (IOException e) {
                //System.out.println("Proxy to client HTTPS read timed out");
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is called when user requests a page that is blocked by the proxy.
     * Sends an access forbidden message back to the client
     */
    private void blockedSiteRequested() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String line = "HTTP/1.0 403 Access Forbidden \n" +
                    "User-Agent: ProxyServer/1.0\n" +
                    "\r\n";
            bufferedWriter.write(line);
            bufferedWriter.flush();
            clientSocket.close();
        } catch (IOException e) {
            //System.out.println("Error writing to client when requested a blocked site");
            e.printStackTrace();
        }
    }
}
