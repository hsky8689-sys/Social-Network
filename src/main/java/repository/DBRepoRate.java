package repository;
import domain.*;
import java.sql.*;
import java.util.*;
public class DBRepoRate extends DBRepository<Rata> {
    public DBRepoRate(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public boolean adauga(Rata o) {
        String sqlVechi = "insert into rate (username,email,pass,tip,id_card,viteza,rezistenta) values (?,?,?,?,?,?,?)";
        String insertSQL = "insert into utilizatori(nume,prenume,username,pass,viteza,rezistenta,tipRata,dataNastere,email) values (NULL,NULL,?,?,?,?,?,NULL,?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, o.getUsername());
            statement.setString(2, o.getPassword());
            statement.setDouble(3, o.getViteza());
            statement.setDouble(4, o.getRezistenta());
            switch (o.getTip()) {
                case tipRata.SWIMMING -> {
                    statement.setString(5, "SWIMMING");
                    break;
                }
                case tipRata.FLYING -> {
                    statement.setString(5, "FLYING");
                    break;
                }
                case tipRata.FLYING_AND_SWIMMING -> {
                    statement.setString(5, "FLYING_AND_SWIMMING");
                    break;
                }
            }
            statement.setString(5, o.getTip().name());
            statement.setString(6, o.getUsername());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean sterge(Rata o) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("delete from utilizatori where id = ?")
        ) {
            statement.setLong(1, o.getId());
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean gaseste(Rata o) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from utilizatori U where U.id = ?")) {
            statement.setLong(1, o.getId());
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public List<Rata> utilizatori() {
        ArrayList<Rata> rate = new ArrayList<Rata>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from utilizatori U where U.viteza is not null");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("pass");
                String tipString = resultSet.getString("tipRata");
                Long viteza = resultSet.getLong("viteza");
                Long rezistenta = resultSet.getLong("rezistenta");
                tipRata tip = null;
                Rata rata = null;
                switch (tipString) {
                    case "SWIMMING" -> {
                        tip = tipRata.FLYING_AND_SWIMMING;
                        rata = new RataInotatoare(id, username, email, password, tip, -1L, viteza, rezistenta);
                        break;
                    }
                    case "FLYING" -> {
                        tip = tipRata.FLYING;
                        rata = new RataZburatoare(id, username, email, password, tip, -1L, viteza, rezistenta);
                        break;
                    }
                    case "FLYING_AND_SWIMMING" -> {
                        tip = tipRata.FLYING_AND_SWIMMING;
                        rata = new RataFullStack(id, username, email, password, tip, -1L, viteza, rezistenta);
                        break;
                    }
                }
                rate.add(rata);
            }
            return rate;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}