/**
 * Modélise une cellule d'une grille de jeu — Monde 3, Niveau 4.
 *
 * Une cellule est caractérisée par trois informations :
 * <ul>
 *   <li>Sa <b>position</b> (ligne / colonne) dans la grille.</li>
 *   <li>Son <b>type</b> : {@link CellType#EMPTY vide},
 *       {@link CellType#WALL mur} ou {@link CellType#TRAP piège}.</li>
 *   <li>La <b>présence ou non d'une pièce</b> à ramasser.</li>
 * </ul>
 *
 * Le type détermine si un personnage peut traverser la cellule :
 * seules les cellules de type {@link CellType#WALL} sont infranchissables.
 * Un piège ({@link CellType#TRAP}) est traversable mais déclenche un effet
 * sur le joueur.
 */
public class Cell {

   

    /**
     * Types possibles d'une cellule.
     */
    public enum CellType {
        /** Case vide — traversable librement. */
        EMPTY,
        /** Mur — infranchissable. */
        WALL,
        /** Piège — traversable, mais fait perdre des vies. */
        TRAP,
        /**
         * Porte verrouillée — infranchissable comme un mur.
         * Monde 3, Niveau 5.
         */
        LOCKED_DOOR
    }

   

    /** Ligne de la cellule dans la grille (index 0-based, immuable). */
    private final int row;

    /** Colonne de la cellule dans la grille (index 0-based, immuable). */
    private final int col;

    /** Type de la cellule. */
    private CellType type;

    /** {@code true} si une pièce se trouve sur cette cellule. */
    private boolean hasCoin;

    

    /**
     * Crée une cellule sans pièce.
     *
     * @param row  Ligne dans la grille (≥ 0).
     * @param col  Colonne dans la grille (≥ 0).
     * @param type Type de la cellule.
     */
    public Cell(int row, int col, CellType type) {
        this(row, col, type, false);
    }

    /**
     * Crée une cellule avec ou sans pièce.
     *
     * @param row     Ligne dans la grille (≥ 0).
     * @param col     Colonne dans la grille (≥ 0).
     * @param type    Type de la cellule.
     * @param hasCoin {@code true} si la cellule contient une pièce.
     */
    public Cell(int row, int col, CellType type, boolean hasCoin) {
        this.row     = row;
        this.col     = col;
        this.type    = type;
        this.hasCoin = hasCoin;
    }

    

    /** @return La ligne de cette cellule. */
    public int getRow() { return row; }

    /** @return La colonne de cette cellule. */
    public int getCol() { return col; }

    /** @return Le type de cette cellule. */
    public CellType getType() { return type; }

    /**
     * Modifie le type de cette cellule.
     * @param type Nouveau type.
     */
    public void setType(CellType type) { this.type = type; }

    /** @return {@code true} si une pièce est présente sur cette cellule. */
    public boolean hasCoin() { return hasCoin; }

    /**
     * Pose ou retire la pièce sur cette cellule.
     * @param hasCoin {@code true} pour poser une pièce, {@code false} pour la retirer.
     */
    public void setHasCoin(boolean hasCoin) { this.hasCoin = hasCoin; }

    /**
     * Indique si un personnage peut entrer dans cette cellule.
     * Les cellules de type {@link CellType#WALL} et {@link CellType#LOCKED_DOOR}
     * sont infranchissables — aucune modification du code de collision nécéssaire.
     *
     * @return {@code true} si la cellule est traversable.
     */
    public boolean isWalkable() {
        return type != CellType.WALL && type != CellType.LOCKED_DOOR;
    }

   

    /**
     * Retourne le caractère représentant cette cellule à l'affichage.
     * Le type de la cellule détermine le caractère de base :
     * <ul>
     *   <li>{@link CellType#WALL} : {@code '#'}.</ li>
     *   <li>{@link CellType#TRAP} : {@code 'X'}.</ li>
     *   <li>{@link CellType#LOCKED_DOOR} : {@code 'D'}.</ li>
     *   <li>{@link CellType#EMPTY} : {@code ' '}.</ li>
     * </ul>
     * Si une pièce est présente, elle est représentée par {@code 'o'} et   
     * superpose le caractère de base (même sur un piège ou une porte verrouillée).
     * Notez que le caractère de la cellule ne dépend pas de la présence du joueur ;
     * Le token joueur ({@code '1'}) n'est pas géré ici ; c'est
     * {@link Level#display()} qui le superpose à la position du joueur.
     *
     * @return Le caractère d'affichage de cette cellule.
     */
    public char toChar() {
        switch (type) {
            case WALL:        return Level.WALL;
            case TRAP:        return Level.TRAP;
            case LOCKED_DOOR: return Level.DOOR;
            default:          return hasCoin ? Level.COIN : Level.EMPTY;
        }
    }

    /** @return Description textuelle : {@code Cell(row, col, TYPE[, pièce])}. */
    @Override
    public String toString() {
        String s = "Cell(" + row + ", " + col + ", " + type;
        if (hasCoin) s += ", pièce";
        return s + ")";
    }
}