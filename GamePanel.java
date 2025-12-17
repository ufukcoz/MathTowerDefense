
package math.tower.defense;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;


public class GamePanel extends JPanel implements ActionListener{
 javax.swing.Timer timer; 
    
    // Listeler (Main'in erişimi için public)
    public ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Tower> towers = new ArrayList<>();
    
    // Yol Koordinatları (U şeklinde)
    private Point[] path = { 
        new Point(50, 100), new Point(700, 100), 
        new Point(700, 400), new Point(100, 400), new Point(100, 600) 
    };
    
    // Yardımcılar
    LevelManager levelManager = new LevelManager();
    QuestionFactory questionFactory = new QuestionFactory();
    
    // Oyun Değişkenleri
    private int currentLevel = 1;
    private int baseHealth = 100;
    
    // Limit ve Hak Yönetimi
    private int failedAttempts = 0; // Yanlış cevap sayısı
    
    // Ulti Yetenekleri
    private int slowDurationTimer = 0; // Yavaşlatma süresi sayacı
    private boolean meteorUsed = false;
    private boolean slowUsed = false;

    // Menüye dönüş komutu (Main'den atanır)
    private Runnable onEscPressed;

    public GamePanel(Runnable onEscPressed) {
        this.onEscPressed = onEscPressed;
        this.setLayout(new BorderLayout()); 
        this.setBackground(Color.WHITE);
        this.setFocusable(true); // ESC için odaklanma

        // --- ALT KONTROL PANELİ ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.DARK_GRAY);
        
        JButton btnMeteor = new JButton("☄️ Meteor");
        btnMeteor.setBackground(Color.ORANGE);
        btnMeteor.setFocusable(false);
        btnMeteor.addActionListener(e -> meteorRain());

        JButton btnSlow = new JButton("❄️ Dondur (5sn)");
        btnSlow.setBackground(Color.CYAN);
        btnSlow.setFocusable(false);
        btnSlow.addActionListener(e -> slowWave());
        
        JLabel infoLabel = new JLabel(" [ESC: Menü] ");
        infoLabel.setForeground(Color.WHITE);

        bottomPanel.add(btnMeteor);
        bottomPanel.add(btnSlow);
        bottomPanel.add(infoLabel);
        
        this.add(bottomPanel, BorderLayout.SOUTH);

        // --- MOUSE TIKLAMA (KULE KOYMA) ---
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Sadece oyun alanına (alt panele değil) tıklanırsa
                if (e.getY() > getHeight() - 50) return; 

                int limit = levelManager.getMaxTowers(currentLevel);
                // Kullanılan hak = Başarılı Kuleler + Yanlış Cevaplar
                int usedRights = towers.size() + failedAttempts;

                if (usedRights < limit) {
                    // Yola denk geliyor mu kontrol et
                    if (!isOnRoad(e.getX(), e.getY())) {
                        placeTowerAsync(e.getX(), e.getY()); // THREAD BAŞLAT
                    } else {
                        JOptionPane.showMessageDialog(null, "Yolun üzerine kule dikemezsin!", "Hata", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Kule hakkın bitti! (Yanlış cevaplar da haktan düşer)");
                }
            }
        });

