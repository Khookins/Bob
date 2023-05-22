package bobpack.Panels;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import bobpack.Style.Colours;
import bobpack.Style.Styles;

import javax.swing.text.Element;

import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConsolePanel extends Panel {
    private JEditorPane editorPane;
    private JScrollPane scrollPane;

    private ControlPanel controlPanel;

    private BlockingQueue<Message> messageQueue;
    private Thread messageProcessor;

    private class Message {
        String text;
        int delay;

        Message(String text, int delay) {
            this.text = text;
            this.delay = delay;
        }
    }

    private class MessageProcessor implements Runnable {
        @Override
        public void run() {
            try {
                while(!Thread.currentThread().isInterrupted()) {
                    Message message = messageQueue.take();
                    Thread.sleep(message.delay);

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                    public void run() {
                        HTMLDocument doc = (HTMLDocument) editorPane.getDocument();
                        Element bodyElement = doc.getRootElements()[0].getElement(1);
                        String text = message.text;
                        try {
                            doc.insertBeforeEnd(bodyElement, text);
                            editorPane.setCaretPosition(editorPane.getDocument().getLength());
                        } catch (BadLocationException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                    });
                }
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    public ConsolePanel() {
        panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Console"));

        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        editorPane.setEditorKit(new HTMLEditorKit());

        String initialHTML = "<html><head><style>"
                + "body {font-family: monospace; font-size: 12px; margin:0;}"
                + "p {margin: 0; padding: 0;}"
        + "</style></head><body></body></html>";
        
        editorPane.setText(initialHTML);
        editorPane.setCaretColor(editorPane.getBackground());

        scrollPane = new JScrollPane(editorPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        messageQueue = new LinkedBlockingQueue<>();
        messageProcessor = new Thread(new MessageProcessor());
        messageProcessor.start();

    }
    
    public void addControlPanel(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }

    private void addToQueue(String message, int delay) {
        messageQueue.add(new Message(message, delay));
    }
    
    public void say(String message) {
        addToQueue("<p>" + message + "</p>", 50);
    }

    public void say(String message, int delay) {
        addToQueue("<p>" + message + "</p>", 50 + delay);
    }

    public void sayStyled(String message, String styles) {
        addToQueue("<p style=\"" + styles + "\">" + message + "</p>", 50);
    }
    
    public void sayItalic(String message) {
        addToQueue("<p style=\"font-style:italic\">" + message + "</p>", 50);
    }
    
    public void sayBold(String message) {
        addToQueue("<p style=\"font-weight:bold\">" + message + "</p>", 50);
    }

    public void ask(String prompt, Consumer<String> onResponse) {
        sayStyled(prompt, "font-style:italic; color:"+Colours.ASK);
        controlPanel.awaitTextInput(prompt, onResponse);
    }

    public void announce(String message) {
        sayBold(message);
    }

    public void askOptions(String prompt, String[] options, Consumer<String> onResponse) {
        sayItalic(prompt);
        controlPanel.awaitOptionsInput(prompt, options, onResponse);
    }

    public void sayStyled(String message, String styles, int delay) {
        addToQueue("<p style=\"" + styles + "\">" + message + "</p>", 50 + delay);
    }
}
