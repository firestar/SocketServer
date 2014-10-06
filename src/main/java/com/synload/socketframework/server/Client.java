package com.synload.socketframework.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import com.synload.eventsystem.EventPublisher;
import com.synload.socketframework.encoding.Transmission;
import com.synload.socketframework.encoding.TransmissionMessage;
import com.synload.socketframework.events.CommandEvent;

public class Client {
    private InputStream is;
    private OutputStream os;
    private Socket socket;

    public Client(Socket socket) {
        this.socket = socket;
    }

    public void handle() throws IOException {
        is = socket.getInputStream();
        os = socket.getOutputStream();
        rData();
    }

    /*
     * Recieve data from the connected server socket
     */
    public void rData() {
        byte[] buffer = new byte[8 * 1024];
        int readLength = 0;
        try {
            while ((readLength = is.read(buffer)) != -1) {
                TransmissionMessage transmission = Transmission
                        .decodeMessage(Arrays.copyOf(buffer, readLength));
                EventPublisher.raiseEvent(new CommandEvent(transmission, this),
                        false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream getIs() {
        return is;
    }

    public OutputStream getOs() {
        return os;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
