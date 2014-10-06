package com.synload.socketframework.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.synload.eventsystem.EventTrigger;
import com.synload.eventsystem.Handler;
import com.synload.eventsystem.HandlerRegistry;
import com.synload.socketframework.annotations.Module;

public class ModuleLoader {
    public enum TYPE {
        METHOD, CLASS
    }

    public static void load(String path) {
        String fileName;
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                fileName = listOfFiles[i].getName();
                if (fileName.endsWith(".jar")) {
                    try {
                        URLClassLoader cl = new URLClassLoader(
                                new URL[] { new File(path + fileName).toURI()
                                        .toURL() });
                        List<String> classList = getClasses(path + fileName);
                        ModuleClass module = null;
                        for (String clazz : classList) {
                            try {
                                Class loadedClass = cl.loadClass(clazz);
                                try {
                                    ModuleClass tmp = registerForAnnotation(
                                            loadedClass, Handler.MODULE,
                                            TYPE.CLASS, null);
                                    if (tmp != null)
                                        module = tmp;
                                    registerForAnnotation(loadedClass,
                                            Handler.EVENT, TYPE.METHOD, module);
                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                // cl.close();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /*
     * Checks for Addons, Methods in each class
     */
    @SuppressWarnings("unchecked")
    public static ModuleClass registerForAnnotation(Class c,
            Handler annotationClass, TYPE type, ModuleClass module)
            throws InstantiationException, IllegalAccessException {
        if (TYPE.CLASS == type) {
            if (c.isAnnotationPresent(annotationClass.getAnnotationClass())) {
                /*
                 * Loaded a module, declare it as such and register it!
                 */
                Module moduleAnnotation = (Module) c
                        .getAnnotation(Handler.MODULE.getAnnotationClass());
                ModuleClass mod = (ModuleClass) c.newInstance();
                ModuleRegistry.getLoadedModules().put(moduleAnnotation.name(),
                        mod);
                System.out.println("[INFO] Loaded module: "
                        + moduleAnnotation.name());
                System.out.println("[INFO] Author: "
                        + moduleAnnotation.author());
                mod.initialize();
                return mod;
            }
        } else if (TYPE.METHOD == type) {
            for (Method m : c.getMethods()) {
                if (m.isAnnotationPresent(annotationClass.getAnnotationClass())) {
                    EventTrigger et = new EventTrigger();
                    et.setHostClass(c);
                    et.setMethod(m);
                    et.setModule(module);
                    System.out.println("[INFO] Loaded Method: " + m.getName());
                    HandlerRegistry.register(
                            annotationClass.getAnnotationClass(), et);
                }
            }
        }
        return null;
    }

    public static List<String> getClasses(String file) throws IOException {
        List<String> classNames = new ArrayList<String>();
        ZipInputStream zip = new ZipInputStream(new FileInputStream(file));
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip
                .getNextEntry())
            if (entry.getName().endsWith(".class") && !entry.isDirectory()) {
                // This ZipEntry represents a class. Now, what class does it
                // represent?
                StringBuilder className = new StringBuilder();
                for (String part : entry.getName().split("/")) {
                    if (className.length() != 0)
                        className.append(".");
                    className.append(part);
                    if (part.endsWith(".class"))
                        className.setLength(className.length()
                                - ".class".length());
                }
                classNames.add(className.toString());
            }
        return classNames;
    }
}
