package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.Utils;
import fr.utt.lo02.witchhunt.managers.PlayerManager;
import fr.utt.lo02.witchhunt.managers.RoundManager;
import fr.utt.lo02.witchhunt.player.strategy.Strategy;
import fr.utt.lo02.witchhunt.player.strategy.IdentityStrategy;
import fr.utt.lo02.witchhunt.player.strategy.RespondStrategy;
import fr.utt.lo02.witchhunt.player.strategy.TurnStrategy;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
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

    public void accuseSomeone() {
        PlayerManager pManager = PlayerManager.getInstance();

        Set<String> possibleTargets = pManager.getUnrevealedPlayers();
        possibleTargets.remove(name);//player can't choose himself

        String target;
        if (!knownIdentities.isEmpty()) {
            Set<String> judiciousTargets = possibleTargets;

            if (knownIdentities.containsValue(Identity.WITCH)) {
                //put all unrevealed witch players as judicious targets
                judiciousTargets = new HashSet<>();

                for (Map.Entry<String, Identity> entry : knownIdentities.entrySet()) {
                    if (entry.getValue().equals(Identity.WITCH) && possibleTargets.contains(entry.getKey()))
                        judiciousTargets.add(entry.getKey());
                }
            } else {
                //remove players known as villager from judicious targets
                for (String pName : knownIdentities.keySet())
                    judiciousTargets.remove(pName);
            }

            //choose target from judicious targets if not empty, from possible targets otherwise
            target = choosePlayerFrom(judiciousTargets.isEmpty() ? possibleTargets : judiciousTargets);
        } else {
            target = choosePlayerFrom(possibleTargets);
        }

        RoundManager.getInstance().accuse(name, target);
    }

    @Override
    public void playTurn() {
        turnStrategy.playTurn(this);
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
        //TODO create strategies for that
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
