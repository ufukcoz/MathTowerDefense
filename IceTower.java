
package math.tower.defense;

import java.awt.Color;
import java.awt.Graphics;


public class IceTower extends Tower{
    public IceTower(int x, int y) {
        // x, y, range (menzil), cooldown, damage (hasarı 0 çünkü sadece yavaşlatır)
        super(x, y, 150, 60, 0); 
    }
    
    // EKSİK OLAN METOT BUYDU:
    @Override
    public void attack(Enemy e) {
        // Buz kulesi hasar vermez, yavaşlatır
        e.slowDown();
        
        // Eğer azıcık can da yaksın istersen şu satırı açabilirsin:
        
    }

    @Override
    public void draw(Graphics g) {
        // --- 1. MENZİL DAİRESİ (Görsel) ---
        g.setColor(new Color(0, 255, 255, 50)); // Şeffaf Mavi
        
        int r = range;
        int d = range * 2;
        int cx = x + 17 - r; // Merkezleme hesabı
        int cy = y + 17 - r;
        
        g.fillOval(cx, cy, d, d);
        g.setColor(Color.CYAN);
        g.drawOval(cx, cy, d, d);

        // --- 2. KULE GÖVDESİ ---
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 35, 35);
        
        // --- 3. İKON ---
        g.setColor(Color.WHITE);
        g.drawString("❄️", x+10, y+25);
    }
}
