/**
 * Classe représentant un joueur dans un jeu.
 * Le nom du joueur ne peut pas être changé après la création de l'objet.
 * Le score du joueur peut être modifié en ajoutant ou en retirant des points.
 * Le score ne peut pas devenir négatif.
 */
public class Player {
    private String name;
    private int score;
    private static int playerCount = 0;
    
    public Player() {
        this(null, 0);
    }
    
    public Player(String name, int score) {
        playerCount++;
        if (name == null) {
            this.name = "Joueur" + playerCount;
        } else {
            this.name = name;
        }
        this.score = score;
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
     */ 
    public String getname() {
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
        return PlayerCount;
    }
}