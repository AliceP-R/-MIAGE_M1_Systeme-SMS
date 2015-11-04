package prof;

public class testSaisie {

	public static void main(String[] args) {
		SimpleFenetre fenetreAff = new SimpleFenetre("Affichage principal");
		fenetreAff.updateMessage("Bienvenue dans notre service de SMS.\n");
		fenetreAff.updateMessage("1 : demande d’abonnement, 2 pour demande de désabonnement");

		FentereSaisie fSaisie = new FentereSaisie("Votre choix ?");
		String messAff = new String();
		String messBase = fSaisie.getText();

		while (true) {
			try {
				Thread.sleep(1110);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String s = fSaisie.getText();
			if (s.compareTo(messBase) != 0) {
				if (s.charAt(s.length() - 1) == '\n') {
					int n = s.length() - messBase.length();
					System.out.println(n);
					fenetreAff.updateMessage(new String(s.substring(messBase.length())));
					messBase = new String(s);

					fSaisie.update();
				}
			}
		}

	}

}
