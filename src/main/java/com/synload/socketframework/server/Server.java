package com.synload.socketframework.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server extends ServerSocket {
    public int maxOpenClients = 100;
    public int openClients = 0;
    public class ThreadedAccept implements Runnable{
        Server s;
        public ThreadedAccept(Server s){
            this.s = s;
        }
        public void run() {
            try {
                Client client = new Client(s.accept());
                connectedClients.add(client);
                client.handle();
                openClients--;
                connectedClients.remove(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public List<Client> connectedClients = new ArrayList<Client>();

    public Server(int port) throws IOException {
        super(port);
    }

    public void run() {
        while (!this.isClosed()) {
            if(openClients<maxOpenClients){
                (new Thread(new ThreadedAccept(this))).start();
                openClients++;
            }
        }
    }

    public void closeConnections() throws IOException {
        for (Client c : connectedClients) {
            c.getSocket().close();
        }
    }

    public void closeServer() throws IOException {
        this.close();
    }

}
