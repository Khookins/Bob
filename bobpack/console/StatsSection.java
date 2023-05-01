package bobpack.console;
import bobpack.Character;

import java.io.IOException;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.TerminalScreen;

import bobpack.Patterns.Observable;
import bobpack.Patterns.Observer;

public class StatsSection extends ConsoleSection implements Observer<Character>{

    public StatsSection(TerminalScreen s, TextGraphics textGraphics, int startX, int startY, int width, int height) {
        super(s, textGraphics, startX, startY, width, height);
    }

    @Override
    public void update(Character character) throws IOException {
        this.contents.clear();
        this.contents.add("Name: " + character.name);
        this.contents.add("HP: " + character.health + "/" + character.maxHealth);
        this.contents.add("MP: " + character.mana + "/" + character.maxMana);
        this.contents.add("XP: " + character.xp + "/" + character.nextXp);
        printContents();
        this.screen.refresh();
    }

    
}
