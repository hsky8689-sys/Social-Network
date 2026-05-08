package validatori;

import domain.User;

public abstract class ValidatorUser implements Validare{
  protected String[] dateUser;
    public ValidatorUser(String[] dateUser){
      this.dateUser=dateUser;
  }
 }
