package gerenciadorprojetos.model.dao;

import gerenciadorprojetos.database.Database;
import gerenciadorprojetos.model.Equipe;
import gerenciadorprojetos.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EquipeDAO {

    public void adicionar(Equipe equipe) {
        String sql = "INSERT INTO equipes(nome, descricao) VALUES(?,?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, equipe.getNome());
            pstmt.setString(2, equipe.getDescricao());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao inserir equipe: " + e.getMessage());
        }
    }

    public void atualizar(Equipe equipe) {
        String sql = "UPDATE equipes SET nome = ?, descricao = ? WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, equipe.getNome());
            pstmt.setString(2, equipe.getDescricao());
            pstmt.setInt(3, equipe.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar equipe: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        String sqlMembros = "DELETE FROM equipe_membros WHERE equipe_id = ?";
        String sqlEquipe = "DELETE FROM equipes WHERE id = ?";
        Connection conn = null;
        try {
            conn = Database.connect();
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtMembros = conn.prepareStatement(sqlMembros)) {
                pstmtMembros.setInt(1, id);
                pstmtMembros.executeUpdate();
            }
            try (PreparedStatement pstmtEquipe = conn.prepareStatement(sqlEquipe)) {
                pstmtEquipe.setInt(1, id);
                pstmtEquipe.executeUpdate();
            }
            
            conn.commit();

        } catch (SQLException e) {
            System.err.println("Erro ao excluir equipe: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Erro ao reverter transação: " + ex.getMessage());
            }
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erro ao restaurar auto-commit: " + e.getMessage());
            }
        }
    }
    
    public void adicionarMembro(int equipeId, int usuarioId) {
        String sql = "INSERT OR IGNORE INTO equipe_membros(equipe_id, usuario_id) VALUES(?,?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, equipeId);
            pstmt.setInt(2, usuarioId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar membro à equipe: " + e.getMessage());
        }
    }
    
    public void adicionarMembros(int equipeId, List<Usuario> membros) {
        String sql = "INSERT OR IGNORE INTO equipe_membros(equipe_id, usuario_id) VALUES(?,?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (Usuario membro : membros) {
                pstmt.setInt(1, equipeId);
                pstmt.setInt(2, membro.getId());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar membros em lote: " + e.getMessage());
        }
    }

    public List<Equipe> listarTodos() {
        List<Equipe> equipes = new ArrayList<>();
        String sql = "SELECT * FROM equipes";
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Equipe equipe = new Equipe(rs.getString("nome"), rs.getString("descricao"));
                equipe.setId(rs.getInt("id"));
                carregarMembrosDaEquipe(conn, equipe);
                equipes.add(equipe);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar equipes: " + e.getMessage());
        }
        return equipes;
    }

    private void carregarMembrosDaEquipe(Connection conn, Equipe equipe) throws SQLException {
        String sql = "SELECT u.* FROM usuarios u " +
                     "JOIN equipe_membros em ON u.id = em.usuario_id " +
                     "WHERE em.equipe_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, equipe.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Usuario membro = new Usuario(
                    rs.getString("nomeCompleto"), rs.getString("cpf"),
                    rs.getString("email"), rs.getString("cargo"),
                    rs.getString("login"), rs.getString("senha")
                );
                membro.setId(rs.getInt("id"));
                equipe.adicionarMembro(membro);
            }
        }
    }
    
    public boolean nomeJaExiste(String nome) {
        String sql = "SELECT COUNT(*) FROM equipes WHERE nome = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar nome da equipe: " + e.getMessage());
        }
        return false;
    }
}