import java.util.List;
import java.util.Scanner;

public class MainAdmin {

    private static JDBC database;

    public static void main(String[] args) {
        database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());
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
                    // Mettre une categorie en avant
                    mettreEnAvant();
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Choix invalide");
            }
        }


    }

    public static void mettreEnAvant(){
        Scanner sc = new Scanner(System.in);
        int choix = 0;
        while (choix != 4){
            System.out.println("1. Afficher les catégories actuellement en avant");
            System.out.println("2. Ajouter une catégorie actuellement en avant");
            System.out.println("3. Retirer une catégorie actuellement en avant");
            System.out.println("4. Quitter");
            choix = sc.nextInt();

            switch (choix){
                case 1:
                    System.out.println("Catégories actuellement en avant : ");
                    List<List<String>> listCat = database.executeQuery("SELECT c.NomCat FROM CategoriePhare cp, Categorie c WHERE cp.CategorieId = c.CategorieId Order BY c.NomCat");
                    System.out.println();
                    System.out.println("-----------------------------------------------");
                    System.out.printf("%-45s %-1s\n", "| Nom de la catégorie" , "|");
                    System.out.println("-----------------------------------------------");
                    for (List<String> row : listCat){
                        System.out.printf("%-45s %-1s\n", "| " + row.get(0) , "|");
                    }
                    System.out.println("-----------------------------------------------");
                    System.out.println();
                    break;
                case 2:

                    List<List<String>> listAllCat = database.executeQuery("SELECT NomCat FROM Categorie c WHERE NOT EXISTS (SELECT * FROM CategoriePhare cp WHERE cp.CategorieId = c.CategorieId) ORDER BY NomCat");

                    System.out.println("Catégories disponibles : ");
                    System.out.println();
                    System.out.println("-----------------------------------------------");
                    System.out.printf("%-45s %-1s\n", "| Nom de la catégorie" , "|");
                    System.out.println("-----------------------------------------------");
                    for (List<String> row : listAllCat){
                        System.out.printf("%-45s %-1s\n", "| " + row.get(0) , "|");
                    }
                    System.out.println("-----------------------------------------------");
                    System.out.println();

                    System.out.println("Entrez le nom de la catégorie à mettre en avant : ");
                    String nomCat = sc.next();
                    database.executeUpdate("INSERT INTO CategoriePhare VALUES ((SELECT CategorieId FROM Categorie WHERE NomCat = '" + nomCat + "'))");
                    break;
                case 3:
                    System.out.println("Entrez le nom de la catégorie à retirer des catégories en avant : ");
                    String nomCatDel = sc.next();
                    database.executeUpdate("DELETE FROM CategoriePhare WHERE CategorieId = (SELECT CategorieId FROM Categorie WHERE NomCat = '" + nomCatDel + "')");
                    break;
                default:
                    System.out.println("Choix invalide");
            }
        }




    }
}
