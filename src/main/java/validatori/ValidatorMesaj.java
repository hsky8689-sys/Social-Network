package validatori;

import exceptii.WrongInputException;

public class ValidatorMesaj extends ValidatorUser{
    public ValidatorMesaj(String[] dateUser) {
        super(dateUser);
    }
    @Override
    public String valideaza() {
        String erori="";
        if(dateUser.length!=1)return "Trebuie sa validam fix o componenta";
        if(dateUser[0].isBlank()||dateUser[0].isEmpty())erori+="Mesajul trebuie sa contina text\n";
        if(dateUser[0].length()>200)erori+="Mesajul nu poate avea mai mult de 200 de caractere";
        return erori;
    }
}
