package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.player.Player;

public class Discard extends Action{

    public Discard(){
        super("Discard a card from your hand.");
    }

    @Override
    public void execute(Player caller) {
        String cardName = caller.chooseCardFrom(caller.getHand());

        CardManager.getInstance().discard(cardName);
    }
}
