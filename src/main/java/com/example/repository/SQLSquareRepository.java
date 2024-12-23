package com.example.repository;


import com.example.domain.Square;
import org.sqlite.SQLiteDataSource;

import java.sql.*;


public class SQLSquareRepository extends MemoryRepository<Square> implements AutoCloseable {

    private static final String JDBC_URL =
            "jdbc:sqlite:src/main/java/com/example/square.db";

    private Connection conn = null;

    public SQLSquareRepository() {
        openConnection();
        createSchema();
        loadData();
    }

    private void loadData() {
        try {
            try (PreparedStatement statement = conn.prepareStatement("SELECT * from squares");
                 ResultSet rs = statement.executeQuery();) {
                while (rs.next()) {
                    Square square = new Square(rs.getInt("id"), rs.getString("name"),
                            rs.getInt("width"));
                    data.add(square);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void openConnection() {
        try {
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(JDBC_URL);
            if (conn == null || conn.isClosed())
                conn = ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la conectarea cu baza de date", e);
        }
    }

    private void createSchema() {
        try {
            try (final Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS squares(id int PRIMARY KEY, name varchar(100), width int);");
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] createSchema : " + e.getMessage());
        }
    }

    @Override
    public void add(Square elem) throws RepositoryException {
        super.add(elem);
        // daca se ajunge aici, trebuie actualizata baza de date

        try {
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO squares VALUES (?, ?, ?)")) {
                statement.setInt(1, elem.getId());
                statement.setString(2, elem.getName());
                statement.setInt(3, elem.getWidth());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Eroare la salvarea in baza de date", e);
        }
    }

    @Override
    public void close() throws Exception {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            // In metoda close e descurajata aruncarea de exceptii
            e.printStackTrace();
        }
    }
}
