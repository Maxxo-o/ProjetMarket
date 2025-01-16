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
    private JDBC database;

    public Client(int idClient, String Nom, String Prenom, int Age, String sexe, String Adresse, Profil profil,
            int magasinId, JDBC database) {
        this.idClient = idClient;
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.Age = Age;
        this.sexe = sexe;
        this.Adresse = Adresse;
        this.idMagasin = magasinId;
        this.database = database;
        this.panier = new Panier(magasinId, database);
        this.profil = profil;
    }

    public Client(int idClient, int magasinId, Profil profil, JDBC database) {

        List<List<String>> result = database.executeQuery("SELECT * FROM Client WHERE clientId = " + idClient);

        this.idClient = Integer.parseInt(result.get(0).get(0));
        this.Nom = result.get(0).get(1);
        this.Prenom = result.get(0).get(2);
        this.Age = Integer.parseInt(result.get(0).get(3));
        this.sexe = result.get(0).get(4);
        this.Adresse = result.get(0).get(5);
        this.database = database;
        this.panier = new Panier(magasinId, database);
        this.idMagasin = magasinId;
        this.profil = profil;

    }

    public void validatePanier() {

        //TODO: update typeProfil et prodPref
        //TODO: demander mode de livraison

        for (Produit p : panier.getProduits().keySet()) {
            List<List<String>> result = database.executeQuery(
                    "SELECT * FROM Stocker WHERE produitId = " + p.getIdProduit() + " AND MagId = " + idMagasin);
            if (result.isEmpty()) {
                System.out.println("Produit non disponible dans ce magasin");
                return;
            } else if (Integer.parseInt(result.getFirst().get(2)) < panier.getProduits().get(p)) {
                System.out.println("Stock insuffisant");
                return;
            } else {
                database.execute("UPDATE Stocker SET QteStock = QteStock - " + panier.getProduits().get(p)
                        + " WHERE produitId = " + p.getIdProduit() + " AND MagId = " + idMagasin);
            }
        }

        String idGen = "";
        try {
            idGen = database.executeUpdateAutoGenKey(String.format("""
                        INSERT INTO Commande (HeureDebut, HeureFin, MagId, ClientId, EtatCom)
                        VALUES (CAST(TO_TIMESTAMP('%s', 'YYYY-MM-DD HH24:MI:SS.FF') AS DATE), SYSDATE, %s, %s, 'En preparation')
                    """,
                    panier.getHeureDebut(),
                    idMagasin,
                    idClient), "Commande");
        } catch (Exception e) {
            System.out.println("Erreur lors de la validation du panier");
        }

        for (Produit p : panier.getProduits().keySet()) {
            database.execute(String.format("""
                        INSERT INTO Composer (produitId, CommandeId, QteCom)
                        VALUES (%s, %s, %s)
                    """,
                    p.getIdProduit(),
                    idGen,
                    panier.getProduits().get(p)));
        }
        ClientPreferences.updatePreferer(idClient, database);
        ClientPreferences.updateAppartenirType(idClient, database);
    }

    public void addProduct(Produit p, int quantite) {
        Scanner sc = new Scanner(System.in);
        int hasStock = panier.addProduct(p, quantite);
        if (hasStock > 0) {
            System.out.print("Le produit est en stock mais seulement en " + hasStock + " exemplaires.");
            System.out.println("Voulez-vous les ajouter à votre panier ? (O/N)");
            String nom = sc.nextLine();
            if (nom.equals("O")) {
                panier.addProduct(p, hasStock);
            }
        } else if (hasStock == -1) {
            System.out.println("Le produit a été ajouté à votre panier");
        } else {
            System.out.println("Le produit n'est pas disponible dans ce magasin");
            List<Produit> prodRemplacement = Remplacement.Remplacer(profil, p, idMagasin, database);
            Statistique.NbProposeRempla++;
            System.out.println("Voici plusieurs produits de remplacement : ");
            for (Produit prodRempla : prodRemplacement) {
                System.out.println("- Proposition " + (prodRemplacement.indexOf(prodRempla) + 1) + ": "
                        + prodRempla.getLibelle());
            }
            System.out.println("Voulez-vous ajouter un de ces produits à votre panier ? (o/n)");
            String nom = sc.nextLine();
            if (nom.equalsIgnoreCase("o")) {
                System.out.println("Entrez le numero du produit que vous voulez ajouter : ");
                String nomProduit = sc.nextLine();
                Produit produit = prodRemplacement.get(Integer.parseInt(nomProduit) - 1);

                this.addProduct(produit, quantite);
                if (!profil.getArticlesPref().contains(produit)) {
                    this.profil.getArticlesPref().add(produit);
                    database.execute("INSERT INTO Preferer (ProduitId,ClientId) VALUES (" + produit.getIdProduit()
                            + ", " + idClient + ")");
                }
                Statistique.NbPChoisiRempla++;
            }
        }

    }

    public void removeProduct(Produit p, int quantite) {
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
        if (panier == null) {
            panier = new Panier(idMagasin, database);
        }
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

    public Profil getProfil() {
        if (profil == null) {
        }
        return profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public void setIdMagasin(int idMagasin) {
        this.idMagasin = idMagasin;
        panier = new Panier(idMagasin, database);
    }

    public int getIdMagasin() {
        return idMagasin;
    }
}
