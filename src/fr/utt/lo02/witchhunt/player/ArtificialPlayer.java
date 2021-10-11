package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.card.RumourCard;
import fr.utt.lo02.witchhunt.player.strategy.IdentityStrategy;
import fr.utt.lo02.witchhunt.player.strategy.RespondStrategy;
import fr.utt.lo02.witchhunt.player.strategy.TurnStrategy;

import java.util.ArrayList;
import java.util.Objects;

public class ArtificialPlayer extends Player{

    private TurnStrategy turnStrategy;
    private RespondStrategy respondStrategy;
    private IdentityStrategy identityStrategy;

    public ArtificialPlayer(ArrayList<RumourCard> hand, TurnStrategy turnStrategy, RespondStrategy respondStrategy, IdentityStrategy identityStrategy){
        super(hand);
        this.turnStrategy = Objects.requireNonNull(turnStrategy, "ArtificialPlayer constructor: turnStrategy can't be null");
        this.respondStrategy = Objects.requireNonNull(respondStrategy, "ArtificialPlayer constructor: respondStrategy can't be null");
        this.identityStrategy = Objects.requireNonNull(identityStrategy, "ArtificialPlayer constructor: identityStrategy can't be null");
    }

    @Override
    public void playTurn() {
        turnStrategy.playTurn();
    }

    @Override
    public void respondAccusation() {
        respondStrategy.respondAccusation();
    }

    @Override
    public void chooseIdentity() {
        setIdentity(identityStrategy.chooseIdentity());
    }
}
