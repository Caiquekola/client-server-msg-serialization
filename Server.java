import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
    public final static int portaServidor = 2304;

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(portaServidor);
        System.out.println("Servidor iniciado na porta: "+portaServidor);
        while(true){
            Socket cliente = server.accept();
            System.out.println("Cliente conectado:"+cliente.getInetAddress().getHostAddress());
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            saida.writeObject(new Date());
            saida.close();
            cliente.close();

        }
    }
}