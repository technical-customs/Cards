package controls;

import main.CardCalc;
import gui.CardGui;
import card.Card;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseControl extends MouseAdapter{
    private CardCalc model;
    private CardGui gui;
    
    
    public MouseControl(CardCalc model, CardGui gui){
        this.model = model;
        this.gui = gui;
    }
    @Override
    public void mouseClicked(MouseEvent me){
        Point mc = me.getPoint();
        //check if there is a deck
        try{
            if(!gui.getDeck().getCards().isEmpty()){
                //has cards to click
                
                if(gui.getNorthMat().getPlayer() != null){
                    if(!gui.getNorthMat().getPlayer().getCardsInHand().isEmpty()){
                        
                        for(Card card: gui.getNorthMat().getPlayer().getCardsInHand()){
                            if(card.getCardRect().contains(mc)){
                                card.highlight(!card.getHighlight());
                            }
                        }
                        for(Card card: gui.getSouthMat().getPlayer().getCardsInHand()){
                            if(card.getCardRect().contains(mc)){
                                card.highlight(!card.getHighlight());
                            }
                        }
                        for(Card card: gui.getEastMat().getPlayer().getCardsInHand()){
                            if(card.getCardRect().contains(mc)){
                                card.highlight(!card.getHighlight());
                            }
                        }
                        for(Card card: gui.getWestMat().getPlayer().getCardsInHand()){
                            if(card.getCardRect().contains(mc)){
                                card.highlight(!card.getHighlight());
                            }
                        }
                    }
                }
                
            }
        }catch(Exception ex){
            
        }
    }
}