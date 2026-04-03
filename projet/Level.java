/**
 * Représente un niveau de jeu sous forme de grille de {@link Cell}.
 *
 * Monde 3, Niveau 4 — la grille interne {@code char[][]} est remplacée
 * par une grille {@link Cell}{@code [][]}.  Chaque cellule connaît sa
 * position, son type ({@link Cell.CellType}) et la présence éventuelle
 * d'une pièce.
 *
 * Toutes les fonctionnalités précédentes sont conservées :
 * <ul>
 *   <li>Monde 3 Niv.1 : pièces '.' (+{@value #COIN_POINTS} pts, disparaissent),
 *       "NIVEAU TERMINÉ".</li>
 *   <li>Monde 3 Niv.2 : pièges '*' (-{ @value #TRAP_LIVES} vies, détruits),
 *       5 vies, respawn, "GAME OVER".</li>
 * </ul>
 *
 * L'interface publique est <b>inchangée</b> : {@link #getCell(int, int)} et
 * {@link #setCell(int, int, char)} travaillent toujours avec des {@code char}
 * pour la compatibilité avec {@code LevelLoader}, {@code Main}, etc.
 */
public class Level {

    // ------------------------------------------------------------------ //
    //  Constantes de caractères (conservées pour compatibilité)           //
    // ------------------------------------------------------------------ //

    /** Caractère représentant un mur. */
    public static final char WALL   = '#';

    /** Caractère représentant une case vide. */
    public static final char EMPTY  = ' ';

    /** Caractère représentant le joueur affiché sur la grille. */
    public static final char PLAYER = '1';

    /** Caractère représentant une pièce. */
    public static final char COIN   = '.';

    /** Caractère représentant un piège. */
    public static final char TRAP   = '*';

    /** Caractère représentant une porte verrouillée (Monde 3, Niv.5). */
    public static final char DOOR   = 'D';

    /** Points gagnés par pièce ramassée. */
    public static final int  COIN_POINTS = 10;

    /** Vies perdues quand le joueur marche sur un piège. */
    public static final int  TRAP_LIVES  = 2;

    // ------------------------------------------------------------------ //
    //  Attributs                                                           //
    // ------------------------------------------------------------------ //

    /**
     * Grille de cellules — remplace le {@code char[][]}.
     * {@code cells[r][c]} est la cellule à la ligne r, colonne c.
     */
    private Cell[][] cells;    // ← NOUVEAU (remplace char[][] grid)

    /** Nombre de lignes. */
    private int rows;

    /** Nombre de colonnes. */
    private int cols;

    /** Le joueur placé dans ce niveau. */
    private Player player;

    /** Ligne courante du joueur (-1 si absent). */
    private int playerRow;

    /** Colonne courante du joueur (-1 si absent). */
    private int playerCol;

    /** Ligne de départ du joueur — utilisée pour le respawn après un piège. */
    private int startRow;

    /** Colonne de départ du joueur — utilisée pour le respawn après un piège. */
    private int startCol;

    /** Nombre de pièces encore présentes dans le niveau. */
    private int remainingCoins;

    /** {@code true} quand le joueur n'a plus de vies (GAME OVER). */
    private boolean gameOver;

    /** Nombre total d'instances de Level créées. */
    private static int levelCount = 0;

    // ------------------------------------------------------------------ //
    //  Constructeurs                                                       //
    // ------------------------------------------------------------------ //

    /**
     * Crée un niveau à partir d'un tableau de caractères existant.
     * Chaque caractère est converti en {@link Cell}.
     *
     * @param grid Tableau 2D de caractères ('#', ' ', '.', '*').
     */
    public Level(char[][] grid) {
        levelCount++;
        this.rows = grid.length;
        this.cols = (rows > 0) ? grid[0].length : 0;
        this.cells = new Cell[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = charToCell(r, c, grid[r][c]);
            }
        }

