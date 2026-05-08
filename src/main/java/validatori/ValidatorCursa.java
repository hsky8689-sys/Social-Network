package validatori;

import exceptii.WrongInputException;

public class ValidatorCursa extends ValidatorUser{
    public ValidatorCursa(String[] dateUser) {
        super(dateUser);
    }
    @Override
    public String valideaza() {
        String erori="";
        if(super.dateUser.length<2)return "O cursa are minim 2 componente";
        if(super.dateUser[0].isBlank())erori+="Id-ul de cursa nu poate fi nul\n";
        try{
            Long id = Long.parseLong(dateUser[0]);
            if(id<0)throw new WrongInputException("Id-ul trebuie sa fie un numar real pozitiv");
        }catch (NumberFormatException e){
            erori+="Id-ul de cursa trebuie sa fie un numar real pozitiv";
        }
        catch (WrongInputException e){
            erori+=e.getMessage()+"\n";
        }
        if(super.dateUser[1].isBlank())erori+="Numele evenimentului nu poate fi nul\n";
        for(int i=2;i<super.dateUser.length;i++){
            String[] dateUser = super.dateUser[i].split("\\,");
            if(dateUser.length==8) {
                if(dateUser[4]=="FLYING")erori+="Cursa contine rate ce nu pot inota\n";
                else {
                    ValidatorRata validatorRata = new ValidatorRata(dateUser);
                    erori += validatorRata.valideaza();
                }
            }
            else if(dateUser.length==2){
                ValidatorCuloar validatorCuloar = new ValidatorCuloar(dateUser);
                erori+=validatorCuloar.valideaza();
            }
            else continue;
        }
        return erori;
    }
}
