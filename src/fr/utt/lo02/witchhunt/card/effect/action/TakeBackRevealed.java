package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.player.Player;

import java.util.ArrayList;

public final class TakeBackRevealed extends Action{

    public TakeBackRevealed(){
        super("Take one of your own revealed\nRumour cards into your hand.");
    }

    @Override
    public boolean execute(Player caller, CardEffect effect) {
        CardManager cManager = CardManager.getInstance();

        ArrayList<String> revealedCards = new ArrayList<>();
        for (String card: caller.getHand()) {
            if(cManager.getByName(card).isRevealed())
                revealedCards.add(card);
        }

        if(revealedCards.isEmpty()){
            return false;
        } else {
            String card = caller.chooseCardFrom(revealedCards);
            cManager.getByName(card).setRevealed(false);
            return true;
        }
    }

    @Override
    public String cantExecute() {
        return "You have no revealed cards.";
    }
}
