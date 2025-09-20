package gerenciadorprojetos;

import gerenciadorprojetos.controller.AppController;
import gerenciadorprojetos.database.Database; // IMPORT NOVO
import javax.swing.SwingUtilities;

public class GerenciadorProjetos {
    public static void main(String[] args) {
        // INICIALIZA O BANCO DE DADOS ANTES DE QUALQUER COISA
        Database.initialize(); 

        SwingUtilities.invokeLater(() -> {
            AppController controller = new AppController();
            controller.iniciar();
        });
    }
}