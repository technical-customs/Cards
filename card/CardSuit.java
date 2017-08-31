package card;


import java.util.Objects;

public class CardSuit{
    protected String suitname;

    public String getSuitname(){
        return suitname;
    }
    
    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (!CardSuit.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final CardSuit other = (CardSuit) obj;
        if(!this.getSuitname().equalsIgnoreCase(other.getSuitname())){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.suitname);
        return hash;
    }
}