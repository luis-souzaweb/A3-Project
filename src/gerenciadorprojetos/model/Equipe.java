package gerenciadorprojetos.model;

import java.util.ArrayList;
import java.util.List;

public class Equipe {
    private int id;
    private String nome;
    private String descricao;
    private final List<Usuario> membros;

    public Equipe(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
        this.membros = new ArrayList<>();
    }
    
    // --- GETTERS ---
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public List<Usuario> getMembros() { return membros; }
    
    // --- SETTERS ---
    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public void adicionarMembro(Usuario membro) { this.membros.add(membro); }

    @Override
    public String toString() {
        return this.getNome();
    }
}