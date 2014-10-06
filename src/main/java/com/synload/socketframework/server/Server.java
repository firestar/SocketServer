package com.synload.socketframework.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server extends ServerSocket {
    public List<Client> connectedClients = new ArrayList<Client>();

    public Server(int port) throws IOException {
        super(port);
    }

    public void run() {
        while (!this.isClosed()) {
            try {
                Client client = new Client(this.accept());
                client.handle();
                connectedClients.add(client);
            } catch (IOException e) {
                e.printStackTrace();
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
