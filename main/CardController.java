package main;


import main.CardCalc;
import gui.CardGui;
import controls.KeyControl;
import controls.MouseControl;

public class CardController{
    
    final private CardCalc model;
    final private CardGui gui;
    final private KeyControl keyControl;
    final private MouseControl mouseControl;
    private boolean run = false;
    
    public CardController(CardCalc model, CardGui gui){
        this.model = model;
        this.gui = gui;
        this.keyControl = new KeyControl(model,gui);
        this.mouseControl = new MouseControl(model,gui);
        
        gui.addKeyControls(keyControl);
        gui.addMouseControls(mouseControl);
        gui.setFocusable(true);
        gui.requestFocus();
    }
    
    //addCardsPlayed to the placeholder
    private void syncCards(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                while(run){
                    try{
                        gui.getPlayed().addAll(model.getCardsPlayed());
                        gui.syncMats();
                        //System.out.println("Removed: " + model.getCardsPlayed().toString());
                        Thread.sleep(1000);
                        
                    }catch(Exception ex){
                        
                    }
                }
            }
        }).start();
    }
    
    
}