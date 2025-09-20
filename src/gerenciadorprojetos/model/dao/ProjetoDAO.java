package gerenciadorprojetos.model.dao;

import gerenciadorprojetos.database.Database;
import gerenciadorprojetos.model.Projeto;
import gerenciadorprojetos.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAO {

    public void adicionar(Projeto projeto) {
        String sql = "INSERT INTO projetos(nome, descricao, dataInicio, dataTerminoPrevista, status, gerente_id) VALUES(?,?,?,?,?,?)";
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, projeto.getNome());
            pstmt.setString(2, projeto.getDescricao());
            pstmt.setString(3, projeto.getDataInicio());
            pstmt.setString(4, projeto.getDataTerminoPrevista());
            pstmt.setString(5, projeto.getStatus());
            if (projeto.getGerente() != null) pstmt.setInt(6, projeto.getGerente().getId());
            else pstmt.setNull(6, Types.INTEGER);
            pstmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro ao inserir projeto: " + e.getMessage()); }
    }

    public void atualizar(Projeto projeto) {
        String sql = "UPDATE projetos SET nome = ?, descricao = ?, dataInicio = ?, dataTerminoPrevista = ?, status = ?, gerente_id = ? WHERE id = ?";
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, projeto.getNome());
            pstmt.setString(2, projeto.getDescricao());
            pstmt.setString(3, projeto.getDataInicio());
            pstmt.setString(4, projeto.getDataTerminoPrevista());
            pstmt.setString(5, projeto.getStatus());
            if (projeto.getGerente() != null) pstmt.setInt(6, projeto.getGerente().getId());
            else pstmt.setNull(6, Types.INTEGER);
            pstmt.setInt(7, projeto.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro ao atualizar projeto: " + e.getMessage()); }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM projetos WHERE id = ?";
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro ao excluir projeto: " + e.getMessage()); }
    }

    public List<Projeto> listarTodos() {
        List<Projeto> projetos = new ArrayList<>();
        String sql = "SELECT p.id, p.nome, p.descricao, p.dataInicio, p.dataTerminoPrevista, p.status, p.gerente_id, u.nomeCompleto as gerente_nome " +
                     "FROM projetos p LEFT JOIN usuarios u ON p.gerente_id = u.id";
        try (Connection conn = Database.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Usuario gerente = null;
                int gerenteId = rs.getInt("gerente_id");
                String nomeGerente = rs.getString("gerente_nome");
                if (nomeGerente != null) {
                    gerente = new Usuario(nomeGerente, "", "", "", "", "");
                    gerente.setId(gerenteId); 
                }
                
                Projeto projeto = new Projeto(
                    rs.getString("nome"), rs.getString("descricao"), 
                    rs.getString("dataInicio"), rs.getString("dataTerminoPrevista"), gerente
                );
                projeto.setId(rs.getInt("id"));
                projeto.setStatus(rs.getString("status"));
                projetos.add(projeto);
            }
        } catch (SQLException e) { System.err.println("Erro ao listar projetos: " + e.getMessage()); }
        return projetos;
    }
}