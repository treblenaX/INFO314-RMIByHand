import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    private static Socket socket;
    private static InputStream is;
    private static OutputStream os;

    /**
     * This method name and parameters must remain as-is
     */
    public static int add(int lhs, int rhs) {
        connect();

        RemoteMethod add = new RemoteMethod("add", new Object[] {lhs, rhs});

        writeRequest(add);

        String response = readResponse();

        disconnect();

        return Integer.parseInt(response);
    }
    /**
     * This method name and parameters must remain as-is
     */
    public static int divide(int num, int denom) throws ArithmeticException {
        connect();

        RemoteMethod divide = new RemoteMethod("divide", new Object[] {num, denom});

        writeRequest(divide);
        String response = readResponse();

        if (response.equals("ArithmeticException")) throw new ArithmeticException();

        disconnect();

        return Integer.parseInt(response);
    }
    /**
     * This method name and parameters must remain as-is
     */
    public static String echo(String message) {
        connect();

        RemoteMethod echo = new RemoteMethod("echo", new Object[] {message});


        writeRequest(echo);

        String response = readResponse();

        disconnect();

        return response;
    }

    private static void connect() {
        try {
            socket = new Socket(server, PORT);
            is = socket.getInputStream();
            os = socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeRequest(RemoteMethod rm) {
        try {
            // serialize the object
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(rm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readResponse() {
        StringBuilder data = new StringBuilder();
        try {
            // readResponse the resposne
            int character;
    
            while ((character = is.read()) != -1) {
                data.append((char) character);
            }
    

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    private static void disconnect() {
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Do not modify any code below this line
    // --------------------------------------
    static String server = "localhost";
    public static final int PORT = 10314;

    public static void main(String... args) {
        // All of the code below this line must be uncommented
        // to be successfully graded.
        System.out.print("Testing... ");

        if (add(2, 4) == 6)
            System.out.print(".");
        else
            System.out.print("X");

        try {
            divide(1, 0);
            System.out.print("X");
        }
        catch (ArithmeticException x) {
            System.out.print(".");
        }

        if (echo("Hello").equals("You said Hello!"))
            System.out.print(".");
        else
            System.out.print("X");

        System.out.println(" Finished");
    }
}