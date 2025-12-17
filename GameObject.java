
package math.tower.defense;

import java.awt.Graphics;


public  abstract class GameObject {
    protected int x, y;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Her nesne kendini farklı çizer (Polymorphism)
    public abstract void draw(Graphics g);

    public int getX() { return x; }
    public int getY() { return y; }
}

