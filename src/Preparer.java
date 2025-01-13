public class Preparer {
    public static void commanedsAPreparer(JDBC database) {
        database.executeQuery("""
                    SELECT Commande.*, DateLivraison
                    FROM Commande
                    LEFT JOIN Delivrer ON Commande.CommandeId = Delivrer.CommandeId
                    WHERE EtatCom = 'En preparation'
                    ORDER BY DateLivraison
                """).forEach(System.out::println);
    }

    public static void marquerCommande(JDBC database, int CommandeId, String EtatCom) {
        database.executeUpdate(String.format("""
                UPDATE Commande
                SET EtatCom = %s
                WHERE CommandeId = %s
                """, EtatCom, CommandeId));
    }
}
