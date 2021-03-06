package network;

import card.Card;
import card.CardSuit;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.CardDeck;

public final class Client{
    private String username = "Anonymous";
    private volatile Set<SocketChannel> userGroup;
    private SocketChannel channel;
    private boolean connected;
    
    //imp
    private CardDeck deck;
    
    public Client(){
        
    }
    public Client(String ipAddress, int portNumber){
        connectChannel(ipAddress,portNumber);
    }
    
    //Connection
    private void openChannel(){
        try{
            channel = SocketChannel.open();
            
            if(channel.isOpen()){
                System.out.println("Opened");
            }
        }catch(Exception ex){System.out.println("error connecting");}
    }
    public void connectChannel(String ipAddress, int portNumber){
        //new Thread(new Runnable(){
            //@Override
            //public void run(){
        openChannel();
                
        if(channel == null){
            return;
        }
                
        System.out.println("Client connecting");
                
                
        try{
            if(channel.isConnected()){
                
            }
            channel.connect(new InetSocketAddress(ipAddress,portNumber));
        }catch(IOException ex) {
            System.out.println("Connect x " + ex);
        }
        try{
            channel.finishConnect();
        }catch(IOException ex) {
            System.out.println("Finish Connect x " + ex);
        }

        try{
            while(!channel.finishConnect()){}
        }catch(IOException ex){
            
        }
                    
                    
        try {
            if(channel.finishConnect()) {
                connected = true;
                //read();
                //init here....
                userGroup = new HashSet<>();
                deck = new CardDeck();
            }
        } catch (IOException ex) {
        }
        
        
    }
    public void disconnectChannel(){
        if(connected == false){
            return;
        }
        if(channel != null ){
            connected = false;
            try{
                channel.socket().close();
                channel.close();
                
                if(channel.socket().isClosed()){
                    System.out.println("Closed");
                }
                
            }catch(Exception ex){
                System.err.println("Disconnect ex: " + ex);
            }
        }
    }
    public boolean isChannelConnected(){
        if(channel == null){
            return false;
        }
        return channel.isOpen();
    }
    private void checkConnection(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                while(connected){
                    try{
                        Thread.sleep(1000);
                        //write("Test");
                        
                        String string = "";
                        ByteBuffer buf = ByteBuffer.wrap(string.getBytes());
                        buf.put(string.getBytes());
                        buf.flip();

                        while(buf.hasRemaining()) {
                            try {
                                channel.write(buf);
                            } catch (IOException ex) {
                                System.err.println("Write ex: " + ex);
                                disconnectChannel();
                                //System.exit(0);
                                return;
                            }
                        }
                    }catch(Exception ex){
                        
                        return;
                    }
                }
            }
        }).start();
    }
    
    //R+W
    
    private void read() throws IOException{
        new Thread(new Runnable(){
            @Override
            public void run(){
                while(connected){
                    try{
                        if(channel != null){
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int numRead = channel.read(buffer);

                            if(numRead == -1){
                                disconnectChannel();
                                System.err.println("Read Closed: " + channel.toString());
                                return;
                            }

                            byte[] data = new byte[numRead];
                            System.arraycopy(buffer.array(),0,data,0, numRead);
                          
                            System.out.println("READ:   " + channel.toString() + ": " + new String(data));
                        }
                    }catch(IOException ex){
                        System.err.println("Read Exception: " + ex);
                        disconnectChannel();
                        return;
                    }
                }
            }
        }).start();
    }
    public String read(int bytes) throws IOException{
        
        if(connected){
            try{
                if(channel != null){
                    ByteBuffer buffer = ByteBuffer.allocate(bytes);
                    int numRead = channel.read(buffer);

                    if(numRead == -1){
                        disconnectChannel();
                        System.err.println("Read Closed: " + channel.toString());
                        return null;
                    }

                    byte[] data = new byte[numRead];
                    System.arraycopy(buffer.array(),0,data,0, numRead);

                    return new String(data);
                }
            }catch(IOException ex){
                System.err.println("Read Exception: " + ex);
                disconnectChannel();
                return null;
            }
        }
           return null; 
    }
    public void write(String string){
        if(!connected){
            System.out.println("NOT CONNECTED");
            return;
        }
        if(connected){
            if(channel != null){
                ByteBuffer buf = ByteBuffer.wrap((string + " ").getBytes());
                buf.put((string + " ").getBytes());
                
                buf.flip();

                while(buf.hasRemaining()) {
                    try {
                        channel.write(buf);
                    } catch (IOException ex) {
                        System.err.println("Write to client key ex: " + ex);
                        buf.clear();
                        disconnectChannel();
                    }
                }
                buf.clear();
                
            }
        }
    }
    
    
    //Getters and Setters
    public boolean getConnected(){
        return connected;
    }
    protected SocketChannel getChannel(){
        if(channel != null){
            return channel;
        }else{
            return null;
        }
    }
    public String getUsername(){
        return this.username;
    }
    public void setUsername(String username){
        this.username = username;
    }
            
    private void cInput(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    Scanner scanner = new Scanner(System.in);
                    
                    while(scanner.hasNextLine()){
                        String line = scanner.nextLine();
                        System.out.println("CLIENT: " + line);
                        write(line);
                              
                    }

                }catch(Exception ex){System.err.println(ex);}
            
            }
            
            
        }).start();
        
    }
    private void timeout(int time){
        if(time < 0){
            return;
        }
        if(time == 0){
            System.out.println("Disconnect");
            disconnectChannel();
            System.exit(0);
        }
        new Thread(new Runnable(){
            @Override
            public void run(){
                int x = 0;
                while(true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    x++;

                    if(x == time){
                        System.out.println("Timeout");
                        disconnectChannel();
                        System.exit(0);
                    }
                }
            }
        }).start();
    }
    
    //users
    public Set getUserGroup(){
        return userGroup;
    }
    private void searchForUser(String user){
        //ask server for public users list and add from there
    }
    private void addUserToGroup(SocketChannel user){
        userGroup.add(user);
    }
    private void removeUserFromeGroup(SocketChannel user){
        userGroup.remove(user);
    }
    private void removeAllUsers(){
        userGroup.clear();
    }
    private void searchForUsers(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                while(connected){
                    
                    try{
                        //Thread.sleep();
                        
                        if(userGroup.isEmpty()){
                            continue;
                        }
                        
                        Iterator<SocketChannel> userIter = userGroup.iterator();

                        while(userIter.hasNext()){
                            SocketChannel sc = userIter.next();
                            
                            if(!sc.isOpen()){
                                //user is offline
                            }else{
                                //user is online
                            }
                        }
                    }catch(Exception ex){
                        System.err.println("Search ex: " + ex);
                    }
                }
            }
        }).start();
    }
    private void displayUsers(int time){
        new Thread(new Runnable(){
            @Override
            public void run(){
                //check 

                int x = 0;
                while(connected){
                    Iterator<SocketChannel> usergroupIter = getUserGroup().iterator();
                    if(!getUserGroup().isEmpty()){
                        try {
                            if(usergroupIter.hasNext()){
                                System.out.println("User: " + usergroupIter.next().toString());
                            }
                            Thread.sleep(time*1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }).start();
    }
    
    
    public CardDeck getDeck(){
        return this.deck;
    }
    public Card makeCard(String string){
        //first make string into array
        Object[] strArr = string.split(",");
        
        //check if size is two
        if(strArr.length == 2){
            
            //first element is value
            
            if(Arrays.asList(deck.getValues().getValues()).contains(strArr[0])){
                
                if(deck.getSuitNames().contains((String) strArr[1])){
                    return new Card(strArr[0],deck.getSuit((String)strArr[1]));
                }
            }
        }
        return null;
    }
    public static void main(String[] args){
        Client client = new Client();
        
        try{
            client.connectChannel("127.0.0.1", 3803);
            
            if(client.isChannelConnected()){
                System.out.println("Connected");
                client.cInput();
                client.timeout(30);
            }
        }catch(Exception ex){}
    }
}