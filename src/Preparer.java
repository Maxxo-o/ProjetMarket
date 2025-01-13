public class Preparer {
    public static void commanedsAPreparer() {
        JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());

        database.executeQuery("""
                    SELECT *
                    FROM Commande
                    JOIN Delivrer ON Commande.CommandeId = Delivrer.CommandeId
                    WHERE EtatCom = 'En preparation'
                    ORDER BY DateLivraison
                """).forEach(System.out::println);
    }

    public static void marquerCommande(int CommandeId, String EtatCom) {
        JDBC database = new JDBC(ProjectConfig.getURL(), ProjectConfig.getUsername(), ProjectConfig.getPassword());

        database.executeUpdate(String.format("""
                UPDATE Commande
                SET EtatCom = %s
                WHERE CommandeId = %s
                """, EtatCom, CommandeId));
    }
}
