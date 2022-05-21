package testEntities;

import entities.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import testFactory.AccountFactory;

public class AccountTest {

    @Test
    public void depositShouldIncreaseBalanceWhenPositAmount(){
            double value = 200.0;
            double expectedValue = 196.0;
            Account acc = AccountFactory.createEmptyAccount();
            
            acc.deposit(value);

            Assertions.assertEquals(expectedValue,acc.getBalance());
    }
    @Test
    public void depositShouldDoNothingWhenValueNegative(){

        double value = -10.0;
        double expectedValue = 0.0;
        Account acc = AccountFactory.createEmptyAccount();

        acc.deposit(value);

        Assertions.assertEquals(expectedValue,acc.getBalance());
    }
    @Test
    public void fullWithdrawShouldClearBalance(){
        double initialBalance = 100.0;
        Account account =  AccountFactory.createInitialBalance(initialBalance);
        double expectedValue =0.0;
        double result = account.fullWithdraw();
        Assertions.assertEquals(expectedValue,account.getBalance());
        Assertions.assertTrue(result == initialBalance);
    }
    @Test
    public void withdrawShouldDecreseBalanceWhenSufficientBalance(){

        Account account =  AccountFactory.createInitialBalance(800.0);

        account.withdraw(500);

        Assertions.assertEquals(300.0,account.getBalance());
    }
    @Test
    public void withdrawShouldThrowEcveptionWhenSufficientBalance(){
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            Account account =  AccountFactory.createInitialBalance(800.0);
            account.withdraw(801);
        });
    }
}
