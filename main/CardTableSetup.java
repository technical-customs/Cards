package main;


import main.CardController;
import main.CardCalc;
import gui.CardGui;
import display.LoggerDisplay;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

class CardTableSetup{
    
    
    public static void main(String[] args){
        CardCalc model = new CardCalc();
        CardGui gui = new CardGui();
        CardController controller = new CardController(model, gui);
        
        
        LoggerDisplay ld = new LoggerDisplay();
        
        while(true){
            ld.writeToDisplay(Arrays.toString(model.getPlayers()));
            ld.writeToDisplay(null);
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CardTableSetup.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}