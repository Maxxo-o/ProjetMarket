import java.util.ArrayList;
import java.util.List;

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

    public Client(int idClient, int magasinId){
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
            //TO DO : FAIRE SEQUENCE POUR COMMANDEID
            database.execute("INSERT INTO Commande ( DateCommande, MagId, ClientId, EtatCom) " +
                    "VALUES (SYSDATE,"+ idMagasin + ", " + idClient + " , 'En preparation')");
        }
        catch (Exception e){
            System.out.println("Erreur lors de la validation du panier");
        }


        for (Produit p : panier.getProduits().keySet()) {
            database.execute("INSERT INTO Composer (produitId, CommandeId, QteCom) " +
                        "VALUES (" + p.getIdProduit() + ", 1," + panier.getProduits().get(p) + ")");
        }
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
