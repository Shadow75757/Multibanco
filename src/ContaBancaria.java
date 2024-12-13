package src;

public class ContaBancaria {
    private int numeroConta;
    private String titular;
    private double saldo;
    private String pin;

    // Constructor that requires account number, account holder name, and balance
    public ContaBancaria(int numeroConta, String titular, double saldo) {
        this.numeroConta = numeroConta;
        this.titular = titular;
        this.saldo = saldo;
        this.pin = ""; // Default pin, you can modify it later
    }

    // Getter and Setter methods for account attributes
    public int getNumeroConta() {
        return numeroConta;
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void sacar(double valor) {
        if (saldo >= valor) {
            saldo -= valor;
        }
    }

    public void depositar(double valor) {
        saldo += valor;
    }
}
