
package math.tower.defense;

import java.util.Random;
import javax.swing.JOptionPane;

public class QuestionFactory {
  private Random rand = new Random();

    public boolean ask(int level) {
        int n1 = 0, n2 = 0, ans = 0;
        String op = "";
        
        // Rastgele işlem seç (0:Topla, 1:Çıkar, 2:Çarp, 3:Böl)
        int islemTuru = rand.nextInt(4);

        // Sayı büyüklüğü levele göre değişir
        int limit = 10 * level; 

        switch(islemTuru) {
            case 0: // Toplama
                n1 = rand.nextInt(limit) + 1; 
                n2 = rand.nextInt(limit) + 1;
                ans = n1 + n2; op = "+"; break;
            case 1: // Çıkarma
                n1 = rand.nextInt(limit) + 5; 
                n2 = rand.nextInt(n1); // Negatif çıkmasın
                ans = n1 - n2; op = "-"; break;
            case 2: // Çarpma (Sayıları küçültelim çok zor olmasın)
                n1 = rand.nextInt(limit/2 + 2) + 1; 
                n2 = rand.nextInt(5) + 1;
                ans = n1 * n2; op = "x"; break;
            case 3: // Bölme (Tam bölünen sayı bulalım)
                n2 = rand.nextInt(limit/2 + 2) + 1; // Bölen
                ans = rand.nextInt(5) + 1;          // Sonuç
                n1 = n2 * ans;                      // Bölünen
                op = "/"; break;
        }

        String input = JOptionPane.showInputDialog(null, 
            n1 + " " + op + " " + n2 + " = ?", "Güvenlik Sorusu", JOptionPane.QUESTION_MESSAGE);

        if(input == null || input.isEmpty()) return false;
        
        try {
            return Integer.parseInt(input.trim()) == ans;
        } catch(Exception e) {
            return false;
        }
    }
}
