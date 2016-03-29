import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

/**
 * Cette classe represente une calculatrice RPN avec une pile de taille 4.
 * @author Dany Khalife, Vincent Lamarre, Guillaume Docquier
 * @date 9/4/2014
 */
public class Calculatrice extends java.util.Observable {
    private final int TAILLE_MAX = 4;
    private Stack<Double> pile = new Stack<Double>();
    private ArrayList<Exercice> exercices = new ArrayList<Exercice>();
    private Exercice exEnCours;
    private Random random = new Random();
    private ArrayList<String> sequence = new ArrayList<String>();
    protected String displayValue;
    private boolean userInput;
    private boolean apprentissage;
    private boolean exerciceReussi;
    
    /**
     * Constructeur par defaut
     */
    public Calculatrice(){
        exercices.add(new Exercice("1 + 1", "1.0 E 1.0 +"));
        exercices.add(new Exercice("(3 + 1) / 2", "3.0 E 1.0 + E 2.0 /"));
        exercices.add(new Exercice("(3 / 4) * (5 / 6)", "3.0 E 4.0 / E 5.0 E 6.0 / *"));
        apprentissage = false;
        exerciceReussi = false;
        //Remplir la pile de 0 pour eviter emptyStackException
        for (int i = 0; i < 4; i++)
            pile.push(0.0);
    }
    
    /**
     * Met les valeurs de la pile a 0 et reinitialise l'exerice en cours.
     */
    private void renewExercise() {
        pile.clear();
        for (int i = 0; i < 4; i++)
            pile.push(0.0);
        sequence.clear();
        userInput = false;
        exerciceReussi = false;
    }
    
    /**
     * Met la premiere valeur du stack a 0. 
     * Fonction utilisee pour le bouton Clear (C)
     * Indique que ce n'est pas l'utilisateur qui a entree cette donnee.
     */
    public void clearDisplay(){
        pile.pop();
        pile.push(0.0);
        if (userInput) {
            sequence.remove(sequence.size()-1);
        }
        userInput = false;
        setChanged();
        notifyObservers();
    }
    
    /**
     * Inverse l'etat du mode apprentissage. 
     * Si on ferme le mode apprentissage, l'exercice est detruit.
     */
    public void toggleApprentissage() {
        apprentissage = !apprentissage;
        if (!apprentissage)
            exEnCours = null;
        setChanged();
        notifyObservers();
    }
    
    /**
     * @return L'etat du mode apprentissage.
     */
    public boolean getApprentissage() {
        return apprentissage;
    }
    
    /**
     * Determine la premiere valeur sur le stack. Si la derniere valeur entree l'a ete par l'usager,
     * la nouvelle valeur est une concatenation. Sinon, la valeur remplace l'ancienne. Il ne peut pas
     * y avoir plus d'une fois le caractere "." La sequence est aussi mise a jour.
     */
    public void setAffichage(String s){
        //Derniere entree automatique
        if (!userInput) {
            displayValue = s;
            userInput = true;
        }
        //Derniere entree manuelle
        else {
            String currentValue = displayValue;
            if(s=="."){
                if(!(currentValue.indexOf(s)>0) && (currentValue.length()>0))
                    displayValue = currentValue+s;   
            }        
            else           
                displayValue = currentValue+s;     
        }
        
        //Si sequence debutee
        if (!sequence.isEmpty()) {
            //Si la derniere entree n'est pas un operateur
            String last = sequence.get(sequence.size()-1);
            int test = last.compareTo("E") * last.compareTo("*") * last.compareTo("-") * last.compareTo("+") * last.compareTo("/");
            if (test != 0)
                sequence.remove(sequence.size()-1);
        }
        
        //Retirer l'ancienne valeur, ajouter la nouvelle et mettre la jour la sequence.
        pile.pop();
        if(displayValue.compareTo(".") == 0)
            displayValue = "0.";
        pile.push(Double.valueOf(displayValue));
        sequence.add(Double.valueOf(displayValue).toString()); //Pour que 1 devienne 1.0
        setChanged();
        notifyObservers();
    }
    
    /**
     * Cette methode permet d'obtenir un iterateur pour lire la pile
     * Utlisez l'iterateur de cette maniere:
     * Iterator<Double> it = calc.lirePile();
     * while(it.hasNext()){
     * 		Double valeur = it.next();
     *		...	
     * }
     * @return un iterateur sur la pile
     */
    public Iterator<Double> lirePile(){
        return pile.iterator();
    }
	
    /**
     * Cette methtode permet de reinitialiser la calculatrice
     */
    public void reinitialiser(){
        renewExercise();
        clearDisplay();
        setChanged();
        notifyObservers();
    }
	
