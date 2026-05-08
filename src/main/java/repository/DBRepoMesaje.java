package repository;
import domain.Message;
import domain.User;
import service.Network;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class DBRepoMesaje extends DBRepoArray<Message, User> {
    public DBRepoMesaje(String url, String username, String password) {
        super(url, username, password);
    }
    @Override
    public boolean gaseste(Message o) {
        String sql = "select * from mesaje M where (M.id_user1=? and M.id_user2=?) or (M.id_user1=? and M.id_user2=?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, o.getSender());
            statement.setLong(3, o.getReciever());
            statement.setLong(2, o.getReciever());
            statement.setLong(4, o.getSender());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
    @Override
    public boolean adauga(Message o) {
        String sql = "insert into mesaje(id_user1,id_user2,content,time_stamp) values (?,?,?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, o.getSender());
            statement.setLong(2, o.getReciever());
            statement.setString(3, o.getContent());
            statement.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            return statement.executeUpdate() > 0;
        } catch (SQLException | RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean sterge(Message o) {
        String sql = "delete from mesaje M where M.id_user1=? or M.id_user2=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, o.getSender());
            statement.setLong(2, o.getSender());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
    @Override
    public List<Message> utilizatori() {
        String sql = "select * from mesaje";
        List<Message> mesaje = new ArrayList<Message>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery();) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long id_user1 = resultSet.getLong("id_user1");
                Long id_user2 = resultSet.getLong("id_user2");
                String continut = resultSet.getString("content");
                Date data = resultSet.getDate("time_stamp");
                mesaje.add(new Message(id, id_user1, id_user2, continut, data));
            }
        } catch (SQLException e) {
            return null;
        }
        return mesaje;
    }
    @Override
    public boolean adaugaMembru(Message o, User p) {
        return false;
    }
    @Override
    public boolean stergeMembru(Message o, User p) {
        return false;
    }
    @Override
    public List<Message> gasesteDupaMembru(User p) {
        String sql = "select * from mesaje M where M.id_user1=? or M.id_user2=?";
        List<Message> mesaje = new ArrayList<Message>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setLong(1, (Long)p.getId());
            statement.setLong(2, (Long)p.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id_mesaj = resultSet.getLong("id");
                Long id_user1 = resultSet.getLong("id_user1");
                Long id_user2 = resultSet.getLong("id_user2");
                String continut = resultSet.getString("content");
                Date data = resultSet.getDate("time_stamp");
                mesaje.add(new Message(id_mesaj, id_user1, id_user2, continut, data));
            }
        } catch (SQLException e) {
            return null;
        }
        return mesaje;
    }
    @Override
    public boolean stergeMembruDinToate(User p) {
        return false;
    }
    @Override
    public List<User> gasesteMembrii(Message o, Network network) {
        return null;
    }
}