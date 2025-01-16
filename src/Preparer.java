public class Preparer {
    public static void commanedsAPreparer(JDBC database, String EtatCom) {
        database.executeQuery(String.format("""
                    SELECT Commande.*, DateLivraison, ModeLivraison
                    FROM Commande
                    JOIN Delivrer ON Commande.CommandeId = Delivrer.CommandeId
                    WHERE EtatCom = %s
                    ORDER BY DateLivraison
                """, EtatCom)).forEach(System.out::println);
    }

    public static void marquerCommande(JDBC database, int CommandeId, String EtatCom) {
        String fin = EtatCom.equals("Finalisee") ? "HeureFinCommande = SYSDATE," : "";
        database.executeUpdate(String.format("""
                UPDATE Commande
                SET %sEtatCom = %s
                WHERE CommandeId = %s
                """, fin, EtatCom, CommandeId));
    }
}
