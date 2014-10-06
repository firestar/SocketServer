package com.synload.eventsystem;

import com.synload.socketframework.annotations.Event;
import com.synload.socketframework.annotations.Module;

public enum Handler {
    EVENT(Event.class), MODULE(Module.class);
    private Class annotationClass;

    private Handler(Class clazz) {
        this.annotationClass = clazz;
    }

    public Class getAnnotationClass() {
        return annotationClass;
    }
}
