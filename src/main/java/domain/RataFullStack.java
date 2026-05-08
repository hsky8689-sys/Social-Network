package domain;
public class RataFullStack extends Rata implements Zburator,Inotator{
    public RataFullStack(Long id, String username, String email, String password, tipRata tip, Long id_card, Long viteza, Long rezistenta) {
        super(id, username, email, password, tip, id_card, viteza, rezistenta);
    }
    @Override
    public void zboara() {
        System.out.println("Rata "+super.getUsername()+" a inceput sa zboare");
    }
    @Override
    public void inoata() {
        System.out.println("Rata "+super.getUsername()+" a inceput sa inoate");
    }
}
