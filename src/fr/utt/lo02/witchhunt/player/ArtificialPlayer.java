package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.Utils;
import fr.utt.lo02.witchhunt.player.strategy.Strategy;
import fr.utt.lo02.witchhunt.player.strategy.identity.IdentityStrategy;
import fr.utt.lo02.witchhunt.player.strategy.respond.RespondStrategy;
import fr.utt.lo02.witchhunt.player.strategy.turn.TurnStrategy;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.HashMap;

public final class ArtificialPlayer extends Player {

    private TurnStrategy turnStrategy;
    private RespondStrategy respondStrategy;
    private IdentityStrategy identityStrategy;

    private final HashMap<String, Identity> knownIdentities = new HashMap<>();

    public ArtificialPlayer(String name, HashMap<Strategy.StrategyType, Class<? extends Strategy>> strategies) {
        super(name);
        try {
            turnStrategy = (TurnStrategy) strategies.get(Strategy.StrategyType.TURN).getConstructor().newInstance();
            respondStrategy = (RespondStrategy) strategies.get(Strategy.StrategyType.RESPOND).getConstructor().newInstance();
            identityStrategy = (IdentityStrategy) strategies.get(Strategy.StrategyType.IDENTITY).getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void savePlayerIdentity(String playerName, Identity identity) {
        knownIdentities.put(playerName, identity);
    }

    @Override
    public void playTurn() {
        turnStrategy.playTurn(name);
    }

    @Override
    public void respondAccusation(String accuser) {
        respondStrategy.respondAccusation(this, accuser);
    }

    @Override
    public void chooseIdentity() {
        setIdentity(identityStrategy.chooseIdentity());
    }

    @Override
    public boolean chooseToRevealOrDiscard() {
        //reveal if villager
        return identityCard.getIdentity().equals(Identity.VILLAGER);
    }

    @Override
    public String chooseCardFrom(Set<String> listOfCardNames) {
        //TODO create strategies to choose a card
        return Utils.randomFromSet(listOfCardNames);
    }

    @Override
    public String choosePlayerFrom(Set<String> listOfPlayerNames) {
        //TODO create strategies to choose a player
        return Utils.randomFromSet(listOfPlayerNames);
    }
}
