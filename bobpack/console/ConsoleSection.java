package bobpack.console;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.TerminalScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;


public class ConsoleSection {
    public int startX;
    public int startY;
    public int width;
    public int height;
    public static TextGraphics textGraphics;
    public List<String> contents;
    protected TerminalScreen screen;

    public ConsoleSection(TerminalScreen s, TextGraphics tg, int startX, int startY, int width, int height) {
        if (textGraphics == null) {
            textGraphics = tg;
        }
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.contents = new ArrayList<String>();
        this.screen = s;
        }

    public void clear() throws IOException {
        contents.clear();
        textGraphics.fillRectangle(new TerminalPosition(startX, startY), new TerminalSize(width, height), ' ');
        screen.refresh();
    }

    public void drawString(int x, int y, String s) {
        textGraphics.putString(startX + x, startY + y, s);
    }

    public void write(String input) throws IOException {
        for (String line : input.split("\n")) {
            contents.add(line);
        }
        printContents();
    }

    public void printContents() throws IOException {
        int currentY = startY;

        List<String> lines = new ArrayList<>();

        for (String line : this.contents) {
            List<String> words = Arrays.asList(line.split(" "));
            StringBuilder wrappedLine = new StringBuilder();
            for (String word : words) {
                if (wrappedLine.length() + word.length() + 1 > this.width) {
                    if (word.length() > this.width) {
                        int start = 0;
                        while (start < word.length()) {
                            int end = Math.min(start + this.width, word.length());
                            lines.add(word.substring(start, end));
                            start += this.width;
                        }
                    } else {
                        lines.add(wrappedLine.toString());
                        wrappedLine.setLength(0);
                    }
                }
                if (wrappedLine.length() > 0) {
                    wrappedLine.append(" ");
                }
                wrappedLine.append(word);
            }
            lines.add(wrappedLine.toString());
        }

        // Remove the oldest messages if the number of lines exceeds the height of the console     
        if (lines.size() > this.height) {
            lines = lines.subList(lines.size() - this.height, lines.size());
        }

        // Write the lines within the console boundaries
        for (String line : lines) {
            textGraphics.putString(startX, currentY, line);
            currentY++;
        }

        screen.refresh();
    }

    
}

