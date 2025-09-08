import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandParser {
    public static Command parse(String input) {
        String trimmed = input.trim();
        boolean background = trimmed.endsWith("&");
        if (background) trimmed = trimmed.substring(0, trimmed.length() - 1).trim();

        String[] parts = trimmed.split("\\s+");
        List<String> tokens = new ArrayList<>(Arrays.asList(parts));

        String inputRedirect = null, outputRedirect = null;
        boolean pipe = trimmed.contains("|");

        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).equals("<")) {
                inputRedirect = tokens.get(i + 1);
                tokens.remove(i); tokens.remove(i); i--;
            } else if (tokens.get(i).equals(">")) {
                outputRedirect = tokens.get(i + 1);
                tokens.remove(i); tokens.remove(i); i--;
            }
        }

        return new Command(input, tokens, background, inputRedirect, outputRedirect, pipe);
    }
}
