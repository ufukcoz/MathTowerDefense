
package math.tower.defense;


public class DBHelper {
public static class LevelConfig {
        public int maxTowers;   // Kule Limiti
        public int totalEnemies;// Düşman Sayısı
        public String difficulty;
        
        public LevelConfig(int t, int e, String d) {
            this.maxTowers = t;
            this.totalEnemies = e;
            this.difficulty = d;
        }
    }

    public LevelConfig getLevelData(int level) {
        // --- DENGE AYARLARI ---
        
        if (level == 1) {
            // Level 1: 5 Kule Hakkı, 10 Düşman
            return new LevelConfig(5, 10, "Kolay");
        }
        
        if (level == 2) {
            // Level 2: 8 Kule Hakkı, 20 Düşman
            return new LevelConfig(8, 20, "Orta");
        }
        
        if (level == 3) {
            // Level 3: 12 Kule Hakkı, 30 Düşman (Zor)
            return new LevelConfig(12, 30, "Zor");
        }
        
        // Varsayılan (Hata olursa)
        return new LevelConfig(5, 10, "Kolay");
    }
}
