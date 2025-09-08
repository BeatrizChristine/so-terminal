import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {
    private static BufferedWriter writer;

    public static void initialize() {
        try {
            writer = new BufferedWriter(new FileWriter("shell_log.txt", true));
            log("=== Nova sessão iniciada em " + LocalDateTime.now() + " ===");
        } catch (IOException e) {
            System.err.println("Erro ao iniciar log: " + e.getMessage());
        }
    }

    public static void log(String message) {
        try {
            writer.write("[" + LocalDateTime.now() + "] " + message + "\n");
            writer.flush();
        } catch (IOException e) {
            System.err.println("Erro ao gravar log: " + e.getMessage());
        }
    }
}

