/**
 * @author : vincent lamarre, guillaume docquier
 * @date : 22 septembre 2015
 */
public class Main {

    public static void main(String[] args) {
        //Instanciation de notre modele
        Calculatrice modele = new Calculatrice();

        //Creation du controleur
        Controleur controleur = new Controleur(modele);

        //Creation des vues
        VuePrincipale vue1 = new VuePrincipale(controleur);
        VueModeApprentissage vue2 = new VueModeApprentissage(controleur);
        //Positionnement de la vue secondaire a cote de la vue principale
        vue2.setLocation(vue1.getLocationOnScreen().x+vue1.getSize().width, vue1.getLocation().y);

        //Ajout des vues comme observer de notre modele
        modele.addObserver(vue1);
        modele.addObserver(vue2);
    }
}
