package domain;

import service.Network;

import java.sql.SQLException;

public abstract class Rata extends User<Long>{
    private tipRata tip;
    private Long id_card,viteza,rezistenta;
    public Rata(Long id,String username,String email,String password,tipRata tip,Long id_card,Long viteza,Long rezistenta) {
        super(id,username,email,password);
        this.id_card=id_card;
        this.tip=tip;this.viteza=viteza;this.rezistenta=rezistenta;
    }
    public tipRata getTip() {
        return tip;
    }
    public void setTip(tipRata tip) {
        this.tip = tip;
    }
    public Long getViteza() {
        return viteza;
    }
    public void setViteza(Long viteza) {
        this.viteza = viteza;
    }
    public double getRezistenta() {
        return rezistenta;
    }
    public void setRezistenta(Long rezistenta) {
        this.rezistenta = rezistenta;
    }
    public Long getIdCard(){return id_card;}
    @Override
    public String toString() {
        String rez = super.getId() + "," +
                super.getUsername() + "," +
                getEmail() + "," +
                super.getPassword() + ",";
        if(getTip()==tipRata.FLYING)rez+="FLYING";
        if(getTip()==tipRata.SWIMMING)rez+="SWIMMING";
        if(getTip()==tipRata.FLYING_AND_SWIMMING)rez+="FLYING_AND_SWIMMING";
                rez+=","+id_card + "," +
                viteza + "," + rezistenta + ",";
                return rez;
    }
    @Override
    public boolean logout(Network where) {
        return super.logout(where);
    }
    @Override
    public boolean login(Network where,String user,String pass) throws SQLException {
        return super.login(where,user,pass);
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
    public void setCard(Long id) {
       this.id_card=id;
    }
}
