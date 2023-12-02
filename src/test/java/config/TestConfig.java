package config;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConfig {
    public static class H2DatabaseConfig {
        public static Connection createConnection() {
            try {
                JdbcDataSource dataSource = new JdbcDataSource();
                dataSource.setURL("jdbc:h2:mem:testdb");
                dataSource.setUser("sa");
                dataSource.setPassword("password");
                Connection connection = dataSource.getConnection();
                connection.createStatement().execute(
                        " create table voiture (\n" +
                                "                         \tid integer primary key auto_increment,\n" +
                                "                         \tmarque varchar(30) not null,\n" +
                                "                         \tmodele varchar(30) not null,\n" +
                                "                         \tfinition varchar(30) not null,\n" +
                                "                         \tcarburant char,\n" +
                                "                         \tkm integer,\n" +
                                "                         \tannee integer,\n" +
                                "                         \tprix integer\n" +
                                "                         );");
                connection.createStatement().execute("INSERT INTO voiture (id, marque, modele, finition, carburant, km, annee, prix) " +
                        "VALUES (1, 'Toyota', 'C4 Picasso', 'LE', 'E', 50000, 2020, 25000)");
                return connection;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to create H2 database connection.", e);
            }
        }
    }

}
