package bobpack;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.lanterna.TextColor.ANSI;

import bobpack.console.CustomConsole;

public class Character {
    public String name;
    public int health;
    public int energy;
    public int mana;
    public int rangedAmmo;
    public Map<String, Item> inventory;
    public Map<String, Item> equipment;
    public ArrayList<StatusEffect> statusEffects;
    private static CustomConsole cc;

    
    class Stats {
        int strength;
        float agility;
    }

    class Equipment {
        Weapon weapon;
        ArrayList<Armor> armor;
    }



    public Character(String name, int health, int energy, int mana, int rangedAmmo, CustomConsole cc) {
        this.name = name;
        this.health = health;
        this.energy = energy;
        this.mana = mana;
        this.rangedAmmo = rangedAmmo;
        this.inventory = new HashMap<String, Item>();
        this.equipment = new HashMap<String, Item>();
        Character.cc = cc;
     }

    public void applyStatusEffects() {
        for (StatusEffect effect : statusEffects) {
            effect.apply(this);
        }
    }
    
    public void addItem(Item item) throws IOException {
        inventory.put(item.name, item);
        cc.say(this.name + " adds " + item.name + " to their inventory !");
        cc.say("Their inventory is now: " + showInventory());
    }

    public String showInventory() {
        String inventoryString = "";
        for (Map.Entry<String, Item> entry : inventory.entrySet()) {
            inventoryString += entry.getKey() + ", ";
        }
        return inventoryString;
    }

    public void equip(String key, Item toEquip) throws IOException {
        equipment.put("weapon", toEquip);
        cc.say(this.name + " equips " + toEquip.name + " in their " + key + " slot!");
    }

    public void attack(Character target) throws IOException {
        cc.setTextColour(ANSI.RED_BRIGHT);
        cc.say(this.name + " attacks " + target.name + "!");
        if (equipment.containsKey("weapon")) {
            Weapon weapon = (Weapon) equipment.get("weapon");
            if (weapon.energyUse > this.energy) {
                StatusEffect exhausted = new StatusEffect()
            }else{
                target.takeDamage(weapon.damage);
            }
        }
    }

    public void takeDamage(int damage) throws IOException {
        this.health -= damage;
        cc.say(this.name + " takes " + damage + " damage!");
    }
}
