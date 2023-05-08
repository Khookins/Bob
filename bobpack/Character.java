package bobpack;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.lanterna.TextColor.ANSI;

import bobpack.Panels.ConsolePanel;
import bobpack.Patterns.Observable;
import bobpack.Style.Styles;

public class Character extends Observable{
    public String name;
    public int health;
    public int maxHealth;
    public int energy;
    public int mana;
    public int maxMana;
    public int xp = 0;
    public int nextXp = 100;
    public int rangedAmmo;
    public Map<String, Item> inventory;
    public Map<String, Item> equipment;
    public ArrayList<StatusEffect> statusEffects;
    private static ConsolePanel consolePanel;

    
    class Stats {
        int strength;
        float agility;
    }

    class Equipment {
        Weapon weapon;
        ArrayList<Armor> armor;
    }

    public Character(int health, int energy, int mana)
    {
        this.health = health;
        this.energy = energy;
        this.mana = mana;
        this.inventory = new HashMap<String, Item>();
        this.equipment = new HashMap<String, Item>();
        equipment.put("weapon", new Weapon("fists", 1, 1));
    }



    public Character(String name, int health, int energy, int mana, int rangedAmmo) {
        this.name = name;
        this.maxHealth = health;
        this.health = health;
        this.energy = energy;
        this.mana = mana;
        this.rangedAmmo = rangedAmmo;
        this.inventory = new HashMap<String, Item>();
        this.equipment = new HashMap<String, Item>();
        equipment.put("weapon", new Weapon("fists", 1, 1));
    }
     
    public static void setConsolePanel(ConsolePanel consolePanel) {
        Character.consolePanel = consolePanel;
    }

    public void say(String message) {
        consolePanel.say(this.name + ": " + message);
    }

    public void applyStatusEffects() {
        for (StatusEffect effect : statusEffects) {
            effect.apply(this);
        }
    }
    
    public void addItem(Item item) {
        inventory.put(item.name, item);
        consolePanel.say(this.name + " adds " + item.name + " to their inventory !");
        consolePanel.say("Their inventory is now: " + showInventory());
    }

    public String showInventory() {
        String inventoryString = "";
        for (Map.Entry<String, Item> entry : inventory.entrySet()) {
            inventoryString += entry.getKey() + ", ";
        }
        return inventoryString;
    }

    public void equip(String key, Item toEquip) {
        equipment.put("weapon", toEquip);
        consolePanel.say(this.name + " equips " + toEquip.name + " in their " + key + " slot!");
    }

    public void attack(Character target) {
        consolePanel.sayStyled(this.name + " attacks " + target.name, Styles.ATTACK);
        if (equipment.containsKey("weapon")) {
            Weapon weapon = (Weapon) equipment.get("weapon");
            if (weapon.energyUse > this.energy) {
                //StatusEffect exhausted = new StatusEffect()
            }else{
                target.takeDamage(weapon.damage);
            }
        }
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        consolePanel.sayStyled(this.name + " takes " + damage + " damage", Styles.DAMAGE);
        notifyObservers();
    }

    public String getSummary() {
        return "Name: " + this.name + "\nHealth: " + this.health + "\nEnergy: " + this.energy + "\nMana: " + this.mana
                + "\nRanged Ammo: " + this.rangedAmmo + "\nInventory: " + this.showInventory() + "\nWeapon: "
                + this.equipment.get("weapon").name;
    }



    public String getName() {
        return name;
    }

    public String getHealth() {
        return "" + health;
    }

    public String getEnergy() {
        return "" + energy;
    }

    public String getMana() {
        return "" + mana;
    }

    public String getRangedAmmo() {
        return "" + rangedAmmo;
    }


}
