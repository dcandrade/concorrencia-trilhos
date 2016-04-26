package GUI;

import model.Rail;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import util.ITrain;

/**
 *
 * @author solenir
 */
public class Client extends JFrame {

    private final Rail railFrame;
     

    public Client() throws AlreadyBoundException, IOException {
        super();
        this.railFrame = new Rail(Color.blue);
        
    }

    public Rail getRail() {
        return railFrame;
    }
    
    public void repaintRail(){
        this.railFrame.repaint();
    }
    
    private void createInitialFrame() {
        Container panel = getContentPane();
        panel.setLayout(new BorderLayout());
        //setSizeJFrame(1000, 700);
        //panel.setBounds(400, 400, 400, 400);
        panel.add(this.railFrame);
        setContentPane(panel);
    }
  

    public JSlider addTrain(final ITrain train) {
        this.railFrame.insertPoint(train);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
        slider.setMajorTickSpacing(1);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider j = (JSlider) e.getSource();
                if (!j.getValueIsAdjusting()) {
                    try {
                        train.setSpeed((int) j.getValue());
                    } catch (RemoteException ex) {
                        System.err.println(ex.getLocalizedMessage());
                    }
                }
            }
        });

        return slider;
    }

    public void display() {
        this.setVisible(true);
        getContentPane().setVisible(true);
        Thread thread = new Thread(this.railFrame);
        thread.start();
    }

    /**
     * Método que modifica o tamanho da janela da aplicação e a centraliza na
     * tela do PC.
     *
     * @param x Int com a largura da janela.
     * @param y Int com a altura da janela.
     */
    public void setSizeJFrame(int x, int y) {
        setSize(x, y);
        Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((tela.width - x) / 2, (tela.height - y) / 2);
    }

    public static void main(String[] args) throws AlreadyBoundException, IOException {
        Client client = new Client();
        client.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent evento) {
                System.exit(0);
            }
        });
        
        client.createInitialFrame();
        client.display();
    }

}
