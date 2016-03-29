/**
 * Classe du controleur qui fait la communication entre la vue et le modele
 * @author : vincent lamarre, guillaume docquier
 * @date : 22 septembre 2015
 */
public class Controleur implements java.awt.event.ActionListener {
    private Calculatrice modele;
    
    //Constructeur a partir du modele
    Controleur(Calculatrice c){
        this.modele = c;
    }
    
    //Pour gerer les boutons d'action (operations, enter, clear, etc) 
    public void actionPerformed(java.awt.event.ActionEvent e){
	switch(e.getActionCommand()){            
            case "boutonClear":
                modele.clearDisplay();
                break;
            case "boutonDiv":
                modele.diviser();
                break;
            case "boutonEnter":
                modele.empiler();
                break;              
            case "boutonPlus":
                modele.ajouter();
                break;
            case "boutonMoins":
                modele.soustraire();
                break;
            case "boutonMult":
                modele.multiplier();
                break;            
            case "boutonToggleApprentissage":                
                modele.toggleApprentissage();
                break;
            case "boutonNouveau":
                modele.nouvelExercice();
                break;
            case "boutonRecommencer":
                modele.reinitialiser();
                break;
            default: break;    
        }               
    }
    
    //Pour gerer seulement les chiffres et les virgules
    public void addToSequence(String s)
    {
        modele.setAffichage(s);
    }    
}
