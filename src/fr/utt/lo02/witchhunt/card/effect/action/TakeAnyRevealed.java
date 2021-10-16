package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.player.Player;

import java.util.ArrayList;

public final class TakeAnyRevealed extends Action{

    public TakeAnyRevealed(){
        super("Take a revealed Rumour card from\nany other player into your hand.");
    }

    @Override
    public boolean execute(Player caller, CardEffect effect) {
        ArrayList<String> revealedCards = CardManager.getInstance().getRevealedNonDiscardedCards();

        if(revealedCards.isEmpty()){
            //TODO: can't execute this action -> can't play card
            return false;
        }else{
            String card = caller.chooseCardFrom(revealedCards);
            CardManager.getInstance().takeFromDiscarded(card);
            caller.getHand().add(card);
            return true;
        }
    }
}
