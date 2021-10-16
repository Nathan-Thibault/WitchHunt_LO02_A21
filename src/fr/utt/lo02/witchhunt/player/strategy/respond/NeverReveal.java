package fr.utt.lo02.witchhunt.player.strategy.respond;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.card.effect.EffectType;
import fr.utt.lo02.witchhunt.player.ArtificialPlayer;

import java.util.ArrayList;

public final class NeverReveal implements RespondStrategy{
    @Override
    public void respondAccusation(ArtificialPlayer caller) {
        tryNotToReveal(caller);
    }

    public static void tryNotToReveal(ArtificialPlayer caller){
        CardManager cManager = CardManager.getInstance();

        ArrayList<String> cards = caller.getHand();
        String card = caller.chooseCardFrom(cards);

        //try to play witch effect of every card in hand until there's one that works
        while (!cards.isEmpty()){
            if(cManager.getByName(card).playEffect(EffectType.WITCH, caller)){
                return;
            } else {
                cards.remove(card);
                card = caller.chooseCardFrom(cards);
            }
        }
    }
}
