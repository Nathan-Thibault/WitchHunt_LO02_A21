package fr.utt.lo02.witchhunt.card;

public abstract class Card {
    protected boolean revealed;

    public Card(boolean revealed){
        this.revealed = revealed;
    }

    public boolean isRevealed(){
        return revealed;
    }

    public void setRevealed(boolean revealed){
        this.revealed = revealed;
    }
}
