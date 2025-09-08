import java.io.*;
import java.nio.file.*;
import java.util.List;

public class CommandExecutor {
    public static void execute(Command cmd) throws IOException {
        Logger.log("Executando: " + cmd.rawInput);

        String command = cmd.getCommandName();
        List<String> args = cmd.getArgs();

        // Comandos internos
        switch (command) {
            case "cd": changeDirectory(args); return;
            case "pwd": printWorkingDirectory(); return;
            case "clear": clearScreen(); return;
            case "mkdir": makeDirectory(args); return;
            case "rmdir": removeDirectory(args); return;
            case "rm": removeFile(args); return;
            case "mv": moveFile(args); return;
            case "cp": copyFile(args); return;
            case "echo": echo(args, cmd.outputRedirect); return;
            case "cat": cat(args, cmd.inputRedirect); return;
            case "ls": listDirectory(); return;
        }

        // Comando externo
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(cmd.tokens);
        builder.directory(new File(Shell.currentDirectory)); // atualiza o diretório
        if (cmd.inputRedirect != null) builder.redirectInput(new File(cmd.inputRedirect));
        if (cmd.outputRedirect != null) builder.redirectOutput(new File(cmd.outputRedirect));

        Process process = builder.start();

        if (!cmd.runInBackground) {
            try {
                int status = process.waitFor();
                Logger.log("Processo finalizado com status: " + status);
            } catch (InterruptedException e) {
                Logger.log("Processo interrompido.");
            }
        }
    }

    // Métodos internos

    private static void changeDirectory(List<String> args) {
        if (args.isEmpty()) return;

        Path newPath;
        String path = args.get(0);

        if (path.equals("..")) {
            newPath = Paths.get(Shell.currentDirectory).getParent();
        } else {
            newPath = Paths.get(Shell.currentDirectory, path).normalize();
        }

        if (newPath != null && Files.exists(newPath) && Files.isDirectory(newPath)) {
            Shell.currentDirectory = newPath.toAbsolutePath().toString();
        } else {
            System.out.println("Diretório inválido: " + path);
        }
    }

    private static void printWorkingDirectory() {
        System.out.println(Shell.currentDirectory);
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void makeDirectory(List<String> args) throws IOException {
        if (!args.isEmpty()) Files.createDirectories(Paths.get(Shell.currentDirectory, args.get(0)));
    }

    private static void removeDirectory(List<String> args) throws IOException {
        if (!args.isEmpty()) Files.deleteIfExists(Paths.get(Shell.currentDirectory, args.get(0)));
    }

    private static void removeFile(List<String> args) throws IOException {
        if (!args.isEmpty()) Files.deleteIfExists(Paths.get(Shell.currentDirectory, args.get(0)));
    }

    private static void moveFile(List<String> args) throws IOException {
        if (args.size() < 2) return;
        Path source = Paths.get(Shell.currentDirectory, args.get(0));
        Path target = Paths.get(Shell.currentDirectory, args.get(1));
        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    private static void copyFile(List<String> args) throws IOException {
        if (args.size() < 2) return;
        Path source = Paths.get(Shell.currentDirectory, args.get(0));
        Path target = Paths.get(Shell.currentDirectory, args.get(1));
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    private static void echo(List<String> args, String outputRedirect) throws IOException {
        String content = String.join(" ", args);

        if (outputRedirect != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                    Paths.get(Shell.currentDirectory, outputRedirect).toFile()))) {
                writer.write(content + "\n");
            }
        } else {
            System.out.println(content);
        }
    }

    private static void cat(List<String> args, String inputRedirect) throws IOException {
        BufferedReader reader;

        if (inputRedirect != null) {
            reader = new BufferedReader(new FileReader(
                    Paths.get(Shell.currentDirectory, inputRedirect).toFile()));
        } else if (!args.isEmpty()) {
            reader = new BufferedReader(new FileReader(
                    Paths.get(Shell.currentDirectory, args.get(0)).toFile()));
        } else {
            reader = new BufferedReader(new InputStreamReader(System.in));
        }

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        reader.close();
    }

    private static void listDirectory() {
        File dir = new File(Shell.currentDirectory);
        File[] files = dir.listFiles();

        if (files != null) {
            for (File f : files) {
                String type = f.isDirectory() ? "[DIR] " : "      ";
                System.out.println(type + f.getName());
            }
        } else {
            System.out.println("Erro ao acessar o diretório.");
        }
    }
}
