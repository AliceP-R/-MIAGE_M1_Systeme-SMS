package prof;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SimpleFenetre extends JFrame {

	private String titre;
	private String Messages[];
	int last = 0;
	int nbMess = 0;

	public SimpleFenetre(String titre) {
		super();
		this.titre=titre;
		Messages = new String[5];
		build();
	}

	private void build() {
		setTitle(this.titre); // On donne un titre à l'application
		setSize(320, 240); // On donne une taille à la fenètre
		setLocationRelativeTo(null); // On centre la fenètre sur l'àcran
		setResizable(false); // On interdit la redimensionnement de la fenètre
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setContentPane(buildContentPane());
	}


	private JPanel buildContentPane() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5,1));
		panel.setBackground(Color.white);

		for (int i = 0; i < nbMess; i++) {
			int pos = (nbMess<5? i:(last+i)%5);
			JLabel label = new JLabel(Messages[pos]);
			panel.add(label);
		}
		return panel;
	}

	public void updateMessage(String message) {
		nbMess = (nbMess >= 5 ? 5 : nbMess + 1);
		Messages[last] = new String(message);
		last++;
		last = last % 5;
		setContentPane(buildContentPane());
		setVisible(true);
	}
}