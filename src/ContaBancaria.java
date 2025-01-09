package src;

public class ContaBancaria {

    private int id;            // New field for the database record ID
    private int numeroConta;
    private String titular;
    private double saldo;
    private int pin;
    private String status;     // New field for status

    // Constructor that requires account details from the database
    public ContaBancaria(int id, int numeroConta, String titular, double saldo, int pin, String status) {
        this.id = id;          // Set the id from the database
        this.numeroConta = numeroConta;
        this.titular = titular;
        this.saldo = saldo;
        this.pin = pin;
        this.status = status;  // Set the status
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public int getNumeroConta() {
        return numeroConta;
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public int getPin() {
        return pin;
    }

    public String getStatus() {
        return status;
    }

    // Optionally add setter for pin if needed
    public void setPin(int pin) {
        this.pin = pin;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Other methods for account operations
    public void sacar(double valor) {
        if (saldo >= valor) {
            saldo -= valor;
        }
    }

    public void depositar(double valor) {
        saldo += valor;
    }
}