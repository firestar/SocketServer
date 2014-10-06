package com.synload.socketframework.module;

import com.synload.socketframework.server.Client;

public abstract class ModuleClass {
    public abstract void initialize();

    public abstract void close(Client c);

    public abstract void open(Client c);
}
