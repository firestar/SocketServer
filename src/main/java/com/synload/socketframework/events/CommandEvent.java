package com.synload.socketframework.events;

import com.synload.eventsystem.EventClass;
import com.synload.eventsystem.Handler;
import com.synload.socketframework.encoding.TransmissionMessage;
import com.synload.socketframework.server.Client;

public class CommandEvent extends EventClass {
    private TransmissionMessage message;
    private Client client;

    public CommandEvent(TransmissionMessage message, Client client) {
        this.setHandler(Handler.EVENT);
        this.message = message;
        this.client = client;
    }

    public TransmissionMessage getTransmission() {
        return message;
    }

    public void setMessage(TransmissionMessage message) {
        this.message = message;
    }

    public Client getClient() {
        return client;
    }
}
