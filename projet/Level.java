/**
 * Represents a game level modeled as a 2D grid of characters.
 * A cell contains either '#' (wall) or ' ' (empty space).
 * A level can hold one Player, placed at a specific position in the grid.
 * The player is represented by the character '1' on the displayed grid.
 */
public class Level {

    /** Character used to represent a wall. */
    public static final char WALL   = '#';

    /** Character used to represent empty space. */
    public static final char EMPTY  = ' ';

    /** Character used to represent the player on the grid. */
    public static final char PLAYER = '1';

    /** The 2D grid of characters that makes up this level. */
    private char[][] grid;

    /** Number of rows in the grid. */
    private int rows;

    /** Number of columns in the grid. */
    private int cols;

    /** The player placed in this level (composition). */
    private Player player;

    /** Row position of the player in the grid (-1 if no player). */
    private int playerRow;

    /** Column position of the player in the grid (-1 if no player). */
    private int playerCol;

    /** Total number of Level instances created. */
    private static int levelCount = 0;



    /**
     * Creates a Level from an existing 2D char array, with no player.
     *
     * @param grid A 2D array of characters ('#' or ' ').
     */
    public Level(char[][] grid) {
        levelCount++;
        this.grid      = grid;
        this.rows      = grid.length;
        this.cols      = (rows > 0) ? grid[0].length : 0;
        this.player    = null;
        this.playerRow = -1;
        this.playerCol = -1;
    }

    /**
     * Creates an empty Level of the given size (all cells are ' '), with no player.
     *
     * @param rows Number of rows.
     * @param cols Number of columns.
     */
    public Level(int rows, int cols) {
        levelCount++;
        this.rows = rows;
        this.cols = cols;
        this.grid = new char[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = EMPTY;
            }
        }

        this.player    = null;
        this.playerRow = -1;
        this.playerCol = -1;
    }

    /**
     * Creates a Level of the given size with an optional border of walls, with no player.
     *
     * @param rows       Number of rows.
     * @param cols       Number of columns.
     * @param withBorder If {@code true}, the border cells are set to '#'.
     */
    public Level(int rows, int cols, boolean withBorder) {
        this(rows, cols);

        if (withBorder) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    boolean isBorder = (r == 0 || r == rows - 1
                                     || c == 0 || c == cols - 1);
                    if (isBorder) {
                        grid[r][c] = WALL;
                    }
                }
            }
        }
    }

    /**
     * Places a player in the level at the given position.
     *
     * @param player The player to place.
     * @param row    Target row.
     * @param col    Target column.
     * @throws IllegalArgumentException       if the player is null.
     * @throws ArrayIndexOutOfBoundsException if the position is outside the grid.
     * @throws IllegalStateException          if the target cell is a wall.
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
        if (grid[row][col] == WALL) {
            throw new IllegalStateException(
                "Impossible de placer le joueur sur un mur en ("
                + row + ", " + col + ").");
        }

        // Erase previous player position if any
        if (this.player != null) {
            grid[playerRow][playerCol] = EMPTY;
        }

        this.player    = player;
        this.playerRow = row;
        this.playerCol = col;
        grid[row][col] = PLAYER;
    }

   

    /**
     * Moves the player one cell in the given direction.
     * If the target cell is a wall or outside the grid, the player stays in place.
     * The level is displayed after every call, even if the player did not move.
     *
     * @param direction The direction to move ({@link Direction}).
     * @throws IllegalStateException if no player has been placed in this level.
     */
    public void movePlayer(Direction direction) {
        if (player == null) {
            throw new IllegalStateException(
                "Aucun joueur n'est place dans ce niveau.");
        }

        // Compute target position using switch/case on the enum
        int targetRow = playerRow;
        int targetCol = playerCol;

        switch (direction) {
            case UP:
                targetRow = playerRow - 1;
                break;
            case DOWN:
                targetRow = playerRow + 1;
                break;
            case LEFT:
                targetCol = playerCol - 1;
                break;
            case RIGHT:
                targetCol = playerCol + 1;
                break;
        }

        // Check bounds
        boolean outOfBounds = (targetRow < 0 || targetRow >= rows
                            || targetCol < 0 || targetCol >= cols);

        if (outOfBounds) {
            System.out.println("Deplacement " + direction + " bloque : hors de la grille.");
        } else if (grid[targetRow][targetCol] == WALL) {
            System.out.println("Deplacement " + direction + " bloque : mur en ("
                + targetRow + ", " + targetCol + ").");
        } else {
            // Move the player
            grid[playerRow][playerCol] = EMPTY;
            playerRow = targetRow;
            playerCol = targetCol;
            grid[playerRow][playerCol] = PLAYER;
            System.out.println("Deplacement " + direction
                + " -> joueur en (" + playerRow + ", " + playerCol + ").");
        }

        // Always display the level after the move attempt
        display();
    }


    /**
     * Prints the level grid to the standard output.
     * Each row is printed on its own line.
     */
    public void display() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                System.out.print(grid[r][c]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // ------------------------------------------------------------------ //
    //  Accessors                                                           //
    // ------------------------------------------------------------------ //

    /** @return The row count. */
    public int getRows() { return rows; }

    /** @return The column count. */
    public int getCols() { return cols; }

    /**
     * Returns the character at position (row, col).
     * @param row Row index.
     * @param col Column index.
     * @return The character at the given position.
     */
    public char getCell(int row, int col) { return grid[row][col]; }

    /**
     * Sets the character at position (row, col).
     * @param row  Row index.
     * @param col  Column index.
     * @param cell The character to place.
     */
    public void setCell(int row, int col, char cell) { grid[row][col] = cell; }

    /** @return The player placed in this level, or {@code null} if none. */
    public Player getPlayer() { return player; }

    /** @return The player's row index, or -1 if no player is placed. */
    public int getPlayerRow() { return playerRow; }

    /** @return The player's column index, or -1 if no player is placed. */
    public int getPlayerCol() { return playerCol; }

    /** @return The total number of Level instances created. */
    public static int getLevelCount() { return levelCount; }

    /**
     * Two levels are equal if they have the same dimensions and identical cell content.
     * @param obj The object to compare with.
     * @return {@code true} if equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Level)) return false;
        Level other = (Level) obj;
        if (this.rows != other.rows || this.cols != other.cols) return false;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (this.grid[r][c] != other.grid[r][c]) return false;
            }
        }
        return true;
    }

    /**
     * Returns a string representation of the level including player info.
     *
     * @return A multi-line string of the grid, followed by player info if present.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                sb.append(grid[r][c]);
            }
            sb.append('\n');
        }
        if (player != null) {
            sb.append("Joueur : ").append(player.getname())
              .append(" en (").append(playerRow).append(", ").append(playerCol).append(')');
        }
        return sb.toString();
    }
}