package gerenciadorprojetos.controller;

import gerenciadorprojetos.model.Equipe;
import gerenciadorprojetos.model.Projeto;
import gerenciadorprojetos.model.Usuario;
import gerenciadorprojetos.model.dao.EquipeDAO;
import gerenciadorprojetos.model.dao.ProjetoDAO;
import gerenciadorprojetos.model.dao.UsuarioDAO;
import gerenciadorprojetos.view.MainFrame;
import java.util.List;
import javax.swing.*;

public class AppController {

    private final MainFrame view;
    private final UsuarioDAO usuarioDAO;
    private final ProjetoDAO projetoDAO;
    private final EquipeDAO equipeDAO;

    public AppController() {
        this.usuarioDAO = new UsuarioDAO();
        this.projetoDAO = new ProjetoDAO();
        this.equipeDAO = new EquipeDAO();
        this.view = new MainFrame();
        this.vincularAcoes();
    }

    public void iniciar() {
        atualizarTodasTabelas();
        view.setVisible(true);
    }

    private void vincularAcoes() {
        view.getBtnAdicionarUsuario().addActionListener(e -> adicionarUsuario());
        view.getBtnAdicionarProjeto().addActionListener(e -> adicionarProjeto());
        view.getBtnAdicionarEquipe().addActionListener(e -> adicionarEquipe());
        view.getBtnAdicionarMembro().addActionListener(e -> adicionarMembroAEquipe());
        view.getBtnEditar().addActionListener(e -> editarSelecionado());
        view.getBtnExcluir().addActionListener(e -> excluirSelecionado());
        view.getTabelaUsuarios().getSelectionModel().addListSelectionListener(e -> atualizarEstadoBotoes());
        view.getTabelaProjetos().getSelectionModel().addListSelectionListener(e -> atualizarEstadoBotoes());
        view.getTabelaEquipes().getSelectionModel().addListSelectionListener(e -> atualizarEstadoBotoes());
        view.getPainelAbas().addChangeListener(e -> atualizarEstadoBotoes());
    }

    private void atualizarTodasTabelas() {
        view.atualizarTabelaUsuarios(usuarioDAO.listarTodos());
        view.atualizarTabelaProjetos(projetoDAO.listarTodos());
        view.atualizarTabelaEquipes(equipeDAO.listarTodos());
    }

    private void atualizarEstadoBotoes() {
        boolean itemSelecionado = false;
        int abaSelecionada = view.getPainelAbas().getSelectedIndex();
        if (abaSelecionada == 0) itemSelecionado = view.getTabelaUsuarios().getSelectedRow() != -1;
        else if (abaSelecionada == 1) itemSelecionado = view.getTabelaProjetos().getSelectedRow() != -1;
        else if (abaSelecionada == 2) itemSelecionado = view.getTabelaEquipes().getSelectedRow() != -1;
        view.getBtnEditar().setEnabled(itemSelecionado);
        view.getBtnExcluir().setEnabled(itemSelecionado);
    }

    private void editarSelecionado() {
        int abaSelecionada = view.getPainelAbas().getSelectedIndex();
        if (abaSelecionada == 0) { // Editar Usuário
            int selectedRow = view.getTabelaUsuarios().getSelectedRow();
            if (selectedRow == -1) return;
            int usuarioId = (int) view.getTabelaUsuarios().getValueAt(selectedRow, 0);
            Usuario usuarioParaEditar = null;
            for (Usuario u : usuarioDAO.listarTodos()) {
                if (u.getId() == usuarioId) { usuarioParaEditar = u; break; }
            }
            if (usuarioParaEditar != null) mostrarDialogoUsuario(usuarioParaEditar);
        } else if (abaSelecionada == 1) { // Editar Projeto
            int selectedRow = view.getTabelaProjetos().getSelectedRow();
            if (selectedRow == -1) return;
            int projetoId = (int) view.getTabelaProjetos().getValueAt(selectedRow, 0);
            Projeto projetoParaEditar = null;
            for (Projeto p : projetoDAO.listarTodos()) {
                if (p.getId() == projetoId) { projetoParaEditar = p; break; }
            }
            if (projetoParaEditar != null) mostrarDialogoProjeto(projetoParaEditar);
        } else if (abaSelecionada == 2) { // Editar Equipe
            int selectedRow = view.getTabelaEquipes().getSelectedRow();
            if (selectedRow == -1) return;
            int equipeId = (int) view.getTabelaEquipes().getValueAt(selectedRow, 0);
            Equipe equipeParaEditar = null;
            for (Equipe e : equipeDAO.listarTodos()) {
                if (e.getId() == equipeId) { equipeParaEditar = e; break; }
            }
            if (equipeParaEditar != null) mostrarDialogoEquipe(equipeParaEditar);
        }
    }

