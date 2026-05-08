package validatori;

import exceptii.WrongInputException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidatorPersoana extends ValidatorUser{
    public ValidatorPersoana(String[] dateUser) {
        super(dateUser);
    }
    @Override
    public String valideaza() {
        String erori="";
        if(super.dateUser.length!=7)return "O persoana are fix 7 atribute";
        try {
            if (super.dateUser[0].isEmpty())
                throw new WrongInputException("ID-ul nu poate ramane necompletat");
            long id = Long.parseLong(super.dateUser[0]);
            if (id < 0)
                throw new WrongInputException("ID-ul trebuie sa fie un numar natural pozitiv");
        } catch (NumberFormatException e) {
            erori+="ID-ul trebuie sa fie un numar natural\n";
        } catch (WrongInputException e) {
            erori+=e.getMessage()+"\n";
        }
        try {
            if(super.dateUser[1].isEmpty())throw new WrongInputException("Numele nu poate ramane necompletat");
        }catch (WrongInputException e){
            erori+=e.getMessage()+"\n";
        }
        try {
            if(super.dateUser[2].isEmpty())throw new WrongInputException("Prenumele nu poate ramane necompletat");
        }catch (WrongInputException e){
            erori+=e.getMessage()+"\n";
        }
        try{
            try{
            if(super.dateUser[3].isEmpty())throw new WrongInputException("Data nasterii nu poate ramane necompletata");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate date = LocalDate.parse(super.dateUser[3],formatter);
            if(date.isAfter(LocalDate.now()) || date.isEqual(LocalDate.now()))
                throw new WrongInputException("Data nasterii nu poate fi in viitor...");
                }
            catch (DateTimeParseException e){
                    erori+=super.dateUser[3]+" nu este o data calendaristica valida\n";
                }
        }
        catch(WrongInputException e){
            erori+=e.getMessage()+"\n";
        }
        try {
            if (super.dateUser[4].isEmpty()) throw new WrongInputException("Ocupatia nu poate ramane necompletata");
        }
        catch (WrongInputException e){
            erori+=e.getMessage()+"\n";
        }
        try{
            if(super.dateUser[5].isEmpty())throw new WrongInputException("Mail-ul nu poate ramane necompletat");
            if (!super.dateUser[5].matches("[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]+")) {
                throw new WrongInputException("Exemplu de mail corect: user123@gmail.com");
            }
        }
        catch(WrongInputException e){
            erori+=e.getMessage()+"\n";
        }
        try {
            if (super.dateUser[6].isEmpty()) throw new WrongInputException("Parola nu poate ramane necompletata");
            if(super.dateUser[6].length()<10)throw new WrongInputException("Parola trebuie sa aiba minim 10 caractere");
        }
        catch (WrongInputException e){
            erori+=e.getMessage()+"\n";
        }
        return erori;
    }
}
