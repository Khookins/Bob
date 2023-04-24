package bobpack;

public class Weapon extends Item{
    public int damage;
    public int energyUse;

    public Weapon(String name, int damage, int energyUse) {
        super(name);
        this.damage = damage;
        this.energyUse = energyUse;
    }

    
}
