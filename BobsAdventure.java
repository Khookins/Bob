import java.io.IOException;
import java.util.Objects;
import bobpack.Character;
import bobpack.Weapon;
import bobpack.console.CustomConsole;

public class BobsAdventure {
    private static CustomConsole cc;
    
    public static void main(String[] args) throws IOException
    {
        //Make a new CustomConsole object, so we can use it to control our game
        cc = new CustomConsole();
        boolean isDead = false;


        //The CustomConsole has two important methods: askOptions and askText
        //Both of these methods will return what the user has chosen. For example, look at the characterName variable below:
        String characterName = "";
        int askedAmount = 0;
        while(characterName == "" && isDead == false)
        {
            characterName = cc.askText("What is your name?");
            if (characterName == "" && askedAmount == 0) {
                cc.say("ENTER A REAL NAME MAN OR THERE WILL BE TROUBLE YOU WILL BE KILLED BY SKELETONS");
                askedAmount += 1;
                continue;
            }
            if (characterName == "" && askedAmount == 1) {
                cc.say("come on man, i told you what will happen. last chance");
                askedAmount += 1;
                continue;
            }
            if (characterName == "" && askedAmount == 2) {
                cc.say("...");
                askedAmount += 1;
                continue;
            }
            if (characterName == "" && askedAmount == 3) {
                cc.say("please stop. its for your own good");
                askedAmount += 1;
                continue;
            }
            if (characterName == "" && askedAmount == 4) {
                cc.say("alright. end of the line buddy");
                cc.say("YOU WERE KILLED BY SKELETONS. I DONT KNOW WHAT YOU THOUGHT WOULD HAPPEN");
                cc.askContinue();
                isDead = true;
                continue;
            }
        }
        
        if(isDead == true){ return; }


        cc.say("Hello " + characterName + "!");
        cc.askContinue("Press any key to continue");

        //cc.askOptions("Where do you want to go?")


        Character player = new Character(characterName, 100, 100, 100, 100, cc);
        
        cc.say("You are a mighty warrior, " + player.name + "!");
        cc.say("You have " + player.health + " health, " + player.energy + " energy, " + player.mana + " mana, and "
                + player.rangedAmmo + " ranged ammo.");
        cc.say("You awake in a dark forest, with no memory of how you got there.");
        cc.say("you look at the ground next to you, and see a sword.");
        cc.askContinue();

        
        String answer = "?";        
        do{
            answer = cc.askOptions("Do you pick up the sword?", new String[] { "Yes", "No" });
            switch (answer) {
                case "Yes":
                    cc.say("You pick up the sword.");
                    player.addItem(new Weapon("Rusty Floor Sword", 10, 2));
                    cc.say("Might as well equip it while we are here!");
                    player.equip("weapon",player.inventory.get("Rusty Floor Sword"));
                    break;
                case "No":
                    cc.say("You leave the sword on the ground.");
                    cc.say("...");
                    cc.say("Not sure why you would want to do that, but whatever");
                    break;
            }
        }while(answer.equals("?"));
        
        cc.askContinue("You ready to continue?");

        cc.say("You continue on your journey, and find a cave.");
        cc.say("You enter the cave, and find a chest.");
        cc.say("You open the chest slowly, and BAM - out jumps a skeleton!");

        Character skeleton = new Character("Skeleton", 30, 30, 0, 0, cc);
        skeleton.addItem(new Weapon("Bone Hand", 5, 0));
        skeleton.equip("weapon", skeleton.inventory.get("Bone Hand"));
        
        cc.askContinue("The skeleton attacks you!");
        cc.say("but you jump back, and it misses...");
        cc.say("Lucky you! but what are you going to do now?");

        while (skeleton.health > 0)
        {
            answer = "?";
            do {
                answer = cc.askOptions("Do you attack the skeleton?", new String[] { "Yes", "No" });
                switch (answer) {
                    case "Yes":
                        cc.say("You attack the skeleton!");
                        player.attack(skeleton);
                        break;
                    case "No":
                        cc.say("You arent going to attack the skeleton. Kill me, o bony one");
                        skeleton.attack(player);
                        break;
                }
            } while (answer.equals("?"));
            if (answer == "No") {
                cc.say("ahhh... it doesn't look like you are going to worm your way out of this one!");
                cc.say("Its fight or die baby!");
            }
                
            if (answer.equals("Yes") && player.health == 100)
            {
                cc.say("YEAH! thats how we do it around here!");
                cc.askContinue("wait, what is it doing now?");
                skeleton.attack(player);
                cc.askContinue("OUCH! you forgot they hit back.");
            } else if (answer == "Yes") {
                cc.askContinue("Here we go again...");
                skeleton.attack(player);
                cc.say("Itll take more than that to kill " + player.name + ", coward!");
            }
            
        }

        cc.askContinue("Finally, the skeleton is dead. You rob it for everything it has, and continue on your journey.");

        
        




        





        cc.askContinue("Thats the end of the game. Press any key to exit.");
    }
}
