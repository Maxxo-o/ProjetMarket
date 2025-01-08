import java.util.ArrayList;

public class Client {
    private int idClient;
    private String Nom;
    private String Prenom;
    private int Age;
    private String Adresse;
    private Panier panier;
    private Profil profil;
    private String sexe;


    public Client(int idClient, String Nom, String Prenom, int Age, String sexe, String Adresse, Profil profil) {
        this.idClient = idClient;
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.Age = Age;
        this.sexe = sexe;
        this.Adresse = Adresse;
        this.panier = new Panier();
        this.profil = profil;
    }

    public Client(int idClient){
        JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());

        ArrayList<ArrayList<String>> result = database.executeQuery("SELECT * FROM Client WHERE clientId = " + idClient, 6);

        System.out.println(result);

        this.idClient = Integer.parseInt(result.get(0).get(0));
        this.Nom = result.get(0).get(1);
        this.Prenom = result.get(0).get(2);
        this.Age = Integer.parseInt(result.get(0).get(3));
        this.sexe = result.get(0).get(4);
        this.Adresse = result.get(0).get(5);
    }



    public void validatePanier(){
        JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());

        try {

            //TO DO : FAIRE SEQUENCE POUR COMMANDEID
            database.execute("INSERT INTO Commande (commandeId, DateCommande, MagId, ClientId) " +
                    "VALUES (1, SYSDATE, 1, " + idClient + ")");
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
            panier = new Panier();
        }
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

    public Profil getProfil() {
        if (profil == null){
            profil = new Profil();
        }
        return profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }
}
