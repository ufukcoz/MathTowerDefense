
package math.tower.defense;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import javax.swing.*;

public class MainMenuPanel extends JPanel{
    // Constructor artık 4. bir parametre alıyor: levelStarter
    public MainMenuPanel(ActionListener oynaAction, ActionListener yeniOyunAction, ActionListener exitAction, Consumer<Integer> levelStarter) {
        this.setLayout(new GridBagLayout()); 
        this.setBackground(new Color(40, 40, 60)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); 
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; 

        // --- BAŞLIK ---
        JLabel title = new JLabel("KULE SAVUNMA");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        this.add(title, gbc);

        // --- BUTONLAR ---
        
        // 1. PLAY
        JButton btnPlay = createButton("PLAY");
        btnPlay.addActionListener(oynaAction);
        gbc.gridy = 1;
        this.add(btnPlay, gbc);

        // 2. NEW GAME
        JButton btnNewGame = createButton("NEW GAME");
        btnNewGame.addActionListener(yeniOyunAction);
        gbc.gridy = 2;
        this.add(btnNewGame, gbc);

        // 3. LEVELS (DÜZELTİLDİ: Artık seçince başlatıyor)
        JButton btnLevels = createButton("LEVELS");
        btnLevels.addActionListener(e -> {
            String[] levels = {"Level 1", "Level 2", "Level 3"};
            
            // Seçim Penceresi
            int secim = JOptionPane.showOptionDialog(this, 
                    "Hangi Leveldan Başlamak İstersin?", 
                    "Level Seçimi",
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.QUESTION_MESSAGE, 
                    null, 
                    levels, 
                    levels[0]);

            // Eğer bir seçim yapıldıysa (Pencere kapatılmadıysa)
            if(secim != -1) {
                // secim 0 ise Level 1, secim 1 ise Level 2...
                // Main dosyasına "Bu leveli başlat" emri gönderiyoruz:
                levelStarter.accept(secim + 1); 
            }
        });
        gbc.gridy = 3;
        this.add(btnLevels, gbc);

        // 4. EXIT
        JButton btnExit = createButton("EXIT");
        btnExit.setBackground(new Color(200, 50, 50)); 
        btnExit.setForeground(Color.WHITE);
        btnExit.addActionListener(exitAction);
        gbc.gridy = 4;
        this.add(btnExit, gbc);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusable(false);
        btn.setPreferredSize(new Dimension(200, 50));
        return btn;
    }
}
