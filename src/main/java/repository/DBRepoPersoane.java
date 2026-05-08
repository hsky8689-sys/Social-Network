package repository;

import domain.Persoana;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DBRepoPersoane extends DBRepository<Persoana>{
    public DBRepoPersoane(String url, String username, String password) {
        super(url, username, password);
    }
    @Override
    public boolean adauga(Persoana o) {
        String sqlNou = "insert into utilizatori(nume,prenume,username,pass,viteza,rezistenta,tipRata,dataNastere,email) values (?,?,?,?,NULL,NULL,NULL,?,?)";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sqlNou);
            ){
            statement.setString(1,o.getNume());statement.setString(2,o.getPrenume());
            statement.setString(3,o.getUsername());statement.setString(4,o.getPassword());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            java.time.LocalDate date = LocalDate.parse(o.getDataNastere(),formatter);
            Date sqlDate = Date.valueOf(date);
            statement.setDate(5,sqlDate);
            statement.setString(6,o.getEmail());
            return statement.executeUpdate() > 0;
        }
        catch (RuntimeException | SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean sterge(Persoana o) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement("delete from utilizatori where id=?")) {
            statement.setLong(1,(Long)o.getId());
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted>0;
        }catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    @Override
    public boolean gaseste(Persoana o) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement("select * from utilizatori U where U.id=?")) {
            statement.setLong(1,(Long)o.getId());
            try(ResultSet result = statement.executeQuery()){
                return result.next();
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
    }
    @Override
    public List<Persoana> utilizatori(){
        ArrayList<Persoana> persoane = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement("select * from utilizatori U where U.viteza is null");
            ResultSet result = statement.executeQuery();){
            while (result.next()){
                Long id = result.getLong("id");
                String nume = result.getString("nume");
                String prenume = result.getString("prenume");
                String dataNastere = result.getString("dataNastere");
                String ocupatie = result.getString("ocupatie");
                String email = result.getString("email");
                String pass = result.getString("pass");
                Persoana om = new Persoana(id,nume,prenume,dataNastere,ocupatie,email,pass);
                persoane.add(om);
            }
            return persoane;
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return null;
        }
    }
}
