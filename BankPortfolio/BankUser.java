class BankUser {
    private int userId;
    private int pin;
    private String name;
    private double balance;

    public BankUser(int userId, int pin, String name, double balance) {
        this.userId = userId;
        this.pin = pin;
        this.name = name;
        this.balance = balance;
    }


    public int getUserId() {
        return userId;
    }

    public int getPin() {
        return pin;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

 
    public void cashIn(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }
    public boolean transfer(BankUser receiver, double amount) {
        if (this.balance >= amount && amount > 0) {
            this.balance -= amount;
            receiver.setBalance(receiver.getBalance() + amount);
            return true;
        } else {
            return false;
        }
    }
}
