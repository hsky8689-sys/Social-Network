package validatori;

public class ValidatorCuloar extends ValidatorUser{
    public ValidatorCuloar(String[] dateUser) {
        super(dateUser);
    }
    @Override
    public String valideaza() {
        String rez="";
        if(dateUser[0].isBlank())rez+="ID-ul culoarului nu poate fi nul";
        try{
            Long id = Long.parseLong(dateUser[0]);
            if(id<0)rez+="ID-ul culoarului trebuie sa fie un nr real pozitiv";
        }catch (NumberFormatException e){
            rez+="ID-ul culoarului trebuie sa fie un nr real pozitiv";
        }
        if(dateUser[1].isBlank())rez+="Distanta culoarului nu poate fi nula";
        try{
            Long id = Long.parseLong(dateUser[0]);
            if(id<0)rez+="Distanta culoarului trebuie sa fie un nr real pozitiv";
        }catch (NumberFormatException e){
            rez+="Distanta culoarului trebuie sa fie un nr real pozitiv";
        }
        return rez;
    }
}
