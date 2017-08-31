package main;


import card.CardSuit;
import card.CardValues;
import card.Card;
import card.Diamond;
import card.Club;
import card.Heart;
import card.Spade;
import display.LoggerDisplay;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import network.Client;
import network.Server;

public class CardDeck {
    private int numofdecks;
    private ArrayList<Card> cards;
    final private List<Card> dealtCards;
    
    final private CardValues values;
    final private CardSuit[] suits;
    
    private int x,y,width,height;
    
    public CardDeck(){
        cards = new ArrayList<>();
        dealtCards = new ArrayList<>();
        values = new CardValues();
        suits = new CardSuit[4];
        suits[0] = new Spade();
        suits[1] = new Heart();
        suits[2] = new Diamond();
        suits[3] = new Club();
    }
    
    public Card makeCard(String string){
        String[] strArr = string.split(" ");
        if(strArr.length == 2){
            String value = strArr[0];
            Object obj;
            int t;
            
            try{
                t = Integer.parseInt(value);
                obj = t;
                
            }catch(NumberFormatException ex){
                obj = value.toUpperCase();
            }
            if(Arrays.asList(getValues().getValues()).contains(obj)){
                if(getSuitNames().contains(strArr[1])){
                    return new Card(obj,getSuit(strArr[1]));
                }
            }
        }
        return null;
    }
    private void addCardToDeck(Card card){
        cards.add(card);
    }
    
    public void fixDeck(CardDeck deck){
        clearDeck();
        
        for(Card card: deck.getCards()){
            addCardToDeck(card);
        }
    }
    
    public void putDeckInOrder(Card[] cards){
        for(Card card: cards){
            addCardToDeck(card);
        }
    }
    public void clearDeck(){
        numofdecks = 0;
        cards.clear();
        dealtCards.clear();
    }
    public void collectDeck(){
        cards.addAll(dealtCards);
        dealtCards.clear();
    }
    public void makeDeck(int num){
        collectDeck();
        cards.clear();
        
        numofdecks = num;
        for(int xx = 0; xx < num; xx++){
            for(CardSuit suit: suits){
                for(Object value: values.getValues()){
                    addCardToDeck(new Card(value,suit));
                }
            }
        }
    }
    public void shuffleDeck(){
        Collections.shuffle(cards);
        Collections.shuffle(cards);
    }
    public ArrayList<Card> getCards(){
        return cards;
    }
    public List<Card> getDealtCards(){
        return dealtCards;
    }
    public Card dealCard(){
        try{
            Card card = cards.get(0);
            dealtCards.add(card);
            cards.remove(card);
            return card;
        }catch(Exception ex){
            return null;
        }
    }
    
    public CardValues getValues(){
        return values;
    }
    public List<String> getSuitNames(){
        ArrayList<String> suitL = new ArrayList<>();
                
        for(CardSuit s: suits){
            suitL.add(s.getSuitname());
        }
        return suitL;
    }
    public CardSuit getSuit(String string){
        for(CardSuit s: suits){
            if(string.equalsIgnoreCase(s.getSuitname())){
                return s;
            }
        }
        return null;
    }
    public CardSuit[] getCardSuits(){
        return suits;
    }
    
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public synchronized void draw(Graphics2D g2){
        if(!cards.isEmpty()){
            Iterator<Card> cardIter = cards.iterator();
            
            while(cardIter.hasNext()){
                Card card = cardIter.next();
                card.setX(this.x);
                card.setY(this.y);
                card.draw(g2);
            }
        }
    }
    
    @Override
    public String toString(){
        return "Cards: " + cards.toString();
    }
    
    public static void main(String[] args) throws InterruptedException, IOException{
        //LoggerDisplay ld = new LoggerDisplay();
        //ld.setTitle("Cards");
        
        Server server = new Server(3803);
        if(server.getConnected()){
            server.sInput();
            server.getDeck().makeDeck(1);
            server.getDeck().shuffleDeck();
        }
        
        
        Client client = new Client(server.getServerAddress(),3803);
        if(client.getConnected()){
            client.write("request deck");
            String deck = client.read(1024);
            System.out.println("ClIENT DECK REQUEST READ: " + deck);
            System.out.println("ClIENT INITIAL DECK READ: " + client.getDeck().toString());
            //split deck into array
            
            String[] deckArr = deck.split(",");
            System.out.println("DECK READ: " + Arrays.toString(deckArr));
            
            //make cards
            for(String s: deckArr){
                System.out.println("Card READ: " + s);
                
                client.getDeck().addCardToDeck(client.getDeck().makeCard(s));
            }
            
            System.out.println("AFTER ADD DECK READ: " + client.getDeck().toString());
        }
        
        
        List<Player> players = new ArrayList<>();
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        Player p3 = new Player("P3");
        Player p4 = new Player("P4");
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        
        List<List<Card>> playerCards = new ArrayList();

        CardDeck mainDeck = new CardDeck();
        mainDeck.makeDeck(1);
        mainDeck.shuffleDeck();
        
        CardDeck cd1 = new CardDeck();
        cd1.makeDeck(1);
        cd1.shuffleDeck();
        
        CardDeck cd2 = new CardDeck();
        cd2.makeDeck(1);
        cd2.shuffleDeck();
        
        CardDeck cd3 = new CardDeck();
        cd3.makeDeck(1);
        cd3.shuffleDeck();
        
        CardDeck cd4 = new CardDeck();
        cd4.makeDeck(1);
        cd4.shuffleDeck();
        
        
        
        /*
        Player winner = null;
        while(winner == null){
        for(Player player: players){
            player.clearHand();
        }
        
        playerCards.clear();
        cd.collectDeck();
        Thread.sleep(1000);
        for(int x = 0; x < 5; x++){
            for(Player player: players){
                player.addCardToHand(cd.dealCard());
            }
        }
        for(Player player: players){
            playerCards.add(player.getCardsInHand());
            ld.writeToDisplay(player.toString());
        }
        ld.writeToDisplay("\n");
        Thread.sleep(1000);
        
        List<Card> comphands = null;
        try{
            comphands = Arrays.asList(PokerRules.compareHands(playerCards));
        }catch(Exception ex){ continue;}
        
        
        if(comphands == null){
           ld.writeToDisplay("Tie");
        }else{
            for(Player player: players){
                if(player.getCardsInHand().containsAll(comphands)){
                    ld.writeToDisplay("Winner is " + player.getName() + " - " + PokerRules.getHandValue(player.getCardsInHand()));
                    player.setPoints(player.getPoints()+ PokerRules.checkHand(player.getCardsInHand()).getA());
                    ld.writeToDisplay(player.getName() + " Points:" + player.getPoints());
                }
            }
        }
        
        ld.writeToDisplay("\n");
        
        for(Player player: players){
            if(player.getPoints() >= 5){
                winner = player;
            }
        }
        
        }
        ld.writeToDisplay("WINNER OF POKER: " + winner.getName() + " Points:" + winner.getPoints());
        ld.writeToDisplay("\n");
        ld.writeToDisplay("RUNNER UPS: ");
        
        for(Player player: players){
            if(player.equals(winner)){
                continue;
            }
            ld.writeToDisplay(player.getName() + " Points:" + player.getPoints());
        }
        */
    }
}