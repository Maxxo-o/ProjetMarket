import java.util.ArrayList;
import java.util.List;

public class Profil {
    private ArrayList<String> nomProfils;
    private String categoriePref;
    private String marquePref;
    private String nutriscorePref;
    private double prixMoyArt;
    private double prixMaxArt;
    private double prixMinArt;
    private List<Produit> ArticlesPref;
    private int idClient;
    private JDBC database;


    public Profil(int idClient) {
        this.idClient = idClient;
        this.nomProfils = new ArrayList<>();
        this.ArticlesPref = new ArrayList<>();
        database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());
        List<List<String>> result = database.executeQuery("SELECT p.* FROM TypesDeProfil p, Appartenir_Type t WHERE t.clientId = " + idClient+ " AND p.ProfilId = t.ProfilId" );
        setArticlesPref();

    }

    /* METHODES :
    Recupérer dans préférer la liste des produits les plus achetés par le client
    Recupérer dans préférer la liste des produits les plus achetés par les clients ayant le même profil
    GET :
    categorie pref
    marque pref
    nutriscore pref
    prixMoyArt


     */


    public ArrayList<String> getNomProfils() {
        return nomProfils;
    }

    public void setCategoriePref() {
        List<List<String>> result = requetePref("Categorie");
        if (!result.isEmpty()) categoriePref = result.get(0).get(0);
    }

    public void setMarquePref() {
        List<List<String>> result = requetePref("Marque");
        if (!result.isEmpty()) marquePref = result.get(0).get(0);
    }

    public void setNutriscorePref() {
        List<List<String>> result = requetePref("nutriscore");
        if (!result.isEmpty()) nutriscorePref = result.get(0).get(0);
    }

    public List<List<String>> requetePref(String type) {
        return database.executeQuery("SELECT * FROM (" +
                "    SELECT p." + type + ", count(c.ProduitId) AS TotalQuantite" +
                "    FROM Client cl, Commande co, Composer c, Produit p" +
                "    WHERE " +
                "        cl.ClientId = " + idClient +
                "        AND cl.ClientId = co.ClientId" +
                "        AND co.CommandeId = c.CommandeId" +
                "        AND c.ProduitId = p.ProduitId" +
                "    GROUP BY " +
                "        p." + type + "" +
                "    ORDER BY " +
                "        TotalQuantite DESC" +
                ") " +
                "WHERE ROWNUM = 1");
    }

    public String getCategoriePref() {
        return categoriePref;
    }

    public String getMarquePref() {
        return marquePref;
    }

    public String getNutriscorePref() {
        return nutriscorePref;
    }

    // A MEDITER
    /*public void setPrixMoyArt() {
        List<List<String>> result = database.executeQuery("SELECT AVG(prixUnitaire) FROM Produit p Commande c Composer co WHERE c.ClientId = " + idClient + " AND co.ProduitId = p.ProduitId AND co.CommandeId = c.CommandeId");
        System.out.println(result);
        //if (!result.isEmpty()) prixMoyArt = Double.parseDouble(result.get(0).get(0));
    }*/

    public double getPrixMoyArt() {
        return prixMoyArt;
    }

    /*public void setPrixMaxArt() {
        List<List<String>> result = database.executeQuery("SELECT MAX(prixUnitaire) FROM Produit p Commande c Composer co WHERE c.ClientId = " + idClient + " AND co.ProduitId = p.ProduitId AND co.CommandeId = c.CommandeId");
        if (!result.isEmpty()) prixMaxArt = Double.parseDouble(result.get(0).get(0));
    }

    public double getPrixMaxArt() {
        return prixMaxArt;
    }

    public void setPrixMinArt() {
        List<List<String>> result = database.executeQuery("SELECT MIN(prixUnitaire) FROM Produit p Commande c Composer co WHERE c.ClientId = " + idClient + " AND co.ProduitId = p.ProduitId AND co.CommandeId = c.CommandeId");
        if (!result.isEmpty()) prixMinArt = Double.parseDouble(result.get(0).get(0));
    }

    public double getPrixMinArt() {
        return prixMinArt;
    }*/

    public void setArticlesPref() {
        List<List<String>> result = database.executeQuery("SELECT ProduitId From Preferer WHERE ClientId = " + idClient);
        for (List<String> l : result) {
            ArticlesPref.add(new Produit(Integer.parseInt(l.get(0))));
        }
    }

    public List<Produit> getArticlesPref() {
        if (ArticlesPref == null) ArticlesPref = new ArrayList<>();
        return ArticlesPref;
    }

    public void setNomProfil() {
        List<List<String>> result = database.executeQuery("SELECT NomProfil FROM TypesDeProfil p, Appartenir_Type a WHERE a.ClientId = " + idClient + " AND a.ProfilId = p.ProfilId");
        for (List<String> l : result) {
            nomProfils.add(l.get(0));
        }
    }

    public ArrayList<String> getNomProfil() {
        return nomProfils;
    }

}
