
package math.tower.defense;

import java.awt.Color;
import java.awt.Graphics;


public class NormalEnemy extends Enemy {

    public NormalEnemy(int x, int y, int health, int speed) {
        super(x, y, health, speed);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x, y, 30, 30);
        
        // Can Barı
        g.setColor(Color.BLACK);
        g.drawRect(x, y-5, 30, 4);
        g.setColor(Color.GREEN);
        int barWidth = (int)(((double)health/maxHealth) * 30);
        g.fillRect(x, y-5, barWidth, 4);
    }
    
}
