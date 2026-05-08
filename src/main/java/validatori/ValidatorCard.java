package validatori;
import exceptii.WrongInputException;

import java.util.Arrays;

public class ValidatorCard extends ValidatorUser{
    public ValidatorCard(String[] dateUser) {
        super(dateUser);
    }
    @Override
    public String valideaza() {
        String erori="";
        if(super.dateUser.length<2)return "Un card are minim 2 componente";
        if(super.dateUser[0].isBlank()||super.dateUser[0].isEmpty())erori+="Numele cardului nu poate fi nul\n";
        if(super.dateUser[1].isBlank()||super.dateUser[1].isEmpty())erori+="Tipul cardului nu poate fi nul\n";
        if(!super.dateUser[1].equals("Inotatori")&&!super.dateUser[1].equals("Zburatori"))erori+="Tipul trebuie sa fie unul dintre Inotatori sau Zburatori";
        return erori;
    }
}
