package bobpack.Panels;

import javax.swing.*;

import bobpack.Patterns.Observable;
import bobpack.Patterns.Observer;
import bobpack.Character;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class StatsPanel extends Panel {
    private Map<String, Supplier<String>> statsMap;
    private Map<String, JTextArea> statsTextMap;
    private Character character;

    public StatsPanel() {
        this.panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        statsMap = new HashMap<>();
        statsTextMap = new HashMap<>();
    }

    public void addStat(String statName, Supplier<String> statValueSupplier) {
        JTextArea statArea = new JTextArea(statName + ": " + statValueSupplier.get());
        statArea.setEditable(false);
        statArea.setLineWrap(true);
        statArea.setWrapStyleWord(true);
        statArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, statArea.getPreferredSize().height));
        statsMap.put(statName, statValueSupplier);
        statsTextMap.put(statName, statArea);
        panel.add(statArea);
    }

    public void updateStat(String statName, String statValue) {
        JTextArea statArea = statsTextMap.get(statName);
        if (statArea != null) {
            statArea.setText(statName + ": " + statsMap.get(statName).get());
        }
    }

    public void clearStats() {
        statsMap.clear();
        statsTextMap.clear();
        panel.removeAll();
    }

    public void update(Character character) {
        for (String statName : statsMap.keySet()) {
            updateStat(statName, statsMap.get(statName).get());
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}
