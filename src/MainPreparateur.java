import java.util.Scanner;

public class MainPreparateur {

    public static void main(String[] args) {
        JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());
        Scanner sc = new Scanner(System.in);
        int choix = 0;
        while (choix != 5) {
            System.out.println("1. Afficher les commandes en attente de préparation");
            System.out.println("2. Marquer une commande comme préparée");
            System.out.println("3. Afficher les commandes en attente de livraison");
            System.out.println("4. Marquer une commande comme livrée");
            System.out.println("5. Quitter");
            choix = sc.nextInt();


        }
        database.disconnect();
    }
}
