import java.util.List;

public class Preparer {
    public static void commanedsAPreparer(JDBC database, String EtatCom) {
        List<List<String>> list = database.executeQuery("SELECT Commande.*, DateLivraison, ModeLivraison\n" +
                "FROM Commande\n" +
                "JOIN Delivrer ON Commande.CommandeId = Delivrer.CommandeId\n" +
                "WHERE EtatCom = '" + EtatCom + "'\n" +
                "ORDER BY DateLivraison");
        for (List<String> row : list) {
            System.out.printf("""
                    Commande %s
                    Date de livraison : %s
                    Mode de livraison : %s
                    """, row.get(0), row.get(6), row.get(7));
        }
    }

    public static void marquerCommande(JDBC database, int CommandeId, String EtatCom) {
        String fin = EtatCom.equals("Finalisee") ? "HeureFinCommande = SYSDATE," : "";
        database.executeUpdate(String.format("""
                UPDATE Commande
                SET %sEtatCom = '%s'
                WHERE CommandeId = %s
                """, fin, EtatCom, CommandeId));
    }
}
