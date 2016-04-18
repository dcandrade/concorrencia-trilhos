package GUI;

import model.Quadro;
import model.Point;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import model.Train;
import util.ITrain;

/**
 *
 * @author solenir
 */
public class Client extends JFrame {

    private final Quadro quadro;

    public Client() throws RemoteException, NotBoundException {
        Container panel = getContentPane();
        panel.setLayout(new BorderLayout());
        setSizeJFrame(1000, 700);
        panel.setBounds(400, 400, 400, 400);
        this.quadro = new Quadro(Color.blue);
        panel.add(quadro);
        setContentPane(panel);
        panel.setVisible(true);

        this.quadro.repaint();
        Train trainOne = new Train(Point.UPPER_BLOCK);
        trainOne.start();
        Train trainTwo = new Train(Point.DOWN_LEFT_BLOCK);
        trainTwo.start();

        Registry registry = LocateRegistry.getRegistry(3333);
        final ITrain trainThree = (ITrain) registry.lookup("Train" + Point.DOWN_RIGHT_BLOCK);

        this.quadro.insertPoint(trainOne);
        this.quadro.insertPoint(trainTwo);
        this.quadro.insertPoint(trainThree);

        JSlider slide = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
        slide.setMajorTickSpacing(1);
        slide.setMinorTickSpacing(1);
        slide.setPaintTicks(true);
        slide.setPaintLabels(true);
        slide.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider j = (JSlider) e.getSource();
                if (!j.getValueIsAdjusting()) {
                    try {
                        trainThree.setSpeed((int) j.getValue());
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        panel.add(slide, BorderLayout.SOUTH);

        setVisible(true);
        Thread t = new Thread(this.quadro);
        t.start();
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Client desenho = new Client();
        desenho.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent evento) {
                System.exit(0);
            }
        });
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

    /**
     * Método que modifica a visibilidade da janela da aplicação.
     *
     * @param b True, se visível. False, se invisível.
     */
    public void setVisibleJFrame(boolean b) {
        setVisible(b);
    }

}
