package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.Utils;
import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.ArrayList;

public final class MakeDiscard extends Action{

    public MakeDiscard(){
        super("The player who accused you discards\na random card from their hand.");
    }

    @Override
    public boolean execute(Player caller, CardEffect effect) {
        PlayerManager pManager = PlayerManager.getInstance();
        ArrayList<String> playersWithCards = pManager.getPlayersWithCards();

        if(playersWithCards.isEmpty()){
            //TODO: can't execute this action -> can't play card
            return false;
        } else {
            String target = caller.choosePlayerFrom(playersWithCards);
            //discard random card from target's hand
            CardManager.getInstance().discard(Utils.randomFromList(pManager.getByName(target).getHand()));
            return true;
        }
    }
}
