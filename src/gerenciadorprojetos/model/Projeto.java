package gerenciadorprojetos.model;

public class Projeto {
    private int id;
    private String nome, descricao, dataInicio, dataTerminoPrevista, status;
    private Usuario gerente;

    public Projeto(String nome, String descricao, String dataInicio, String dataTerminoPrevista, Usuario gerente) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataTerminoPrevista = dataTerminoPrevista;
        this.status = "Planejado";
        this.gerente = gerente;
    }
    
    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getStatus() { return status; }
    public String getDataInicio() { return dataInicio; }
    public String getDataTerminoPrevista() { return dataTerminoPrevista; }
    public Usuario getGerente() { return gerente; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setStatus(String status) { this.status = status; }
    public void setDataInicio(String dataInicio) { this.dataInicio = dataInicio; }
    public void setDataTerminoPrevista(String dataTerminoPrevista) { this.dataTerminoPrevista = dataTerminoPrevista; }
    public void setGerente(Usuario gerente) { this.gerente = gerente; }

    @Override
    public String toString() {
        return this.getNome();
    }
}