        this.player    = null;
        this.playerRow = -1;
        this.playerCol = -1;
        this.startRow  = -1;
        this.startCol  = -1;
        this.gameOver  = false;
        this.remainingCoins = countCoinsInGrid();
    }

    /**
     * Crée un niveau vide (toutes les cellules sont EMPTY).
     *
     * @param rows Nombre de lignes.
     * @param cols Nombre de colonnes.
     */
    public Level(int rows, int cols) {
        levelCount++;
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new Cell(r, c, Cell.CellType.EMPTY);
            }
        }

        this.player         = null;
        this.playerRow      = -1;
        this.playerCol      = -1;
        this.startRow       = -1;
        this.startCol       = -1;
        this.remainingCoins = 0;
        this.gameOver       = false;
    }

    /**
     * Crée un niveau avec une bordure optionnelle de murs.
     *
     * @param rows       Nombre de lignes.
     * @param cols       Nombre de colonnes.
     * @param withBorder Si {@code true}, les cases du bord sont des murs.
     */
    public Level(int rows, int cols, boolean withBorder) {
        this(rows, cols);

        if (withBorder) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (r == 0 || r == rows - 1 || c == 0 || c == cols - 1) {
                        cells[r][c].setType(Cell.CellType.WALL);
                    }
                }
            }
        }
    }

    // ------------------------------------------------------------------ //
    //  Helpers privés                                                      //
    // ------------------------------------------------------------------ //

    /**
     * Convertit un caractère en objet {@link Cell}.
     *
     * @param r  Ligne.
     * @param c  Colonne.
     * @param ch Caractère source ('#', ' ', '.', '*').
     * @return   La cellule correspondante.
     */
    private Cell charToCell(int r, int c, char ch) {
        switch (ch) {
            case WALL: return new Cell(r, c, Cell.CellType.WALL);
            case TRAP: return new Cell(r, c, Cell.CellType.TRAP);
            case DOOR: return new Cell(r, c, Cell.CellType.LOCKED_DOOR);
            case COIN: return new Cell(r, c, Cell.CellType.EMPTY, true); // vide + pièce
            default:   return new Cell(r, c, Cell.CellType.EMPTY);
        }
    }

    /** Compte les pièces présentes dans la grille de cellules. */
    private int countCoinsInGrid() {
        int count = 0;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (cells[r][c].hasCoin()) count++;
        return count;
    }

    /**
     * Collecte la pièce présente sur la cellule :
     * ajoute les points, décrémente le compteur, affiche un message,
     * et affiche "NIVEAU TERMINÉ" quand la dernière pièce est prise.
     *
     * @param p Le joueur qui collecte.
     */
    private void collectCoin(Player p) {
        p.addPoints(COIN_POINTS);
        remainingCoins--;
        System.out.println("Pièce ramassée ! +" + COIN_POINTS
                + " pts  (score : " + p.getscore() + " pts"
                + "  |  pièces restantes : " + remainingCoins + ")");
        if (remainingCoins == 0) {
            System.out.println("*** NIVEAU TERMINÉ ***");
        }
    }

    /**
     * Déclenche le piège à la position (trapRow, trapCol) :
     * <ol>
     *   <li>Détruit le piège (type → EMPTY).</li>
     *   <li>Retire {@value #TRAP_LIVES} vies au joueur.</li>
     *   <li>Si encore en vie → respawn à la position de départ.</li>
     *   <li>Sinon → GAME OVER.</li>
     * </ol>
     */
    private void triggerTrap(int trapRow, int trapCol) {
        // 1. Détruire le piège
        cells[trapRow][trapCol].setType(Cell.CellType.EMPTY);

        // 2. Retirer des vies
        player.removeLives(TRAP_LIVES);
        System.out.println("!!! PIÈGE en (" + trapRow + ", " + trapCol + ") !"
                + "  -" + TRAP_LIVES + " vies"
                + "  (vies restantes : " + player.getLives() + ")");

        // 3. Respawn ou game over
        if (!player.isAlive()) {
            playerRow = -1;
            playerCol = -1;
            gameOver  = true;
            System.out.println("*** GAME OVER ***");
        } else {
            playerRow = startRow;
            playerCol = startCol;
            System.out.println("Retour à la position de départ ("
                    + startRow + ", " + startCol + ").");
        }
    }

    // ------------------------------------------------------------------ //
    //  setPlayer                                                           //
    // ------------------------------------------------------------------ //

    /**
     * Place le joueur dans le niveau à la position (row, col).
     * Le premier appel enregistre cette position comme point de départ
     * pour le respawn.
     *
     * @param player Le joueur à placer.
     * @param row    Ligne cible.
     * @param col    Colonne cible.
     * @throws IllegalArgumentException       si le joueur est {@code null}.
     * @throws ArrayIndexOutOfBoundsException si la position est hors grille.
     * @throws IllegalStateException          si la cellule cible est un mur.
     */
    public void setPlayer(Player player, int row, int col) {
        if (player == null) {
            throw new IllegalArgumentException(
                "Impossible de placer un joueur null dans le niveau.");
        }
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new ArrayIndexOutOfBoundsException(
                "Position (" + row + ", " + col + ") hors des limites de la grille "
                + rows + "x" + cols + ".");
        }
        if (cells[row][col].getType() == Cell.CellType.WALL) {
            throw new IllegalStateException(
                "Impossible de placer le joueur sur un mur en ("
                + row + ", " + col + ").");
        }

        // Enregistrer la position de départ au premier appel
        if (this.startRow == -1) {
            this.startRow = row;
            this.startCol = col;
        }

        // Collecter la pièce si la cellule de départ en contient une
        if (cells[row][col].hasCoin()) {
            cells[row][col].setHasCoin(false);
            collectCoin(player);
        }

        this.player    = player;
        this.playerRow = row;
        this.playerCol = col;
        this.gameOver  = false;
    }

    // ------------------------------------------------------------------ //
    //  movePlayer                                                          //
    // ------------------------------------------------------------------ //

    /**
     * Déplace le joueur d'une case dans la direction donnée.
     *
     * <p><b>Monde 3 Niv.6 — comportement torique :</b> la grille est
     * considérée comme un tore.  Sortir par un bord fait réapparaître le
     * joueur sur le bord opposé.  Il n'y a donc plus de blocage
     * "hors de la grille" ; seuls les murs et les portes verrouillées
     * (via {@link Cell#isWalkable()}) bloquent le déplacement.
     *
     * <ul>
     *   <li>Mur / porte verrouillée → le joueur reste en place.</li>
     *   <li>Pièce → collectée (+{@value #COIN_POINTS} pts, disparaît).</li>
     *   <li>Piège → -{@value #TRAP_LIVES} vies, détruit, respawn
     *       (ou GAME OVER si plus de vies).</li>
     * </ul>
     *
     * Le niveau est toujours affiché après l'appel.
     *
     * @param direction Direction du déplacement.
     * @throws IllegalStateException si aucun joueur n'est placé.
     */
    public void movePlayer(Direction direction) {
        if (player == null) {
            throw new IllegalStateException("Aucun joueur n'est place dans ce niveau.");
        }
        if (gameOver) {
            System.out.println("La partie est terminée (GAME OVER).");
            return;
        }

        // Calculer la position cible AVANT application du tore
        int targetRow = playerRow;
        int targetCol = playerCol;

        switch (direction) {
            case UP:    targetRow = playerRow - 1; break;
            case DOWN:  targetRow = playerRow + 1; break;
            case LEFT:  targetCol = playerCol - 1; break;
            case RIGHT: targetCol = playerCol + 1; break;
        }

        // ---- Monde 3 Niv.6 : comportement torique ----
        // Le modulo Java peut retourner un résultat négatif (ex. -1 % 7 = -1).
        // On ajoute rows/cols avant le modulo pour garantir un résultat positif.
        targetRow = ((targetRow % rows) + rows) % rows;
        targetCol = ((targetCol % cols) + cols) % cols;

        // Vérifier si la cellule cible est franchissable (mur ou porte)
        if (!cells[targetRow][targetCol].isWalkable()) {
            System.out.println("Deplacement " + direction + " bloque : mur en ("
                    + targetRow + ", " + targetCol + ").");

        } else {
            // Lire le contenu AVANT de déplacer le joueur
            Cell target     = cells[targetRow][targetCol];
            boolean hasCoin = target.hasCoin();
            boolean isTrap  = (target.getType() == Cell.CellType.TRAP);

            // Déplacer le joueur
            playerRow = targetRow;
            playerCol = targetCol;
            System.out.println("Deplacement " + direction
                    + " -> joueur en (" + playerRow + ", " + playerCol + ").");

            // Réagir au contenu de la cellule
            if (hasCoin) {
                target.setHasCoin(false);   // la pièce disparaît
                collectCoin(player);
            } else if (isTrap) {
                triggerTrap(targetRow, targetCol);
            }
        }

        // Toujours afficher le niveau après la tentative de déplacement
        display();
    }

    // ------------------------------------------------------------------ //
    //  display                                                             //
    // ------------------------------------------------------------------ //

    /**
     * Affiche la grille sur la sortie standard.
     * Le token joueur ('1') est superposé à sa position courante.
     */
    public void display() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r == playerRow && c == playerCol) {
                    System.out.print(PLAYER);
                } else {
                    System.out.print(cells[r][c].toChar());
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    // ------------------------------------------------------------------ //
    //  Accesseurs — interface publique inchangée                          //
    // ------------------------------------------------------------------ //

    /** @return Le nombre de lignes. */
    public int getRows() { return rows; }

    /** @return Le nombre de colonnes. */
    public int getCols() { return cols; }

    /**
     * Retourne le caractère représentant la cellule (row, col).
     * Compatible avec l'interface char-based des versions précédentes.
     *
     * @param row Ligne.
     * @param col Colonne.
     * @return Caractère d'affichage.
     */
    public char getCell(int row, int col) {
        if (row == playerRow && col == playerCol) return PLAYER;
        return cells[row][col].toChar();
    }

    /**
     * Modifie la cellule (row, col) à partir d'un caractère.
     * Compatible avec l'interface char-based des versions précédentes.
     * Met à jour le compteur de pièces si nécessaire.
     *
     * @param row  Ligne.
     * @param col  Colonne.
     * @param ch   Nouveau caractère ('#', ' ', '.', '*').
     */
    public void setCell(int row, int col, char ch) {
        boolean hadCoin  = cells[row][col].hasCoin();
        boolean willHave = (ch == COIN);
        if (hadCoin && !willHave) remainingCoins--;
        else if (!hadCoin && willHave) remainingCoins++;
        cells[row][col] = charToCell(row, col, ch);
    }

    /**
     * Retourne directement l'objet {@link Cell} à la position (row, col).
     * Méthode supplémentaire offerte par Monde 3 Niv.4 pour accéder au
     * modèle objet.
     *
     * @param row Ligne.
     * @param col Colonne.
     * @return La cellule à cette position.
     */
    public Cell getCellObject(int row, int col) { return cells[row][col]; }

    /** @return Le joueur placé dans ce niveau, ou {@code null}. */
    public Player getPlayer() { return player; }

    /** @return La ligne courante du joueur, ou -1 si absent. */
    public int getPlayerRow() { return playerRow; }

    /** @return La colonne courante du joueur, ou -1 si absent. */
    public int getPlayerCol() { return playerCol; }

    /** @return La ligne de départ du joueur (pour le respawn). */
    public int getStartRow() { return startRow; }

    /** @return La colonne de départ du joueur (pour le respawn). */
    public int getStartCol() { return startCol; }

    /** @return Le nombre de pièces restantes dans le niveau. */
    public int getRemainingCoins() { return remainingCoins; }

    /** @return {@code true} si toutes les pièces ont été ramassées. */
    public boolean isCompleted() { return remainingCoins == 0; }

    /** @return {@code true} si le joueur n'a plus de vies (GAME OVER). */
    public boolean isGameOver() { return gameOver; }

    /** @return Le nombre total d'instances de Level créées. */
    public static int getLevelCount() { return levelCount; }

    // ------------------------------------------------------------------ //
    //  equals / toString                                                   //
    // ------------------------------------------------------------------ //

    /**
     * Deux niveaux sont égaux s'ils ont les mêmes dimensions et le même
     * contenu (comparaison via {@link Cell#toChar()}).
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Level)) return false;
        Level other = (Level) obj;
        if (this.rows != other.rows || this.cols != other.cols) return false;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (this.cells[r][c].toChar() != other.cells[r][c].toChar()) return false;
        return true;
    }

    /**
     * Retourne une représentation textuelle du niveau avec les informations
     * du joueur si présent.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r == playerRow && c == playerCol) {
                    sb.append(PLAYER);
                } else {
                    sb.append(cells[r][c].toChar());
                }
            }
            sb.append('\n');
        }
        if (player != null) {
            sb.append("Joueur : ").append(player.getName())
              .append(" en (").append(playerRow).append(", ").append(playerCol).append(')')
              .append("  |  Score : ").append(player.getscore()).append(" pts")
              .append("  |  Vies : ").append(player.getLives())
              .append("  |  Pièces restantes : ").append(remainingCoins);
        }
        return sb.toString();
    }
}