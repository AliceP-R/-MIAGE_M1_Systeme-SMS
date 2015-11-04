package prof;

import java.awt.Dimension;
import javax.swing.*;
public class FentereSaisie extends JFrame{

	JFrame fenText;
	JTextArea textArea1;

	public FentereSaisie(String indication) {
		fenText = new JFrame("Saisie Message");
		fenText.setSize(300, 100);
		JPanel pannel = new JPanel();
		textArea1 = new JTextArea(indication);
		JScrollPane scrollPane = new JScrollPane(textArea1);
		scrollPane.setPreferredSize(new Dimension(200, 70));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		pannel.add(scrollPane);
		fenText.getContentPane().add(pannel);
		fenText.setVisible(true);
	}
	public String getText(){
		return this.textArea1.getText();
	}
	// remet le focus sur la fenetre de saisie.
	public void update(){
		fenText.setVisible(true);
	}
}