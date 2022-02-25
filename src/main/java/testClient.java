import com.cedarsoftware.util.io.JsonWriter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class testClient {
    public static void main(String[] args) {
        Random random = new Random();
        String[] words = {"бизнес", "архитектура", "devOps", "качество", "Тесты", "конфигурация", "быть", "артефакт"};
        String result;
        try (
                Socket socket = new Socket("localhost", 8989);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {
            for (int i = 0; i < 1000; i++) {
                String word = words[random.nextInt(words.length)];
                System.out.println("№_" + i + " Поиск слова - <" + word + ">");
                out.println(word);
                result = in.readLine();
                if (result.equals("Ничего не найдено!")) {
                    System.out.println(result);
                } else {
                    System.out.println(JsonWriter.formatJson(result));
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
