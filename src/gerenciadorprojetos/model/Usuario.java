package gerenciadorprojetos.model;

public class Usuario extends Pessoa {
    private int id;
    private String email, cargo, login, senha;

    public Usuario(String nomeCompleto, String cpf, String email, String cargo, String login, String senha) {
        super(nomeCompleto, cpf);
        this.email = email;
        this.cargo = cargo;
        this.login = login;
        this.senha = senha;
    }

    // --- GETTERS ---
    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getCargo() { return cargo; }
    public String getLogin() { return login; }
    public String getSenha() { return senha; }

    // --- SETTERS ---
    public void setId(int id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return this.getNomeCompleto();
    }
}