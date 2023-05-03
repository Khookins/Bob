package bobpack.console;

import java.io.IOException;
import java.util.List;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;

public class InteractiveSection extends ConsoleSection{
    private String[] currentMenu;

    public InteractiveSection(TerminalScreen s, TextGraphics textGraphics, int startX, int startY, int width, int height) {
        super(s, textGraphics, startX, startY, width, height);
    }

    public String askText(Terminal terminal, String prompt) throws IOException {
        textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        clear();
        textGraphics.putString(startX, startY + 1, prompt);
        screen.refresh();

        StringBuilder input = new StringBuilder();
        while (true) {
            KeyStroke keyStroke = terminal.pollInput();

            if (keyStroke != null) {
                textGraphics.setBackgroundColor(TextColor.ANSI.WHITE);
                textGraphics.setForegroundColor(TextColor.ANSI.BLACK);
                if (keyStroke.getKeyType() == KeyType.Character) {
                    input.append(keyStroke.getCharacter());
                    textGraphics.putString(startX + input.length() - 1, startY + 2,
                            String.valueOf(keyStroke.getCharacter()));
                } else if (keyStroke.getKeyType() == KeyType.Backspace && input.length() > 0) {
                    textGraphics.putString(startX + input.length() - 1, startY + 2, " ");
                    input.setLength(input.length() - 1);
                } else if (keyStroke.getKeyType() == KeyType.Enter) {
                    textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
                    textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
                    break;
                }
                screen.refresh();
            }
        }

        return input.toString();
    }
    
    public String askOptions(Terminal terminal, String question, String[] choices) throws IOException {
        screen.refresh();
        return internalAsk(terminal, question, choices);
    }

    private String internalAsk(Terminal terminal, String question, String[] choices) throws IOException {
        clear();
        screen.refresh();
        textGraphics.putString(startX, startY, question);
        int selectedIndex = 0;
        while (true) {
            displayChoices(choices, selectedIndex);

            KeyStroke keyStroke = terminal.pollInput();
            screen.refresh();

            if (keyStroke != null) {
                if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                    selectedIndex = (selectedIndex + choices.length - 1) % choices.length;
                } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                    selectedIndex = (selectedIndex + 1) % choices.length;
                } else if (keyStroke.getKeyType() == KeyType.Enter) {
                    return choices[selectedIndex];
                }
            }
            screen.refresh();
        }
        
        
    }

    public void waitForContinue(Terminal terminal) throws IOException {
        while (true) {
            KeyStroke keyStroke = terminal.pollInput();
            if (keyStroke != null) {
                break;
            }
        }
    }

    private void displayChoices(String[] choices, int selectedIndex) throws IOException {
        for (int i = 0; i < choices.length; i++) {
            String prefix = (i == selectedIndex) ? "> " : "  ";
            textGraphics.setForegroundColor((i == selectedIndex) ? TextColor.ANSI.BLACK : TextColor.ANSI.WHITE);
            textGraphics.setBackgroundColor((i == selectedIndex) ? TextColor.ANSI.WHITE : TextColor.ANSI.DEFAULT);
            textGraphics.putString(startX, startY + i + 3, prefix + choices[i]);
            textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
            textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            screen.refresh();
        }
        screen.refresh();
    }

    public String[] getNormalMenuOptions() {
        return new String[] { "Attack", "Defend", "Use Item", "Run" };
    }

    public void setAnyKeyMode(Terminal terminal) throws IOException {
        this.clear();
        this.write("Press any key to continue...");
        waitForContinue(terminal);
        this.currentMenu = getNormalMenuOptions();
    }
}

    

