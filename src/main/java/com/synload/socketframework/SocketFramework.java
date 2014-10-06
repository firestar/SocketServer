package com.synload.socketframework;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.synload.socketframework.module.ModuleLoader;
import com.synload.socketframework.server.Server;

public class SocketFramework {
    public static Server server;
    public static int port = 2006;
    public static String modulePath = "modules/";

    public static void main(String[] args) {
        Map<String, String> AVs = parseArguments(args);
        if (AVs.containsKey("port")) {
            port = Integer.parseInt(AVs.get("port"));
        }

        ModuleLoader.load(modulePath);

        // Starting server up!
        try {
            server = new Server(port);
            System.out.println("Starting the server on port: " + port);
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Map<String, String> parseArguments(String[] args) {
        Map<String, String> output = new HashMap<String, String>();
        for (int x = 0; x < args.length; x++) {
            if (args[x].startsWith("-")) {
                // New Argument
                String argument = args[x].substring(1, args[x].length());
                String value = "";
                if (args.length > (x + 1)) {
                    if (args[x + 1].startsWith("\"")) {
                        x++;
                        if (args[x].endsWith("\"")) {
                            value += args[x].substring(1, args[x].length() - 1);
                        } else {
                            value += args[x].substring(1, args[x].length());
                            for (int i = x; i < args.length; i++) {
                                x = i;
                                if (args[x].endsWith("\"")) {
                                    value += args[x].substring(0,
                                            args[x].length() - 1);
                                    break;
                                } else {
                                    value += args[x];
                                }
                            }
                        }
                    } else {
                        x++;
                        value = args[x];
                    }
                    output.put(argument, value);
                } else {
                    System.out.println("Error decoding the argument: "
                            + argument);
                }
            }
        }
        return output;
    }
}
