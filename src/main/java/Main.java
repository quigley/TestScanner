import java.util.ArrayList;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) {
        if (args[0].isEmpty()) {
            System.err.println("Please specify the path to the ");
        }
        try {
            TestScanner testScanner = new TestScanner();
            ArrayList<Method> methods = testScanner.GetAllTestMethods(args[0], "");
            for (Method method : methods){
                System.out.println(method.getName());
            }
        } catch (Exception ex) {
            System.err.println(String.format("Error running scanner: %s", ex));

        }
    }
}
