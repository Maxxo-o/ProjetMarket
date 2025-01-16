import java.util.Scanner;

public class MainAdmin {


    public static void main(String[] args) {
        JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());
        Scanner sc = new Scanner(System.in);

        int choix = 0;

        while (choix != 5){

            System.out.println("1. Voir les statistiques");
            System.out.println("2. Mettre une categorie en avant");
            System.out.println("3. Ajouter des produit aux catalogues");
            System.out.println("4. Augmenter le stock d'un produit");
            System.out.println("5. Quitter");
            choix = sc.nextInt();

            switch (choix){
                case 1:
                    // Statistiques
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Choix invalide");
            }
        }


    }
}
