import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainAdmin {

    private static JDBC database;

    public static void main(String[] args) {
        database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());
        Scanner sc = new Scanner(System.in);

        int choix = 0;

        while (choix != 5){
            System.out.println();
            System.out.println("-----------------------------------------------");
            System.out.println("1. Voir les statistiques");
            System.out.println("2. Mettre une categorie en avant");
            System.out.println("3. Ajouter des produits au catalogue depuis un fichier CSV");
            System.out.println("4. Changer d'utilisateur");
            System.out.println("5. Quitter");
            System.out.println("-----------------------------------------------");
            System.out.println();
            choix = sc.nextInt();

            switch (choix){
                case 1:
                    // Statistiques
                    VoirStats();
                    break;
                case 2:
                    // Mettre une categorie en avant
                    mettreEnAvant();
                    break;
                case 3:
                    AddProduitsCSV.askPath(database);
                    break;
                case 4:
                    MainClient.main(args);
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
    public static void VoirStats(){
        int choix = 0;
        while (choix != 6) {
            System.out.println("-----------------------------------------------");
            System.out.println("1. Statistiques des produits");
            System.out.println("2. Statistiques des categories");
            System.out.println("3. Statistiques des clients");
            System.out.println("4. Statistiques des commandes");
            System.out.println("5. Statistiques des algorithmes");
            System.out.println("6. Revenir en arrière");
            System.out.println("-----------------------------------------------");

            Scanner sc = new Scanner(System.in);
            choix = sc.nextInt();
            switch (choix)
            {
                case 1:
                    statsProd();
                    break;
                case 2:
                    statsCat();
                    break;
                case 3:
                    statsClient();
                    break;
                case 4:
                    statsCom();
                    break;
                case 5:
                    System.out.println("Algorithmes de recommandation :");
                    System.out.println("Nombre de recommandations proposées : " + Statistique.NbProposeRecom);
                    System.out.println("Nombre de recommandations acceptées : " + Statistique.NbProposeRecom);
                    System.out.println(Statistique.efficaciteRecom()*100 + "% des recommandations ont permis un achat.\n");
                    System.out.println("Algorithmes de remplacement :");
                    System.out.println("Nombre de remplacement proposées : " + Statistique.NbProposeRempla);
                    System.out.println("Nombre de remplacement acceptées : " + Statistique.NbPChoisiRempla);
                    System.out.println(Statistique.efficaciteRenpla()*100 + "% des produits de remplacement ont permis un achat.\n");
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Choix invalide");
            }
        }

    }

    public static void statsProd(){
        int choix = 0;
        while (choix != 4) {
            System.out.println("-----------------------------------------------");
            System.out.println("1. Produit le plus commandé");
            System.out.println("2. Produit le plus vendu");
            System.out.println("3. Produit le plus rentable");
            System.out.println("4. Revenir en arrière");
            System.out.println("-----------------------------------------------");

            Scanner sc = new Scanner(System.in);
            choix = sc.nextInt();
            switch (choix) {
                case 1:
                    List<List<String>> prodsCom = Statistique.produit(database, "Le plus commendé");
                    AffichageListe(prodsCom, "du produit");

                    break;
                case 2:
                    List<List<String>> prodsVent = Statistique.produit(database, "Le plus vendu");
                    AffichageListe(prodsVent, "du produit");
                    break;
                case 3:
                    List<List<String>> prodsRent = Statistique.produit(database, "Le plus grand CA");
                    AffichageListe(prodsRent, "du produit");
                    break;
                case 4:
                    break;
            }
        }
    }

    public static void statsCat(){
        int choix = 0;
        while (choix != 4) {
            System.out.println("-----------------------------------------------");
            System.out.println("1. Categorie la plus commandée");
            System.out.println("2. Categorie la plus vendu");
            System.out.println("3. Categorie la plus rentable");
            System.out.println("4. Revenir en arrière");
            System.out.println("-----------------------------------------------");

            Scanner sc = new Scanner(System.in);
            choix = sc.nextInt();
            switch (choix) {
                case 1:
                    List<List<String>> prodsCom = Statistique.categorie(database, "Le plus commendé");
                    AffichageListe(prodsCom, "de la categorie");

                    break;
                case 2:
                    List<List<String>> prodsVent = Statistique.categorie(database, "Le plus vendu");
                    AffichageListe(prodsVent, "de la categorie");
                    break;
                case 3:
                    List<List<String>> prodsRent = Statistique.categorie(database, "Le plus grand CA");
                    AffichageListe(prodsRent, "de la categorie");
                    break;
                case 4:
                    break;
            }
        }
    }

    public static void statsClient(){
        int choix = 0;
        while (choix != 4) {
            System.out.println("-----------------------------------------------");
            System.out.println("1. Client avec le plus de commande");
            System.out.println("2. Client avec le plus de produit acheté");
            System.out.println("3. Client ayant dépensé le plus");
            System.out.println("4. Revenir en arrière");
            System.out.println("-----------------------------------------------");

            Scanner sc = new Scanner(System.in);
            choix = sc.nextInt();
            switch (choix) {
                case 1:
                    List<List<String>> prodsCom = Statistique.client(database, "Le plus commendé");
                    AffichageListe(prodsCom, "du client");

                    break;
                case 2:
                    List<List<String>> prodsVent = Statistique.client(database, "Le plus vendu");
                    AffichageListe(prodsVent, "du client");
                    break;
                case 3:
                    List<List<String>> prodsRent = Statistique.client(database, "Le plus grand CA");
                    AffichageListe(prodsRent, "du client");
                    break;
                case 4:
                    break;
            }
        }
    }

    public static void AffichageListe( List<List<String>> prods, String type){
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        System.out.printf("%-2s | %-30s | %-20s | %-20s | %-20s \n", "n°", "Nom " + type, "Nombre Commandes", "Quantité commandée", "Chiffre d'affaire");
        System.out.println("-----------------------------------------------------------------------------------------------------------");

        for (List<String> row : prods) {
            System.out.printf("%-2s | %-30s | %-20s | %-20s | %-20s \n", prods.indexOf(row) + 1, row.get(1), row.get(2), row.get(3), row.get(4) + "€");
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------");

        System.out.println();
    }

    public static void statsCom(){
        Statistique.getTempsMoyenRealisationCommande(database);
        Statistique.getTempsMoyenRealisationPanier(database);
    }

}
