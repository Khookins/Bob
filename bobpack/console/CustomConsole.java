package bobpack.console;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.gui2.Window;

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
    private TerminalScreen screen;
    private TextGraphics textGraphics;

    private ConsoleSection mainSection;
    private StatsSection statsSection;
    private InteractiveSection interactiveSection;
    private StatsSection enemySection;

    private static SwingTerminalFrame terminal;

    /** Constructor for Custom Console */
    public CustomConsole() throws IOException {
        terminal = new DefaultTerminalFactory().setInitialTerminalSize(new TerminalSize(120, 40)) // Set the initial size of the terminal
                .createSwingTerminal();
        terminal.setVisible(true);
        screen = new TerminalScreen(terminal);
        screen.startScreen();
        textGraphics = screen.newTextGraphics();
        screen.setCursorPosition(null);

        mainSection = new ConsoleSection(screen, textGraphics, 0, 0, 80, 15);
        statsSection = new StatsSection(screen, textGraphics, 80, 15, 40, 15);
        interactiveSection = new InteractiveSection(screen, textGraphics, 0, 15, 80, 15);
        enemySection = new StatsSection(screen, textGraphics, 80, 0, 40, 15);
    }

    public String askOptions(String question, String[] choices) throws IOException {
        return interactiveSection.askOptions(terminal, question, choices);
    }

    public String askText(String prompt) throws IOException {
        return interactiveSection.askText(terminal, prompt);
    }

    public void say(String s) throws IOException {
        mainSection.write(s);
        refresh();
    }
    
    public void refresh() throws IOException {
        screen.clear();
        drawBorders();
        mainSection.printContents();
        statsSection.printContents();
        interactiveSection.printContents();
        enemySection.printContents();
        screen.refresh();
    }

    public void askContinue(String continueMessage) throws IOException {
        interactiveSection.setAnyKeyMode(terminal);
        screen.refresh();
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

    private void drawBorders() {
        // Draw vertical border between mainScreen and statsScreen
        for (int i = 0; i < mainSection.height + interactiveSection.height; i++) {
            textGraphics.putString(mainSection.width, i, "|");
        }

        // Draw horizontal border between mainSection and otherScreen1, as well as statsSection and otherScreen2
        for (int i = 0; i < (mainSection.width + enemySection.width); i++) {
            textGraphics.putString(i, mainSection.height, "-");
        }
    }

    public void writeText(ConsoleSection section, String text) throws IOException {
        mainSection.write(text);
    }
       


    // ... other methods ...
}