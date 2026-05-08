package repository;
import domain.*;
import exceptii.WrongInputException;
import service.Network;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DBRepoEvents extends DBRepoArray<Event,User> implements EventRepoActions<Event, User>{
    public DBRepoEvents(String url, String name, String password) {
        super(url,name,password);
    }
    @Override
    public boolean gaseste(Event o) {
        String sql = "select * from evenimente E where W.id=?";
        try(Connection connection=DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,o.getId());
            try(ResultSet result = statement.executeQuery()){
                return result.next();
            }
        }catch (SQLException e){
            return false;
        }
    }
    @Override
    public boolean adauga(Event o) {
        String sql = "insert into evenimente(nume,id_host) values (?,?)";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,o.getNume());
            statement.setLong(2,o.getIdHost());
            return statement.executeUpdate()>0;
        }catch (SQLException e){
            return false;
        }
    }
    @Override
    public boolean sterge(Event o) {
        return false;
    }
    @Override
    public List<Event> utilizatori(){
        String sql = "select * from evenimente";
        List<Event> evenimente = new ArrayList<Event>();
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()){
            while(resultSet.next()){
                Long idEveniment = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                Long idHost = resultSet.getLong("id_host");
                evenimente.add(new Event(idEveniment,nume,idHost));
            }
            return evenimente;
        }catch (SQLException e){
            return null;
        }
    }
    @Override
    public boolean inscrieParticipant(Event o, User p) {
        String sql = "insert into concurari_evenimente(id_eveniment,id_concurent) values(?,?)";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setLong(1,o.getId());
            statement.setLong(2,(Long)p.getId());
            return statement.executeUpdate()>0;
        }catch (SQLException e){
            if (e.getSQLState().equals("23505")) {
                System.err.println("exista deja participantul");
                return false; // duplicate key
            }
            throw new WrongInputException("sql crapa");
        }
    }
    @Override
    public boolean retrageParticipant(Event o, User p) {
        String sql = "delete from concurari_evenimente where id_eveniment=? and id_concurent=?";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1,o.getId());
            statement.setLong(2,(Long)p.getId());
            try(ResultSet result = statement.executeQuery()){
                return result.next();
            }
        }catch (SQLException e){
            return false;
        }
    }
    @Override
    public boolean aboneaza(Event o, User p) {
        String sql = "insert into bilete_evenimente(id_eveniment,id_spectator) values(?,?)";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setLong(1,o.getId());
            statement.setLong(2,(Long)p.getId());
            return statement.executeUpdate()>0;
        }catch (SQLException e){
            if (e.getSQLState().equals("23505")) {
                System.err.println("exista deja abonatul");
                return false; // duplicate key
            }
            return false;
        }
    }
    public List<User> abonati(Event o){
        String sql = "select * from bilete_evenimente B inner join utilizatori U on U.id=B.id_spectator where B.id_eveniment = ? ";
        List<User> useri = new ArrayList<User>();
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setLong(1,o.getId());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String pass = resultSet.getString("pass");
                Long viteza = resultSet.getLong("viteza");
                Long rezistenta = resultSet.getLong("rezistenta");
                String tipRata = resultSet.getString("tiprata");
                Date dataNastere = resultSet.getDate("datanastere");
                String ocupatie = resultSet.getString("ocupatie");
                if(nume==null){
                    tipRata tip = domain.tipRata.SWIMMING;
                    if(tipRata=="FLYING") {
                        tip = domain.tipRata.FLYING;
                        useri.add(new RataZburatoare(id,username,email,pass,tip,-1L,viteza,rezistenta));
                    }
                    else if(tipRata=="FLYING_AND_SWIMMING"){
                        tip= domain.tipRata.FLYING_AND_SWIMMING;
                        useri.add(new RataFullStack(id,username,email,pass,tip,-1L,viteza,rezistenta));
                    }
                    else useri.add(new RataInotatoare(id,username,email,pass,tip,-1L,viteza,rezistenta));
                }
                else useri.add(new Persoana(id,nume,prenume,dataNastere.toString(),ocupatie,email,pass));
            }
            return useri;
        }catch (SQLException e){
            return null;
        }
    }
    @Override
    public boolean dezaboneaza(Event o, User p) {
        String sql = "delete from bilete_evenimente where id_eveniment=? and id_spectator=?";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1,o.getId());
            statement.setLong(2,(Long)p.getId());
            try(ResultSet result = statement.executeQuery()){
                return result.next();
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
    }
    @Override
    public boolean adaugaMembru(Event o, User p) {
        return false;
    }
    @Override
    public boolean stergeMembru(Event o, User p) {
        return false;
    }
    @Override
    public List<Event> gasesteDupaMembru(User p) {
        return List.of();
    }
    @Override
    public boolean stergeMembruDinToate(User p) {
        return false;
    }
    @Override
    public List<User> gasesteMembrii(Event o, Network network) {
        String sql = "select E.id from evenimente E inner join ";
        return null;
    }
}