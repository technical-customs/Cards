package main;


import card.CardSuit;
import card.CardValues;
import card.Pair;
import card.Card;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class PokerRules{
    private CardDeck deck;
    public PokerRules(CardDeck deck){this.deck = deck;}
    
    public static String getHandValue(List<Card> cards){
        int handvalue = checkHand(cards).getA();
        
        if(handvalue == 1){
            return "High Card";
        }
        if(handvalue == 2){
            return "Pair";
        }
        if(handvalue == 3){
            return "Two Pair";
        }
        if(handvalue == 4){
            return "Three Of A Kind";
        }
        if(handvalue == 5){
            return "Straight";
        }
        if(handvalue == 6){
            return "Flush";
        }
        if(handvalue == 7){
            return "Full House";
        }
        if(handvalue == 8){
            return "Four Of A Kind";
        }
        if(handvalue == 9){
            return "Straight Flush";
        }
        if(handvalue == 10){
            return "Royal Flush";
        }
        return null;
    }
    public static Pair<Integer,Card[]> checkHand(List<Card> cards){
        if(checkForRoyalFlush(cards) != null){
            Pair<Integer,Card[]> p = new Pair<>(10,checkForRoyalFlush(cards));
            return p;
        }else if(checkForStraightFlush(cards) != null){
            Pair<Integer,Card[]> p = new Pair<>(9,checkForStraightFlush(cards));
            return p;
        }else if(checkForFourOfAKind(cards) != null){
            Pair<Integer,Card[]> p = new Pair<>(8,checkForFourOfAKind(cards));
            return p;
        }else if(checkForFullHouse(cards) != null){
            Pair<Integer,Card[]> p = new Pair<>(7,checkForFullHouse(cards));
            return p;
        }else if(checkForFlush(cards) != null){
            Pair<Integer,Card[]> p = new Pair<>(6,checkForFlush(cards));
            return p;
        }else if(checkForStraight(cards) != null){
            Pair<Integer,Card[]> p = new Pair<>(5,checkForStraight(cards));
            return p;
        }else if(checkForThreeOfAKind(cards) != null){
            Pair<Integer,Card[]> p = new Pair<>(4,checkForThreeOfAKind(cards));
            return p;
        }else if(checkForTwoPair(cards) != null){
            Pair<Integer,Card[]> p = new Pair<>(3,checkForTwoPair(cards));
            return p;
        }else if(checkForPair(cards) != null){
            Pair<Integer,Card[]> p = new Pair<>(2,checkForPair(cards));
            return p;
        }else if(checkForHighCard(cards) != null){
            Pair<Integer,Card[]> p = new Pair<>(1,checkForHighCard(cards));
            return p;
        }
        return null;
    }
    
    public static List<Card> compareHands(List<Card> cardsa, List<Card> cardsb){
        if(checkHand(cardsa).getA() > checkHand(cardsb).getA()){
            return cardsa;
        }else if(checkHand(cardsa).getA() < checkHand(cardsb).getA()){
            return cardsb;
        }else if(checkHand(cardsa).getA().equals(checkHand(cardsb).getA())){
            //tie
            
            if(checkHand(cardsa).getA()  == 5 && checkHand(cardsb).getA()  == 5 
                    || checkHand(cardsa).getA()  == 9 && checkHand(cardsb).getA()  == 9 ){
                //either straight or straight flush
                System.out.println("STRAIGHTS");
            }
            Card ahigh = checkForHighCard(Arrays.asList(checkHand(cardsa).getB()))[0];
            Card bhigh = checkForHighCard(Arrays.asList(checkHand(cardsb).getB()))[0];
            
            if(ahigh.getValue().equals(bhigh.getValue())){
                return null;
            }

            if(Card.compareCards(ahigh, bhigh).equals(ahigh)){
                return cardsa;
            }
            if(Card.compareCards(ahigh, bhigh).equals(bhigh)){
                return cardsb;
            }
            
        }
        return null;
    }
    
    public static Card[] compareHands(List<List<Card>> list){
        List<Card> highHand = null;
        
        boolean tie = false;
        List<Card> tieHand = null;
        
        for(int x = 0; x < list.size(); x++){
            int y = x+1;
            
            if(highHand == null){
                highHand = list.get(x);
                continue;
            }
                if(compareHands(highHand, list.get(x)) != null){
                    highHand = compareHands(highHand, list.get(x));
                    if(tie){
                        if(highHand.containsAll(tieHand)){
                            //tiehand(high hand still on top)
                            return null;
                        }else{
                            //other beat original ties
                            tie = false;
                            tieHand = null;
                        }
                    }
                    
                }else if(compareHands(highHand, list.get(x)) == null){
                    System.out.println("Tie between hands");
                    tie = true;
                    
                    if(y >= list.size()){
                        System.out.println("BLANK1");
                        //on last one
                        return null;
                    }else{
                        System.out.println("BLANK2");
                        tieHand = highHand;
                    }
                }
        }
        if(tie){
            
        }
        
        Card[] cc = new Card[highHand.size()];
        cc = highHand.toArray(cc);
        return cc;
    }
    
    public static Card[] checkForRoyalFlush(List<Card> cards){
        if(checkForHighCard(cards)[0].getValue().equals("A") && checkForFlush(cards) != null && checkForStraight(cards) != null){
            Card[] cc = new Card[cards.size()];
            cc = cards.toArray(cc);
            return cc;
        }
        return null;    
    }
    public static Card[] checkForStraightFlush(List<Card> cards){
        if(checkForFlush(cards) != null && checkForStraight(cards) != null){
            Card[] cc = new Card[cards.size()];
            cc = cards.toArray(cc);
            return cc;
        }
        return null;
    }
    public static Card[] checkForFourOfAKind(List<Card> cards){
        List<List<Card>> lcards = new LinkedList<>();
        
        for(Card card: cards){
            if(lcards.isEmpty()){
                LinkedList<Card> ll = new LinkedList<>();
                ll.add(card);
                lcards.add(ll);
                continue;
            }
            
            boolean added = false;
            for(List<Card> cardlist: lcards){
                if(cardlist.get(0).getValue().equals(card.getValue())){
                    cardlist.add(card);
                    added = true;
                }
            }
            
            if(added){
                added = false;
            }else{
                LinkedList<Card> ll = new LinkedList<>();
                ll.add(card);
                lcards.add(ll);
            }
        }
        for(List<Card> c: lcards){
            if(c.size() == 4){
                Card[] cc = new Card[c.size()];
                cc = c.toArray(cc);
                return  cc;
            }
        }
        return null;
    }
    public static Card[] checkForFullHouse(List<Card> cards){
        List<Card> cc = cards;
        Card[] three = checkForThreeOfAKind(cc);
        
        if(three != null){
            for(Card card: three){
                cc.remove(card);
            }
            if(checkForPair(cc) != null){
                return three;
            }
        }
        return null;
    }
    public static Card[] checkForFlush(List<Card> cards){
        CardSuit suit = null;
        
        for(Card card: cards){
            if(suit == null){
                suit = card.getSuit();
                continue;
            }
            
            if(!suit.equals(card.getSuit())){
                return null;
            }
        }
        Card[] cc = new Card[cards.size()];
        cc = cards.toArray(cc);
        return cc;
    }
    public static Card[] checkForStraight(List<Card> cards){
        List<Object> lowSt = Arrays.asList("A",2,3,4,5);
        
        List<Object> values = new LinkedList();
        for(Card card: cards){
            values.add(card.getValue());
        }
        
        if(values.containsAll(lowSt)){
            Card[] cc = new Card[cards.size()];
            cc = cards.toArray(cc);
            return cc;
        }
        
        Card[] high = checkForHighCard(cards);
        int index = Integer.MAX_VALUE;
        
        //for(int x = 0; x < CardValues.getValues().length; x++){
            //if(high[0].getValue() == CardValues.getValues()[x]){
               // index = x;
            //}
        ///}
        if((index-4) < 0){
            return null;
        }
        
        List<Object> st = new LinkedList();
        //for(int y = index; y >= index - 4; y--){
            //st.add(CardValues.getValues()[y]);
        //}
        
        if(values.containsAll(st)){
            Card[] cc = new Card[cards.size()];
            cc = cards.toArray(cc);
            return cc;
        }
        return null;
    }
    public static Card[] checkForThreeOfAKind(List<Card> cards){
        List<List<Card>> lcards = new LinkedList<>();
        
        for(Card card: cards){
            if(lcards.isEmpty()){
                LinkedList<Card> ll = new LinkedList<>();
                ll.add(card);
                lcards.add(ll);
                continue;
            }
            
            boolean added = false;
            for(List<Card> cardlist: lcards){
                if(cardlist.get(0).getValue().equals(card.getValue())){
                    cardlist.add(card);
                    added = true;
                }
            }
            
            if(added){
                added = false;
            }else{
                LinkedList<Card> ll = new LinkedList<>();
                ll.add(card);
                lcards.add(ll);
            }
        }
        for(List<Card> c: lcards){
            if(c.size() == 3){
                Card[] cc = new Card[c.size()];
                cc = c.toArray(cc);
                return  cc;
            }
        }
        return null;
    }
    public static Card[] checkForTwoPair(List<Card> cards){
        List<List<Card>> lcards = new LinkedList<>();
        
        for(Card card: cards){
            if(lcards.isEmpty()){
                LinkedList<Card> ll = new LinkedList<>();
                ll.add(card);
                lcards.add(ll);
                continue;
            }
            
            boolean added = false;
            for(List<Card> cardlist: lcards){
                if(cardlist.get(0).getValue().equals(card.getValue())){
                    cardlist.add(card);
                    added = true;
                }
            }
            
            if(added){
                added = false;
            }else{
                LinkedList<Card> ll = new LinkedList<>();
                ll.add(card);
                lcards.add(ll);
            }
        }
        
        List<List<Card>> l2 = new LinkedList<>();
        
        for(List<Card> c: lcards){
            
            if(c.size() == 2){
                l2.add(c);
            }
        }
        
        
        List<Card> cl = new LinkedList<>();
        if(l2.size() == 2){
            
            int x = 0;
            for(List<Card> l: l2){
                cl.addAll(l);
            }
            
            Card[] cc = new Card[cl.size()];
            cc = cl.toArray(cc);
            
            return cc;
        }
        return null;
    }
    public static Card[] checkForPair(List<Card> cards){
        Card c = null;
        Card pair = null;
        
        for(int x = 0; x < cards.size(); x++){
            c = cards.get(x);
            
            for(int y = x+1; y < cards.size(); y++){
                if(c.getValue().equals(cards.get(y).getValue())){
                    pair = cards.get(y);
                    
                    return new Card[]{c,pair};
                }
            }
        }
        return null;
    }
    public static Card[] checkForHighCard(List<Card> cards){
        return new Card[]{Card.compareCards(cards)};
    } 
}