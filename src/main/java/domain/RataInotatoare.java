package domain;

public class RataInotatoare extends Rata implements Inotator{
    public RataInotatoare(Long id, String username, String email, String password, tipRata tip, Long id_card, Long viteza, Long rezistenta) {
        super(id, username, email, password, tip, id_card, viteza, rezistenta);
    }
    @Override
    public void inoata() {
        System.out.println(getUsername()+" a inceput sa inoate");
    }
}
