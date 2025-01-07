
public class Main {
    public static void main(String[] args) {
        JDBC database = new JDBC("C##JAVA", "C##JAVA");
        database.execute("CREATE TABLE avis (\r\n" + //
                "    CodeP NUMBER(11) NOT NULL,\r\n" + //
                "    CodeMois NUMBER(11) NOT NULL,\r\n" + //
                "    TempMin NUMBER(5,2),\r\n" + //
                "    TempMax NUMBER(5,2),\r\n" + //
                "    AvisMens VARCHAR2(40),\r\n" + //
                "    TempMinMoy NUMBER(5,2),\r\n" + //
                "    TempMaxMoy NUMBER(5,2),\r\n" + //
                "    PRIMARY KEY (CodeP, CodeMois)\r\n" + //
                "  )\r\n" + //
                "");
    }
}
