package entities;

public class Account{

    public static double DEPOSIT_FEE_PERCENTAGE = 0.02;

    private Long id;
    private Double balance;

    public Account(){}

    public Account(Long id, Double balance) {
        this.id = id;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }
    public void deposit(Double value){
        if(value>0){
            value= value - value*DEPOSIT_FEE_PERCENTAGE;
            balance=+value;
        }
    }
    public void withdraw(double value){
        if(value>balance){
            throw new IllegalArgumentException();
        }
        balance=balance -value;
    }
    public double fullWithdraw(){
        double aux = this.balance;
        this.balance = 0.0;
        return aux;
    }
}
