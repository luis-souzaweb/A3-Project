package gerenciadorprojetos;

import gerenciadorprojetos.controller.AppController;
import gerenciadorprojetos.database.Database;
import gerenciadorprojetos.view.SplashScreen;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class GerenciadorProjetos {
    public static void main(String[] args) {
        // Cria a splash screen mas não a mostra ainda
        SplashScreen splash = new SplashScreen();
        
        // A mostra
        splash.setVisible(true);

        // Inicializa o banco de dados (o "trabalho pesado" do carregamento)
        Database.initialize();

        // Cria um timer para fechar a splash screen depois de 3 segundos (3000 ms)
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                splash.dispose(); // Fecha e libera a memória da splash
                
                // Agora, inicia a aplicação principal
                SwingUtilities.invokeLater(() -> {
                    AppController controller = new AppController();
                    controller.iniciar();
                });
            }
        });
        timer.setRepeats(false); // Garante que o timer rode apenas uma vez
        timer.start();
    }
}