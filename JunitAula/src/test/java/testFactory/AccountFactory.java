package testFactory;

import entities.Account;

public class AccountFactory {
    public static Account createEmptyAccount(){
        return new Account(1L,0.0);
    }
    public static Account createInitialBalance(double initial){
        return new Account(1L,initial);
    }
}
