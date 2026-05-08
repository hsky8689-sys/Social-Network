package domain;

public class RataZburatoare extends Rata implements Zburator{
    public RataZburatoare(Long id, String username, String email, String password, tipRata tip, Long id_card, Long viteza, Long rezistenta) {
        super(id, username, email, password, tip, id_card, viteza, rezistenta);
    }
    @Override
    public void zboara() {
        System.out.println(getUsername()+" a inceput sa zboare");
    }
}
