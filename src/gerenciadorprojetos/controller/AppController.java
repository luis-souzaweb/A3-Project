package gerenciadorprojetos.controller;

import gerenciadorprojetos.model.Equipe;
import gerenciadorprojetos.model.Projeto;
import gerenciadorprojetos.model.Usuario;
import gerenciadorprojetos.model.dao.EquipeDAO;
import gerenciadorprojetos.model.dao.ProjetoDAO;
import gerenciadorprojetos.model.dao.UsuarioDAO;
import gerenciadorprojetos.view.MainFrame;
import java.awt.Component;
import java.text.ParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;

public class AppController {

    private final MainFrame view;
    private final UsuarioDAO usuarioDAO;
    private final ProjetoDAO projetoDAO;
    private final EquipeDAO equipeDAO;
    private TableRowSorter<DefaultTableModel> sorterUsuarios;
    private TableRowSorter<DefaultTableModel> sorterProjetos;
    private TableRowSorter<DefaultTableModel> sorterEquipes;

    public AppController() {
        this.usuarioDAO = new UsuarioDAO();
        this.projetoDAO = new ProjetoDAO();
        this.equipeDAO = new EquipeDAO();
        this.view = new MainFrame();
        this.vincularAcoes();
    }

    public void iniciar() {
        configurarFiltros();
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
        view.getBtnVerMembros().addActionListener(e -> verMembrosDaEquipe());
        
        view.getTabelaUsuarios().getSelectionModel().addListSelectionListener(e -> atualizarEstadoBotoes());
        view.getTabelaProjetos().getSelectionModel().addListSelectionListener(e -> atualizarEstadoBotoes());
        view.getTabelaEquipes().getSelectionModel().addListSelectionListener(e -> atualizarEstadoBotoes());
        
        view.getPainelAbas().addChangeListener(e -> {
            atualizarEstadoBotoes();
            filtrarTabelas();
        });
        
        view.getCampoBusca().getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filtrarTabelas(); }
            @Override public void removeUpdate(DocumentEvent e) { filtrarTabelas(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrarTabelas(); }
        });
    }

    private void configurarFiltros() {
        DefaultTableModel modelUsuarios = (DefaultTableModel) view.getTabelaUsuarios().getModel();
        sorterUsuarios = new TableRowSorter<>(modelUsuarios);
        view.getTabelaUsuarios().setRowSorter(sorterUsuarios);
        
        DefaultTableModel modelProjetos = (DefaultTableModel) view.getTabelaProjetos().getModel();
        sorterProjetos = new TableRowSorter<>(modelProjetos);
        view.getTabelaProjetos().setRowSorter(sorterProjetos);
        
        DefaultTableModel modelEquipes = (DefaultTableModel) view.getTabelaEquipes().getModel();
        sorterEquipes = new TableRowSorter<>(modelEquipes);
        view.getTabelaEquipes().setRowSorter(sorterEquipes);
    }

    private void filtrarTabelas() {
        String texto = view.getCampoBusca().getText();
        int abaSelecionada = view.getPainelAbas().getSelectedIndex();

        RowFilter<Object, Object> filtro = null;
        if (texto.trim().length() > 0) {
            filtro = RowFilter.regexFilter("(?i)" + texto);
        }

        if (abaSelecionada == 0) sorterUsuarios.setRowFilter(filtro);
        else if (abaSelecionada == 1) sorterProjetos.setRowFilter(filtro);
        else if (abaSelecionada == 2) sorterEquipes.setRowFilter(filtro);
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
        view.getBtnVerMembros().setEnabled(itemSelecionado && abaSelecionada == 2);
    }
    
    private void editarSelecionado() {
        int abaSelecionada = view.getPainelAbas().getSelectedIndex();
        if (abaSelecionada == 0) {
            int selectedRow = view.getTabelaUsuarios().getSelectedRow();
            if (selectedRow == -1) return;
            int usuarioId = (int) view.getTabelaUsuarios().getValueAt(selectedRow, 0);
            Usuario usuarioParaEditar = null;
            for (Usuario u : usuarioDAO.listarTodos()) {
                if (u.getId() == usuarioId) { usuarioParaEditar = u; break; }
            }
            if (usuarioParaEditar != null) mostrarDialogoUsuario(usuarioParaEditar);
        } else if (abaSelecionada == 1) {
            int selectedRow = view.getTabelaProjetos().getSelectedRow();
            if (selectedRow == -1) return;
            int projetoId = (int) view.getTabelaProjetos().getValueAt(selectedRow, 0);
            Projeto projetoParaEditar = null;
            for (Projeto p : projetoDAO.listarTodos()) {
                if (p.getId() == projetoId) { projetoParaEditar = p; break; }
            }
            if (projetoParaEditar != null) mostrarDialogoProjeto(projetoParaEditar);
        } else if (abaSelecionada == 2) {
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
        if (abaSelecionada == 0) {
            int selectedRow = view.getTabelaUsuarios().getSelectedRow();
            if (selectedRow == -1) return;
            int usuarioId = (int) view.getTabelaUsuarios().getValueAt(selectedRow, 0);
            String nomeUsuario = (String) view.getTabelaUsuarios().getValueAt(selectedRow, 1);
            int confirmacao = JOptionPane.showConfirmDialog(view, "Excluir usuário: " + nomeUsuario + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacao == JOptionPane.YES_OPTION) {
                usuarioDAO.excluir(usuarioId);
                atualizarTodasTabelas();
            }
        } else if (abaSelecionada == 1) {
            int selectedRow = view.getTabelaProjetos().getSelectedRow();
            if (selectedRow == -1) return;
            int projetoId = (int) view.getTabelaProjetos().getValueAt(selectedRow, 0);
            String nomeProjeto = (String) view.getTabelaProjetos().getValueAt(selectedRow, 1);
            int confirmacao = JOptionPane.showConfirmDialog(view, "Excluir projeto: " + nomeProjeto + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacao == JOptionPane.YES_OPTION) {
                projetoDAO.excluir(projetoId);
                atualizarTodasTabelas();
            }
        } else if (abaSelecionada == 2) {
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
    
    private void verMembrosDaEquipe() {
        int selectedRow = view.getTabelaEquipes().getSelectedRow();
        if (selectedRow == -1) return;
        int equipeId = (int) view.getTabelaEquipes().getValueAt(selectedRow, 0);
        String nomeEquipe = (String) view.getTabelaEquipes().getValueAt(selectedRow, 1);
        Equipe equipeSelecionada = null;
        for(Equipe e : equipeDAO.listarTodos()){
            if(e.getId() == equipeId){ equipeSelecionada = e; break; }
        }
        if (equipeSelecionada != null) {
            List<Usuario> membros = equipeSelecionada.getMembros();
            StringBuilder mensagem = new StringBuilder("Membros da Equipe '" + nomeEquipe + "':\n\n");
            if (membros.isEmpty()) {
                mensagem.append("Nenhum membro cadastrado nesta equipe.");
            } else {
                for (Usuario membro : membros) {
                    mensagem.append("- ").append(membro.getNomeCompleto()).append("\n");
                }
            }
            JOptionPane.showMessageDialog(view, mensagem.toString(), "Membros da Equipe", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void mostrarDialogoUsuario(Usuario usuario) {
        boolean isEdit = usuario != null;
        JTextField nomeField = new JTextField(isEdit ? usuario.getNomeCompleto() : "");
        JFormattedTextField cpfField = null;
        try {
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfField = new JFormattedTextField(cpfMask);
            if (isEdit) cpfField.setText(usuario.getCpf());
        } catch (ParseException e) { e.printStackTrace(); cpfField = new JFormattedTextField(); }
        JTextField emailField = new JTextField(isEdit ? usuario.getEmail() : "");
        String[] cargos = {"Colaborador", "Gerente", "Administrador"};
        JComboBox<String> cargoComboBox = new JComboBox<>(cargos);
        if (isEdit) cargoComboBox.setSelectedItem(usuario.getCargo());
        JTextField loginField = new JTextField(isEdit ? usuario.getLogin() : "");
        JPasswordField senhaField = new JPasswordField(isEdit ? usuario.getSenha() : "");

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.add(new JLabel("Nome Completo:")); painel.add(nomeField);
        painel.add(new JLabel("CPF:")); painel.add(cpfField);
        painel.add(new JLabel("Email:")); painel.add(emailField);
        painel.add(new JLabel("Cargo:")); painel.add(cargoComboBox);
        painel.add(new JLabel("Login:")); painel.add(loginField);
        painel.add(new JLabel("Senha:")); painel.add(senhaField);

        String titulo = isEdit ? "Editar Usuário" : "Adicionar Novo Usuário";
        int resultado = JOptionPane.showConfirmDialog(view, painel, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            String nome = nomeField.getText().trim();
            String cpf = cpfField.getText().replaceAll("[^0-9]", "");
            String email = emailField.getText().trim();
            String login = loginField.getText().trim();
            String senha = new String(senhaField.getPassword()).trim();

            if (nome.isEmpty() || cpf.length() != 11 || login.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Todos os campos (Nome, CPF, Login, Senha) são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
            if (!email.isEmpty() && !email.matches(emailRegex)) {
                JOptionPane.showMessageDialog(view, "O formato do e-mail é inválido.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (isEdit) {
                if (usuarioDAO.cpfJaExiste(cpf) && !cpf.equals(usuario.getCpf())) {
                     JOptionPane.showMessageDialog(view, "Este CPF já está cadastrado no sistema.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                     return;
                }
            } else {
                if (usuarioDAO.cpfJaExiste(cpf)) {
                    JOptionPane.showMessageDialog(view, "Este CPF já está cadastrado no sistema.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            String cargoSelecionado = (String) cargoComboBox.getSelectedItem();
            if (isEdit) {
                usuario.setNomeCompleto(nome);
                usuario.setCpf(cpf);
                usuario.setEmail(email);
                usuario.setCargo(cargoSelecionado);
                usuario.setLogin(login);
                usuario.setSenha(senha);
                usuarioDAO.atualizar(usuario);
                JOptionPane.showMessageDialog(view, "Usuário atualizado com sucesso!");
            } else {
                Usuario novoUsuario = new Usuario(nome, cpf, email, cargoSelecionado, login, senha);
                usuarioDAO.adicionar(novoUsuario);
                JOptionPane.showMessageDialog(view, "Usuário adicionado com sucesso!");
            }
            atualizarTodasTabelas();
        }
    }

    private void adicionarUsuario() { mostrarDialogoUsuario(null); }

    private void mostrarDialogoProjeto(Projeto projeto) {
        boolean isEdit = projeto != null;
        List<Usuario> gerentes = usuarioDAO.listarGerentes();
        JTextField nomeField = new JTextField(isEdit ? projeto.getNome() : "");
        JTextField descField = new JTextField(isEdit ? projeto.getDescricao() : "");
        JFormattedTextField dataInicioField = null;
        JFormattedTextField dataTerminoField = null;
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dataInicioField = new JFormattedTextField(dateMask);
            if (isEdit) dataInicioField.setText(projeto.getDataInicio());
            dataTerminoField = new JFormattedTextField(dateMask);
            if (isEdit) dataTerminoField.setText(projeto.getDataTerminoPrevista());
        } catch (ParseException e) { e.printStackTrace(); }
        String[] statusOptions = {"Planejado", "Em Andamento", "Concluído", "Cancelado"};
        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
        if (isEdit) statusComboBox.setSelectedItem(projeto.getStatus());
        JComboBox<Usuario> gerenteComboBox = new JComboBox<>(gerentes.toArray(new Usuario[0]));
        if (isEdit && projeto.getGerente() != null) {
            for (int i = 0; i < gerentes.size(); i++) {
                if (gerentes.get(i).getId() == projeto.getGerente().getId()) {
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
        painel.add(new JLabel("Status:")); painel.add(statusComboBox);
        painel.add(new JLabel("Gerente Responsável:")); painel.add(gerenteComboBox);
        
        String titulo = isEdit ? "Editar Projeto" : "Adicionar Novo Projeto";
        int resultado = JOptionPane.showConfirmDialog(view, painel, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            String nome = nomeField.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(view, "O campo 'Nome do Projeto' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Usuario gerenteSelecionado = (Usuario) gerenteComboBox.getSelectedItem();
            String statusSelecionado = (String) statusComboBox.getSelectedItem();
            if (isEdit) {
                projeto.setNome(nome);
                projeto.setDescricao(descField.getText().trim());
                projeto.setDataInicio(dataInicioField.getText());
                projeto.setDataTerminoPrevista(dataTerminoField.getText());
                projeto.setStatus(statusSelecionado);
                projeto.setGerente(gerenteSelecionado);
                projetoDAO.atualizar(projeto);
                JOptionPane.showMessageDialog(view, "Projeto atualizado com sucesso!");
            } else {
                Projeto novoProjeto = new Projeto(nome, descField.getText().trim(), dataInicioField.getText(), dataTerminoField.getText(), statusSelecionado, gerenteSelecionado);
                projetoDAO.adicionar(novoProjeto);
                JOptionPane.showMessageDialog(view, "Projeto adicionado com sucesso!");
            }
            atualizarTodasTabelas();
        }
    }

    private void adicionarProjeto() {
        if (usuarioDAO.listarTodos().isEmpty()) { // Pode checar listarGerentes() se preferir
            JOptionPane.showMessageDialog(view, "Cadastre ao menos um usuário com o cargo 'Gerente' antes de criar um projeto.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        mostrarDialogoProjeto(null);
    }

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
            String nome = nomeField.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(view, "O campo 'Nome da Equipe' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (isEdit) {
                if (equipeDAO.nomeJaExiste(nome) && !nome.equals(equipe.getNome())) {
                     JOptionPane.showMessageDialog(view, "Este nome de equipe já está em uso.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                     return;
                }
            } else {
                if (equipeDAO.nomeJaExiste(nome)) {
                    JOptionPane.showMessageDialog(view, "Este nome de equipe já está em uso.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if (isEdit) {
                equipe.setNome(nome);
                equipe.setDescricao(descField.getText().trim());
                equipeDAO.atualizar(equipe);
                JOptionPane.showMessageDialog(view, "Equipe atualizada com sucesso!");
            } else {
                Equipe novaEquipe = new Equipe(nome, descField.getText().trim());
                equipeDAO.adicionar(novaEquipe);
                JOptionPane.showMessageDialog(view, "Equipe adicionada com sucesso!");
            }
            atualizarTodasTabelas();
        }
    }

    private void adicionarEquipe() { mostrarDialogoEquipe(null); }

    private void adicionarMembroAEquipe() {
        List<Equipe> equipes = equipeDAO.listarTodos();
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        if (equipes.isEmpty() || usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(view, "É necessário ter ao menos uma equipe e um usuário cadastrados.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Equipe equipeSelecionada = (Equipe) JOptionPane.showInputDialog(view, "Selecione a Equipe:", "Adicionar Membros", JOptionPane.PLAIN_MESSAGE, null, equipes.toArray(), equipes.get(0));
        if (equipeSelecionada == null) return;
        
        JList<Usuario> listaUsuarios = new JList<>(usuarios.toArray(new Usuario[0]));
        listaUsuarios.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    
        int resultado = JOptionPane.showConfirmDialog(view, new JScrollPane(listaUsuarios), "Selecione os Membros (use Ctrl+Click)", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            List<Usuario> membrosSelecionados = listaUsuarios.getSelectedValuesList();
            if (!membrosSelecionados.isEmpty()) {
                equipeDAO.adicionarMembros(equipeSelecionada.getId(), membrosSelecionados);
                atualizarTodasTabelas();
                JOptionPane.showMessageDialog(view, membrosSelecionados.size() + " membro(s) adicionado(s) com sucesso!");
            }
        }
    }
}