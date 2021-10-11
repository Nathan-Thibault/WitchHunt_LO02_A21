package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.card.RumourCard;

import java.util.ArrayList;

public class PhysicalPlayer extends Player{

    public PhysicalPlayer(ArrayList<RumourCard> hand){
        super(hand);
    }

    @Override
    public Player accuse() {
        //TODO
        return null;
    }

    @Override
    public RumourCard selectCardFromHand() {
        //TODO
        return null;
    }

    @Override
    public void chooseIdentity(Identity identity) {
        //TODO
    }
}
