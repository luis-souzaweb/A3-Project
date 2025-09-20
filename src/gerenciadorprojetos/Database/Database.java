package gerenciadorprojetos.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:gerenciador_projetos.db";
    private static Connection conn = null;

    // Pega a conexão com o banco de dados
    public static Connection connect() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(URL);
                System.out.println("Conexão com SQLite estabelecida.");
            }
            return conn;
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            return null;
        }
    }

    // Cria as tabelas se elas não existirem
    public static void initialize() {
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nomeCompleto TEXT NOT NULL,"
                + " cpf TEXT UNIQUE NOT NULL,"
                + " email TEXT UNIQUE NOT NULL,"
                + " cargo TEXT,"
                + " login TEXT UNIQUE NOT NULL,"
                + " senha TEXT NOT NULL"
                + ");";

        String sqlProjetos = "CREATE TABLE IF NOT EXISTS projetos ("
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + " nome TEXT NOT NULL,"
            + " descricao TEXT,"
            + " dataInicio TEXT,"              
            + " dataTerminoPrevista TEXT,"     
            + " status TEXT,"
            + " gerente_id INTEGER,"
            + " FOREIGN KEY (gerente_id) REFERENCES usuarios (id)"
            + ");";
        
        String sqlEquipes = "CREATE TABLE IF NOT EXISTS equipes ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nome TEXT NOT NULL UNIQUE,"
                + " descricao TEXT"
                + ");";

        // Tabela de ligação para o relacionamento N:M entre equipes e usuários
        String sqlEquipeMembros = "CREATE TABLE IF NOT EXISTS equipe_membros ("
                + " equipe_id INTEGER NOT NULL,"
                + " usuario_id INTEGER NOT NULL,"
                + " PRIMARY KEY (equipe_id, usuario_id),"
                + " FOREIGN KEY (equipe_id) REFERENCES equipes (id),"
                + " FOREIGN KEY (usuario_id) REFERENCES usuarios (id)"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlProjetos);
            stmt.execute(sqlEquipes);
            stmt.execute(sqlEquipeMembros);
            System.out.println("Tabelas do banco de dados verificadas/criadas.");
        } catch (SQLException e) {
            System.err.println("Erro ao criar as tabelas: " + e.getMessage());
        }
    }
}