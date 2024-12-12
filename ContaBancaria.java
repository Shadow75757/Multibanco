// Definição da classe que representa uma Conta Bancária
public class ContaBancaria {
    // Atributos privados da conta
    private int numeroConta;    // Número identificador único da conta
    private String titular;     // Nome do titular da conta
    private double saldo;       // Saldo atual da conta

    // Construtor para criar uma nova conta bancária
    // Recebe o número da conta, nome do titular e saldo inicial
    public ContaBancaria(int numeroConta, String titular, double saldoInicial) {
        // Inicializa os atributos com os valores passados como parâmetro
        this.numeroConta = numeroConta;  // Define o número da conta
        this.titular = titular;          // Define o nome do titular
        this.saldo = saldoInicial;       // Define o saldo inicial
    }

    // Método para realizar depósitos na conta
    public void depositar(double valor) {
        // Verifica se o valor do depósito é positivo
        if (valor > 0) {
            // Adiciona o valor ao saldo atual
            this.saldo += valor;
        }
    }

    // Método para realizar levantamentos (saques) na conta
    public void levantar(double valor) {
        // Verifica se o valor do levantamento é positivo 
        // E se há saldo suficiente para o levantamento
        if (valor > 0 && valor <= this.saldo) {
            // Subtrai o valor do saldo atual
            this.saldo -= valor;
        }
    }

    // Método getter para obter o número da conta
    public int getNumeroConta() {
        return numeroConta;
    }

    // Método getter para obter o nome do titular da conta
    public String getTitular() {
        return titular;
    }

    // Método getter para obter o saldo atual da conta
    public double getSaldo() {
        return saldo;
    }
}