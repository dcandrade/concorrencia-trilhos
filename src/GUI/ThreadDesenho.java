package GUI;


import model.Quadro;
import model.Ponto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 *
 * @author solenir
 */
public class ThreadDesenho extends JFrame {
    private final Quadro quadro;

    public ThreadDesenho() {
        Container panel = getContentPane();
        panel.setLayout(new BorderLayout());
        setSize(600, 600);
        panel.setBounds(350, 350, 350, 350);
        this.quadro = new Quadro(Color.blue, 4, 4, 250, 250);
        panel.add(quadro, BorderLayout.CENTER);
        setContentPane(panel);
        panel.setVisible(true);
        
        this.quadro.repaint();
        Ponto point = new Ponto(0, 0);
        
        this.quadro.insertPoint(point);
        panel.add(point.getSlider(), BorderLayout.SOUTH);
        setVisible(true);
        Thread t = new Thread(this.quadro);
        t.start();
    }


    public static void main(String[] args) {
        ThreadDesenho desenho = new ThreadDesenho();
        desenho.addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent evento) {
                System.exit(0);
            }
        });
    }

}
