
package math.tower.defense;

import java.awt.Color;
import java.awt.Graphics;
public class TankEnemy extends Enemy {
public TankEnemy(int x, int y, int level) {
        
        // HESAPLAMA BURAYA TAŞINDI:
        // Eğer Level 3 ise can 400 olsun, değilse (Level 2) 200 olsun.
        // Hız hep 1 (Tank olduğu için)
        super(x, y, (level == 3 ? 400 : 200), 1);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, 35, 35);
        drawHealthBar(g);
    }}
  
 

