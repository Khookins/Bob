package bobpack.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public class ControlPanel extends Panel {
    private JEditorPane inputField;
    private JButton submitButton;
    private JLabel promptLabel;

    private JList<String> optionsList;
    private CardLayout cardLayout;
    private JPanel cards;

    private Consumer<String> onResponse;
    
    public ControlPanel() {
        panel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Control"));

        inputField = new JEditorPane();
        inputField.setContentType("text/plain");
        inputField.setBorder(BorderFactory.createCompoundBorder(
        inputField.getBorder(),
        BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        inputField.setCaretPosition(0);
        promptLabel = new JLabel();

        optionsList = new JList<>();
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        

        cards.add(inputField, "INPUT");
        cards.add(new JScrollPane(optionsList), "OPTIONS");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(promptLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.8;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(cards, gbc);
    }

    public void awaitTextInput(String prompt, Consumer<String> onResponse) {
        this.promptLabel.setText(prompt);

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && onResponse != null) {
                    onResponse.accept(inputField.getText());
                    inputField.removeKeyListener(this);
                } else {
                    System.out.println(onResponse);
                    System.out.println(e.getKeyCode());
                }
            }
        });

        cardLayout.show(cards, "INPUT");
        inputField.requestFocusInWindow();
        
        panel.revalidate();
        panel.repaint();
    }

    public void awaitOptionsInput(String prompt, String[] options, Consumer<String> onResponse) {
        this.promptLabel.setText(prompt);

        optionsList.setListData(options);
        optionsList.setSelectedIndex(0);
        

        optionsList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && onResponse != null) {
                    onResponse.accept(optionsList.getSelectedValue());
                    optionsList.removeKeyListener(this);
                } else {
                    System.out.println(onResponse);
                    System.out.println(e.getKeyCode());
                }
            }
        });

        cardLayout.show(cards, "OPTIONS");
        optionsList.requestFocusInWindow();
        panel.revalidate();
        panel.repaint();

        


    }


}
