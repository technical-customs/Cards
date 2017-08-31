package controls;


import main.Player;
import main.CardCalc;
import gui.CardGui;
import card.Card;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import javax.swing.JOptionPane;

/**
 * 
 * @author techcust
 * 
 * A - add player, upto 4
 * N - new Deck
 * D - deal cards
 * S - show cards
 * E - empty field
 * C - collect deck
 */
public class KeyControl extends KeyAdapter{
    private CardCalc model;
    private CardGui gui;
    public KeyControl(CardCalc model, CardGui gui){
        this.model = model;
        this.gui = gui;
    }
    @Override
    public void keyPressed(KeyEvent ke){
        int keyCode = ke.getKeyCode();
        
        if(keyCode == KeyEvent.VK_A){
            
            if(model.getPlayers()[0] == null){
                Player player = model.addPlayer("Player North");
                player.setNorthsouth(true);
                //System.out.println("Add Player North");
                
                player.setX(gui.getNorthMat().getX());
                player.setY(gui.getNorthMat().getY());
                
                gui.getNorthMat().addPlayer(player);
                return;
            }else if(model.getPlayers()[1] == null){
                Player player = model.addPlayer("Player South");
                player.setNorthsouth(true);
                //System.out.println("Add Player South");
                
                player.setX(gui.getSouthMat().getX());
                player.setY(gui.getSouthMat().getY());
                gui.getSouthMat().addPlayer(player);
                return;
            }else if(model.getPlayers()[2] == null){
                Player player = model.addPlayer("Player East");
                player.setNorthsouth(false);
                //System.out.println("Add Player East");
                
                player.setX(gui.getEastMat().getX());
                player.setY(gui.getEastMat().getY());
                gui.getEastMat().addPlayer(player);
                return;
            }else if(model.getPlayers()[3] == null){
                Player player = model.addPlayer("Player West");
                player.setNorthsouth(false);
                //System.out.println("Add Player West");
                
                player.setX(gui.getWestMat().getX());
                player.setY(gui.getWestMat().getY());
                gui.getWestMat().addPlayer(player);
                return;
            }
        }
        
        if(keyCode == KeyEvent.VK_N){
            //System.out.println("Make new deck");
            try{
                model.recollectDeck();
            }catch(Exception ex){
                System.out.println("collect " + ex);
            }
            
            model.getDeck().makeDeck(1);
            model.getDeck().shuffleDeck();
            gui.setupDeck(model.getDeck());
            
        }
        if(keyCode == KeyEvent.VK_D){
            //System.out.println("Deal cards");
            int choice = 0;
            do{
                String sc = (JOptionPane.showInputDialog(null, "How Many Cards To Deal (1-7)?","Card Deal", JOptionPane.PLAIN_MESSAGE));
                if(sc == null){
                    return;
                }
                try{
                    choice = Integer.parseInt(sc);
                }catch(NumberFormatException ex){
                    
                }
                
            }while(choice < 1 || choice > 7);
            
            model.dealCards(choice);
            //syncCards();
            //System.out.println(model.toString());
        }
        if(keyCode == KeyEvent.VK_S){
            //System.out.println("Show cards");
            
            Iterator<Card> cardIter = model.getCardsInPlay().iterator();
            while(cardIter.hasNext()){
                Card card = cardIter.next();
                card.show(!card.getShow());
            }
        }
        if(keyCode == KeyEvent.VK_E){
            System.out.println("Empty Field");
            
            model.emptyField();
            //gui.getPlayed().addAll(model.getCardsPlayed());
            
            for(Card card: model.getCardsPlayed()){
                card.reset();
                card.setX(gui.getTableTop().getPlayedPlaceHolderLocation().x);
                card.setY(gui.getTableTop().getPlayedPlaceHolderLocation().y);
            }
            //Iterator<Card> cardIter = model.getCardsPlayed().iterator();
            //while(cardIter.hasNext()){
            //   Card card = cardIter.next();
            //    card.reset();
            //    card.setX(gui.getTableTop().getPlayedPlaceHolderLocation().x);
            //    card.setY(gui.getTableTop().getPlayedPlaceHolderLocation().y);
            //}
            //syncCards();
        }
        if(keyCode == KeyEvent.VK_C){
            System.out.println("Collect cards");
            
            model.collectHighlightedCard();
            //syncCards();
            //gui.getPlayed().addAll(model.getCardsPlayed());
            
        }   
    }
}