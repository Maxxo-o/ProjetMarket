import java.util.List;

public class Produit {

    private int idProduit;
    private String libelle;
    private double prixAuKg;
    private double prixUnitaire;
    private double poids;
    private String nutriscore;
    private String marque;
    private boolean isBio;
    private String souCategorie;
    private String categoriePincipale;

    // CONSTRUCTEURS

    // Constructeur complet
    public Produit(int idProduit, String libelle, double prixUnitaire, double prixAuKg, double poids,
            String souCategorie, String categoriePincipale, String marque, String nutriscore, boolean bio) {
        this.idProduit = idProduit;
        this.libelle = libelle;
        this.prixUnitaire = prixUnitaire;
        this.prixAuKg = prixAuKg;
        this.poids = poids;
        this.souCategorie = souCategorie;
        this.categoriePincipale = categoriePincipale;
        this.marque = marque;
        this.nutriscore = nutriscore;
        this.isBio = bio;
    }

    public Produit(int idProduit, String libelle, double prixUnitaire, double prixAuKg, double poids,
            String souCategorie, String marque, String nutriscore, boolean bio, JDBC database) {
        this.idProduit = idProduit;
        this.libelle = libelle;
        this.prixUnitaire = prixUnitaire;
        this.prixAuKg = prixAuKg;
        this.poids = poids;
        this.souCategorie = souCategorie;
        this.marque = marque;
        this.nutriscore = nutriscore;
        this.isBio = bio;
    }

    // Constructeur par l'id du produit
    public Produit(int idProduit, JDBC database) {
        List<List<String>> result = database.executeQuery(String.format("""
                SELECT DISTINCT
                    Produit.ProduitId,
                    NomProd,
                    PrixAuKg,
                    PrixUnitaire,
                    Poids,
                    Nutriscore,
                    Marque,
                    bio,
                    cs.NomCat AS "Sou-Categorie",
                    cp.NomCat AS "Categorie Principale"
                FROM Produit
                JOIN Categorie cs ON Produit.CategorieId = cs.CategorieId
                JOIN Etre ON cs.CategorieId = Etre.CategorieId_SousCategorie
                JOIN Categorie cp ON Etre.CategorieId_Principale = cp.CategorieId
                WHERE Produit.ProduitId = %s
                """, idProduit));
        if (!result.isEmpty())
            ContructFromBd(result);
        else
            System.out.println("Produit non trouvé");
    }

    public Produit(String libelle, JDBC database) {
        List<List<String>> result = database.executeQuery(String.format("""
                SELECT DISTINCT
                    Produit.ProduitId,
                    NomProd,
                    PrixAuKg,
                    PrixUnitaire,
                    Poids,
                    Nutriscore,
                    Marque,
                    bio,
                    cs.NomCat AS "Sou-Categorie",
                    cp.NomCat AS "Categorie Principale"
                FROM Produit
                JOIN Categorie cs ON Produit.CategorieId = cs.CategorieId
                JOIN Etre ON cs.CategorieId = Etre.CategorieId_SousCategorie
                JOIN Categorie cp ON Etre.CategorieId_Principale = cp.CategorieId
                WHERE NomProd = %s
                """, libelle));
        if (!result.isEmpty())
            ContructFromBd(result);
        else
            System.out.println("Produit non trouvé");
    }

    private void ContructFromBd(List<List<String>> result) {
        this.idProduit = Integer.parseInt(result.get(0).get(0));
        this.libelle = result.get(0).get(1);
        this.prixAuKg = (result.get(0).get(2) != null) ? Double.parseDouble(result.get(0).get(2)) : 0;
        this.prixUnitaire = (result.get(0).get(3) != null) ? Double.parseDouble(result.get(0).get(3)) : 0;
        this.poids = (result.get(0).get(4) != null) ? Double.parseDouble(result.get(0).get(4)) : 0;
        this.nutriscore = result.get(0).get(5);
        this.marque = result.get(0).get(6);
        this.isBio = Boolean.parseBoolean(result.get(0).get(7));
        this.souCategorie = result.get(0).get(8);
        this.categoriePincipale = result.get(0).get(9);
    }

    // Constructeur par copie
    public Produit(Produit produit) {
        this.idProduit = produit.idProduit;
        this.libelle = produit.libelle;
        this.prixAuKg = produit.prixAuKg;
        this.prixUnitaire = produit.prixUnitaire;
        this.poids = produit.poids;
        this.nutriscore = produit.nutriscore;
        this.marque = produit.marque;
        this.isBio = produit.isBio;
        this.souCategorie = produit.souCategorie;
        this.categoriePincipale = produit.categoriePincipale;
    }

    // N'utilise pas
    public String queryPushProductToBd() {
        return "INSERT INTO Produit (NomProd, PrixAuKg, PrixUnitaire, Poids, Nutriscore, Categorie, Marque) VALUES ('"
                + libelle + "', " + prixAuKg + ", " + prixUnitaire + ", " + poids + ", '" + nutriscore + "', '"
                + souCategorie + "', '" + marque + "')";
    }

    @Override
    public String toString() {
        return "Le produit " +
                "d'id " + idProduit +
                ", a pour libelle '" + libelle + '\'' +
                ", a pour prix unitaire " + prixUnitaire + '€' +
                ", a pour prix au kilo " + prixAuKg + '€' +
                ", a pour poids " + poids + "g" +
                ", a pour categorie '" + souCategorie + '\'' +
                ", a pour marque '" + marque + '\'' +
                ", a pour nutriscore '" + nutriscore + '\'' +
                ", et est " + (isBio ? "bio" : "non-bio") +
                '.';
    }

    public boolean isBio() {
        return isBio;
    }

    public void setBio(boolean bio) {
        isBio = bio;
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

    public String getSouCategorie() {
        return souCategorie;
    }

    public void setSouCategorie(String categorie) {
        this.souCategorie = categorie;
    }

    public String getcategoriePincipale() {
        return categoriePincipale;
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
