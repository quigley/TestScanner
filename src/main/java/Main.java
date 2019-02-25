import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String path = null;
        String format = null;
        if (args[0].isEmpty()) {
            System.err.println("Please specify the path to the ");
        }
        for (String arg : args) {
            if (arg.toLowerCase().contains("-path")) {
                path = arg.split("=")[1];
            }
            if (arg.toLowerCase().contains("-format")) {
                format = arg.split("=")[1];
            }
        }
        try {
            TestScanner testScanner = new TestScanner();
            if (format.equalsIgnoreCase("java")) {
                ArrayList<Method> methods = testScanner.GetAllTestMethods(path);
                methods.forEach(System.out::println);
            }
            if (format.equalsIgnoreCase("cucumber")) {
                List<String > featureFiles = testScanner.GetAllFeatureFiles(path);
                featureFiles.forEach(System.out::println);
            }

        } catch (Exception ex) {
            System.err.println(String.format("Error running scanner: %s", ex));

        }
    }
}
