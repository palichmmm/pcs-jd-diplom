import com.cedarsoftware.util.io.JsonWriter;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<PageEntry> result;
        int port = 8989;
        String word, json;
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Starting server at " + port + "...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept(); // ждем подключения
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
                    System.out.printf("New connection accepted. Port: %d%n", clientSocket.getPort());
                    while (!clientSocket.isClosed()) {
                        word = in.readLine();
                        if (word != null) {
                            System.out.print("Получено слово <" + word + "> Результат поиска - ");
                            result = engine.search(word);
                        } else {
                            break;
                        }
                        if (result.size() != 0) {
                            System.out.println(result.size() + " Ok");
                            json = new Gson().toJson(result);
                            System.out.println(JsonWriter.formatJson(json));
                        } else {
                            System.out.println(" null");
                            json = "Ничего не найдено!";
                            System.out.println(json);
                        }
                        out.println(json);
                    }
                    if (clientSocket.isClosed()) {
                        System.out.println("Соединение закрыто!");
                    }
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            System.out.println("Server closed!");
        }
    }
}