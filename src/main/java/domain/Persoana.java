package domain;

import service.Network;

import java.sql.SQLException;

public class Persoana extends User{
    private String nume,prenume,dataNastere,ocupatie;
    private int empatie;
    public Persoana(Long id,String nume,String prenume,String dataNastere,String ocupatie,String email,String password) {
        super(id,nume+' '+prenume,email,password);
        this.nume=nume;this.prenume=prenume;
        this.dataNastere=dataNastere;
        this.ocupatie=ocupatie;
        this.empatie=0;
    }
    @Override
    public String toString() {
        return   super.getId() + "," +
                 nume + "," +
                 prenume + "," +
                 dataNastere + "," +
                 ocupatie + "," +
                 super.getEmail()+ "," +
                 super.getPassword() + ","
                ;
    }
    public String getNume() {return nume;}
    public String getPrenume() {return prenume;}
    public String getDataNastere() {return dataNastere;}
    public String getOcupatie() {return ocupatie;}
    public int getEmpatie() {return empatie;}
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    @Override
    public boolean logout(Network where) {
        return super.logout(where);
    }
    @Override
    public boolean sendMessage(Network where,Message msg) {
        if(where==null||msg==null)return false;
        return true;
        //return msg.getReciever().recieveMessage(where,msg);
    }
    @Override
    public boolean recieveMessage(Network where,Message msg) {
        if(where==null||msg==null)return false;
        return where.getAdminMesaje().adauga(msg);
    }
    @Override
    public boolean login(Network where,String user,String pass) throws SQLException {
        return super.login(where,user,pass);
    }
}
