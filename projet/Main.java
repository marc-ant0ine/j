import java.util.Scanner;

/**
 * Point d'entrée du programme — couvre tous les objectifs jusqu'au Monde 3, Niveau 3.
 *
 * ── Monde 1 ──────────────────────────────────────────────────────────────────
 *   Niv.1 : Player (nom, score)
 *   Niv.2 : Encapsulation, score jamais négatif
 *   Niv.3 : Javadoc
 *   Niv.4 : toString()  "nom : score pt(s)"
 *   Niv.5 : equals(), ==, null
 *   Niv.6 : playerCount static, constructeur sans nom (JoueurN)
 *
 * ── Monde 2 ──────────────────────────────────────────────────────────────────
 *   Niv.1 : Level (grille de caractères, display)
 *   Niv.2 : setPlayer, exceptions (mur / hors grille / null)
 *   Niv.3 : movePlayer, Direction enum
 *   Niv.4 : GameController (boucle ZQSD)
 *   Niv.5 : LevelLoader.load() — fichier texte, System.err si absent
 *   Niv.6 : args[], message d'utilisation si aucun argument
 *
 * ── Monde 3 ──────────────────────────────────────────────────────────────────
 *   Niv.1 : pièces '.' (+10 pts, disparaissent), "NIVEAU TERMINÉ"
 *   Niv.2 : pièges '*' (-2 vies, détruits), 5 vies, respawn, "GAME OVER",
 *            R=recommencer / X=quitter
 *   Niv.3 : plusieurs fichiers en args, nom demandé au départ,
 *            passage auto au niveau suivant, score+vies conservés,
 *            remerciement quand tous les niveaux sont terminés
 *
 * Usage :
 *   java Main <fichier1.txt> [fichier2.txt] ...
 *   java -jar game.jar <fichier1.txt> [fichier2.txt] ...
 */
public class Main {

    /** Message d'utilisation — Monde 2 Niv.6 */
    private static final String USAGE =
            "Utilisation : java Main <fichier1> [fichier2] ...\n"
          + "  Chaque fichier texte représente un niveau du jeu.\n"
          + "\n"
          + "Exemples :\n"
          + "  java Main level1.txt\n"
          + "  java Main level1.txt level2.txt\n"
          + "  java -jar game.jar level1.txt level2.txt";

