package Models;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketListener implements Runnable {
    public ExecutorService pool;
    public boolean startStop;
    public ServerSocket server;
    public int serverPort;
    public Backend back;

    public SocketListener(Backend back) {
        this.back = back;
        startStop = true;
        pool = Executors.newCachedThreadPool();
        serverPort = 8085;
        configServerPort();
    }

    private void configServerPort() {
        boolean needChangePort = true;
        while (needChangePort) {
            try {
                server = new ServerSocket(serverPort);
                needChangePort = false;
            } catch (IOException e) {
                serverPort += 10;
            }
        }
    }

    public void stopServer() throws IOException {
        startStop = false;
        pool.shutdown();
        server.close();
    }

    public void resumServer() {
        pool = Executors.newCachedThreadPool();
        startStop = true;
        configServerPort();
        run();
    }

    @Override
    public void run() {
        //System.out.println("Proxy Started!!! Listening...");
        while (startStop) {
            Socket request = null;
            try {
                //System.err.println("new tcp socket");
                request = server.accept();
                pool.execute(new RequestHandler(request));
            } catch (IOException e) {
                //System.out.println("socket closed, go to closing");
            }
        }
    }
}

