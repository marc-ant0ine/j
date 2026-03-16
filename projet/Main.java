public class Main {
    /**
     * Point d'entrée principal du programme.
     * @param args Les arguments de la ligne de commande.
     * Affiche des informations sur deux joueurs, modifie leurs scores, et affiche à nouveau les informations.
     * Fais appel à la classe Player pour créer deux joueurs.
     * Fais des modifications sur leurs scores.
     */
    public static void main(String[] args) {

        // Display level count BEFORE creation (should be 0)
        System.out.println("Nombre total de niveaux : " + Level.getLevelCount());


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

        Level level2 = new Level(6, 8, true);

          // Horizontal wall on row 2, gap at column 6
          for (int c = 1; c < 7; c++) {
             if (c != 6) {
                 level2.setCell(2, c, Level.WALL);
              }
         }

          // Vertical wall on column 3, gap at row 4
          for (int r = 3; r < 5; r++) {
             if (r != 4) {
                level2.setCell(r, 3, Level.WALL);
            }
       }

         // Vertical wall on column 5, gap at row 3
         for (int r = 3; r < 5; r++) {
             if (r != 3) {
                 level2.setCell(r, 5, Level.WALL);
             }
          }

         System.out.println(level2);

        
         Level level3 = new Level(8, 10, true);

          // Horizontal wall on row 2, gap at column 7
          for (int c = 1; c < 9; c++) {
              if (c != 7) {
                  level3.setCell(2, c, Level.WALL);
              }
          }

          // Horizontal wall on row 5, gap at column 2
          for (int c = 1; c < 9; c++) {
              if (c != 2) {
                  level3.setCell(5, c, Level.WALL);
              }
          }

          // Vertical wall on column 5, gap at row 3
          for (int r = 3; r < 5; r++) {
              if (r != 3) {
                  level3.setCell(r, 5, Level.WALL);
              }
          }

          System.out.println(level3);

        // Afficher le nombre de joueurs AVANT création (devrait être 0)
        System.out.println("Nombre total de joueurs : " + Player.getPlayerCount());
        
        // Créer Alice et Bob (CORRECTION: utiliser les bons noms de variables)
        Player alice = new Player("Alice", 1);
        Player bob = new Player("Bob", 2);
        
        // Créer des joueurs sans nom
        Player p1 = new Player();
        System.out.println(p1);
        
        Player p6 = new Player("Joueur", 2);
        
        Player p2 = new Player();
        System.out.println(p2);
        
        Player p7 = new Player("Charlie");
        System.out.println(p7);
        
        // Afficher p1 et p2
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p1.getname());
        System.out.println(p2.getname());
        System.out.println(p1.getscore());
        System.out.println(p2.getscore());
        
        // Modifier les scores
        p1.addPoints(50);   
        p2.removePoints(30);
        
        System.out.println(p1.getname());
        System.out.println(p2.getname());
        System.out.println(p1.getscore());
        System.out.println(p2.getscore());
        
        // Tests d'égalité
        System.out.println("Alice equals \"Alice\" : " + alice.equals("Alice"));
        System.out.println("Alice equals Bob : " + alice.equals(bob));
        
        Player bob2 = new Player("BOB", 100);
        System.out.println("Bob equals BOB : " + bob.equals(bob2));
        System.out.println("Bob == BOB : " + (bob == bob2));
        
        Player b = bob;  
        System.out.println("Bob == b : " + (bob == b));
        
        Player p3 = new Player();
        System.out.println(p3);
        System.out.println();
        
        // Suppression d'une référence
        System.out.println("Référence bob2 avant suppression : " + bob2);
        bob2 = null; 
        System.out.println("Référence bob2 après suppression : " + bob2);
        
        // Afficher le nombre total de joueurs créés
        System.out.println("Nombre total de joueurs : " + Player.getPlayerCount());
        
        // Afficher Alice et Bob
        System.out.println(alice);
        System.out.println(bob);

        try {
            level1.setPlayer(alice, 0, 0);  // (0,0) is a wall in level1
         } catch (IllegalStateException e) {
             System.out.println("Erreur : " + e.getMessage());
          }

       
         try {
            level1.setPlayer(alice, 10, 10);  
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

          try {
               level1.setPlayer(null, 1, 2);
           } catch (IllegalArgumentException e) {
               System.out.println("Erreur : " + e.getMessage());
           }

        String filePath;
           if (args.length >= 1) {
              filePath = args[0];
            } else {
              filePath = "level.txt";   
        }

           Level level = LevelLoader.loadFromFile(filePath);

           if (level == null) {

              System.exit(1);
            }


            Player player = new Player("Alice", 0);

            boolean placed = false;
            for (int r = 0; r < level.getRows() && !placed; r++) {
                for (int c = 0; c < level.getCols() && !placed; c++) {
                    if (level.getCell(r, c) == Level.EMPTY) {
                        level.setPlayer(player, r, c);
                       placed = true;
                   }
               }
           }

          if (!placed) {
           System.err.println("Erreur : aucune cellule vide disponible pour placer le joueur.");
           System.exit(1);
       }


        GameController controller = new GameController();
        controller.run(level);









        String filePath = (args.length > 0) ? args[0] : "level1.txt";

          Level level = LevelLoader.load(filePath);

          if (level == null) {

              System.err.println("Impossible de démarrer le jeu : niveau non chargé.");
              return;
            }

          System.out.println("Niveau chargé depuis '" + filePath + "' ("
                  + level.getRows() + " lignes x " + level.getCols() + " colonnes).");

          Player player = new Player("Hero");

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
              System.err.println("Erreur : aucune cellule vide trouvée pour placer le joueur.");
               return;
          }

           GameController controller = new GameController();
        controller.run(level);

      if (args.length == 0) {
    System.out.println(USAGE);  
    return;                      
}
    String filePath = args[0];       
       }
       
}

       
