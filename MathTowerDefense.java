package math.tower.defense;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MathTowerDefense {

    static CardLayout cardLayout = new CardLayout();
    static JPanel mainContainer = new JPanel(cardLayout);
    static GamePanel gamePanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Matematik Kulesi Savunması");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 750);
        frame.setResizable(false);

        // 1. Oyun Paneli (ESC ile menüye döner)
        gamePanel = new GamePanel(() -> cardLayout.show(mainContainer, "MENU"));

        // 2. Menü Paneli (Parametreler güncellendi)
        MainMenuPanel menuScreen = new MainMenuPanel(
            // PLAY BUTONU
            e -> {
                if (gamePanel.enemies.isEmpty()) {
                    gamePanel.startLevel(1);
                } else {
                    gamePanel.timer.start();
                }
                cardLayout.show(mainContainer, "GAME");
                gamePanel.requestFocusInWindow();
            },
            
            // NEW GAME BUTONU
            e -> {
                gamePanel.startLevel(1); // Her zaman 1'den başlat
                cardLayout.show(mainContainer, "GAME");
                gamePanel.requestFocusInWindow();
            },
            
            // EXIT BUTONU
            e -> System.exit(0),
            
            // YENİ EKLENEN: LEVEL SEÇİMİ BUTONU
            // Menüden gelen "seçilenLevel" sayısını alıp oyunu başlatıyoruz
            (selectedLevel) -> {
                gamePanel.startLevel(selectedLevel); // Seçilen levelı başlat
                cardLayout.show(mainContainer, "GAME"); // Ekrana geç
                gamePanel.requestFocusInWindow();
            }
        );

        mainContainer.add(menuScreen, "MENU");
        mainContainer.add(gamePanel, "GAME");

        frame.add(mainContainer);
        cardLayout.show(mainContainer, "MENU");

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
