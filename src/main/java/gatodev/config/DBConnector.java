package gatodev.config;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

@Getter
public class DBConnector {
    //Objetos
    private final Connection con;
    public static final DBConnector dbConnector = new DBConnector();
    private final Logger log = LoggerFactory.getLogger(DBConnector.class);

    //Credenciales
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "agenda";

    private DBConnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            //Creación de la base de datos automática
            try (Connection temp = DriverManager.getConnection(URL, USER, PASSWORD);
                 Statement st = temp.createStatement()) {

                //Consulta si la DB ya existe
                String query = "SHOW DATABASES LIKE '" + DB_NAME + "'";
                ResultSet rs = st.executeQuery(query);
                if (!rs.next()) {
                    st.executeUpdate("CREATE DATABASE " + DB_NAME);
                    log.info("DB {} creada con éxito.", DB_NAME);
                }
            }

            con = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
