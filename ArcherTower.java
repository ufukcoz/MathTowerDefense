
package math.tower.defense;

import java.awt.Color;
import java.awt.Graphics;


public class ArcherTower extends Tower {
    public ArcherTower(int x, int y) {
        // Menzil: 150, Hasar: 20, Hız: 30 tick
        super(x, y, 150, 20, 30);
    }

    @Override
    public void attack(Enemy e) {
        if (cooldown <= 0) {
            e.takeDamage(this.damage);
            cooldown = maxCooldown; 
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 40, 40);
        g.setColor(new Color(0, 0, 255, 50)); // Şeffaf menzil
        g.fillOval(x + 20 - range, y + 20 - range, range * 2, range * 2);
    }
}
