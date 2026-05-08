package validatori;
public class ValidatorEvent extends ValidatorUser{
    public ValidatorEvent(String[] dateUser) {
        super(dateUser);
    }
    @Override
    public String valideaza() {
        if(dateUser.length!=1)return "Un eveniment are fix o componenta";
        if(dateUser[0].isBlank()||dateUser[0].isEmpty())return "Numele nu poate fi nul";
        if(dateUser[0].length()>200)return "Numele nu poate avea peste 200 de caractere";
        return "";
    }
}