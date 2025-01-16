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
            System.out.println("5. Changer d'utilisateur");
            System.out.println("6. Quitter");
            choix = sc.nextInt();

            switch (choix) {
                case 1:
                    // Afficher les commandes en attente de préparation
                    Preparer.commanedsAPreparer(database, "En preparation");
                    break;
                case 2:
                    // Marquer une commande comme préparée
                    System.out.println("Entrez l'id de la commande à marquer comme préparée : ");
                    int idCommande = sc.nextInt();
                    Preparer.marquerCommande(database, idCommande, "En attente de livraison");
                    break;
                case 3:
                    // Afficher les commandes en attente de livraison
                    Preparer.commanedsAPreparer(database, "En attente de livraison");
                    break;
                case 4:
                    // Marquer une commande comme livrée
                    System.out.println("Entrez l'id de la commande à marquer comme livrée : ");
                    idCommande = sc.nextInt();
                    Preparer.marquerCommande(database, idCommande, "Finalisee");
                    break;
                case 5:
                    MainClient.main(args);
                    break;
                case 6:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Choix invalide");
            }

        }
        database.disconnect();
    }
}
