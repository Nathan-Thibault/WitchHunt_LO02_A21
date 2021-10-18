package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public final class TakeAnyRevealed extends Action{

    public TakeAnyRevealed(){
        super("Take a revealed Rumour card from\nany other player into your hand.");
    }

    @Override
    public boolean execute(Player caller, HashMap<String, Object> args) {
        ArrayList<String> revealedCards = CardManager.getInstance().getRevealedNonDiscardedCards();

        //remove caller cards since he has to chose from another player
        for(String card : caller.getOwnedCards()){
            revealedCards.remove(card);
        }

        if(revealedCards.isEmpty()){
            return false;
        }else{
            String card = caller.chooseCardFrom(revealedCards);
            Player owner = Objects.requireNonNull(PlayerManager.getInstance().getOwnerOf(card));

            owner.getOwnedCards().remove(card);
            caller.getOwnedCards().add(card);
            return true;
        }
    }

    @Override
    public String cantExecute() {
        return "There isn't any revealed cards to choose.";
    }
}
