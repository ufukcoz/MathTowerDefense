package math.tower.defense;

import java.awt.Point;

public abstract class Tower extends GameObject {

    protected int range;
    protected int damage;
    protected int cooldown = 0;
    protected int maxCooldown;

    public Tower(int x, int y, int range, int damage, int maxCooldown) {
        super(x, y);
        this.range = range;
        this.damage = damage;
        this.maxCooldown = maxCooldown;
    }

    public abstract void attack(Enemy e);

    public void update() {
        if (cooldown > 0) {
            cooldown--;
        }
    }

    public Point getLocation() {
        return new Point(x, y);
    }

    public int getRange() {
        return range;
    }
}
