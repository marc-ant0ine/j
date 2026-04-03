import java.util.Scanner;

/**
 * Gère la boucle de jeu et les entrées clavier pour un niveau donné.
 *
 * Contrôles :
 *   Z = Haut   Q = Gauche   S = Bas   D = Droite   X = Quitter
 *
 * Monde 3 Niv.2 : après un GAME OVER, propose R=recommencer / X=quitter.
 * Monde 3 Niv.3 : run(Level, Scanner) reçoit le Scanner de Main pour ne
 *                 pas fermer System.in entre les niveaux.
 *                 run() retourne dès que le niveau est terminé (isCompleted())
 *                 pour que Main puisse passer au niveau suivant.
 */
public class GameController {

    /**
     * Lance la boucle de jeu pour un niveau.
     * Retourne quand :
     *   - Le joueur tape X   → level.isCompleted() == false  (abandon).
     *   - Toutes les pièces sont ramassées → level.isCompleted() == true.
     *
     * @param level   Le niveau à jouer (un joueur doit déjà y être placé).
     * @param scanner Scanner partagé lisant System.in (créé dans Main).
     */
    public void run(Level level, Scanner scanner) {

        System.out.println("=== Contrôles : Z=Haut  Q=Gauche  S=Bas  D=Droite  X=Quitter ===");
        System.out.println();
        level.display();

        while (scanner.hasNextLine()) {

            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                System.out.println("Entrée invalide. Utilisez Z, Q, S ou D.");
                continue;
            }

            char key = Character.toUpperCase(line.charAt(0));
            Direction direction;

            switch (key) {
                case 'Z': direction = Direction.UP;    break;
                case 'S': direction = Direction.DOWN;  break;
                case 'Q': direction = Direction.LEFT;  break;
                case 'D': direction = Direction.RIGHT; break;
                case 'X':
                    System.out.println("Fin du jeu. À bientôt !");
                    return;   // isCompleted() == false → Main détecte l'abandon
                default:
                    System.out.println("Touche inconnue : '" + key
                            + "'. Utilisez Z, Q, S, D ou X.");
                    continue;
            }

            level.movePlayer(direction);

            // ---- GAME OVER : proposer recommencer ou quitter ----
            if (level.isGameOver()) {
                if (!askRestart(scanner, level)) {
                    return;  // joueur a choisi de quitter
                }
                continue;    // joueur a choisi de recommencer → on reprend la boucle
            }

            // ---- Niveau terminé : retour à Main pour passer au suivant ----
            if (level.isCompleted()) {
                return;
            }
        }
        // Fin de flux (Ctrl+D / Ctrl+Z)
    }

    /**
     * Méthode de compatibilité : crée son propre Scanner.
     * À utiliser quand on joue un seul niveau indépendamment.
     *
     * @param level Le niveau à jouer.
     */
    public void run(Level level) {
        Scanner scanner = new Scanner(System.in);
        run(level, scanner);
        // Ne ferme PAS scanner : fermer System.in empêcherait de relire depuis Main.
    }

    // ------------------------------------------------------------------ //
    //  GAME OVER — recommencer ou quitter                                  //
    // ------------------------------------------------------------------ //

    /**
     * Affiche le menu GAME OVER et attend R ou X.
     *
     * Si R : réinitialise les vies, replace le joueur au départ, affiche le niveau.
     *
     * @param scanner Scanner partagé.
     * @param level   Le niveau en cours.
     * @return {@code true} = recommencer, {@code false} = quitter.
     */
    private boolean askRestart(Scanner scanner, Level level) {
        System.out.println();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║            GAME  OVER              ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║  R = Recommencer  |  X = Quitter  ║");
        System.out.println("╚════════════════════════════════════╝");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            char key = Character.toUpperCase(line.charAt(0));

            if (key == 'R') {
                Player player = level.getPlayer();
                player.resetLives();
                level.setPlayer(player, level.getStartRow(), level.getStartCol());

                System.out.println();
                System.out.println("=== Nouvelle partie ! Bonne chance ! ===");
                System.out.println("Vies : " + player.getLives()
                        + "  |  Score : " + player.getscore() + " pts");
                System.out.println();
                System.out.println(
                        "=== Contrôles : Z=Haut  Q=Gauche  S=Bas  D=Droite  X=Quitter ===");
                System.out.println();
                level.display();
                return true;

            } else if (key == 'X') {
                return false;

            } else {
                System.out.println("Tapez R pour recommencer ou X pour quitter.");
            }
        }
        return false; // Ctrl+D / Ctrl+Z
    }
}