    private void excluirSelecionado() {
        int abaSelecionada = view.getPainelAbas().getSelectedIndex();
        if (abaSelecionada == 0) { // Excluir Usuário
            int selectedRow = view.getTabelaUsuarios().getSelectedRow();
            if (selectedRow == -1) return;
            int usuarioId = (int) view.getTabelaUsuarios().getValueAt(selectedRow, 0);
            String nomeUsuario = (String) view.getTabelaUsuarios().getValueAt(selectedRow, 1);
            int confirmacao = JOptionPane.showConfirmDialog(view, "Excluir usuário: " + nomeUsuario + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacao == JOptionPane.YES_OPTION) {
                usuarioDAO.excluir(usuarioId);
                atualizarTodasTabelas();
            }
        } else if (abaSelecionada == 1) { // Excluir Projeto
            int selectedRow = view.getTabelaProjetos().getSelectedRow();
            if (selectedRow == -1) return;
            int projetoId = (int) view.getTabelaProjetos().getValueAt(selectedRow, 0);
            String nomeProjeto = (String) view.getTabelaProjetos().getValueAt(selectedRow, 1);
            int confirmacao = JOptionPane.showConfirmDialog(view, "Excluir projeto: " + nomeProjeto + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacao == JOptionPane.YES_OPTION) {
                projetoDAO.excluir(projetoId);
                atualizarTodasTabelas();
            }
        } else if (abaSelecionada == 2) { // Excluir Equipe
            int selectedRow = view.getTabelaEquipes().getSelectedRow();
            if (selectedRow == -1) return;
            int equipeId = (int) view.getTabelaEquipes().getValueAt(selectedRow, 0);
            String nomeEquipe = (String) view.getTabelaEquipes().getValueAt(selectedRow, 1);
            int confirmacao = JOptionPane.showConfirmDialog(view, "Excluir equipe: " + nomeEquipe + "?\n(Todos os membros serão desvinculados)", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacao == JOptionPane.YES_OPTION) {
                equipeDAO.excluir(equipeId);
                atualizarTodasTabelas();
            }
        }
    }

    // --- DIÁLOGO DE USUÁRIO ---
    private void mostrarDialogoUsuario(Usuario usuario) {
        boolean isEdit = usuario != null;
        JTextField nomeField = new JTextField(isEdit ? usuario.getNomeCompleto() : "");
        JTextField cpfField = new JTextField(isEdit ? usuario.getCpf() : "");
        JTextField emailField = new JTextField(isEdit ? usuario.getEmail() : "");
        JTextField cargoField = new JTextField(isEdit ? usuario.getCargo() : "");
        JTextField loginField = new JTextField(isEdit ? usuario.getLogin() : "");
        JPasswordField senhaField = new JPasswordField(isEdit ? usuario.getSenha() : "");

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.add(new JLabel("Nome Completo:")); painel.add(nomeField);
        painel.add(new JLabel("CPF:")); painel.add(cpfField);
        painel.add(new JLabel("Email:")); painel.add(emailField);
        painel.add(new JLabel("Cargo:")); painel.add(cargoField);
        painel.add(new JLabel("Login:")); painel.add(loginField);
        painel.add(new JLabel("Senha:")); painel.add(senhaField);

        String titulo = isEdit ? "Editar Usuário" : "Adicionar Novo Usuário";
        int resultado = JOptionPane.showConfirmDialog(view, painel, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            String senha = new String(senhaField.getPassword());
            if (isEdit) {
                usuario.setNomeCompleto(nomeField.getText());
                usuario.setCpf(cpfField.getText());
                usuario.setEmail(emailField.getText());
                usuario.setCargo(cargoField.getText());
                usuario.setLogin(loginField.getText());
                usuario.setSenha(senha);
                usuarioDAO.atualizar(usuario);
                JOptionPane.showMessageDialog(view, "Usuário atualizado com sucesso!");
            } else {
                Usuario novoUsuario = new Usuario(nomeField.getText(), cpfField.getText(), emailField.getText(), cargoField.getText(), loginField.getText(), senha);
                usuarioDAO.adicionar(novoUsuario);
                JOptionPane.showMessageDialog(view, "Usuário adicionado com sucesso!");
            }
            atualizarTodasTabelas();
        }
    }

    private void adicionarUsuario() {
        mostrarDialogoUsuario(null);
    }

