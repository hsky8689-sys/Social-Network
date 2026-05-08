package repository;
import domain.Rata;
import domain.Card;
import domain.tipRata;
import exceptii.WrongInputException;
import service.Network;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DBRepoCarduri extends DBRepoArray<Card, Rata>{
    public DBRepoCarduri(String url, String username, String password) {
        super(url, username, password);
    }
    @Override
    public boolean gaseste(Card o) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement("select * from carduri C where C.id=?")) {
            statement.setLong(1,o.getId());
            try(ResultSet result = statement.executeQuery()){
                return result.next();
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
    }
    @Override
    public boolean adauga(Card o) {
        String sql = "insert into carduri(nume,tipcard) values (?,?)";
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,o.getNumeCard());
            statement.setString(2,o.getTipCard());
            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            return false;
        }
    }
    @Override
    public boolean sterge(Card o) {
        String sql = "delete from carduri C where C.id=?";
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,o.getId());
            return statement.executeUpdate()>0;
        }catch (SQLException e){
            return false;
        }
    }
    @Override
    public List<Card> utilizatori() throws SQLException {
        String sql = "select * from carduri";
        List<Card> carduri = new ArrayList<Card>();
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()){
            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                List<Rata> rate = new ArrayList<Rata>();
                String tip = resultSet.getString("tipCard");
                carduri.add(new Card(id,nume,rate,tip));
            }
            return carduri;
        }catch (SQLException e){
            return null;
        }
    }
    @Override
    public boolean adaugaMembru(Card o, Rata p) {
        List<Card> card = gasesteDupaMembru(p);
        if(card!=null)stergeMembru(card.getFirst(),p);
        if(o.getTipCard().equals("Zburatori") && p.getTip()==tipRata.SWIMMING) throw new WrongInputException("O rata zburatoare nu poate ajunge intr-un card de inotatori");
        if(o.getTipCard().equals("Inotatori") && p.getTip()==tipRata.FLYING) throw new WrongInputException("O rata inotatoare nu poate ajunge intr-un card de zburatori");
        String sql = "insert into apartenente_carduri(id_card,id_rata) values (?,?)";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,o.getId());
            statement.setLong(2,p.getId());
            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            if (e.getSQLState().equals("23505")) {
                return false; // duplicate key
            }
            throw new RuntimeException(e); // alte probleme SQL
        }
    }
    @Override
    public boolean stergeMembru(Card o, Rata p) {
        String sql = "delete from apartenente_carduri A where A.id_card=? and A.id_rata=?";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,o.getId());
            statement.setLong(2,p.getId());
            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            return false;
        }
    }
    @Override
    public List<Card> gasesteDupaMembru(Rata p) {
        List<Card> card = new ArrayList<Card>();
        String sql = "select * from apartenente_carduri A inner join Carduri C on C.id=A.id_card where A.id_rata=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setLong(1, p.getId());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet==null)throw new SQLException("Nu exista cardul");
            while(resultSet.next()){
                Long id = resultSet.getLong("id_card");
                String nume = resultSet.getString("nume");
                String tip = resultSet.getString("tipcard");
                card.add(new Card(id,nume,new ArrayList<Rata>(),tip));
            }
        } catch (SQLException e) {
            return null;
        }
        return card;
    }
    @Override
    public boolean stergeMembruDinToate(Rata p) {
        return false;
    }
    @Override
    public List<Rata> gasesteMembrii(Card o, Network network) {
        List<Rata> rate = new ArrayList<Rata>();
        String sql = "select A.id_rata from apartenente_carduri A inner join utilizatori U ON U.id = A.id_rata where A.id_card = ?";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1,o.getId());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Long idRata = resultSet.getLong("id_rata");
                Rata rata = network.getAdminRate().getById(idRata);
                if(rata!=null)rate.add(rata);
            }
            return rate;
        }catch (SQLException e){
            return null;
        }
    }
}
