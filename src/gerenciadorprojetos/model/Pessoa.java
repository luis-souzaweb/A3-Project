package gerenciadorprojetos.model;

public class Pessoa {
    private String nomeCompleto;
    private String cpf;

    public Pessoa(String nomeCompleto, String cpf) {
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
    }

    // --- GETTERS ---
    public String getNomeCompleto() { return nomeCompleto; }
    public String getCpf() { return cpf; }

    // --- SETTERS ---
    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}