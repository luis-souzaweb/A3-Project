package gerenciadorprojetos.view;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class SplashScreen extends JWindow {

    public SplashScreen() {
        setSize(450, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        add(panel);

        // 1. Cria o JLabel, definindo apenas o alinhamento
        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);

        // 2. Tenta carregar a imagem
        ImageIcon imageIcon = null;
        try {
            imageIcon = new ImageIcon(getClass().getResource("/resources/splash.png"));
        } catch (Exception e) {
            System.err.println("Imagem da splash screen não encontrada!");
        }
        
        // 3. Decide se vai usar o ÍCONE ou o TEXTO
        if (imageIcon != null) {
            imageLabel.setIcon(imageIcon);
        } else {
            imageLabel.setText("Carregando Sistema...");
        }

        panel.add(imageLabel, BorderLayout.CENTER);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        progressBar.setString("Inicializando banco de dados...");
        progressBar.setStringPainted(true);
        progressPanel.add(progressBar);
        
        panel.add(progressPanel, BorderLayout.SOUTH);
    }
}