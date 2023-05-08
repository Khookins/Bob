import java.io.IOException;
import java.util.Objects;
import bobpack.Character;
import bobpack.Weapon;
import bobpack.Panels.*;
import bobpack.Game.*;

import javax.swing.*;
import java.awt.*;

public class BobsAdventure {
    private static Game game;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Bob's Adventure");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
        frame.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new GridLayout(2,2));

        game = new Game(contentPane);
        game.start();

        frame.setVisible(true);


        
        
    }
    
    
}