        // --- KLAVYE (ESC TUŞU) ---
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    timer.stop();
                    if (onEscPressed != null) onEscPressed.run();
                }
            }
        });
        
        timer = new javax.swing.Timer(16, this);
    }
    
    // YOL KONTROLÜ
    private boolean isOnRoad(int x, int y) {
        int yolKalinligi = 45; 
        for (int i = 0; i < path.length - 1; i++) {
            Point p1 = path[i];
            Point p2 = path[i+1];
            
            int minX = Math.min(p1.x, p2.x) - yolKalinligi/2;
            int maxX = Math.max(p1.x, p2.x) + yolKalinligi/2;
            int minY = Math.min(p1.y, p2.y) - yolKalinligi/2;
            int maxY = Math.max(p1.y, p2.y) + yolKalinligi/2;

            if (x >= minX && x <= maxX && y >= minY && y <= maxY) return true;
        }
        return false;
    }
    
    // OYUNU DURDURMADAN SORU SORMA (ASENKRON)
    private void placeTowerAsync(int x, int y) {
        new Thread(() -> {
            boolean correct = questionFactory.ask(currentLevel);
            
            // Cevap geldikten sonra arayüzü güncelle
            SwingUtilities.invokeLater(() -> {
                if (correct) {
                    Object[] options = {"Okçu (Tekli)", "Buz (Alan)"};
                    int secim = JOptionPane.showOptionDialog(this, "Hangi Kule?", "Seçim",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                    if (secim == 0) towers.add(new ArcherTower(x - 20, y - 20));
                    else if (secim == 1) towers.add(new IceTower(x - 20, y - 20));
                    
                    repaint();
                } else {
                    failedAttempts++;
                    JOptionPane.showMessageDialog(this, "Yanlış! Hakkın yandı.");
                }
                this.requestFocusInWindow(); // Odak tekrar oyuna dönsün
            });
        }).start();
    }
    
    public void startLevel(int lvl) {
        currentLevel = lvl;
        enemies = levelManager.createWave(lvl);
        towers.clear(); 
        baseHealth = 100;
        failedAttempts = 0; // Hakları sıfırla
        
        meteorUsed = false;
        slowUsed = false;
        slowDurationTimer = 0;
        
        timer.start(); // Oyun burada başlar
        this.requestFocusInWindow(); // ESC için odaklan
    }
    
    // --- ÖZEL GÜÇLER ---
    public void meteorRain() {
        if(meteorUsed) { JOptionPane.showMessageDialog(this, "Zaten kullandın!"); return; }
        
        if(questionFactory.ask(currentLevel)) {
            for(Enemy e : enemies) e.takeDamage(50);
            meteorUsed = true;
        }
        this.requestFocusInWindow();
    }
    
    public void slowWave() {
        if(slowUsed) { JOptionPane.showMessageDialog(this, "Zaten kullandın!"); return; }
        
        if(questionFactory.ask(currentLevel)) {
            for(Enemy e : enemies) e.slowDown();
            slowDurationTimer = 300; // ~5 Saniye (60FPS x 5)
            slowUsed = true;
        }
        this.requestFocusInWindow();
    }

  @Override
    public void actionPerformed(ActionEvent e) {
        
        // --- ADIM 1: HIZLARI NORMALE DÖNDÜR (RESET) ---
        // Her turun başında herkesi "masum" kabul et, hızlarını düzelt.
        if (slowDurationTimer > 0) {
            slowDurationTimer--;
            if (slowDurationTimer == 0) {
                 for(Enemy en : enemies) en.resetSpeed();
            }
            // Ulti varsa resetleme yapmıyoruz, herkes yavaş kalıyor.
        } else {
            // Ulti yoksa herkesi varsayılan hıza çek
            for(Enemy en : enemies) en.resetSpeed();
        }

        // --- ADIM 2: KULELER ETKİLEŞİME GİRSİN (YAVAŞLATMA HESABI) ---
        // DİKKAT: Hareket etmeden ÖNCE kulelere "Vur" diyoruz.
        // Böylece buz kulesi menzilindekiler hareket etmeden önce yavaşlıyor.
        for(Tower t : towers) {
            t.update(); 
            
            // --- BUZ KULESİ (AURA ETKİSİ) ---
            if (t instanceof IceTower) {
                // Cooldown beklemeden sürekli kontrol
                for(Enemy en : enemies) {
                    double dist = Math.sqrt(Math.pow(t.getX() - en.getX(), 2) + Math.pow(t.getY() - en.getY(), 2));
                    
                    if(!en.isDead() && dist < t.getRange()) {
                        t.attack(en); // Hızı düşür (Örn: 4 -> 2.4)
                    }
                }
            } 
            
            // --- OKÇU KULESİ (HASAR) ---
            else {
                if (t.cooldown <= 0) {
                    for(Enemy en : enemies) {
                        double dist = Math.sqrt(Math.pow(t.getX() - en.getX(), 2) + Math.pow(t.getY() - en.getY(), 2));
                        if(!en.isDead() && dist < t.getRange()) { 
                            t.attack(en); 
                            t.cooldown = t.maxCooldown;
                            break; 
                        }
                    }
                }
            }
        }
        
        // --- ADIM 3: DÜŞMANLARI HAREKET ETTİR ---
        // Artık kimin yavaş kimin hızlı olduğu belli. Şimdi yürütebiliriz.
        for(Enemy en : enemies) {
            if(en.getCurrentPathIndex() < path.length) {
                Point target = path[en.getCurrentPathIndex()]; 
                
                if(Math.abs(en.getX() - target.x) < 5 && Math.abs(en.getY() - target.y) < 5) {
                    en.incrementPathIndex();
                } else {
                    en.moveTowards(target); // Buradaki hız artık güncel (yavaşsa yavaş)
                }
            } else {
                baseHealth -= 10; 
                en.takeDamage(9999);
            }
        }
        
        // --- TEMİZLİK VE BİTİŞ ---
        enemies.removeIf(Enemy::isDead);
        
        if(baseHealth <= 0) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "KAYBETTİN! Canın bitti.");
            if(onEscPressed != null) onEscPressed.run();
        } else if(enemies.isEmpty() && baseHealth > 0) {
            timer.stop();
            if(currentLevel < 3) {
                JOptionPane.showMessageDialog(this, "LEVEL " + currentLevel + " TAMAMLANDI!");
                startLevel(currentLevel + 1);
            } else {
                JOptionPane.showMessageDialog(this, "TEBRİKLER! OYUN BİTTİ.");
                if(onEscPressed != null) onEscPressed.run();
            }
        }
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Yol Çizimi
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(40));
        g.setColor(new Color(220, 220, 220));
        if (path.length > 0) for(int i=0; i<path.length-1; i++) g.drawLine(path[i].x, path[i].y, path[i+1].x, path[i+1].y);
        g2.setStroke(new BasicStroke(1));

        // Kale
        Point kale = path[path.length-1];
        g.setColor(new Color(100, 60, 20)); g.fillRect(kale.x-30, kale.y-30, 60, 50);
        g.setColor(Color.BLACK); g.fillArc(kale.x-20, kale.y-10, 40, 40, 0, 180);

        for(Tower t : towers) t.draw(g);
        for(Enemy en : enemies) en.draw(g);
        
        // Üst Bilgi Barı
        g.setColor(new Color(0, 0, 0, 150)); g.fillRect(0, 0, getWidth(), 40);
        g.setColor(Color.WHITE); g.setFont(new Font("Arial", Font.BOLD, 18));
        
        g.drawString("Level: " + currentLevel, 20, 25);
        
        int limit = levelManager.getMaxTowers(currentLevel);
        int used = towers.size() + failedAttempts;
        g.setColor(used >= limit ? Color.RED : Color.GREEN);
        g.drawString("Kule: " + (towers.size()) + "/" + limit, 120, 25);
        
        g.setColor(Color.WHITE);
        g.drawString("Can: " + baseHealth, 250, 25);
        
        if (slowDurationTimer > 0) {
            g.setColor(Color.CYAN);
            g.drawString("DONDURMA: " + (slowDurationTimer/60 + 1) + "s", 400, 25);
        }
    }
    }

