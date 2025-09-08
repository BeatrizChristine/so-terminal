import java.util.Scanner;

public class Shell {
    public static String currentDirectory = System.getProperty("user.dir");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Logger.initialize();

        System.out.println("Bem-vindo ao Shell Beatriz Christine!");
        while (true) {
            System.out.print(currentDirectory + " > ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) continue;

            if (input.equals("exit")) {
                Logger.log("Saindo da sessão.");
                break;
            }

            try {
                Command command = CommandParser.parse(input);
                CommandExecutor.execute(command);
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
