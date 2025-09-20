package gerenciadorprojetos.view;

import gerenciadorprojetos.model.Equipe;
import gerenciadorprojetos.model.Projeto;
import gerenciadorprojetos.model.Usuario;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class MainFrame extends JFrame {

    // --- ATRIBUTOS (VARIÁVEIS) DA CLASSE ---
    private JButton btnAdicionarUsuario, btnAdicionarProjeto, btnAdicionarEquipe, btnAdicionarMembro;
    private JButton btnEditar, btnExcluir;
    private JTable tabelaUsuarios, tabelaProjetos, tabelaEquipes;
    private DefaultTableModel modelUsuarios, modelProjetos, modelEquipes;
    private JTabbedPane painelAbas;

    // --- CONSTRUTOR DA CLASSE ---
    public MainFrame() {
        setTitle("Sistema de Gerenciamento de Projetos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Painel de botões superior (Ações de Adicionar)
        JPanel painelBotoesAdicionar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAdicionarUsuario = new JButton("Adicionar Usuário");
        btnAdicionarProjeto = new JButton("Adicionar Projeto");
        btnAdicionarEquipe = new JButton("Adicionar Equipe");
        btnAdicionarMembro = new JButton("Adicionar Membro à Equipe");
        
        painelBotoesAdicionar.add(btnAdicionarUsuario);
        painelBotoesAdicionar.add(btnAdicionarProjeto);
        painelBotoesAdicionar.add(btnAdicionarEquipe);
        painelBotoesAdicionar.add(btnAdicionarMembro);
        
        add(painelBotoesAdicionar, BorderLayout.NORTH);

        // Painel com Abas
        painelAbas = new JTabbedPane();

        // Aba de Usuários
        modelUsuarios = new DefaultTableModel(new Object[]{"ID", "Nome", "CPF", "Email", "Cargo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Não deixa editar na tabela
        };
        tabelaUsuarios = new JTable(modelUsuarios);
        esconderColuna(tabelaUsuarios, 0); // Esconde a coluna de ID
        painelAbas.addTab("Usuários", new JScrollPane(tabelaUsuarios));

        // Aba de Projetos
        modelProjetos = new DefaultTableModel(new Object[]{"ID", "Nome", "Descrição", "Status", "Gerente"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaProjetos = new JTable(modelProjetos);
        esconderColuna(tabelaProjetos, 0);
        painelAbas.addTab("Projetos", new JScrollPane(tabelaProjetos));

        // Aba de Equipes
        modelEquipes = new DefaultTableModel(new Object[]{"ID", "Nome", "Descrição", "Membros"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaEquipes = new JTable(modelEquipes);
        esconderColuna(tabelaEquipes, 0);
        painelAbas.addTab("Equipes", new JScrollPane(tabelaEquipes));
        
        add(painelAbas, BorderLayout.CENTER);
        
        // Painel de botões inferior (Ações de Edição)
        JPanel painelBotoesEdicao = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEditar = new JButton("Editar Selecionado");
        btnExcluir = new JButton("Excluir Selecionado");
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        
        painelBotoesEdicao.add(btnEditar);
        painelBotoesEdicao.add(btnExcluir);
        
        add(painelBotoesEdicao, BorderLayout.SOUTH);
    }
    
    // --- MÉTODOS DE ACESSO (GETTERS) PARA O CONTROLLER ---

    public JButton getBtnAdicionarUsuario() { return btnAdicionarUsuario; }
    public JButton getBtnAdicionarProjeto() { return btnAdicionarProjeto; }
    public JButton getBtnAdicionarEquipe() { return btnAdicionarEquipe; }
    public JButton getBtnAdicionarMembro() { return btnAdicionarMembro; }
    public JButton getBtnEditar() { return btnEditar; }
    public JButton getBtnExcluir() { return btnExcluir; }
    
    public JTable getTabelaUsuarios() { return tabelaUsuarios; }
    public JTable getTabelaProjetos() { return tabelaProjetos; }
    public JTable getTabelaEquipes() { return tabelaEquipes; }
    
    public JTabbedPane getPainelAbas() { return painelAbas; }

    // --- MÉTODOS PARA ATUALIZAR AS TABELAS ---

    public void atualizarTabelaUsuarios(List<Usuario> usuarios) {
        modelUsuarios.setRowCount(0);
        for (Usuario u : usuarios) {
            modelUsuarios.addRow(new Object[]{u.getId(), u.getNomeCompleto(), u.getCpf(), u.getEmail(), u.getCargo()});
        }
    }

    public void atualizarTabelaProjetos(List<Projeto> projetos) {
        modelProjetos.setRowCount(0);
        for (Projeto p : projetos) {
            String nomeGerente = (p.getGerente() != null ? p.getGerente().getNomeCompleto() : "N/A");
            modelProjetos.addRow(new Object[]{p.getId(), p.getNome(), p.getDescricao(), p.getStatus(), nomeGerente});
        }
    }

    public void atualizarTabelaEquipes(List<Equipe> equipes) {
        modelEquipes.setRowCount(0);
        for (Equipe e : equipes) {
            int numMembros = e.getMembros() != null ? e.getMembros().size() : 0;
            modelEquipes.addRow(new Object[]{e.getId(), e.getNome(), e.getDescricao(), numMembros});
        }
    }
    
    // --- MÉTODO AUXILIAR PARA ESCONDER COLUNAS ---
    private void esconderColuna(JTable tabela, int indiceColuna) {
        TableColumnModel tcm = tabela.getColumnModel();
        tcm.getColumn(indiceColuna).setMinWidth(0);
        tcm.getColumn(indiceColuna).setMaxWidth(0);
        tcm.getColumn(indiceColuna).setWidth(0);
    }
}