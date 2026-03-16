import java.util.Scanner;

/**
 * The GameController class manages the game loop and user input for moving the player in a level.
 * It reads ZQSD keys from standard input to move the player and displays the level after each move.
 * The game can be exited by typing 'X' or pressing Ctrl+D/Ctrl+Z.
 */
public class GameController {

    /**
     * Starts the interactive game loop.
     * Reads ZQSD keys from standard input and moves the player accordingly.
     * The level is displayed after each move attempt (even if the player didn't move).
     * Type 'X' or press Ctrl+D/Ctrl+Z to quit.
     *
     * @param level  The level in which the player moves.
     */
    public void run(Level level) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Contrôles : Z=Haut  Q=Gauche  S=Bas  D=Droite  X=Quitter ===");
        System.out.println();
        level.display();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                System.out.println("Entrée invalide. Utilisez Z, Q, S ou D.");
                continue;
            }

            // Only take the first character, case-insensitive
            char key = Character.toUpperCase(line.charAt(0));

            Direction direction;

            switch (key) {
                case 'Z':
                    direction = Direction.UP;
                    break;
                case 'S':
                    direction = Direction.DOWN;
                    break;
                case 'Q':
                    direction = Direction.LEFT;
                    break;
                case 'D':
                    direction = Direction.RIGHT;
                    break;
                case 'X':
                    System.out.println("Fin du jeu. À bientôt !");
                    scanner.close();
                    return;
                default:
                    System.out.println("Touche inconnue : '" + key + "'. Utilisez Z, Q, S ou D.");
                    continue;
            }

            level.movePlayer(direction);
        }

        scanner.close();
        System.out.println("Fin du jeu. À bientôt !");
    }
}