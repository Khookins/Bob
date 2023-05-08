package bobpack.Game;

import bobpack.*;
import bobpack.Character;
import bobpack.Panels.*;
import bobpack.Style.Styles;

import javax.swing.*;
import java.awt.*;

public class Game {
    private ConsolePanel consolePanel;
    private ControlPanel controlPanel;
    private EnemyPanel enemyPanel;
    private PlayerPanel playerPanel;

    private Character player;


    public Game(Container frame) {
        this.consolePanel = new ConsolePanel();
        this.enemyPanel = new EnemyPanel();
        this.playerPanel = new PlayerPanel();
        this.controlPanel = new ControlPanel();

        frame.add(consolePanel.getPanel());
        frame.add(enemyPanel.getPanel());
        frame.add(playerPanel.getPanel());
        frame.add(controlPanel.getPanel());

        setup();

    }
    
    private void setup() {
        Character.setConsolePanel(this.consolePanel);
        consolePanel.addControlPanel(controlPanel);
        player = new Character(100, 100, 100);

    }
    
    public void start() {
        consolePanel.say("Welcome to the game!");
        consolePanel.ask("What is your name?", name -> afterNameEntered(name, 0));
    }

    private void say(String message) {
        consolePanel.say(message);
    }

    private void say(String message, int delay) {
        consolePanel.say(message, delay);
    }

    private void say(String message, String styles) {
        consolePanel.sayStyled(message, styles);
    }

    private void say(String message, String styles, int delay) {
        consolePanel.sayStyled(message, styles, delay);
    }

    private void announce(String message)
    {
        consolePanel.announce(message);
    }

    private void afterNameEntered(String name, int timesAsked) {
        if (name == null || name.equals("")) {
            if (timesAsked == 0) {
                say("You must enter a name!");
                consolePanel.ask("What is your name?", nextName -> afterNameEntered(nextName, timesAsked + 1));
                return;
            }
            if (timesAsked == 1) {
                say("Nothing is not a name! How does that even sound when you are replying to this??? What is the sound of UNDEFINED");
                consolePanel.ask("What is your name?", nextName -> afterNameEntered(nextName, timesAsked + 1));
                return;
            }
            if (timesAsked == 2) {
                say("I'm not going to ask you again. I know some real tough fellas around these parts.");
                say("You don't want to meet them.");
                say("ESPECIALLY if you dont have a name...");
                say("Real tough fellas hate people who aint got a name...");
                consolePanel.ask("What is your name?", nextName -> afterNameEntered(nextName, timesAsked + 1));
            }
            if (timesAsked == 3) {
                say("Im not going to tell you what is going to happen to you, but i think you can guess.");
                say("If you need a little help - it rhymes with: boo bill be billed by beletons");
                say("Tough one, i know. maybe do some thinking about it while you are being killed by skeletons");
                say("I mean, deciding on what your name is.");
                say("On that note, ");
                consolePanel.ask("What is your name?", nextName -> afterNameEntered(nextName, timesAsked + 1));
            }
            if (timesAsked == 4) {
                say("...");
                say("...");
                say("...");
                announce("YOU WERE KILLED BY SKELETONS");
            }
            return;
        } else {
            say("Hello, " + name + "!");
            player.name = name;
            playerPanel.addStats(player);
            player.addObserver(playerPanel);
            say("You are a mighty Warrior!");
            say("In fact, you should already know this. It seems awfully familiar, doesn't it?");
            say("Shaking off the strange feeling that you've done this before, you look around.");

            say("It seems like you are in a cave.");
            say("Looking around, you see a rusty floor sword");
            consolePanel.askOptions("Pick up the sword?", new String[] { "Yes", "No" },
                    response -> afterSwordPickedUp(response));
        }

    }
    
    private void afterSwordPickedUp(String response)
    {
        if (response.equalsIgnoreCase("Yes")) {
            say("You pick up the sword.");
            player.equip("weapon", new Weapon("Who you callin Rusty?", 10, 2));
        } else {
            say("You leave the sword on the ground.");
            say("...", 1000);
            say("You don't know why you would do that, but okay.",1000);
            say("Maybe you havent had your tetanus shot yet. That thing does look a bit rusty.");
        }

        say("You continue on your journey, and find a chest.");
        player.say("Hell yes! Chests are always good!");
        say("Unless they contain skeletons... But that would be really unfortunate.");
        say("Just to be safe:");
        consolePanel.askOptions("Open the chest?", new String[] { "Yes", "No" },
                nextResponse -> afterChestOpened(nextResponse));

    }
    
    private void afterChestOpened(String response)
    {
        String bonusMessage = "";
        if (response.equalsIgnoreCase("No")) {
            say("HA, you thought you had free will!");
            say("At the end of the day, the only important thing is gold coins, magic hats and skeletons.");
            bonusMessage = "wait, what - skeletons?";
        }

        say("You open the chest...");
        if (!bonusMessage.equals("")) {
            player.say(bonusMessage);
        }

        say("BAM! Out jumps a skeleton!");
        Character skeleton = new Character("skellertone", 50, 100, 0, 0);
        skeleton.equip("weapon", new Weapon("Boneier Hand", 10, 2));
        skeleton.addObserver(enemyPanel);
        enemyPanel.setEnemy(skeleton);

        say("It swipes at you!");
        say("but it misses!");
        say("ahhh...");

        say("Its that time of the day...");
        say("FIGHT TIME!");

        consolePanel.askOptions("Do you fight the skeleton?", new String[] { "Yes", "No" },
                nextResponse -> introSkeletonFight(nextResponse, skeleton, true));
    }
    
    private void introSkeletonFight(String response, Character skeleton, boolean firstTime)
    {
        if (firstTime) {
            String bonusMessage = "";
            if (response.equalsIgnoreCase("No")) {
                say("You run away from the skeleton.");
                say("You successfully make it outside.");
                say("You are free!");
                say("You are safe!");
                say("You are...");
                say("SYKE! AINT NO RUNNING AWAY FROM SKELE S SKELLERTONE");
                say("By an ancient force, you are teleported exactly back to where you were standing before you run away");
                say("You arent going to weasel your way out of this one");
                say("ITS DIE OR FIGHT BABY");
            } else {
                player.say("LETS GOOOOOO");
                player.attack(skeleton);
                player.say("HAHAHAHAHA");
                player.say("You are no match for my weapon, if i have one");
                bonusMessage = "You forgot they hit back.";
            }
            skeleton.attack(player);
            say("OUCH!");
            if (!bonusMessage.equals("")) {
                say(bonusMessage);
            }
        }  
        if (player.health > 0) {
            if (skeleton.health > 0) {
                if(!firstTime) {
                    if (response.equalsIgnoreCase("Yes")) {
                        say("You attack the skeleton.");
                        player.attack(skeleton);
                    } else {
                        player.say("Kill me, O bony one");
                    }
                    skeleton.attack(player);
                }
                consolePanel.askOptions("Do you attack the skeleton?", new String[] { "Yes", "No" },
                        nextResponse -> introSkeletonFight(nextResponse, skeleton, false));
            } else {
                say("YOU WIN");
                afterSkeletonDefeated();
            }
        } else {
            say("YOU DIED");
        }
        

    }
    
    private void afterSkeletonDefeated()
    {
        say("Congratulations. you won the whole game");
    }
    

}
