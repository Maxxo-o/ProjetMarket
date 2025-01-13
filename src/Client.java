import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    private int idClient;
    private String Nom;
    private String Prenom;
    private int Age;
    private String Adresse;
    private Panier panier;
    private Profil profil;
    private String sexe;
    private int idMagasin;

    public Client(int idClient, String Nom, String Prenom, int Age, String sexe, String Adresse, Profil profil, int magasinId) {
        this.idClient = idClient;
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.Age = Age;
        this.sexe = sexe;
        this.Adresse = Adresse;
        this.idMagasin = magasinId;
        this.panier = new Panier(magasinId);
        this.profil = profil;
    }

    public Client(int idClient, int magasinId, Profil profil) {
        JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());

        List<List<String>> result = database.executeQuery("SELECT * FROM Client WHERE clientId = " + idClient);

        this.idClient = Integer.parseInt(result.get(0).get(0));
        this.Nom = result.get(0).get(1);
        this.Prenom = result.get(0).get(2);
        this.Age = Integer.parseInt(result.get(0).get(3));
        this.sexe = result.get(0).get(4);
        this.Adresse = result.get(0).get(5);

        this.panier = new Panier(magasinId);
        this.idMagasin = magasinId;
        this.profil = profil;

    }



    public void validatePanier(){
        JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());

        for (Produit p : panier.getProduits().keySet()) {
            List<List<String>> result = database.executeQuery("SELECT * FROM Stocker WHERE produitId = " + p.getIdProduit() +" AND MagId = " + idMagasin);
            if (result.isEmpty()) {
                System.out.println("Produit non disponible dans ce magasin");
                return;
            }
            else if (Integer.parseInt(result.getFirst().get(2)) < panier.getProduits().get(p)) {
                System.out.println("Stock insuffisant");
                return;
            }
            else {
                database.execute("UPDATE Stocker SET QteStock = QteStock - " + panier.getProduits().get(p) + " WHERE produitId = " + p.getIdProduit() + " AND MagId = " + idMagasin);
            }
        }


        try {
            database.execute("INSERT INTO Commande ( DateCommande, MagId, ClientId, EtatCom) " +
                    "VALUES (SYSDATE,"+ idMagasin + ", " + idClient + " , 'En preparation')");
        }
        catch (Exception e){
            System.out.println("Erreur lors de la validation du panier");
        }


        for (Produit p : panier.getProduits().keySet()) {
            database.execute("INSERT INTO Composer (produitId, CommandeId, QteCom) " +
                        "VALUES (" + p.getIdProduit() + ", 99," + panier.getProduits().get(p) + ")");
        }
    }


    public void addProduct(Produit p, int quantite){
        Scanner sc = new Scanner(System.in);
        int hasStock =  panier.addProduct(p, quantite);
        if (hasStock > 0){
            System.out.print("Le produit est en stock mais seulement en "+hasStock +" exemplaires.");
            System.out.println("Voulez-vous les ajouter à votre panier ? (O/N)");
            String nom = sc.nextLine();
            if (nom.equals("O")){
                panier.addProduct(p, hasStock);
            }
        }
        else if (hasStock == -1){
            System.out.println("Le produit a été ajouté à votre panier");
        }
        else {
            System.out.println("Le produit n'est pas disponible dans ce magasin");
            List<Produit> recommandations = Recommandation.Recommander(profil, p, idMagasin);
            System.out.println("Voici quelques recommandations : ");
            for (Produit recommandation : recommandations){
                System.out.println("- Proposition "+ (recommandations.indexOf(recommandation)+1)+": " +recommandation.getLibelle());
            }
            System.out.println("Voulez-vous ajouter un de ces produits à votre panier ? (O/N)");
            String nom = sc.nextLine();
            if (nom.equals("O")){
                System.out.println("Entrez le numero du produit que vous voulez ajouter : ");
                String nomProduit = sc.nextLine();
                Produit produit = recommandations.get(Integer.parseInt(nomProduit)-1);

                this.addProduct(produit, quantite);
                JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());
                if (!profil.getArticlesPref().contains(produit)) {
                    this.profil.getArticlesPref().add(produit);
                    database.execute("INSERT INTO Preferer (ProduitId,ClientId) VALUES (" + produit.getIdProduit() + ", " + idClient + ")");
                }
            }
        }


    }

    public void removeProduct(Produit p, int quantite){
        panier.removeProduct(p, quantite);
    }




    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }

    public Panier getPanier() {
        if (panier == null){
            panier = new Panier(idMagasin);
        }
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

    public Profil getProfil() {
        if (profil == null){
        }
        return profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public void setIdMagasin(int idMagasin) {
        this.idMagasin = idMagasin;
        panier = new Panier(idMagasin);
    }

    public int getIdMagasin() {
        return idMagasin;
    }
}
