import java.util.List;

public class Command {
    String rawInput;
    List<String> tokens;
    boolean runInBackground;
    String inputRedirect;
    String outputRedirect;
    boolean isPipe;

    public Command(String rawInput, List<String> tokens, boolean runInBackground,
                   String inputRedirect, String outputRedirect, boolean isPipe) {
        this.rawInput = rawInput;
        this.tokens = tokens;
        this.runInBackground = runInBackground;
        this.inputRedirect = inputRedirect;
        this.outputRedirect = outputRedirect;
        this.isPipe = isPipe;
    }

    public String getCommandName() {
        return tokens.get(0);
    }

    public List<String> getArgs() {
        return tokens.subList(1, tokens.size());
    }
}
