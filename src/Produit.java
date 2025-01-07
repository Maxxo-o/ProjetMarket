public class Produit {

    private int idProduit;
    private String libelle;
    private double prixUnitaire;
    private double prixAuKg;
    private double poids;
    private String description;
    private String categorie;
    private String marque;
    private String nutriscore;


    // CONSTRUCTEURS

    // Constructeur complet
    public Produit(int idProduit, String libelle, double prixUnitaire, double prixAuKg, double poids, String description, String categorie, String marque, String nutriscore) {
        this.idProduit = idProduit;
        this.libelle = libelle;
        this.prixUnitaire = prixUnitaire;
        this.prixAuKg = prixAuKg;
        this.poids = poids;
        this.description = description;
        this.categorie = categorie;
        this.marque = marque;
        this.nutriscore = nutriscore;
    }


    // Constructeur par l'id du produit
    public Produit(int idProduit) {
        // Recupérer dans la bd le produit
    }

    // Constructeur par copie
    public Produit(Produit produit) {
        this.idProduit = produit.idProduit;
        this.libelle = produit.libelle;
        this.prixUnitaire = produit.prixUnitaire;
        this.prixAuKg = produit.prixAuKg;
        this.poids = produit.poids;
        this.description = produit.description;
        this.categorie = produit.categorie;
        this.marque = produit.marque;
        this.nutriscore = produit.nutriscore;
    }


    @Override
    public String toString() {
        return "Le produit " +
                "d'id " + idProduit +
                ", a pour libelle '" + libelle + '\'' +
                ", a pour prix unitaire " + prixUnitaire + '€' +
                ", a pour prix au kilo " + prixAuKg +'€' +
                ", a pour poids " + poids + "kg" +
                ", a pour description '" + description + '\'' +
                ", a pour categorie '" + categorie + '\'' +
                ", a pour marque '" + marque + '\'' +
                ", et a pour nutriscore '" + nutriscore + '\'' +
                '.';
    }

    public int getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public double getPrixAuKg() {
        return prixAuKg;
    }

    public void setPrixAuKg(double prixAuKg) {
        this.prixAuKg = prixAuKg;
    }

    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getNutriscore() {
        return nutriscore;
    }

    public void setNutriscore(String nutriscore) {
        this.nutriscore = nutriscore;
    }
}
