package main;

import card.Card;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CardCalc {
    final private int MAXPLAYERS = 4, MAXHAND = 7;
    final private CardDeck deck;
    final private List<Card> cardsPlayed;
    final private List<Card> cardsInPlay;
    final private Player[] players;
    
    public CardCalc(){
        deck = new CardDeck();
        cardsPlayed = new ArrayList<>(); //card grave
        cardsInPlay = new ArrayList<>(); //cards in all hands
        players = new Player[MAXPLAYERS];
        
    }
    
    public synchronized Player addPlayer(String name){
        Player player = new Player(name);
        
        for(int x = 0; x < MAXPLAYERS; x++){
            if(players[x] == null){
                players[x] = player;
                return player;
            }
        }
        System.out.println("MAX PLAYERS REACHED");
        return null;
    }
    public synchronized Player getPlayer(String name){
        for(Player player: players){
            if(player.getName().equalsIgnoreCase(name)){
                return player;
            }
        }
        return null;
    }
    public synchronized Player[] getPlayers(){
        return players;
    }
    
    public synchronized CardDeck getDeck(){
        return deck;
    }
    public synchronized List<Card> calcDeck(){
        List<Card> preceivedDeck = (ArrayList) deck.getCards().clone();
        for(Card card: cardsPlayed){
            preceivedDeck.remove(card);
        }
        for(Card card: cardsInPlay){
            preceivedDeck.remove(card);
        }
        Collections.shuffle(preceivedDeck);
        Collections.shuffle(preceivedDeck);
        
        return preceivedDeck;
    }
    public synchronized List<Card> getCardsInPlay(){
        //gets cards on field
        cardsInPlay.clear();
        
        for(Player player: players){
            if(player == null){
                continue;
            }
            if(player.getCardsInHand().isEmpty()){
                continue;
            }
            for(Card card: player.getCardsInHand()){
                cardsInPlay.add(card);
            }
        }
        return cardsInPlay;
    }
    
    public synchronized List<Card> getCardsPlayed(){
        return cardsPlayed;
    }
    public synchronized void dealCards(int num){
        try{
            if(num <= 0 || num > MAXHAND){
                num = MAXHAND;
            }
            for(int x = 0; x < num; x++){
                for(Player player: players){
                    if(player == null){
                        continue;
                    }
                    if(player.getCardsInHand().size() == MAXHAND){
                        continue;
                    }
                    if(deck.getCards().isEmpty()){
                        throw new Exception();
                    }
                    player.addCardToHand(deck.dealCard());
                }
            }
        }catch(Exception ex){
            System.err.println("Deal Exception ex: " + ex);
        }finally{
            getCardsInPlay();
        }
        
    }
    public synchronized void emptyField(){
        //sends all cards in play and hand to played
        List<Player> pList = Arrays.asList(players);
        
        Iterator<Player> pIter = pList.iterator();
        while(pIter.hasNext()){
        //for(Player player: players){
            Player player = pIter.next();
        
            if(player == null){
                continue;
            }
            System.out.println("NUMBER OF CARDS: " + player.getCardsInHand().size());
            
            Iterator<Card> cardIter = player.getCardsInHand().iterator();
            
            while(cardIter.hasNext()){
                try{
                    collectCard(cardIter.next());
                }catch(Exception ex){
                }
                
            }
        }
    }
    public synchronized void collectCard(Card card){
        //sends card to played
        try{
            List<Player> pList = Arrays.asList(players);
        
            Iterator<Player> pIter = pList.iterator();
            while(pIter.hasNext()){
            //for(Player player: players){
                Player player = pIter.next();
                
            
                if(player == null){
                    continue;
                }
                System.out.println("Collect");
                
                if(player.removeCard(card)){
                    player.getRemoved().clear();
                    
                    getCardsPlayed().add(card);
                    getCardsInPlay().remove(card);
                }
            }
        }catch(Exception ex){
            System.out.println("Collect ex: " + ex);
        }
        
    }
    public synchronized void collectHighlightedCard(){
        List<Card> highlighted = new ArrayList<>();
        try{
            Iterator<Card> cardIter = cardsInPlay.iterator();
            while(cardIter.hasNext()){
                Card card = cardIter.next();
                
                if(card.getHighlight()){
                    //System.out.println("HIGHLIGHTED: " + card.toString());
                    highlighted.add(card);
                }
            }
            
            for(Card card: highlighted){
                collectCard(card);
            }
            //System.out.println("Highlighted: " + highlighted.toString());
        }catch(Exception ex){
            System.out.println("Collect Highlight ex: " + ex);
            
        }
        
    }
    public synchronized void recollectDeck(){
        emptyField();
        cardsPlayed.clear();
        deck.collectDeck();
        
        for(Card card: deck.getCards()){
            card.show(false);
            card.reset();
        }
    }
    
    @Override
    public String toString(){
        //System.out.println("Cards in play: " + getCardsInPlay().toString());
        //System.out.println("In Deck: " + calcDeck().toString());
        //System.out.println("Cards Played: " + getCardsPlayed().toString());
        
        for(Player player: players){
            if(player == null){
                continue;
            }
            System.out.println(player.toString());
        }
        return "";
    }
    
    public static void main(String[] args){
        CardCalc cc = new CardCalc();
        cc.getDeck().makeDeck(1);
        cc.getDeck().shuffleDeck();
        
        
        
        cc.addPlayer("Joe");
        cc.addPlayer("Chris");
        cc.addPlayer("Steve");
        cc.addPlayer("Will");
        
        for(Player player: cc.getPlayers()){
            System.out.println(player.getName() + " cards: " + player.getCardsInHand());
        }
        System.out.println("Cards in play: " + cc.getCardsInPlay().toString());
        System.out.println("In Deck: " + cc.calcDeck().toString());
        System.out.println("Cards Played: " + cc.getCardsPlayed().toString());
        System.out.println();
        
        System.out.println(cc.toString());
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CardCalc.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        cc.dealCards(1);
        for(Player player: cc.getPlayers()){
            System.out.println(player.getName() + " cards: " + player.getCardsInHand());
        }
        System.out.println(cc.toString());
        
        System.out.println("Cards in play: " + cc.getCardsInPlay().toString());
        System.out.println("In Deck: " + cc.calcDeck().toString());
        System.out.println("Cards Played: " + cc.getCardsPlayed().toString());
        System.out.println();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CardCalc.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        cc.dealCards(1);
        for(Player player: cc.getPlayers()){
            System.out.println(player.getName() + " cards: " + player.getCardsInHand());
        }
        System.out.println(cc.toString());
        System.out.println("Cards in play: " + cc.getCardsInPlay().toString());
        System.out.println("In Deck: " + cc.calcDeck().toString());
        System.out.println("Cards Played: " + cc.getCardsPlayed().toString());
        System.out.println();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CardCalc.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        cc.emptyField();
        for(Player player: cc.getPlayers()){
            System.out.println(player.getName() + " cards: " + player.getCardsInHand());
        }
        System.out.println(cc.toString());
        System.out.println("Cards in play: " + cc.getCardsInPlay().toString());
        System.out.println("In Deck: " + cc.calcDeck().toString());
        System.out.println("Cards Played: " + cc.getCardsPlayed().toString());
        System.out.println();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CardCalc.class.getName()).log(Level.SEVERE, null, ex);
        }
        cc.recollectDeck();
        for(Player player: cc.getPlayers()){
            System.out.println(player.getName() + " cards: " + player.getCardsInHand());
        }
        System.out.println("In Deck: " + cc.calcDeck().toString());
        
    }
}