    public static void main(String[] args) {

        // ================================================================ //
        //  MONDE 1 — démonstration complète de Player                      //
        // ================================================================ //
        System.out.println("=== MONDE 1 — Player ===");

        // Niv.6 : compteur AVANT toute création → 0
        System.out.println("Nombre de joueurs avant création : "
                + Player.getPlayerCount());

        // Niv.1 : créer Alice et Bob avec score initial
        Player alice = new Player("Alice", 1);
        Player bob   = new Player("Bob",   2);

        // Niv.6 : joueurs sans nom → "JoueurN"
        Player p1 = new Player();
        Player p2 = new Player();
        System.out.println(p1);  // Joueur3 : 0 pt  |  vies : 5
        System.out.println(p2);  // Joueur4 : 0 pt  |  vies : 5

        Player p7 = new Player("Charlie");
        System.out.println(p7);  // Charlie : 0 pt  |  vies : 5

        // Niv.1 : afficher nom et score séparément
        System.out.println(p1.getName() + " / " + p1.getscore());
        System.out.println(p2.getName() + " / " + p2.getscore());

        // Niv.2 : modifier les scores (jamais négatif)
        p1.addPoints(50);
        p2.removePoints(30);   // score était 0 → reste 0
        System.out.println(p1.getName() + " : " + p1.getscore()); // 50
        System.out.println(p2.getName() + " : " + p2.getscore()); // 0

        // Niv.4 : toString() — accord pt/pts
        System.out.println(alice); // Alice : 1 pt  |  vies : 5
        System.out.println(bob);   // Bob : 2 pts  |  vies : 5

        // Niv.5 : equals() insensible à la casse, false avec non-Player
        System.out.println("alice.equals(\"Alice\") : " + alice.equals("Alice")); // false
        System.out.println("alice.equals(bob)    : " + alice.equals(bob));        // false

        Player bob2 = new Player("BOB", 100);
        System.out.println("bob.equals(bob2) : " + bob.equals(bob2)); // true  (même nom)
        System.out.println("bob == bob2      : " + (bob == bob2));    // false (objets distincts)

        Player b = bob;
        System.out.println("bob == b         : " + (bob == b));       // true  (même référence)

        // Niv.5 : "suppression" d'une référence (ramasse-miettes)
        System.out.println("bob2 avant null : " + bob2);
        bob2 = null;
        System.out.println("bob2 après null : " + bob2);

        // Niv.6 : compteur après toutes les créations
        System.out.println("Nombre total de joueurs : " + Player.getPlayerCount());
        System.out.println();

        // ================================================================ //
        //  MONDE 2 Niv.1 — création et affichage de niveaux                //
        // ================================================================ //
        System.out.println("=== MONDE 2 — Niveaux ===");
        System.out.println("Nombre de niveaux avant création : " + Level.getLevelCount());

        // Niveau 1 : construit depuis un char[][]
        char[][] grid1 = {
            {'#', '#', '#', '#', '#', '#', '#', '#'},
            {'#', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
            {'#', ' ', '#', '#', ' ', '#', ' ', '#'},
            {'#', ' ', '#', ' ', ' ', '#', ' ', '#'},
            {'#', ' ', ' ', ' ', '#', ' ', ' ', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#'}
        };
        Level level1 = new Level(grid1);
        System.out.println(level1);

        // Niveau 2 : construit programmatiquement avec bordure
        Level level2 = new Level(6, 8, true);
        for (int c = 1; c < 7; c++) { if (c != 6) level2.setCell(2, c, Level.WALL); }
        for (int r = 3; r < 5; r++) { if (r != 4) level2.setCell(r, 3, Level.WALL); }
        for (int r = 3; r < 5; r++) { if (r != 3) level2.setCell(r, 5, Level.WALL); }
        System.out.println(level2);

        // Niveau 3 : 8x10 avec bordure et murs intérieurs
        Level level3 = new Level(8, 10, true);
        for (int c = 1; c < 9; c++) { if (c != 7) level3.setCell(2, c, Level.WALL); }
        for (int c = 1; c < 9; c++) { if (c != 2) level3.setCell(5, c, Level.WALL); }
        for (int r = 3; r < 5; r++) { if (r != 3) level3.setCell(r, 5, Level.WALL); }
        System.out.println(level3);

        // ================================================================ //
        //  MONDE 2 Niv.2 — exceptions setPlayer                            //
        // ================================================================ //
        System.out.println("=== MONDE 2 — Exceptions setPlayer ===");

        // Placement sur un mur → IllegalStateException
        try {
            level1.setPlayer(alice, 0, 0);
        } catch (IllegalStateException e) {
            System.out.println("Erreur (mur) : " + e.getMessage());
        }

        // Placement hors grille → ArrayIndexOutOfBoundsException
        try {
            level1.setPlayer(alice, 10, 10);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Erreur (hors grille) : " + e.getMessage());
        }

        // Placement joueur null → IllegalArgumentException
        try {
            level1.setPlayer(null, 1, 2);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur (null) : " + e.getMessage());
        }

        // ================================================================ //
        //  MONDE 2 Niv.6 — vérification des arguments                      //
        // ================================================================ //
        if (args.length == 0) {
            System.out.println();
            System.out.println(USAGE);
            return;
        }

        // ================================================================ //
        //  MONDE 3 Niv.3 — demander le nom du joueur                       //
        // ================================================================ //
        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.print("Entrez votre nom de joueur : ");
        String playerName = scanner.nextLine().trim();
        if (playerName.isEmpty()) {
            playerName = "Joueur";
        }

        // Créé UNE SEULE FOIS : score et vies conservés entre tous les niveaux
        Player player = new Player(playerName);
        System.out.println();
        System.out.println("Bienvenue, " + player.getName() + " !");
        System.out.println("Vies : " + player.getLives()
                + "  |  Score : " + player.getscore() + " pts");
        System.out.println();

        // ================================================================ //
        //  MONDE 3 Niv.3 — boucle sur les niveaux                          //
        // ================================================================ //
        GameController controller = new GameController();

        for (int i = 0; i < args.length; i++) {
            String filePath = args[i];
            int    numLevel = i + 1;

            System.out.println("╔══════════════════════════════════════════╗");
            System.out.printf( "║  Niveau %d / %-31s║%n",
                    numLevel, args.length + " — " + filePath);
            System.out.println("╚══════════════════════════════════════════╝");

            // ---- Monde 2 Niv.5 : charger depuis le fichier texte ----
            Level level = LevelLoader.load(filePath);
            if (level == null) {
                System.err.println("Niveau " + numLevel + " ignoré : fichier invalide.");
                continue;
            }

            // ---- Monde 3 Niv.1 : info sur les pièces ----
            if (level.getRemainingCoins() > 0) {
                System.out.println("Pièces à ramasser : " + level.getRemainingCoins()
                        + "  (+" + Level.COIN_POINTS + " pts chacune)");
            } else {
                System.out.println("(Aucune pièce dans ce niveau)");
            }

            // ---- Placer le joueur sur la première cellule vide ----
            boolean placed = false;
            outer:
            for (int r = 0; r < level.getRows(); r++) {
                for (int c = 0; c < level.getCols(); c++) {
                    if (level.getCell(r, c) == Level.EMPTY) {
                        level.setPlayer(player, r, c);
                        System.out.println("Joueur '" + player.getName()
                                + "' placé en (" + r + ", " + c + ").");
                        placed = true;
                        break outer;
                    }
                }
            }

            if (!placed) {
                System.err.println("Aucune cellule vide dans '"
                        + filePath + "' — niveau ignoré.");
                continue;
            }

            // ---- Lancer la boucle de jeu (GameController) ----
            // Le scanner est partagé : System.in ne sera pas fermé entre les niveaux.
            controller.run(level, scanner);

            // ---- Analyser la sortie de run() ----
            // run() retourne quand :
            //   • le joueur tape X           → isCompleted() == false  (abandon)
            //   • toutes les pièces ramassées → isCompleted() == true   (succès)
            if (!level.isCompleted()) {
                System.out.println();
                System.out.println("À bientôt, " + player.getName() + " !");
                System.out.println("Score : " + player.getscore() + " pts"
                        + "  |  Vies restantes : " + player.getLives());
                scanner.close();
                return;
            }

            // ---- Niveau terminé ----
            System.out.println();
            System.out.println("✓ Niveau " + numLevel + " terminé !"
                    + "  Score : " + player.getscore() + " pts"
                    + "  |  Vies : " + player.getLives());

            if (i < args.length - 1) {
                System.out.println("  → Prochain niveau : " + args[i + 1]);
                System.out.println();
            }
        }

        // ================================================================ //
        //  MONDE 3 Niv.3 — tous les niveaux terminés                       //
        // ================================================================ //
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  Félicitations ! Tous les niveaux        ║");
        System.out.println("║  sont terminés.                          ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf( "║  Merci d'avoir joué, %-19s║%n",
                player.getName() + " !");
        System.out.printf( "║  Score final    : %-22s║%n",
                player.getscore() + " pts");
        System.out.printf( "║  Vies restantes : %-22s║%n",
                player.getLives() + " / " + Player.INITIAL_LIVES);
        System.out.println("╚══════════════════════════════════════════╝");

        scanner.close();
    }
}