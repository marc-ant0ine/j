public class Main {
    /**
     * Point d'entrée principal du programme.
     * @param args Les arguments de la ligne de commande.
     * Affiche des informations sur deux joueurs, modifie leurs scores, et affiche à nouveau les informations.
     * Fais appel à la classe Player pour créer deux joueurs.
     * Fais des modifications sur leurs scores.
     */
    public static void main(String[] args) {
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
        
        Player charlie = new Player("Charlie");
        System.out.println(charlie);
        
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
    }
}