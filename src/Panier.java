import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Panier {
    private HashMap<Produit, Integer> produits;
    private double prixTotal;
    private final int idMagasin;
    private JDBC database;
    private Timestamp start;

    public Panier(int idMagasin, JDBC database) {
        this.produits = new HashMap<>();
        this.prixTotal = 0;
        this.idMagasin = idMagasin;
        this.database = database;
        this.start = Timestamp.valueOf(LocalDateTime.now());
    }

    public Panier(Panier p) {
        this.produits = p.produits;
        this.prixTotal = p.prixTotal;
        this.idMagasin = p.idMagasin;
        this.database = p.database;
        this.start = p.start;
    }


    /*
     La méthode retourne 0 si le produit n'est pas disponible dans le magasin ou si la quantité demandée est négative ou nulle,
     la quantité restante si le stock est insuffisant,
     -1 si le produit a été ajouté correctement
     */
    public int addProduct(Produit p, int quantite){
        if (quantite>0){
            List<List<String>> result = database.executeQuery("SELECT * FROM Stocker WHERE produitId = " + p.getIdProduit() +" AND MagId = " + idMagasin);
            if (result.isEmpty())
            {
                System.out.println("Produit non disponible dans ce magasin");
                return 0;
            }
            else if (Integer.parseInt(result.getFirst().get(2)) < quantite) {
                System.out.println("Stock insuffisant");
                return Integer.parseInt(result.getFirst().get(2));
            }
            else {
                produits.put(p, quantite);
                prixTotal += p.getPrixUnitaire() * quantite;
                return -1;
            }
        }
        return 0;
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
