package repository;
import domain.Friendship;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DBRepoPrietenii extends DBRepository<Friendship> {
    public DBRepoPrietenii(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public boolean adauga(Friendship o) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert into prietenii(id_user1,id_user2) values (?,?)")) {
            statement.setLong(1, o.getFirst());
            statement.setLong(2, o.getSecond());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean sterge(Friendship o) {
        String sql = "delete from prietenii P where (P.id_user1=? and P.id_user2=?) or (P.id_user1=? and P.id_user2=?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, o.getFirst());
            statement.setLong(4, o.getFirst());
            statement.setLong(2, o.getSecond());
            statement.setLong(3, o.getSecond());
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean gaseste(Friendship o) {
        String sql = "select * from prietenii P where (P.id_user1=? and P.id_user2=?) or (P.id_user1=? and P.id_user2=?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, o.getFirst());
            statement.setLong(4, o.getFirst());
            statement.setLong(2, o.getSecond());
            statement.setLong(3, o.getSecond());
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public List<Friendship> utilizatori() throws SQLException {
        String sql = "select * from prietenii";
        List<Friendship> prietenii = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery();) {
            while (result.next()) {
                Long id_user1 = result.getLong("id_user1");
                Long id_user2 = result.getLong("id_user2");
                prietenii.add(new Friendship(id_user1, id_user2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            return null;
        }
        return prietenii;
    }
}