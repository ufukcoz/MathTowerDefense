
package math.tower.defense;

import java.util.ArrayList;


public class LevelManager {
private DBHelper db = new DBHelper();
    private int currentLevel = 1;

    public ArrayList<Enemy> createWave(int level) {
        ArrayList<Enemy> wave = new ArrayList<>();
        DBHelper.LevelConfig conf = db.getLevelData(level);
        
        for(int i = 0; i < conf.totalEnemies; i++) {
            
            // Düşmanlar arası mesafe
            int startX = -60 * (i + 1); 
            int startY = 100; 

            // --- LEVEL 1 ---
            if (level == 1) {
                // KURAL: NormalEnemy 4 Parametre ister (x, y, can, hız)
                // Level 1 olduğu için canı 60, hızı 2 veriyoruz.
                wave.add(new NormalEnemy(startX, startY, 60, 2));
            }
            
            // --- LEVEL 2 ---
            else if (level == 2) {
                // Her 5 düşmandan 1'i TANK
                if (i % 5 == 0) {
                    // KURAL: TankEnemy 3 Parametre ister (x, y, level)
                    // Level bilgisini gönderiyoruz, o kendi canını hesaplıyor.
                    wave.add(new TankEnemy(startX, startY, level));
                } else {
                    // KURAL: NormalEnemy 4 Parametre ister
                    // Level 2 olduğu için daha güçlü yapıyoruz: Can 80, Hız 3
                    wave.add(new NormalEnemy(startX, startY, 80, 3));
                }
            }
            
            // --- LEVEL 3 ---
            else if (level == 3) {
                // Her 3 düşmandan 1'i TANK
                if (i % 3 == 0) {
                    // TankEnemy -> 3 Parametre
                    wave.add(new TankEnemy(startX, startY, level));
                } else {
                    // NormalEnemy -> 4 Parametre
                    // Çok hızlı ve güçlü: Can 120, Hız 4
                    wave.add(new NormalEnemy(startX, startY, 120, 4));
                }
            }
        }
        
        return wave;
    }
    
    public int getMaxTowers(int level) {
        return db.getLevelData(level).maxTowers;
    }
    
    public int getCurrentLevel() { return currentLevel; }
    
    public void nextLevel() { 
        if (currentLevel < 3) currentLevel++; 
    }
}
