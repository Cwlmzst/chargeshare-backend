public class User {
    private int userId;
    private String name;
    private String email;
    private double balance; // Account balance for charging

    public User(int userId, String name, String email, double balance) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean deductBalance(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void addBalance(double amount) {
        balance += amount;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", balance=" + balance +
                '}';
    }
}
