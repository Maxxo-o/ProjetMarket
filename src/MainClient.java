import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MainClient {

    private static JDBC database;
    private static Client c;
    private static List<Panier> panierArchiver;

    public static void main(String[] args) {
        database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());
        c = new Client(1, 1, new Profil(1, database), database);
        panierArchiver = new ArrayList<>();

        Scanner sc = new Scanner(System.in);
        char cas = ' ';

        while (cas != 'q'){
            System.out.println("-------------------------------------------------");
            System.out.println("a. Consulter le catalogue de produit");
            System.out.println("c. Consulter son panier");
            System.out.println("p. Consulter son profil");
            System.out.println("4. cas4");
            System.out.println("q. Quitter l'application");
            System.out.println("-------------------------------------------------");

            cas = sc.next().charAt(0);

            switch (cas){
                case 'a':
                    AffCatalogue();
                    break;
                case 'c':
                    AffPanier();
                    break;
                case 'p':

                    break;
                case 4:
                    cas4();
                    break;
                case 'q':
                    cas5();
                    break;
                default:
                    System.out.println("Cas non reconnu");
            }
        }
        database.disconnect();
    }

    public static void AffCatalogue(){
        String s = "";
        Scanner sc = new Scanner(System.in);

        while (!s.equals("q")){
            System.out.println("-------------------------------------------------");
            System.out.println("m. Rechercher un produit par mot clé");
            System.out.println("c. Rechercher un produit par catégorie");
            System.out.println("q. Revenir au menu principal");
            System.out.println("-------------------------------------------------");

            s = sc.next();
            switch (s) {
                case "m":
                    System.out.println("Entrez le mot clé : ");
                    List<List<String>> prodKey = Research.searchByKeyword(database, sc.next());
                    if (prodKey.isEmpty()) System.out.println("Aucun produit trouvé");
                    else afficherProduits(prodKey);
                    break;
                case "c":
                    System.out.println("Entrez la catégorie : ");
                    List<List<String>> prodCat = Research.listCategory(database, sc.next());
                    if (prodCat.isEmpty()) System.out.println("Aucun produit trouvé");
                    else afficherProduits(prodCat);
                    break;
                default:
                    System.out.println("Cas non reconnu");
            }
        }
    }
    public static void AffPanier(){
        String s = "";
        Scanner sc = new Scanner(System.in);
        while (!s.equals("q")) {
            System.out.println("-------------------------------------------------");
            System.out.println("a. Afficher les produits dans mon panier");
            System.out.println("v. Valider mon panier");
            System.out.println("c. Archiver mon panier");
            System.out.println("r. Restorer mon panier");
            System.out.println("d. Vider mon panier");
            System.out.println("q. Revenir au menu principal");
            System.out.println("-------------------------------------------------");

            s = sc.next();
            switch (s) {
                case "a":
                    afficherProduitPanier(c.getPanier().getProduits());
                    break;
                case "v":
                    validerPanier();
                    break;
                case "c":
                    archiverPanier();
                    break;
                case "r":
                    restorerPanier();
                    break;
                case "d":
                    viderPanier();
                    break;
                default:
                    System.out.println("Cas non reconnu");
            }
        }
    }
    public static void cas4(){
        System.out.println("Cas 4");
    }
    public static void cas5(){
        System.out.println("Merci d'avoir utilisé notre application");
    }

    public static void afficherProduits(List<List<String>> ListProduit){
        System.out.println("Liste des produits : ");
        String separator = "------------------------------------------------------------------------------------------------------------";
        String header = String.format("%2s | %-30s | %-15s | %-15s | %-15s",
                "n°","Nom Produit", "Marque", "Prix", "Categorie");

        System.out.println(separator);
        System.out.println(header);
        System.out.println(separator);

        for (List<String> produit : ListProduit){
            String prix;
            if (produit.get(2) == null) prix = produit.get(3)+"€/u";
            else prix = produit.get(2)+"€/kg";
            System.out.printf("%2s | %-30s | %-15s | %-15s | %-15s%n",
                    ListProduit.indexOf(produit)+1,
                    produit.get(1),
                    produit.get(7),
                    prix,
                    produit.get(6));

        }
        System.out.println(separator);
        String s = "";

        System.out.println("Ajouter un produit au panier ? (o : oui, n : non)");
        Scanner sc = new Scanner(System.in);
        s = sc.next();

        while(!s.equals("n")){
            System.out.println("Entrez le numéro du produit à ajouter : ");
            int num = sc.nextInt()-1;
            if (num < 0 || num >= ListProduit.size()){
                System.out.println("Produit non trouvé");
                continue;
            }
            System.out.println("Entrez la quantité : ");
            int qte = sc.nextInt();
            c.addProduct(new Produit(Integer.parseInt(ListProduit.get(num).get(0)), database), qte);
            System.out.println("Ajouter un autre produit au panier ? (o : oui, n : non)");
            s = sc.next();
        }
        System.out.println();
    }

    public static void afficherProduitPanier(HashMap<Produit, Integer> ListProduit){
        System.out.println("Panier : ");
        String separator = "------------------------------------------------------------------------------------------------------------";
        String header = String.format("%8s | %-30s | %-15s | %-15s | %-15s",
                "Quantite","Nom Produit", "Marque", "Prix", "Categorie");

        System.out.println(separator);
        System.out.println(header);
        System.out.println(separator);

        for (Produit produit : ListProduit.keySet()){
            String prix;
            if (produit.getPrixAuKg() == 0) prix = produit.getPrixUnitaire()+"€/u";
            else prix = produit.getPrixAuKg()+"€/kg";
            System.out.printf("%8s | %-30s | %-15s | %-15s| %-15s%n",
                    ListProduit.get(produit),
                    produit.getLibelle(),
                    produit.getMarque(),
                    prix,
                    produit.getCategorie());

        }
        System.out.println(separator);

        System.out.println();
    }
    public static void validerPanier(){
        c.validatePanier();
        System.out.println("Panier validé");
        System.out.println("Merci d'avoir effectué vos courses dans nos magasins !");
        System.exit(0);
    }
    public static void archiverPanier(){
        System.out.println("Panier archivé");
        panierArchiver.add(c.getPanier().archivePanier());
    }
    public static void restorerPanier(){
        System.out.println("Paniers archivés : ");
        for (Panier p : panierArchiver){
            System.out.println("Panier n°"+panierArchiver.indexOf(p)+1+" : ");
            afficherProduitPanier(p.getProduits());
        }
    }
    public static void viderPanier(){
        System.out.println("Etes-vous sûr de vouloir vider votre panier ? (o : oui, autre caractère : non)");
        Scanner sc = new Scanner(System.in);
        String s = sc.next();
        if (s.equals("o")) {
            c.getPanier().clear();
            System.out.println("Panier vidé");
        }
        else System.out.println("Opération annulée");
    }


    public static void AffProfil(){

    }

    public static void ajouterRecommandation(){

    }

}
