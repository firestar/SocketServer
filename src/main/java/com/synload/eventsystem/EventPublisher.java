package com.synload.eventsystem;

public class EventPublisher {
    public static void raiseEvent(final EventClass event, boolean threaded) {
        if (threaded) {
            new Thread() {
                @Override
                public void run() {
                    raise(event);
                }
            }.start();
        } else {
            raise(event);
        }
    }

    public static void raiseEvent(final EventClass event) {
        raise(event);
    }

    private static void raise(final EventClass event) {
        if (HandlerRegistry.getHandlers().containsKey(
                event.getHandler().getAnnotationClass())) {
            for (EventTrigger trigger : HandlerRegistry.getHandlers(event
                    .getHandler().getAnnotationClass())) {
                // if(((Event)
                // trigger.getMethod().getAnnotation(Event.class)).event().isInstance(event)){
                // REMOVED ANNOTATION Event check
                if (trigger.getMethod().getParameterTypes()[0]
                        .isInstance(event)) {
                    try {
                        trigger.getMethod().invoke(
                                trigger.getHostClass().newInstance(), event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}