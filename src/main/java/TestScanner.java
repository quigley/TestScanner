import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;
import java.nio.file.Path;

public class TestScanner {
    public ArrayList<Method> GetAllTestMethods(String pathToJar) throws IOException {
        Set<Class<?>> classes = new HashSet<>();

        URL[] jarUrl = {new URL("file:" + pathToJar)};
        URLClassLoader loader = new URLClassLoader(jarUrl);

        JarFile jar = new JarFile(pathToJar);

        for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements(); ) {
            JarEntry entry = entries.nextElement();
            String fileName = entry.getName();

            if (fileName.endsWith(".class")) {
                String className = fileName
                        .replace('/', '.')
                        .substring(0, fileName.length() - 6)
                        .split("\\$")[0];
                try {
                    Class<?> c = null;
                    try {
                        c = loader.loadClass(className);
                    } catch (ClassNotFoundException e) {
                    }
                    if (c != null)
                        classes.add(c);
                } catch (Throwable e) {
                    System.err.println("Failed to instantiate " + className);
                }
            }
        }

        ArrayList<Method> methods = new ArrayList<>();
        for (Class<?> _class : classes) {
            try {
                for (Method method : _class.getMethods()) {
                    if (method.getAnnotationsByType(Test.class).length > 0) {
                        methods.add(method);
                    }
                }
            } catch (NoClassDefFoundError ex) {
                System.err.println(String.format("Unable to load: %s", _class));
            }
        }
        return methods;
    }

    public List<String> GetAllFeatureFiles(String pathToJar){
        Stream<Path> filesStream = null;
        try {
           filesStream = Files.walk(Paths.get(pathToJar))
                    .filter(x -> x.getFileName()
                            .toString()
                            .toLowerCase()
                            .endsWith(".feature"));
        }
        catch (IOException ex){
            System.err.println(ex);
        }
        List<String> files = filesStream
                .map(path -> Files.isDirectory(path) ? path.toString() + '/' : path.toString())
                .collect(Collectors.toList());
        return files;
    }
}
