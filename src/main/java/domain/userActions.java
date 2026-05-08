package domain;

import service.Network;

import java.sql.SQLException;

public interface userActions {
    boolean login(Network where,String user,String pass) throws SQLException;
    boolean logout(Network where);
    boolean sendMessage(Network where,Message msg);
    boolean recieveMessage(Network where,Message msg);
}
