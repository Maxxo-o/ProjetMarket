import java.util.ArrayList;
import java.util.HashMap;

public class Panier {
    private HashMap<Produit, Integer> produits;
    private double prixTotal;
    private final int idMagasin;
    private JDBC database;

    public Panier(int idMagasin) {
        this.produits = new HashMap<>();
        this.prixTotal = 0;
        this.idMagasin = idMagasin;
        this.database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());
    }

    public Panier(Panier p){
        this.produits = p.produits;
        this.prixTotal = p.prixTotal;
        this.idMagasin = p.idMagasin;
        this.database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());
    }


    public void addProduct(Produit p, int quantite){
        if (quantite>0){
            ArrayList<ArrayList<String>> result = database.executeQuery("SELECT * FROM Stocker WHERE produitId = " + p.getIdProduit() +" AND MagId = " + idMagasin, 3);
            if (result.isEmpty()) System.out.println("Produit non disponible dans ce magasin");
            else if (Integer.parseInt(result.getFirst().get(2)) < quantite) System.out.println("Stock insuffisant");
            else {
                produits.put(p, quantite);
                prixTotal += p.getPrixUnitaire() * quantite;
            }
        }
    }

    public void removeProduct(Produit p, int quantite){
        if (produits.containsKey(p)){
            if (produits.get(p) > quantite){
                produits.put(p, produits.get(p) - quantite);
                prixTotal -= p.getPrixUnitaire()*quantite;
            } else {
                prixTotal -= p.getPrixUnitaire()*produits.get(p);
                produits.remove(p);
            }
        }
    }

    public void clear(){
        produits.clear();
        prixTotal = 0;
    }

    public void display(){
        System.out.println("Panier : ");
        for (Produit p : produits.keySet()){
            System.out.println(p.getLibelle() + " : " + produits.get(p));
        }
        System.out.println("Prix total : " + prixTotal);
    }

    public Panier archivePanier(){
        Panier p = new Panier(this);
        this.clear();
        return p;
    }

    public void restorePanier(Panier p){
        this.produits = p.produits;
        this.prixTotal = p.prixTotal;
    }

    public void addProductFromPanier(Panier p){
        for (Produit produit : p.produits.keySet()){
            this.addProduct(produit, p.produits.get(produit));
        }
    }

    public HashMap<Produit, Integer> getProduits() {
        return produits;
    }

    public void setProduits(HashMap<Produit, Integer> produits) {
        this.produits = produits;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public int getIdMagasin() {
        return idMagasin;
    }
}
