package validatori;

public class ValidatorPrietenie extends ValidatorUser implements Validare{
    public ValidatorPrietenie(String[] dateUser) {
        super(dateUser);
    }
    @Override
    public String valideaza() {
        String rezultat="";
        if(dateUser.length<2)return "2 useri per prietenie";
        if(dateUser[0].equals(dateUser[1]))return "nu iti poti cere prietenie singur";
        String datePrimul=dateUser[0];
        String dateAlDoilea=dateUser[1];
        System.out.println(datePrimul+"\n"+dateAlDoilea);
        if(datePrimul.length()==8){
            ValidatorRata validatorRata = new ValidatorRata(datePrimul.split("\\,"));
            String ok1=validatorRata.valideaza();
            if(!ok1.isEmpty())rezultat+=ok1+"\n";
        }
        else{
            ValidatorPersoana validatorPersoana = new ValidatorPersoana(datePrimul.split("\\,"));
            String ok1=validatorPersoana.valideaza();
            if(!ok1.isEmpty())rezultat+=ok1+"\n";
        }
        if(dateAlDoilea.length()==8){
            ValidatorRata validatorRata = new ValidatorRata(dateAlDoilea.split("\\,"));
            String ok2=validatorRata.valideaza();
            if(!ok2.isEmpty())rezultat+=ok2+"\n";
        }
        else{
            ValidatorPersoana validatorPersoana = new ValidatorPersoana(dateAlDoilea.split("\\,"));
            String ok2=validatorPersoana.valideaza();
            if(!ok2.isEmpty())rezultat+=ok2+"\n";
        }
        return rezultat;
    }
}
