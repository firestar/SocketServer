package com.synload.eventsystem;

import java.lang.reflect.Method;

import com.synload.socketframework.module.ModuleClass;

public class EventTrigger {
    private Class hostClass;
    private Method method;
    private ModuleClass module;

    public Class getHostClass() {
        return hostClass;
    }

    public void setHostClass(Class hostClass) {
        this.hostClass = hostClass;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public ModuleClass getModule() {
        return module;
    }

    public void setModule(ModuleClass module) {
        this.module = module;
    }
}
