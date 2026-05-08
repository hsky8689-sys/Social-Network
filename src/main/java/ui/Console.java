package ui;
import service.Network;
import domain.*;
import exceptii.AlreadyExistentEntityException;
import exceptii.NullEntityException;
import exceptii.WrongCommandException;
import exceptii.WrongInputException;
import validatori.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import static ui.menuPage.Default_menu;
import static ui.menuPage.Login;
public class Console {
    private menuPage currentPage;
    private Network network;
    public Console(Network network) {
        this.network = network;
        currentPage = menuPage.Default_menu;
    }
    private void AfiseazaOptiuni() {
        switch (currentPage) {
            case Default_menu -> {
                System.out.println("1.Rate");
                System.out.println("2.Persoane");
                System.out.println("3.Prietenii");
                System.out.println("4.Mesagerie");
                System.out.println("5.Evenimente");
                System.out.println("6.Carduri");
                System.out.println("7.Curse");
                System.out.println("8.Login");
                System.out.println("9.Iesire");
            }
            case CRUD_RATE, CRUD_PERSOANE -> {
                System.out.println("1.Creare utilizatori");
                System.out.println("2.Stergere utilizatori");
                System.out.println("3.Listare utilizatori");
                System.out.println("4.Revenire la meniul principal");
                break;
            }
            case Prietenii -> {
                System.out.println("1.Adauga prieten");
                System.out.println("2.Sterge prieten");
                System.out.println("3.Cauta user dupa nume");
                System.out.println("4.Afiseaza numarul de comunitati");
                System.out.println("5.Afiseaza cea mai sociabila comunitate");
                System.out.println("6.Revenire la meniul principal");
            }
            case Mesagerie -> {
                System.out.println("1.Trimite mesaj");
                System.out.println("2.Sterge mesaj");
                System.out.println("3.Vizualizeaza istoric mesaje");
                System.out.println("4.Revenire la meniul principal");
            }
            case Evenimente -> {
                System.out.println("1.Adauga eventimente");
                System.out.println("2.Aboneaza useri la evenimente");
                System.out.println("3.Dezaboneaza useri de la evenimente");
                System.out.println("4.Inscrie useri la evenimente");
                System.out.println("5.Retrage useri inscrisi");
                System.out.println("6.Listeaza evenimente");
                System.out.println("7.Ruleaza evenimente");
                System.out.println("8.Revenire la meniul principal");
                break;
            }
            case Carduri_de_rate -> {
                System.out.println("1.Creeaza card");
                System.out.println("2.Listeaza carduri");
                System.out.println("3.Adaugare rate in card");
                System.out.println("4.Eliminare rate din card");
                System.out.println("5.Performanta medie");
                System.out.println("6.Revenire la meniul principal");
                break;
            }
            case Simulare_cursa -> {
                System.out.println("1.Adauga curse");
                System.out.println("2.Aboneaza useri la curse");
                System.out.println("3.Dezaboneaza useri de la curse");
                System.out.println("4.Inscrie useri la curse");
                System.out.println("5.Retrage useri curse");
                System.out.println("6.Listeaza curse");
                System.out.println("7.Ruleaza curse");
                System.out.println("8.Revenire la meniul principal");
                break;
            }
            case Login -> {
                if (network.getLogged_in() == null) System.out.println("Introduceti credentialele de logare:");
                else {
                    System.out.println("1.Reveniti la meniul principal");
                    System.out.println("2.Delogare");
                }
                break;
            }
        }
    }
    private Rata CreazaRata(Scanner sc){
            Rata r = null;
            String username, email, password,tip,idCard,viteza,rezistenta;
            System.out.println("USERNAME:");
            username = sc.nextLine();
            System.out.println("EMAIL:");
            email = sc.nextLine();
            System.out.println("PAROLA:");
            password = sc.nextLine();
            System.out.println("TIP(FLYING/SWIMMING/FLYING_AND_SWIMMING):");
            tip = sc.nextLine();
            System.out.println("ID-UL CARDULUI DIN CARE FACE PARTE(-1 daca din niciunul):");
            idCard = sc.nextLine();
            System.out.println("VITEZA:");
            viteza = sc.nextLine();
            System.out.println("REZISTENTA:");
            rezistenta = sc.nextLine();
            String erori=network.getAdminRate().valideaza(new String[]{"1",username,email,password,tip,idCard,viteza,rezistenta});
            if(erori.length()>0) {
                System.err.println(erori);
                return null;
            }
            else{
                switch (tip){
                    case "SWIMMING"->{
                        r = new RataInotatoare(0L, username, email, password, tipRata.SWIMMING, Long.parseLong(idCard), (long) Double.parseDouble(viteza), (long) Double.parseDouble(rezistenta));
                    }
                    case "FLYING"->{
                        r = new RataZburatoare(0L, username, email, password, tipRata.FLYING, Long.parseLong(idCard), (long) Double.parseDouble(viteza), (long) Double.parseDouble(rezistenta));
                    }
                    case "FLYING_AND_SWIMMING"->{
                        r = new RataFullStack(0L,username,email,password,tipRata.FLYING_AND_SWIMMING,Long.parseLong(idCard),(long) Double.parseDouble(viteza),(long)Double.parseDouble(rezistenta));
                    }
                }
            }
            return r;
    }
    private Persoana CreazaPersoana(Scanner sc){
            String nume, prenume, dataNastere, ocupatie, email, password;
            System.out.println("NUME:");
            nume = sc.nextLine();
            System.out.println("PRENUME:");
            prenume = sc.nextLine();
            System.out.println("DATA NASTERII IN FORMAT DD-MM-YYYY:");
            dataNastere = sc.nextLine();
            System.out.println("OCUPATIE:");
            ocupatie = sc.nextLine();
            System.out.println("EMAIL:");
            email = sc.nextLine();
            System.out.println("PAROLA:");
            password = sc.nextLine();
            String erori = network.getAdminPersoane().valideaza(new String[]{nume,prenume,dataNastere,ocupatie,email,password});
            if(erori.length()>0){
                System.err.println(erori);
                return null;
            }
            return new Persoana(0L, nume, prenume, dataNastere, ocupatie, email, password);
    }
    public void Run() throws SQLException {
        String crt;
        Scanner sc = new Scanner(System.in);
        while (true) {
            AfiseazaOptiuni();
            switch (currentPage) {
                case Default_menu -> {
                    System.out.println("Alegeti o optiune");
                    try {
                        crt = sc.nextLine();
                        int opt = Integer.parseInt(crt);
                        switch (opt) {
                            case 1 -> {
                                if (network.getLogged_in() != null) {
                                    currentPage = menuPage.CRUD_RATE;
                                } else System.out.println("Doar un utilizator logat poate adauga alti utilizatori");
                                break;
                            }
                            case 2 -> {
                                if (network.getLogged_in() != null) {
                                    currentPage = menuPage.CRUD_PERSOANE;
                                } else System.out.println("Doar un utilizator logat poate adauga alti utilizatori");
                                break;
                            }
                            case 3 -> {
                                if (network.getLogged_in() != null) {
                                    currentPage = menuPage.Prietenii;
                                } else System.out.println("Doar un utilizator logat poate adauga/sterge prieteni");
                                break;
                            }
                            case 4 -> {
                                if(network.getLogged_in()!=null){
                                currentPage = menuPage.Mesagerie;
                                }
                                else System.out.println("Doar un utilizator logat poate trimite/vizualiza msesja");
                                break;
                            }
                            case 5 -> {
                                if(network.getLogged_in()!=null){
                                currentPage = menuPage.Evenimente;
                                }
                                else System.out.println("Doar un utilizator logat poate modifica evenimente");
                                break;
                            }
                            case 6 -> {
                                if(network.getLogged_in()!=null){
                                currentPage = menuPage.Carduri_de_rate;
                                }
                                else System.out.println("Doar un utilizator logat poate administra carduri de rate");
                                break;
                            }
                            case 7 -> {
                                if(network.getLogged_in()!=null){
                                    currentPage = menuPage.Simulare_cursa;
                                }
                                else System.out.println("Doar un utilizator logat poate administra curse");
                                break;
                            }
                            case 8 -> {
                                currentPage = Login;
                                break;
                            }
                            case 9 -> {
                                System.out.println("La revedere");
                                return;
                            }
                            default -> {
                                throw new WrongCommandException("Nu exista comanda "+opt);
                            }
                        }
                    }
                    catch (WrongInputException | WrongCommandException e){
                        System.out.println(e.getMessage());
                    }
                }
                case CRUD_PERSOANE -> {
                    try {
                        crt = sc.nextLine();
                        int opt = Integer.parseInt(crt);
                        switch (opt) {
                            case 1 -> {
                             try {
                                 Persoana p = CreazaPersoana(sc);
                                 if (p==null)
                                     throw new NullEntityException("Persoana este nula");
                                 if(network.getAdminPersoane().adauga(p)) System.out.println("Persoana a fost retinuta cu succes");
                                 else throw new NullEntityException("Nu s-a putut adauga persoana");
                             }catch (WrongInputException | NullEntityException | AlreadyExistentEntityException e){
                                 System.err.println(e.getMessage());
                             }
                            }
                            case 2 -> {
                                try {
                                    System.out.println("ID-UL PERSOANEI STERSE:");
                                    Long id = sc.nextLong();
                                    sc.nextLine();
                                    Persoana p = new Persoana(id,"","","","","","");
                                    if (network.getAdminPersoane().sterge(p))
                                        System.out.println("User sters cu succes");
                                    else throw new NullEntityException("User-ul cu ID "+id+" nu exista");
                                    //network.getAdminPrietenii().stergePrietenii(p);
                                    //network.getAdminMesaje().stergeConversatii(p);
                                    /*
                                    Scoate din evenimente
                                    Scoate din curse
                                    * */
                                }catch (WrongInputException | NullEntityException e){
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 3 -> {
                                try {
                                    List<Persoana> persoane = network.getAdminPersoane().utilizatori();
                                    if(persoane.isEmpty())
                                        throw new NullEntityException("Nu exista persoane logate");
                                    persoane.stream()
                                            .map(p -> "ID:"+p.getId()+" "+p.getUsername())
                                            .forEach(System.out::println);
                                }catch (NullEntityException e){
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 4 -> {
                                currentPage = menuPage.Default_menu;
                            }
                            default -> {
                                throw new WrongCommandException("Nu exista comanda "+opt);
                            }
                        }
                    }
                    catch (WrongInputException | WrongCommandException e){
                        System.out.println(e.getMessage());
                    }
                }
                case CRUD_RATE -> {

                    crt = sc.nextLine();
                    int opt = Integer.parseInt(crt);
                    switch (opt) {
                        case 1 -> {
                            try {
                                Rata r = CreazaRata(sc);
                                if (r==null)
                                    throw new NullEntityException("Rata este nula");
                                if (network.getAdminRate().adauga(r))
                                    System.out.println("Rata a fost retinuta cu succes");
                                else throw new AlreadyExistentEntityException("Rata exista deja");
                            }catch (WrongInputException | NullEntityException | AlreadyExistentEntityException e){
                                System.err.println(e.getMessage());
                            }
                        }
                        case 2 -> {
                            try {
                                String ID;
                                System.out.println("ID-UL RATEI STERSE:");
                                ID=sc.nextLine();
                                Long id = Long.parseLong(ID);
                                Rata r = new RataInotatoare(id,"","","",tipRata.SWIMMING,1L,1L,1L);
                                if (network.getAdminRate().sterge(r)) System.out.println("User sters cu succes");
                                else throw new NullEntityException("Rata nu exista");
                            }catch (WrongInputException | NullEntityException e){
                                System.err.println(e.getMessage());
                            }
                        }
                        case 3 -> {
                            try {
                                List<Rata> rate = network.getAdminRate().utilizatori();
                                if(rate.isEmpty())throw new NullEntityException("Nu exista rate logate");
                                rate.stream()
                                        .map(p ->"ID:"+p.getId()+" "+p.getUsername()+" "+p.getTip().name())
                                        .forEach(System.out::println);
                            }
                            catch (NullEntityException | SQLException |WrongInputException e){
                                System.err.println(e.getMessage());
                            }
                        }
                        case 4 -> {
                            currentPage = menuPage.Default_menu;
                        }
                        default -> {
                            throw new RuntimeException();
                        }
                    }
                    break;
                }
                case Mesagerie -> {
                    try {
                        crt = sc.nextLine();
                        int opt = Integer.parseInt(crt);
                        switch (opt) {
                            case 1 -> {
                                try{
                                    Long id=(Long)network.getLogged_in().getId();
                                    String continut,idReceiver,timestamp;
                                    System.out.println("ID USER:");
                                    idReceiver=sc.nextLine();
                                    System.out.println("CONTINUT MESAJ:");
                                    continut=sc.nextLine();
                                    User reciever=null;reciever=network.getAdminPersoane().getById(Long.parseLong(idReceiver));
                                    if(reciever==null)reciever=network.getAdminRate().getById(Long.parseLong(idReceiver));
                                    if(reciever==null)throw new NullEntityException("Nu exista user cu acest id");
                                    String erori=network.getAdminMesaje().valideaza(new String[]{continut});
                                    if(!erori.isBlank())throw new WrongInputException(erori);
                                    if(network.getAdminMesaje().adauga(new Message(id, (Long)network.getLogged_in().getId(), (Long)reciever.getId(),continut,new Date()))){
                                        System.out.println("Mesajul a fost trimis");
                                    }
                                }
                                catch (NullEntityException | WrongInputException | NumberFormatException e){
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 2 -> {
                                try {
                                    Long id;
                                    System.out.println("ID MESAJ STERS:");
                                    id = sc.nextLong();
                                    sc.nextLine();
                                    Message m = network.getAdminMesaje().getById(id);
                                    if(m==null)throw new NullEntityException("Nu exista mesajul");
                                    if (network.getAdminMesaje().sterge(m)) System.out.println("Mesajul a fost sters");
                                } catch (RuntimeException e) {
                                    e.printStackTrace();
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 3 -> {
                                try{
                                if(network.getLogged_in()!=null){
                                        network.getAdminMesaje().gasesteDupaMembru(network.getLogged_in())
                                                .stream()
                                                .map(p -> "ID:"+p.getId()+"FROM:" + p.getSender() + " TO:" + p.getReciever() + " TEXT:" + p.getContent())
                                                .forEach(System.out::println);
                                }else System.err.println("Trebuie sa fiti logati ca sa va vedeti istoricul de mesaje");
                                } catch (RuntimeException e){
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 4 -> {
                                currentPage = menuPage.Default_menu;
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                case Evenimente -> {
                    try{
                        crt=sc.nextLine();
                        int opt=Integer.parseInt(crt);
                        switch (opt) {
                            case 1 -> {
                                String nume;
                                System.out.println("NUMELE NOULUI EVENIMENT:");
                                nume = sc.nextLine();
                                String erori = network.getAdminEvents().valideaza(new String[]{nume});
                                if (!erori.isEmpty()) {
                                    System.err.println(erori);
                                } else {
                                    if (network.getAdminEvents().adauga(new Event(1L, nume, (Long)network.getLogged_in().getId())))
                                        System.out.println("Evenimentul s-a adaugat cu succes");
                                }
                            }
                            case 2 -> {
                                try {
                                    network.getAdminEvents();
                                    String idEvent, idUser;
                                    System.out.println("ID-UL EVENIMENTULUI:");
                                    idEvent = sc.nextLine();
                                    System.out.println("ID-UL USER-ULUI:");
                                    idUser = sc.nextLine();
                                    User user = network.getAdminRate().getById(Long.parseLong(idUser));
                                    if (user == null) user = network.getAdminPersoane().getById(Long.parseLong(idUser));
                                    if (user == null) throw new NullEntityException("Nu exista userul");
                                    Event eveniment = network.getAdminEvents().getById(Long.parseLong(idEvent));
                                    if (eveniment == null) throw new NullEntityException("Evenimentul nu exista");
                                    if(!network.getAdminEvents().aboneaza(eveniment, user)){
                                        throw new AlreadyExistentEntityException("User-ul e deja abonat la eveniment");
                                    }
                                    else System.out.println("User-ul a fost abonat cu succes la eveniment");
                                } catch (RuntimeException e) {
                                    e.printStackTrace();
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 3 -> {
                                try {
                                    String idEvent, idUser;
                                    System.out.println("ID-UL EVENIMENTULUI:");
                                    idEvent = sc.nextLine();
                                    System.out.println("ID-UL USER-ULUI:");
                                    idUser = sc.nextLine();
                                    User user = network.getAdminRate().getById(Long.parseLong(idUser));
                                    if (user == null) user = network.getAdminPersoane().getById(Long.parseLong(idUser));
                                    if (user == null) throw new NullEntityException("Nu exista userul");
                                    Event eveniment = network.getAdminEvents().getById(Long.parseLong(idEvent));
                                    if (eveniment == null)
                                        throw new NullEntityException("Evenimentul nu exista");
                                    if(!network.getAdminEvents().dezaboneaza(eveniment,user)){
                                        throw new NullEntityException("User-ul nu era abonat la eveniment");
                                    }
                                    else System.out.println("User-ul a fost dezabonat cu succes la eveniment");
                                } catch (RuntimeException e) {
                                    e.printStackTrace();
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 4 -> {
                                try{
                                String idEvent, idUser;
                                System.out.println("ID-UL EVENIMENTULUI:");
                                idEvent = sc.nextLine();
                                System.out.println("ID-UL USER-ULUI:");
                                idUser = sc.nextLine();
                                User user = network.getAdminRate().getById(Long.parseLong(idUser));
                                if (user == null) user = network.getAdminPersoane().getById(Long.parseLong(idUser));
                                if (user == null) throw new NullEntityException("Nu exista userul");
                                Event eveniment = network.getAdminEvents().getById(Long.parseLong(idEvent));
                                if (eveniment == null) throw new NullEntityException("Evenimentul nu exista");
                                    if(!network.getAdminEvents().inscrieParticipant(eveniment,user)){
                                        throw new AlreadyExistentEntityException("User-ul e deja inscris la eveniment");
                                    }
                                else System.out.println("User-ul a fost inscris cu succes la eveniment");
                            }catch(RuntimeException e){
                                e.printStackTrace();
                                System.err.println(e.getMessage());
                            }
                        }
                            case 5->{
                                try{
                                    String idEvent, idUser;
                                    System.out.println("ID-UL EVENIMENTULUI:");
                                    idEvent = sc.nextLine();
                                    System.out.println("ID-UL USER-ULUI:");
                                    idUser = sc.nextLine();
                                    User user = network.getAdminRate().getById(Long.parseLong(idUser));
                                    if (user == null) user = network.getAdminPersoane().getById(Long.parseLong(idUser));
                                    if (user == null) throw new NullEntityException("Nu exista userul");
                                    Event eveniment = network.getAdminEvents().getById(Long.parseLong(idEvent));
                                    if (eveniment == null) throw new NullEntityException("Evenimentul nu exista");
                                    if(!network.getAdminEvents().retrageParticipant(eveniment,user)){
                                        throw new AlreadyExistentEntityException("User-ul nu era inscris la eveniment");
                                    }
                                    else System.out.println("User-ul a fost retras cu succes din eveniment");
                                }catch(RuntimeException e){
                                    e.printStackTrace();
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 6->{
                                try {
                                    List<Event> events = network.getAdminEvents().utilizatori();
                                    if(events.isEmpty())throw new NullEntityException("Nu exista evenimente momentan");
                                    events.stream().map(e->"ID:"+e.getId()+" NUME:"+e.getNume()).forEach(System.out::println);
                                } catch (RuntimeException e) {
                                    e.printStackTrace();
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 7->{
                                try {
                                    String idEvent;
                                    System.out.println("ID-UL EVENIMENTULUI:");
                                    idEvent = sc.nextLine();
                                    Event eveniment = network.getAdminEvents().getById(Long.parseLong(idEvent));
                                    if(eveniment==null)throw new NullEntityException("Nu exista evenimentul");
                                    eveniment.runEvent(network);
                                }catch (RuntimeException e){
                                    e.printStackTrace();
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 8->{
                                currentPage=Default_menu;
                            }
                        }
                    }catch (RuntimeException e){
                        System.err.println(e.getMessage());
                    }
            }
                case Carduri_de_rate -> {
                        try{
                            crt = sc.nextLine();
                        int opt = Integer.parseInt(crt);
                        switch (opt){
                            case 1->{
                                try {
                                    String tip, nume;
                                    ArrayList<Rata> rate = new ArrayList<Rata>();
                                    System.out.println("NUMELE NOULUI CARD:");
                                    nume = sc.nextLine().trim();
                                    System.out.println("TIPUL NOULUI CARD\n1.Inotatori\n2.Zburatori\n");
                                    tip = sc.nextLine().trim();
                                    if (tip.equals("1")) tip = "Inotatori";
                                    if (tip.equals("2")) tip = "Zburatori";
                                    String erori = network.getAdminCarduri().valideaza(new String[]{nume, tip});
                                    if (!erori.isEmpty()) System.err.println(erori);
                                    else {
                                        if (network.getAdminCarduri().adauga(new Card(1L, nume, rate, tip)))
                                            System.out.println("Card adaugat cu succes");
                                        else System.out.println("Card exista deja");
                                    }
                                }catch (RuntimeException e){
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 2->{
                                try {
                                    network.getAdminCarduri()
                                            .utilizatori()
                                            .stream()
                                            .map(c->c.getId()+" "+c.getNumeCard()+" "+c.getTipCard())
                                            .forEach(System.out::println);
                                } catch (RuntimeException e) {
                                    System.err.println("Nu exista carduri");
                                }
                            }
                            case 3->{
                                try{
                                String idCard,idRata;
                                System.out.println("ID-UL CARDULUI:");
                                idCard=sc.nextLine();
                                System.out.println("ID-UL RATEI:");
                                idRata=sc.nextLine();
                                Rata rata = network.getAdminRate().getById(Long.parseLong(idRata));
                                if(rata==null)throw new NullEntityException("Rata nu exista");
                                Card card = network.getAdminCarduri().getById(Long.parseLong(idCard));
                                if(card==null)throw new NullEntityException("Cardul nu exista");
                                if(network.getAdminCarduri().adaugaInComunitate(card,rata)) System.out.println("Rata a fost addaugata cu succes");
                                else throw new WrongInputException("Rata era deja in card");
                                }catch (RuntimeException e){
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 4->{
                                try{
                                String idCard,idRata;
                                System.out.println("ID-UL CARDULUI:");
                                idCard=sc.nextLine();
                                System.out.println("ID-UL RATEI DE STERS:");
                                idRata=sc.nextLine();
                                Card card = network.getAdminCarduri().getById(Long.parseLong(idCard));
                                if(card==null)throw new NullEntityException("Cardul nu exista");
                                Rata rata = network.getAdminRate().getById(Long.parseLong(idRata));
                                if(rata==null)throw new NullEntityException("Rata nu exista");
                                if(network.getAdminCarduri().stergeDinComunitate(card,rata)) System.out.println("Rata a fost stearsa din comunitate");
                                else throw new NullEntityException("Rata nu se afla in acest card");
                                }catch (RuntimeException e){
                            System.err.println(e.getMessage());
                                 }
                    }
                            case 5->{
                                try {
                                    String idCard;
                                    System.out.println("ID-UL Cardului:");
                                    idCard=sc.nextLine();
                                    Card card = network.getAdminCarduri().getById(Long.parseLong(idCard));
                                    if(card==null)throw new NullEntityException("Cardul nu exista");
                                    System.out.println("Performanta medie a cardului este "+network.getAdminCarduri().performantaMedie(card,network));
                                }catch (RuntimeException e) {
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 6->{
                                currentPage=menuPage.Default_menu;
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Optiune invalida");
                        currentPage=menuPage.Default_menu;
                    }
                }
                case Prietenii-> {
                    try {
                        crt = sc.nextLine();
                        int opt = Integer.parseInt(crt);
                        switch (opt){
                            case 1->{
                                    String id;
                                    System.out.println("ID-UL USERULUI PE CARE IL VETI ADAUGA LA PRIETENI");
                                    id=sc.nextLine();
                                    try{
                                        User prieten = null;
                                        prieten=network.getAdminRate().getById(Long.parseLong(id));
                                        if(prieten==null)prieten=network.getAdminPersoane().getById(Long.parseLong(id));
                                        if(prieten==null){throw new NullEntityException("Nu exista vreun user cu id-ul dat");}
                                        String erori=network.getAdminPrietenii().valideaza(new String[]{network.getLogged_in().toString(),prieten.toString()});
                                        if(!erori.isEmpty()){
                                            System.err.println(erori);
                                        }
                                        else{
                                            if(network.getAdminPrietenii().adauga(new Friendship((Long)network.getLogged_in().getId(),Long.parseLong(id)))){
                                                System.out.println("Prietenul a fost adaugat cu succes");
                                            }
                                            else System.out.println("Prietenia exista deja");
                                        }
                                    }
                                    catch (NumberFormatException | NullEntityException e){
                                        System.err.println(e.getMessage());
                                    }
                                    break;
                            }
                            case 2->{
                                try{
                                    String id;
                                    System.out.println("ID-UL PRIETENULUI PE CARE IL STERGETI");
                                    id=sc.nextLine();
                                    Long nrId = Long.parseLong(id);
                                    User fost_prieten=null;
                                    fost_prieten = network.getAdminRate().getById(nrId);
                                    if(fost_prieten==null)fost_prieten=network.getAdminPersoane().getById(nrId);
                                    if(fost_prieten==null){throw new NullEntityException("Nu exista vreun user cu acest id");}
                                    Friendship fr = new Friendship((Long)network.getLogged_in().getId(),(Long)fost_prieten.getId());
                                    if(network.getAdminPrietenii().contine(fr)){
                                        System.out.println(network.getAdminPrietenii().sterge(fr));
                                        System.out.println("Prietenia a fost stearsa");
                                    }
                                    else{
                                        throw new NullEntityException("Prietenia nu exista");
                                    }
                                }catch (NullEntityException | WrongInputException e){
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 3->{
                                try {
                                    String username;
                                    System.out.println("USERNAME-UL CAUTAT:");
                                    username = sc.nextLine();
                                    User gasit;
                                    gasit = network.getAdminPersoane().getByUsername(username);
                                    if (gasit == null) gasit = network.getAdminRate().getByUsername(username);
                                    if(gasit==null) throw new NullEntityException("Nu exista niciun user numit asa");
                                    else System.out.println(gasit.getUsername()+" "+gasit.getEmail());
                                }
                                catch (NullEntityException e){
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 4->{
                                System.out.println("Numarul de comunitati:"+network.getAdminPrietenii().dfsMain(network,true));
                                break;
                            }
                            case 5->{
                                int index_rezultat = network.getAdminPrietenii().dfsMain(network,false);
                                List<User> useri = network.getAdminPrietenii().comunitati().get(index_rezultat);
                                useri.stream()
                                        .map(u->u.getUsername())
                                        .forEach(System.out::println);
                                break;
                            }
                            case 6-> {
                                currentPage = menuPage.Default_menu;
                            }
                        }
                    }catch (WrongInputException e){
                        System.out.println("Introduceti o optiune valida");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                case Simulare_cursa -> {
                    try{
                        crt=sc.nextLine();
                        int opt=Integer.parseInt(crt);
                        switch (opt) {
                            case 1 -> {
                                try{
                                String nume;int nrLane;
                                List<Lane> lanes = new ArrayList<Lane>();
                                System.out.println("NUMELE NOII CURSE:");
                                nume = sc.nextLine();
                                System.out.println("NR DE CULOARE:");
                                nrLane = sc.nextInt();
                                for (int i=0;i<nrLane;i++){
                                    int distanta;
                                    System.out.println("DISTANTA CULOARULUI "+(i+1));
                                    distanta=sc.nextInt();
                                    lanes.add(new Lane(0L,distanta));
                                }
                                String erori = network.getAdminEvents().valideaza(new String[]{nume});
                                if (!erori.isEmpty()) {
                                    System.err.println(erori);
                                } else {
                                    RaceEvent cursa = new RaceEvent(1L, nume, (Long)network.getLogged_in().getId(),nrLane);
                                    if (network.getAdminRaces().adauga(cursa)) System.out.println("Cursa s-a adaugat cu succes");
                                    if(network.getAdminRaces().adaugaCuloare(cursa,lanes)) System.out.println("Culoarele au fost adaugate");
                                }
                                }catch (RuntimeException e){
                                    e.printStackTrace();
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 2 -> {
                                try {
                                    String idEvent, idUser;
                                    System.out.println("ID-UL CURSEI:");
                                    idEvent = sc.nextLine();
                                    System.out.println("ID-UL NOULUI ABONAT:");
                                    idUser = sc.nextLine();
                                    User user = network.getAdminRate().getById(Long.parseLong(idUser));
                                    if (user == null) user = network.getAdminPersoane().getById(Long.parseLong(idUser));
                                    if (user == null) throw new NullEntityException("Nu exista userul");
                                    RaceEvent eveniment = network.getAdminRaces().getById(Long.parseLong(idEvent));
                                    if (eveniment == null) throw new NullEntityException("Cursa nu exista");
                                    if(!network.getAdminRaces().aboneaza(eveniment, user)){
                                        throw new AlreadyExistentEntityException("User-ul e deja abonat la cursa");
                                    }
                                    else System.out.println("User-ul a fost abonat cu succes la cursa");
                                } catch (RuntimeException e) {
                                    e.printStackTrace();
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 3 -> {
                                try {
                                    String idEvent, idUser;
                                    System.out.println("ID-UL CURSEI:");
                                    idEvent = sc.nextLine();
                                    System.out.println("ID-UL USER-ULUI:");
                                    idUser = sc.nextLine();
                                    User user = network.getAdminRate().getById(Long.parseLong(idUser));
                                    if (user == null) user = network.getAdminPersoane().getById(Long.parseLong(idUser));
                                    if (user == null) throw new NullEntityException("Nu exista userul");
                                    RaceEvent eveniment = network.getAdminRaces().getById(Long.parseLong(idEvent));
                                    if (eveniment == null)
                                        throw new NullEntityException("Cursa nu exista");
                                    if(!network.getAdminRaces().dezaboneaza(eveniment,user)){
                                        throw new NullEntityException("User-ul nu era abonat la cursa");
                                    }
                                    else System.out.println("User-ul a fost dezabonat cu succes la cursa");
                                } catch (RuntimeException e) {
                                    e.printStackTrace();
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 4 -> {
                                try{
                                    String idEvent, idUser;
                                    System.out.println("ID-UL CURSEI:");
                                    idEvent = sc.nextLine();
                                    System.out.println("ID-UL NOULUI CONCURENT:");
                                    idUser = sc.nextLine();
                                    User user = network.getAdminRate().getById(Long.parseLong(idUser));

                                    if(user.getClass()== RataZburatoare.class) throw new WrongInputException("Ratele ne-inotatoare nu pot concura in curse");
                                    if (user == null) throw new WrongInputException("Persoanele nu pot concura in curse pentru rate");

                                    RaceEvent eveniment = network.getAdminRaces().getById(Long.parseLong(idEvent));
                                    if (eveniment == null) throw new NullEntityException("Evenimentul nu exista");
                                    if(!network.getAdminRaces().inscrieParticipant(eveniment,user)){
                                        throw new AlreadyExistentEntityException("User-ul e deja inscris la eveniment");
                                    }
                                    else System.out.println("User-ul a fost inscris cu succes la eveniment");
                                }catch(RuntimeException e){
                                    e.printStackTrace();
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 5->{
                                try{
                                    String idEvent, idUser;
                                    System.out.println("ID-UL EVENIMENTULUI:");
                                    idEvent = sc.nextLine();
                                    System.out.println("ID-UL USER-ULUI:");
                                    idUser = sc.nextLine();
                                    User user = network.getAdminRate().getById(Long.parseLong(idUser));
                                    if (user == null) user = network.getAdminPersoane().getById(Long.parseLong(idUser));
                                    if (user == null) throw new NullEntityException("Nu exista userul");
                                    RaceEvent eveniment = network.getAdminRaces().getById(Long.parseLong(idEvent));
                                    if (eveniment == null) throw new NullEntityException("Evenimentul nu exista");
                                    if(!network.getAdminRaces().retrageParticipant(eveniment,user)){
                                        throw new AlreadyExistentEntityException("User-ul nu era inscris la eveniment");
                                    }
                                    else System.out.println("User-ul a fost retras cu succes din eveniment");
                                }catch(RuntimeException e){
                                    e.printStackTrace();
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 6->{
                                try {
                                    List<RaceEvent> events = network.getAdminRaces().utilizatori();
                                    if(events.isEmpty())throw new NullEntityException("Nu exista evenimente momentan");
                                    events.stream().map(e->"ID:"+e.getId()+" NUME:"+e.getNume()).forEach(System.out::println);
                                } catch (RuntimeException e) {
                                    e.printStackTrace();
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 7->{
                                try {
                                    String idEvent;
                                    System.out.println("ID-UL EVENIMENTULUI:");
                                    idEvent = sc.nextLine();
                                    RaceEvent eveniment = network.getAdminRaces().getById(Long.parseLong(idEvent));
                                    if(eveniment==null)throw new NullEntityException("Nu exista evenimentul");
                                    System.out.println(network.getAdminRaces().ruleazaCursa(eveniment,network));
                                    eveniment.runEvent(network);
                                }catch (RuntimeException e){
                                    e.printStackTrace();
                                    System.err.println(e.getMessage());
                                }
                            }
                            case 8->{
                                currentPage=Default_menu;
                            }
                        }
                    }catch (RuntimeException e){
                        System.err.println(e.getMessage());
                    }
                }
                case Login -> {
                    if (network.getLogged_in() == null) {
                        String username, parola;
                        System.out.println("Introduceti numele de utilizator:");
                        username = sc.nextLine();
                        System.out.println("Introduceti parola:");
                        parola = sc.nextLine();
                        if (network.tryLogin(username, parola) != null) {
                            System.out.println("Bine ai revenit," + username);
                        } else {
                            System.out.println("Logarea a esuat\n");
                            currentPage = menuPage.Default_menu;
                        }
                        sc.reset();
                    } else {
                        try {
                            System.out.println("Sunteti logat ca " + network.getLogged_in().getUsername());
                            crt = sc.nextLine();
                            int optiune = Integer.parseInt(crt);
                            if (optiune == 1){
                                currentPage = menuPage.Default_menu;
                            }
                            else if(optiune==2){
                                System.out.println("La revedere,"+network.getLogged_in().getUsername());
                                network.getLogged_in().logout(network);
                                currentPage=menuPage.Default_menu;
                            }
                            else System.out.println("Introduceti o comanda valida");
                        } catch (RuntimeException e) {
                            currentPage=menuPage.Default_menu;
                        }
                    }
                }
            }
        }
    }
}