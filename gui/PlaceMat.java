package gui;


import main.Player;
import card.Card;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlaceMat {
    private Rectangle[] placeHolderLocations = new Rectangle[7];
    private Card[] cardSpot = new Card[7];
    private Map< LinkedHashMap<Integer,Rectangle> ,Card> spotMap = new LinkedHashMap<>();
    
    private List<Card> cards;
    private Player player = null;
    private int x;
    private int y;
    private int width;
    private int height;

    public PlaceMat(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        cards = new ArrayList<>(); 
    }
    
    private synchronized void initiateMat(boolean northsouth){
        if(northsouth){
            //northsouth
            for(int xx = 0; xx < placeHolderLocations.length; xx++){
                
                int px = 0;
                int py = 0;

                if(xx == 0){
                    px = this.x + 2;
                }else{
                    px = (this.x + (Card.CARDWIDTH * xx));
                }
                py = (this.y + (this.height)/2) - (Card.CARDHEIGHT/2);

                placeHolderLocations[xx] = new Rectangle(px,py,Card.CARDWIDTH,Card.CARDHEIGHT);
                LinkedHashMap<Integer, Rectangle> map = new LinkedHashMap<>();
                map.put(xx, placeHolderLocations[xx]);
                spotMap.put(map, null);
            }
        }else{
            //eastwest
        
            for(int xx = 0; xx < placeHolderLocations.length; xx++){
                int px = 0;
                int py = 0;

                if(xx == 0){
                    py = this.y + 2;
                }else{
                    py = (this.y + (Card.CARDWIDTH * xx));
                }
                px = (this.x + (this.width)/2) - (Card.CARDHEIGHT/2);


                placeHolderLocations[xx] = new Rectangle(px,py,Card.CARDHEIGHT,Card.CARDWIDTH);
                LinkedHashMap map = new LinkedHashMap<>();
                map.put(xx, placeHolderLocations[xx]);
                spotMap.put(map, null);
            }
        }
        
        
    }
    public synchronized int getX() {
        return this.x;
    }
    public synchronized void setX(int x) {
        this.x = x;
    }
    public synchronized int getY() {
        return this.y;
    }
    public synchronized void setY(int y) {
        this.y = y;
    }
    public synchronized int getWidth() {
        return this.width;
    }
    public synchronized void setWidth(int width) {
        this.width = width;
    }
    public synchronized int getHeight() {
        return this.height;
    }
    public synchronized void setHeight(int height) {
        this.height = height;
    }
    
    public synchronized void addPlayer(Player player){
        this.player = player;
        initiateMat(player.getNorthsouth());
        cardSync();
    }
    public synchronized Player getPlayer(){
        return this.player;
    }
    
    public synchronized void addCard(Card card, boolean northsouth){
        try{
            if(northsouth){
                for(int xx = 0; xx < cardSpot.length; xx++){
                    //System.out.println("XX: " + xx);
                    
                    
                    //printing each card in hand before add
                    //for(int cc = 0; cc < cardSpot.length; cc++){
                        //System.out.print("CARD " + cc + " - ");
                        //if(cardSpot[cc] == null){
                            //System.out.println("Null");
                            //continue;
                        //}
                        //System.out.println(cardSpot[cc].toString());
                    //}
                    
                    
                    
                    //checks if card is null at position
                    if(cardSpot[xx] != null){
                        continue;
                    }
                    
                    //gets map of positioning
                    
                    for(LinkedHashMap<Integer, Rectangle> m: spotMap.keySet()){
                        //System.out.println("SPOTMAP KEY: " + m.keySet());
                        
                        for(int a: m.keySet()){
                            
                            int num = a;
                            
                            if(num == xx){
                                Rectangle position = (Rectangle) m.get(num);
                                card.setX(position.x);
                                card.setY(position.y);
                                cardSpot[xx] = card;
                                
                                
                                return;
                            }
                        }
                        return;
                    }
                }
                //normal orietation
               

            }else{
                int temp = card.getWidth();
                card.setWidth(card.getHeight());
                card.setHeight(temp);
                
                for(int xx = 0; xx < placeHolderLocations.length; xx++){
                    if(cardSpot[xx] != null){
                        continue;
                    }
                    card.setX(placeHolderLocations[xx].x);
                    card.setY(placeHolderLocations[xx].y);
                    cardSpot[xx] = card;
                }
                
            }
            
            
        }catch(Exception ex){
            System.out.println("No More Cards " + ex);
        }

    }
    
    public synchronized void clearMat(){
        cards.clear();
        for(Card card: cardSpot){
            card = null;
        }
    }

    public synchronized void cardSync(){
        new Thread(new Runnable(){

            @Override
            public void run(){
                while(player != null){
                    try{
                        if(cards.isEmpty()){
                            continue;
                        }
                        clearMat(); 
                        if(!player.getCardsInHand().isEmpty()){

                            Iterator<Card> handIter = player.getCardsInHand().iterator();
                            while(handIter.hasNext()){
                                Card card = handIter.next();
                                addCard(card,player.getNorthsouth());
                            }
                        }else{

                        }
                    }catch(Exception ex){
                        System.out.println("Place Mat Sync Exception: " + ex);
                    }
                    
                }
            }
        }).start();

    }
    
    public synchronized void draw(Graphics2D g2){
        g2.setColor(Color.green);
        g2.fillRect(this.x, this.y, this.width, this.height);
        
        
        
        if(player != null){
            
            for(Rectangle p: placeHolderLocations){
                g2.setColor(Color.white);
                g2.fillRect(p.x, p.y, p.width, p.height);
                g2.setColor(Color.black);
                g2.drawRect(p.x, p.y, p.width, p.height);
            }
            player.draw(g2);
            Iterator<Card> cardIter = player.getCardsInHand().iterator();
            while(cardIter.hasNext()){
                Card card = cardIter.next();
                card.draw(g2);
            }
            Iterator<Card> removedIter = player.getRemoved().iterator();
            while(removedIter.hasNext()){
                Card card = removedIter.next();
                card.draw(g2);
            }
        }
        
    }
}