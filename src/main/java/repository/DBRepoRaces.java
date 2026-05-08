package repository;
import domain.*;
import exceptii.WrongInputException;
import service.Network;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DBRepoRaces extends DBRepoArray<RaceEvent, User> {
    public DBRepoRaces(String url, String username, String password) {
        super(url, username, password);
    }
    @Override
    public boolean gaseste(RaceEvent o) {
        String sql = "select * from curse C where C.id=?";
        try(Connection connection= DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,o.getId());
            try(ResultSet result = statement.executeQuery()){
                return result.next();
            }
        }catch (SQLException e){
            return false;
        }
    }
    public boolean eDejaAbonat(RaceEvent o,User p){
        String sql = "select * from bilete_curse C where C.id_cursa=? and C.id_spectator=?";
        try(Connection connection= DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,o.getId());
            statement.setLong(2,(Long)p.getId());
            try(ResultSet result = statement.executeQuery()){
                return result.next();
            }
        }catch (SQLException e){
            return false;
        }
    }
    public boolean eDejaParticipant(RaceEvent o,User p){
        String sql = "select * from participari_curse C where C.id_cursa=? and C.id_participant=?";
        try(Connection connection= DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1,o.getId());
            statement.setLong(2,(Long)p.getId());
            try(ResultSet result = statement.executeQuery()){
                return result.next();
            }
        }catch (SQLException e){
            return false;
        }
    }
    public boolean adaugaCuloar(RaceEvent o,Lane l){
        String sql="insert into culoare(distanta,id_rata,id_cursa) values (?,?,?)";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1,l.getDistance());
            preparedStatement.setLong(2,-1L);
            preparedStatement.setLong(3,o.getId());
            return preparedStatement.executeUpdate()>0;
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
    }
    public List<User> abonati(RaceEvent o){
        String sql = "select * from bilete_curse B inner join utilizatori U on U.id=B.id_spectator where B.id_eveniment = ? ";
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
    public boolean adauga(RaceEvent o) {
        String sql = "insert into curse(nume,id_host,nr_culoare) values (?,?,?)";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,o.getNume());
            statement.setLong(2,o.getIdHost());
            statement.setInt(3,o.getNrCuloare());
            return statement.executeUpdate()>0;
        }catch (SQLException e){
            return false;
        }
    }
    @Override
    public boolean sterge(RaceEvent o) {
        return false;
    }
    @Override
    public List<RaceEvent> utilizatori(){
        String sql = "select * from curse";
        List<RaceEvent> evenimente = new ArrayList<RaceEvent>();
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()){
            while(resultSet.next()){
                Long idEveniment = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                Long idHost = resultSet.getLong("id_host");
                int nrCuloare = resultSet.getInt("nr_culoare");
                evenimente.add(new RaceEvent(idEveniment,nume,idHost,nrCuloare));
            }
            return evenimente;
        }catch (SQLException e){
            return null;
        }
    }
    public boolean inscrieParticipant(RaceEvent o, User p) {
        if(eDejaAbonat(o,p))return false;
        String sql = "insert into participari_curse(id_cursa,id_participant) values(?,?)";
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
    public boolean retrageParticipant(RaceEvent o, User p) {
        String sql = "delete from participari_curse where id_cursa=? and id_participant=?";
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
    public boolean aboneaza(RaceEvent o, User p) {
        if(eDejaParticipant(o,p))return false;
        String sql = "insert into bilete_curse(id_cursa,id_spectator) values(?,?)";
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
    public boolean dezaboneaza(RaceEvent o, User p) {
        String sql = "delete from bilete_curse where id_cursa=? and id_spectator=?";
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
    public boolean adaugaMembru(Event o, User p) {
        return false;
    }
    public boolean stergeMembru(Event o, User p) {
        return false;
    }
    @Override
    public boolean adaugaMembru(RaceEvent o, User p) {
        return false;
    }
    @Override
    public boolean stergeMembru(RaceEvent o, User p) {
        return false;
    }
    @Override
    public List<RaceEvent> gasesteDupaMembru(User p) {
        return List.of();
    }
    @Override
    public boolean stergeMembruDinToate(User p) {
        return false;
    }
    public List<Lane> gasesteCuloare(RaceEvent o) {
        String sql = "select * from culoare C where C.id_cursa=?";
        List<Lane> lanes = new ArrayList<Lane>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, o.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                int distanta = resultSet.getInt("distanta");
                lanes.add(new Lane(id,distanta));
            }
            return lanes;
        } catch (SQLException e) {
            return null;
        }
    }
    public List<User> gasesteMembrii(RaceEvent o, Network network) {
        String sql = "select * from utilizatori U inner join participari_curse PC on PC.id_participant=U.id where PC.id_cursa=?";
        List<User> participanti=new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             preparedStatement.setLong(1,o.getId());
             ResultSet resultSet = preparedStatement.executeQuery();
             while(resultSet.next()){
                 Long id = resultSet.getLong("id");
                 String username = resultSet.getString("username");
                 String email = resultSet.getString("email");
                 String password = resultSet.getString("pass");
                 Long viteza = resultSet.getLong("viteza");
                 Long rezistenta = resultSet.getLong("rezistenta");
                 String tipRata = resultSet.getString("tiprata");
                 switch (tipRata){
                     case "SWIMMING"->{participanti.add(new RataInotatoare(id,username,email,password, domain.tipRata.SWIMMING,-1L,viteza,rezistenta));break;}
                     case "FLYING_AND_SWIMMING"->{participanti.add(new RataFullStack(id,username,email,password, domain.tipRata.FLYING_AND_SWIMMING,-1L,viteza,rezistenta));break;}
                 }
             }
             return participanti;
        } catch (SQLException e) {
            return null;
        }
    }
}
