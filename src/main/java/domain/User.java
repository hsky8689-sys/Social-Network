package domain;
import service.Network;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Objects;
public abstract class User<T> implements userActions, Serializable {
    private T id;
    private String username;
    private String email;
    private String password;
    public User(T id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public String getPassword() {return this.password;}
    @Override
    public boolean login(Network where,String user,String pass) throws SQLException {
        return where.tryLogin(user, pass) != null;
    }
    @Override
    public boolean logout(Network where) {
        return where.tryLogout();
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password);
    }
    public String getUsername() {
        return this.username;
    }
    public String getEmail(){return this.email;}
    public T getId() {return id;}
}
