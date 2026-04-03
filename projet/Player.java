/**
 * Classe représentant un joueur dans un jeu.
 * Le nom du joueur ne peut pas être changé après la création de l'objet.
 * Le score du joueur peut être modifié en ajoutant ou en retirant des points.
 * Le score ne peut pas devenir négatif.
 */
public class Player {
    public static final int INITIAL_LIVES = 5; 
    private int lives;
    private final String name;
    private int score;
    private static int playerCount = 0;
    
    public Player() {
        this(null, 0);
    }

    public Player(String name) {
    this(name, 0);
}
    
    public Player(String name, int score) {
        playerCount++;
        if (name == null) {
            this.name = "Joueur" + playerCount;
        } else {
            this.name = name;
        }
        this.score = score;
        this.lives = INITIAL_LIVES;
    }
    
    /**
     * Retourne une représentation textuelle de l'objet Player.
     * Format: nom : score pt(s)
     * Le "s" de pts est ajouté seulement si le score est > 1
     */
    public String toString() {
        // Gérer l'accord de pt/pts selon le score
        if (this.score <= 1) {
            return this.name + " : " + this.score + " pt";
        } else {
            return this.name + " : " + this.score + " pts";
        }
    }
    
    /**
     * Ajoute des points au score du joueur.
     * @param points Le nombre de points à ajouter.
     */    
    public void addPoints(int points) {
        if (points <= 0) {
            return;
        }
        this.score += points;
    }
    
    /**
     * Retire des points du score du joueur.
     * @param points Le nombre de points à retirer.
     */ 
    public void removePoints(int points) {
        if (this.score == 0) {
            return; 
        }
        if (points > this.score) {
            this.score = 0;
            return;
        }
        this.score -= points;
    }

     public void removeLives(int amount) {
        if (amount <= 0) return;
        this.lives = (amount > this.lives) ? 0 : this.lives - amount;
    }
 
    /**
     * Remet les vies à INITIAL_LIVES.
     * Appelé par GameController lors d'un redémarrage de partie.
     */
    public void resetLives() {
        this.lives = INITIAL_LIVES;
    }
 
    /**
     * @return true si le joueur a encore au moins une vie.
     */
    public boolean isAlive() {
        return this.lives > 0;
    }
 
    /** @return Le nombre de vies restantes. */
    public int getLives() {
        return this.lives;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Player)) {
            return false;  
        }
        Player other = (Player) obj;
        return this.name.equalsIgnoreCase(other.name);
    }
    
    /**
     * Retourne le nom du joueur.
     *  * @return The name of this player.
     */ 
    public String getName() {
        return this.name;
    }
    
    /**
     * Retourne le score du joueur.
     */ 
    public int getscore() {
        return this.score;
    }
    
    /**
     * Retourne le nombre total de joueurs créés.
     * @return Le nombre de joueurs créés depuis le début du programme
     */
    public static int getPlayerCount() {
        return playerCount;
    }
}