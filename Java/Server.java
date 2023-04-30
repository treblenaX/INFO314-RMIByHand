import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(10314);
            Executor executor = Executors.newFixedThreadPool(5);

            while (true) {
                Socket socket = server.accept();
                executor.execute(() -> {
                    try {
                        InputStream is = socket.getInputStream();
                        OutputStream os = socket.getOutputStream();
    
                        // read the request
                        ObjectInputStream ois = new ObjectInputStream(is);
                        RemoteMethod rm = (RemoteMethod) ois.readObject();
    
                        System.out.println("Received request: " + rm.getMethodName() + " " + Arrays.toString(rm.getArgs()));
                        String methodName = rm.getMethodName();
                        Object[] parameters = rm.getArgs();
    
                        StringBuilder sb = new StringBuilder();
    
                        switch (methodName) {
                            case "add":
                                sb.append(add((int) parameters[0], (int) parameters[1]));
                                System.out.println("Result: " + sb.toString());
                                break;
                            case "divide":
                                try {
                                    sb.append(divide((int) parameters[0], (int) parameters[1]));
                                } catch (Exception e) {
                                    sb.append(e.getClass().getSimpleName());
                                }
                                System.out.println("Result: " + sb.toString());
                                break;
                            case "echo":
                                sb.append(echo((String) parameters[0]));
                                System.out.println("Result: " + sb.toString());
                                break;
                            default:
                                throw new Exception("Unknown method: " + methodName);
                        }
    
                        os.write(sb.toString().getBytes());
                        os.flush();
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Do not modify any code below tihs line
    // --------------------------------------
    public static String echo(String message) { 
        return "You said " + message + "!";
    }
    public static int add(int lhs, int rhs) {
        return lhs + rhs;
    }
    public static int divide(int num, int denom) {
        if (denom == 0)
            throw new ArithmeticException();

        return num / denom;
    }
}