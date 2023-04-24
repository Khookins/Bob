package bobpack.console;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is responsible for handling anything to do with our console output.
 * We can make different functions in this class for other classes to use, without them knowing how to use a console.
 * For example, we can make a function called "printAttack", so that attacks can be printed out with
 * a different colour, or a different font, or whatever we want.
 * 
 * If we want to be smart, you may notice that this class lets us insert text at certain points on the screen.
 * This can allow us to make a user interface, showing character stats, inventory, or whatever else we want,
 * however this is an ADVANCED task, so lets not worry about it unless you are super keen to learn
 */
public class CustomConsole {
    private Screen screen;
    private TextGraphics textGraphics;
    private List<String> history;

    /** Constructor for Custom Console */
    public CustomConsole() throws IOException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
        screen.startScreen();
        textGraphics = screen.newTextGraphics();
        history = new ArrayList<>();
        screen.setCursorPosition(null);
    }

    public String askOptions(String question, String[] choices) throws IOException {
        return internalAsk(question, choices);
    }

    public String askText(String prompt) throws IOException {
        textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        screen.clear();
        textGraphics.putString(0, 1, prompt);
        screen.refresh();

        StringBuilder input = new StringBuilder();
        while (true) {
            KeyStroke keyStroke = screen.pollInput();

            if (keyStroke != null) {
                textGraphics.setBackgroundColor(TextColor.ANSI.WHITE);
                textGraphics.setForegroundColor(TextColor.ANSI.BLACK);
                if (keyStroke.getKeyType() == KeyType.Character) {
                    input.append(keyStroke.getCharacter());
                    textGraphics.putString(0 + input.length() - 1, 2, String.valueOf(keyStroke.getCharacter()));
                    screen.refresh();
                } else if (keyStroke.getKeyType() == KeyType.Backspace && input.length() > 0) {
                    textGraphics.putString(input.length() - 1, 2, " ");
                    input.setLength(input.length() - 1);
                    screen.refresh();
                } else if (keyStroke.getKeyType() == KeyType.Enter) {
                    break;
                }
            }
        }

        return input.toString();
    }

    private String internalAsk(String question, String[] choices) throws IOException {
        textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
        screen.clear();
        textGraphics.putString(0, 1, question);

        int selectedIndex = 0;
        while (true) {
            displayChoices(choices, selectedIndex);
            screen.refresh();

            KeyStroke keyStroke = screen.pollInput();

            if (keyStroke != null) {
                if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                    selectedIndex = (selectedIndex + choices.length - 1) % choices.length;
                } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                    selectedIndex = (selectedIndex + 1) % choices.length;
                } else if (keyStroke.getKeyType() == KeyType.Enter) {
                    break;
                }
            }
        }

        return choices[selectedIndex];
    }

    private void displayChoices(String[] options, int selectedIndex) {
        for (int i = 0; i < options.length; i++) {
            String prefix = (i == selectedIndex) ? "> " : "  ";
            textGraphics.setForegroundColor((i == selectedIndex) ? TextColor.ANSI.BLACK : TextColor.ANSI.WHITE);
            textGraphics.setBackgroundColor((i == selectedIndex) ? TextColor.ANSI.WHITE : TextColor.ANSI.DEFAULT);
            textGraphics.putString(0, i + 3, prefix + options[i]);
        }
    }

    private void displayHistory() {
        if (history.size() == 0)
            return;
        if(history.size() > 20)
            history = history.subList(history.size() - 20, history.size());

        int row = 0;
        for (String line : history) {
            textGraphics.putString(0, row, line);
            row++;
        }
    }

     public void say(String s) throws IOException {
        history.add(s);
        textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        screen.clear();
        displayHistory();
        screen.refresh();
    }

    public void askContinue(String continueMessage) throws IOException {
        history.add(continueMessage);
        textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
        screen.clear();
        displayHistory();
        screen.refresh();

        while (true) {
            KeyStroke keyStroke = screen.pollInput();

            if (keyStroke != null) {
                break;
            }
        }
    }

    public void askContinue() throws IOException {
        askContinue("Press any key to continue...");
    }

    public void setTextColour(ANSI colour) {
        textGraphics.setForegroundColor(colour);
    }

    public void setBackgroundColour(ANSI colour)
    {
        textGraphics.setBackgroundColor(colour);
    }

    // ... other methods ...
}