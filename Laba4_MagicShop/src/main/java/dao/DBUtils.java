
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author elenagoncarova
 */

public class DBUtils {
    private static final String DB_NAME = "magic_wands.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_NAME;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC драйвер не найден!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        String[] createTablesSQL = {

            "CREATE TABLE IF NOT EXISTS woods (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "type TEXT NOT NULL UNIQUE)",  
            
            "CREATE TABLE IF NOT EXISTS cores (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "type TEXT NOT NULL UNIQUE)",
            
            "CREATE TABLE IF NOT EXISTS wizards (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL)",

            "CREATE TABLE IF NOT EXISTS deliveries (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "delivery_date TEXT NOT NULL)", 
            
            "CREATE TABLE IF NOT EXISTS delivery_components (" +
            "delivery_id INTEGER NOT NULL, " +
            "component_id INTEGER NOT NULL, " +
            "component_type TEXT NOT NULL CHECK(component_type IN ('wood', 'core')), " +

            "CREATE TABLE IF NOT EXISTS wands (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "wood_id INTEGER NOT NULL, " +
            "core_id INTEGER NOT NULL, " +
            "is_sold BOOLEAN NOT NULL DEFAULT FALSE, " +
            "price REAL NOT NULL CHECK(price > 0), " +  
            "FOREIGN KEY (wood_id) REFERENCES woods(id), " +
            "FOREIGN KEY (core_id) REFERENCES cores(id))"
        };
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            for (String sql : createTablesSQL) {
                stmt.execute(sql);
            }
            
            System.out.println("База данных успешно инициализирована");
            
        } catch (SQLException e) {
            System.err.println("Ошибка при инициализации базы данных:");
            e.printStackTrace();
            throw new RuntimeException("Не удалось инициализировать БД", e);
        }
    }

    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Подключение к базе данных успешно!");
                System.out.println("URL: " + DB_URL);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка подключения к базе данных:");
            e.printStackTrace();
        }
    }
}

