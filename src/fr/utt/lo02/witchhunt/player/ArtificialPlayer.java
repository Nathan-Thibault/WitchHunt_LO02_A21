package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.Utils;
import fr.utt.lo02.witchhunt.player.strategy.identity.IdentityStrategy;
import fr.utt.lo02.witchhunt.player.strategy.respond.RespondStrategy;
import fr.utt.lo02.witchhunt.player.strategy.turn.TurnStrategy;

import java.util.ArrayList;
import java.util.Objects;

public final class ArtificialPlayer extends Player{

    private TurnStrategy turnStrategy;
    private RespondStrategy respondStrategy;
    private IdentityStrategy identityStrategy;

    public ArtificialPlayer(ArrayList<String> hand, TurnStrategy turnStrategy, RespondStrategy respondStrategy, IdentityStrategy identityStrategy){
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
        respondStrategy.respondAccusation(this);
    }

    @Override
    public void chooseIdentity() {
        setIdentity(identityStrategy.chooseIdentity());
    }

    @Override
    public String chooseCardFrom(ArrayList<String> listOfCardNames) {
        //TODO create strategies to choose a card
        return  Utils.randomFromList(listOfCardNames);
    }

    @Override
    public Player choosePlayer(boolean unreaveled) {
        //TODO create strategies to choose a player
        ArrayList<String> playerNames;

        if (unreaveled){
            playerNames = PlayerManager.getInstance().getUnrevealedPlayers();
        } else {
            playerNames = PlayerManager.getInstance().getAllPlayers();
        }

        return PlayerManager.getInstance().getByName(Utils.randomFromList(playerNames));
    }
}
