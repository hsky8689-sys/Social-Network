package validatori;
import exceptii.WrongInputException;

public class ValidatorRata extends ValidatorUser{
    public ValidatorRata(String[] dateUser) {
        super(dateUser);
    }
    @Override
    public String valideaza() {
        String erori="";
        if(super.dateUser.length!=8)return "O rata are fix 8 atribute";
        try{
            if(super.dateUser[0].isEmpty())throw new WrongInputException("ID-ul nu poate ramane necompletat");
            Long id = Long.parseLong(super.dateUser[0]);
            if(id<0)throw new WrongInputException("ID-ul trebuie sa fie un nr natural");
        }catch (NumberFormatException e){
            erori+="ID-ul trebuie sa fie un nr natural\n";
        }
        catch (WrongInputException e){
            erori+=e.getMessage()+"\n";
        }
        try {
            if(super.dateUser[1].isEmpty())throw new WrongInputException("Username-ul nu poate ramane necompletat");
        }catch (WrongInputException e){
            erori+=e.getMessage()+"\n";
        }
        try{
            String email = super.dateUser[2].trim(); // elimină spațiile dinainte sau de după
            if (email.isEmpty()) throw new WrongInputException("Mail-ul nu poate ramane necompletat");
        }
        catch(WrongInputException e){
            erori+=e.getMessage()+"\n";
        }
        try {
            if (super.dateUser[6].isEmpty()) throw new WrongInputException("Parola nu poate ramane necompletata");
        }
        catch (WrongInputException e){
            erori+=e.getMessage()+"\n";
        }
        try{
            if(super.dateUser[4].isEmpty())throw new WrongInputException("Tipul ratei nu poate ramane necompletat");
            if (!super.dateUser[4].equals("FLYING") && !super.dateUser[4].equals("SWIMMING") && !super.dateUser[4].equals("FLYING_AND_SWIMMING"))
                throw new WrongInputException("Tipul unei rate poate fi unul dintre FLYING/SWIMMING/FLYING_AND_SWIMMING");
        }
        catch (WrongInputException e){
            erori+=e.getMessage()+"\n";
        }
        try {
            String idCard = super.dateUser[5];
            if (idCard.isEmpty())
                throw new WrongInputException("ID-ul de card nu poate ramane necompletat");
            if (idCard.equals("-1")) {
                // valid, rata nu e in niciun card
            } else {
                long id = Long.parseLong(idCard);
                if (id <= 0)
                    throw new WrongInputException("ID-ul de card trebuie sa fie un numar natural pozitiv sau -1");
            }
        } catch (NumberFormatException e) {
            erori+="ID-ul de card trebuie sa fie un numar valid\n";
        } catch (WrongInputException e) {
            erori+=e.getMessage()+"\n";
        }
        try{
            if(super.dateUser[6].isEmpty())throw new WrongInputException("Viteza nu poate ramane necompletata");
            Long nr = Long.parseLong(super.dateUser[6]);
            if(nr<0)throw new WrongInputException("Viteza trebuie sa fie un numar real pozitiv");
        }catch (NumberFormatException e){
            throw new WrongInputException("Viteza trebuie sa fie un numar real");
        }
        catch (WrongInputException e){
            erori+=e.getMessage()+"\n";
        }
        try{
            if(super.dateUser[7].isEmpty())throw new WrongInputException("Rezistenta nu poate ramane necompletata");
            Long nr = Long.parseLong(super.dateUser[7]);
            if(nr<0)throw new WrongInputException("Rezistenta trebuie sa fie un numar real pozitiv");
        }catch (NumberFormatException e){
            throw new WrongInputException("Rezistenta trebuie sa fie un numar real");
        }
        catch (WrongInputException e){
            erori+=e.getMessage()+"\n";
        }
        return erori;
    }
}
