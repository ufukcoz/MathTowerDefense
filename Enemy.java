package math.tower.defense;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class Enemy extends GameObject {

    protected int health;
    protected int maxHealth;
    
    // Hız hassas hesaplansın diye double yapıyoruz
    protected double speed;      
    protected double baseSpeed;  // Orijinal hızı (yavaşlatma bitince dönmek için)
    
    protected int currentPathIndex = 0;

    public Enemy(int x, int y, int health, int speed) {
        super(x, y);
        this.health = health;
        this.maxHealth = health;
        this.speed = speed;
        this.baseSpeed = speed; // Doğduğu anki hızını kaydet
    }

    public void moveTowards(Point target) {
        if (x < target.x) x += speed;
        else if (x > target.x) x -= speed;
        
        if (y < target.y) y += speed;
        else if (y > target.y) y -= speed;
    }

    public void takeDamage(int damage) { 
        this.health -= damage; 
    }
    
    public boolean isDead() { 
        return health <= 0; 
    }
    
    // --- YENİ YAVAŞLATMA MANTIĞI ---
    public void slowDown() {
        // Hızı tamamen bitirmek yerine, orijinal hızın %60'ına düşür
        double newSpeed = this.baseSpeed * 0.6;
        
        // Çok aşırı yavaşlamasın (Minimum 1 birim hız)
        if (this.speed > newSpeed && newSpeed >= 1.0) {
            this.speed = newSpeed;
        }
    }
    
    // Yavaşlatma süresi bitince çağrılır
    public void resetSpeed() {
        this.speed = this.baseSpeed;
    }

    // --- GETTER & SETTER ---
    public int getHealth() { return health; }
    public int getCurrentPathIndex() { return currentPathIndex; }
    public void incrementPathIndex() { currentPathIndex++; }
    
    // Can Barı Çizimi
    protected void drawHealthBar(Graphics g) {
        g.setColor(Color.BLACK); 
        g.drawRect(x, y - 8, 30, 4);
        
        g.setColor(Color.GREEN);
        if (maxHealth > 0) {
            int width = (int)(((double)health / maxHealth) * 30);
            g.fillRect(x, y - 8, width, 4);
        }
    }
}