    /**
     * Cette methode permet d'empiler un operande sur la pile.
     * @param arg L'operande a ajouter 
     */
    public void empiler(){
        if(pile.size() == TAILLE_MAX)
            pile.remove(0);
        
        //Pile(0) vers pile(1)
        //pile.push(pile.peek());
        pile.push(0.0);
        sequence.add("E");
        userInput = false;
        setChanged();
        notifyObservers();
    }
	
    /**
     * Cette methode permet de lire une valeur de la pile
     * Si celle-ci est vide, la valeur 0 sera retournee
     **/
    private Double lire(){ //Obsolete si notre pile contient toujours 4 elements
        if(pile.size() == 0)
            return 0.0;
        return pile.pop();
    }

    /**
     * Cette methode remplace les deux derniers 
     * elements sur la pile par leur somme
     **/
    public void ajouter(){
        Double somme = lire() + lire();
        pile.push(somme);
        sequence.add("+");
        userInput = false;
        displayValue = somme.toString();
        setChanged();
        notifyObservers();
    }

    /**
     * Cette methode remplace les deux derniers 
     * elements sur la pile par leur difference
     **/
    public void soustraire(){
        Double a = lire();
        Double b = lire();
        Double difference = b - a;
        pile.push(difference);
        sequence.add("-");
        userInput = false;
        displayValue = difference.toString();
        setChanged();
        notifyObservers();
    }

    /**
     * Cette methode remplace les deux derniers 
     * elements sur la pile par leur produit
     **/
    public void multiplier(){
        Double produit = lire() * lire();
        pile.push(produit);
        sequence.add("*");
        userInput = false;
        displayValue = produit.toString();
        setChanged();
        notifyObservers();
    }

    /**
     * Cette methode remplace les deux derniers 
     * elements sur la pile par leur division
     **/
    public void diviser(){
    	Double a = lire();
    	Double b = lire();
        Double quotient = b/a;
        pile.push(quotient);
        sequence.add("/");
        userInput = false;
        displayValue = quotient.toString();
        setChanged();
        notifyObservers();
    }
    
    /**
     * Methode interne, utilisee pour coller un tableau de chaines de caracteres
     * @param ar Le tableau de chaines caracteres
     * @param sep Le separateur 
     * @return La chaine resultante de la concatenation de toutes les chaines du tableau, separees par le separateur
     */
    private String join(ArrayList<String> ar, String sep){
        if(ar.isEmpty())
            return "";

        StringBuilder sb = new StringBuilder();
        sb.append(ar.get(0));
        
        for(int i=1; i<ar.size(); ++i){
            sb.append(sep).append(ar.get(i));
        }
        
        return sb.toString();
    }
    
    /**
     * @return La valeur de la sequence en cours.
     */
    public String getSequence() {
        return join(sequence, " ");
    }
    
    /**
     * Cette methode permet d'obtenir un exercice 
     * Si aucun exercice est en cours, celle-ci en assigne un au hasard
     * 
     */
    public void nouvelExercice(){
        renewExercise();
        exEnCours = exercices.get(random.nextInt(exercices.size()));
        setChanged();
        notifyObservers();
    }
    
    /**
     * Cette methode permet d'obtenir l'exercice en cours
     * @return L'exercice en cours. Dans le cas ou il n'y en a aucun, null.
     */
    public Exercice obtenirExerciceEnCours(){
        return exEnCours;
    }
    
    /**
     * Cette methode permet de valider si la sequence effectue 
     * @return True si la sequence actuelle correspond a celle attendue pour l'exercice courant, False sinon.
     */
    private boolean validerSequence(){
        if(exEnCours == null)
            return false;
        
        return exEnCours.obtenirSequence().toLowerCase().startsWith(join(sequence, " ").toLowerCase());
    }
    
    /**
     * Cette methode permet d'obtenir le progres du calcul en cours lorsqu'on est en mode d'apprentissage
     * Cette methode n'est pas utile lorsque la sequence en cours est invalide
     * @return Le progres du calcul en pourcentage
     */
    public int obtenirProgresApprentissage(){
        return (int) (100 * Math.min(1.0, (double) join(sequence, " ").length() / (double) exEnCours.obtenirSequence().length()));
    }
    
    /**
     * @return Une string indiquant la validite de la sequence. 
     * Si la sequence est complete, l'exercice est reussi.
     */
    public String etatSequence() {
        String etat = "Invalide";
        if (validerSequence()) {
            etat = "Valide";
            if (obtenirProgresApprentissage() == 100)
                exerciceReussi = true; 
        }
        if(exerciceReussi) 
            etat = "Reussie";
        return(etat);
    }
}
