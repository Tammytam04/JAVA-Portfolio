import java.awt.event.*;
import java.util.HashMap;
import java.util.Random;
import javax.swing.*;

public class BankingApp {
    private HashMap<Integer, BankUser> users = new HashMap<>();
    private BankUser currentUser;

    public BankingApp() {
        //These are the two default users
        users.put(412435, new BankUser(412435, 7452, "Chris Sandoval", 32000));
        users.put(264863, new BankUser(264863, 1349, "Marc Yim", 1000));
        showLoginScreen();
    }

    public void showLoginScreen() {
        JFrame frame = new JFrame("Banking App - Login");
        JLabel labelId = new JLabel("User ID:");
        JLabel labelPin = new JLabel("PIN:");
        JTextField textId = new JTextField();
        JPasswordField textPin = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Create Account");

        frame.setLayout(null);
        labelId.setBounds(50, 30, 80, 25);
        textId.setBounds(140, 30, 200, 25);
        labelPin.setBounds(50, 70, 80, 25);
        textPin.setBounds(140, 70, 200, 25);
        loginButton.setBounds(140, 110, 200, 25);
        createAccountButton.setBounds(140, 150, 200, 25);

        frame.add(labelId);
        frame.add(textId);
        frame.add(labelPin);
        frame.add(textPin);
        frame.add(loginButton);
        frame.add(createAccountButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int userId = Integer.parseInt(textId.getText());
                int pin = Integer.parseInt(new String(textPin.getPassword()));
                login(userId, pin, frame);
            }
        });

        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createAccount(frame);
            }
        });

        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }

    public void login(int userId, int pin, JFrame frame) {
        BankUser user = users.get(userId);
        if (user != null && user.getPin() == pin) {
            currentUser = user;
            frame.dispose();
            showMainMenu();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid user ID or PIN.");
        }
    }

    public void showMainMenu() {
        JFrame frame = new JFrame("Banking App - Main Menu");
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName());
        JButton checkBalanceButton = new JButton("Check Balance");
        JButton cashInButton = new JButton("Cash-In");
        JButton transferButton = new JButton("Transfer Money");
        JButton logoutButton = new JButton("Logout");
        JButton deleteAccountButton = new JButton("Delete Account");

        frame.setLayout(null);
        welcomeLabel.setBounds(30, 30, 250, 25);
        checkBalanceButton.setBounds(30, 70, 250, 30);
        cashInButton.setBounds(30, 110, 250, 30);
        transferButton.setBounds(30, 150, 250, 30);
        logoutButton.setBounds(30, 190, 250, 30);
        deleteAccountButton.setBounds(30, 230, 250, 30);

        frame.add(welcomeLabel);
        frame.add(checkBalanceButton);
        frame.add(cashInButton);
        frame.add(transferButton);
        frame.add(logoutButton);
        frame.add(deleteAccountButton);

        checkBalanceButton.addActionListener(e -> checkBalance(frame));
        cashInButton.addActionListener(e -> cashIn(frame));
        transferButton.addActionListener(e -> transferMoney(frame));
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?");
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose();
                showLoginScreen();
            }
        });
        deleteAccountButton.addActionListener(e -> deleteAccount(frame));

        frame.setSize(350, 350); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }

    // Check balance
    public void checkBalance(JFrame frame) {
        JOptionPane.showMessageDialog(frame, "Your balance is: ₱" + currentUser.getBalance());
    }

    // Cash-in money
    public void cashIn(JFrame frame) {
        String input = JOptionPane.showInputDialog(frame, "Enter amount to cash-in:");
        double amount = Double.parseDouble(input);
        currentUser.cashIn(amount);
        JOptionPane.showMessageDialog(frame, "Cash-in successful. Your new balance is: ₱" + currentUser.getBalance());
    }

    // Transfer money
    public void transferMoney(JFrame frame) {
        String userIdInput = JOptionPane.showInputDialog(frame, "Enter receiver's User ID:");
        String amountInput = JOptionPane.showInputDialog(frame, "Enter amount to transfer:");

        int receiverId = Integer.parseInt(userIdInput);
        double amount = Double.parseDouble(amountInput);

        BankUser receiver = users.get(receiverId);
        if (receiver != null && currentUser.transfer(receiver, amount)) {
            JOptionPane.showMessageDialog(frame, 
                "Transfer successful.\n" +
                "Your new balance is: ₱" + currentUser.getBalance());
        } else {
            JOptionPane.showMessageDialog(frame, "Transfer failed. Check balance or receiver ID.");
        }
    }
    // Create account function. it also generates random userid and pin
    public void createAccount(JFrame frame) {
        String name = JOptionPane.showInputDialog(frame, "Enter your name:");
        if (name == null || name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Name cannot be empty.");
            return;
        }
        Random random = new Random();
        int newUserId = 100000 + random.nextInt(900000); 
        int newPin = 1000 + random.nextInt(9000); 

        BankUser newUser = new BankUser(newUserId, newPin, name, 0);
        users.put(newUserId, newUser);

        JOptionPane.showMessageDialog(frame, "Account created!\nUser ID: " + newUserId + "\nPIN: " + newPin);
    }

    // Delete account function
    public void deleteAccount(JFrame frame) {
        String userIdInput = JOptionPane.showInputDialog(frame, "Enter your User ID:");
        String pinInput = JOptionPane.showInputDialog(frame, "Enter your PIN:");
    
        int userId = Integer.parseInt(userIdInput);
        int pin = Integer.parseInt(pinInput);
    
        BankUser user = users.get(userId);
        if (user != null && user.getPin() == pin) {
            if (user.getBalance() == 0) {
                users.remove(userId);
                JOptionPane.showMessageDialog(frame, "Account deleted successfully.");
                frame.dispose();  
                showLoginScreen();  
            } else {
                JOptionPane.showMessageDialog(frame, "Cannot delete account. Balance is greater than 0.\n" +
                        "If you want to delete your account, transfer the balance to another account.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid user ID or PIN.");
        }
    }
       
    public static void main(String[] args) {
        new BankingApp();
    }
}