    // --- DIÁLOGO DE PROJETO ---
    private void mostrarDialogoProjeto(Projeto projeto) {
        boolean isEdit = projeto != null;
        List<Usuario> usuarios = usuarioDAO.listarTodos();

        JTextField nomeField = new JTextField(isEdit ? projeto.getNome() : "");
        JTextField descField = new JTextField(isEdit ? projeto.getDescricao() : "");
        JTextField dataInicioField = new JTextField(isEdit ? projeto.getDataInicio() : "");
        JTextField dataTerminoField = new JTextField(isEdit ? projeto.getDataTerminoPrevista() : "");
        JComboBox<Usuario> gerenteComboBox = new JComboBox<>(usuarios.toArray(new Usuario[0]));
        
        // Lógica para pré-selecionar o gerente correto na edição
        if (isEdit && projeto.getGerente() != null) {
            for (int i = 0; i < usuarios.size(); i++) {
                if (usuarios.get(i).getId() == projeto.getGerente().getId()) {
                    gerenteComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.add(new JLabel("Nome do Projeto:")); painel.add(nomeField);
        painel.add(new JLabel("Descrição:")); painel.add(descField);
        painel.add(new JLabel("Data de Início (dd/mm/aaaa):")); painel.add(dataInicioField);
        painel.add(new JLabel("Data de Término (dd/mm/aaaa):")); painel.add(dataTerminoField);
        painel.add(new JLabel("Gerente Responsável:")); painel.add(gerenteComboBox);
        
        String titulo = isEdit ? "Editar Projeto" : "Adicionar Novo Projeto";
        int resultado = JOptionPane.showConfirmDialog(view, painel, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            Usuario gerenteSelecionado = (Usuario) gerenteComboBox.getSelectedItem();
            if (isEdit) {
                // Atualiza o objeto existente com os novos dados da tela
                projeto.setNome(nomeField.getText());
                projeto.setDescricao(descField.getText());
                projeto.setDataInicio(dataInicioField.getText());
                projeto.setDataTerminoPrevista(dataTerminoField.getText());
                projeto.setGerente(gerenteSelecionado);
                
                // Manda o objeto atualizado para o DAO salvar
                projetoDAO.atualizar(projeto);
                JOptionPane.showMessageDialog(view, "Projeto atualizado com sucesso!");
            } else {
                Projeto novoProjeto = new Projeto(
                    nomeField.getText(), 
                    descField.getText(), 
                    dataInicioField.getText(),
                    dataTerminoField.getText(),
                    gerenteSelecionado
                );
                projetoDAO.adicionar(novoProjeto);
                JOptionPane.showMessageDialog(view, "Projeto adicionado com sucesso!");
            }
            atualizarTodasTabelas();
        }
    }

    private void adicionarProjeto() {
        if (usuarioDAO.listarTodos().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Cadastre um usuário antes de criar um projeto.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        mostrarDialogoProjeto(null);
    }

    // --- DIÁLOGO DE EQUIPE ---
    private void mostrarDialogoEquipe(Equipe equipe) {
        boolean isEdit = equipe != null;
        JTextField nomeField = new JTextField(isEdit ? equipe.getNome() : "");
        JTextField descField = new JTextField(isEdit ? equipe.getDescricao() : "");
        
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.add(new JLabel("Nome da Equipe:")); painel.add(nomeField);
        painel.add(new JLabel("Descrição:")); painel.add(descField);

        String titulo = isEdit ? "Editar Equipe" : "Adicionar Nova Equipe";
        int resultado = JOptionPane.showConfirmDialog(view, painel, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            if (isEdit) {
                equipe.setNome(nomeField.getText());
                equipe.setDescricao(descField.getText());
                equipeDAO.atualizar(equipe);
                JOptionPane.showMessageDialog(view, "Equipe atualizada com sucesso!");
            } else {
                Equipe novaEquipe = new Equipe(nomeField.getText(), descField.getText());
                equipeDAO.adicionar(novaEquipe);
                JOptionPane.showMessageDialog(view, "Equipe adicionada com sucesso!");
            }
            atualizarTodasTabelas();
        }
    }

    private void adicionarEquipe() {
        mostrarDialogoEquipe(null);
    }

    // --- LÓGICA DE ADICIONAR MEMBRO ---
    private void adicionarMembroAEquipe() {
        List<Equipe> equipes = equipeDAO.listarTodos();
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        if (equipes.isEmpty() || usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(view, "É necessário ter ao menos uma equipe e um usuário cadastrados.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Equipe equipeSelecionada = (Equipe) JOptionPane.showInputDialog(view, "Selecione a Equipe:", "Adicionar Membro", JOptionPane.PLAIN_MESSAGE, null, equipes.toArray(), equipes.get(0));
        if (equipeSelecionada == null) return;
        Usuario membroSelecionado = (Usuario) JOptionPane.showInputDialog(view, "Selecione o Usuário:", "Adicionar Membro", JOptionPane.PLAIN_MESSAGE, null, usuarios.toArray(), usuarios.get(0));
        if (membroSelecionado != null) {
            equipeDAO.adicionarMembro(equipeSelecionada.getId(), membroSelecionado.getId());
            atualizarTodasTabelas();
            JOptionPane.showMessageDialog(view, "Membro '" + membroSelecionado.getNomeCompleto() + "' adicionado à equipe '" + equipeSelecionada.getNome() + "'.");
        }
    }
}