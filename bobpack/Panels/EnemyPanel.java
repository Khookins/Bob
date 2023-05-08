package bobpack.Panels;

import javax.swing.*;
import bobpack.Character;
import bobpack.Patterns.Observer;

import java.awt.*;
import java.io.IOException;
import java.util.function.Supplier;

public class EnemyPanel extends Panel implements Observer<Character>{
    private StatsPanel statsPanel;

    public EnemyPanel() {
        this.statsPanel = new StatsPanel();
        this.panel = statsPanel.getPanel();

        panel.setBorder(BorderFactory.createTitledBorder("Enemy"));

    }

    public void setEnemy(Character enemy) {
        statsPanel.clearStats();
        addStats(enemy);
    }

    public void addStats(Character character)
    {
        statsPanel.addStat("Name", () -> {
            return character.getName();
        });
        statsPanel.addStat("Health", () -> character.getHealth());
    }
    
    @Override
    public void update(Character character) {
        statsPanel.update(character);
    }
    

}
