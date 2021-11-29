package fr.utt.lo02.witchhunt.card.effect.actions;

import fr.utt.lo02.witchhunt.managers.RoundManager;
import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public final class ChooseNextPlayer extends Action {

    private final String requirement;

    public ChooseNextPlayer(String requirement) {
        super("Choose next player.");

        this.requirement = requirement;
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        PlayerManager pManager = PlayerManager.getInstance();
        Player caller = pManager.getByName(callerName);

        CardEffect effect = (CardEffect) Objects.requireNonNull(args.get("effect"), "ChooseNextPlayer : missing argument effect");

        Set<String> possibleTargets = buildPossibleTargets(callerName);

        //remove the player with the "immune" card against the one playing this action if not null
        possibleTargets.remove((String) args.get("protectedPlayer"));

        String target = caller.choosePlayerFrom(possibleTargets);

        effect.setTarget(target);
        RoundManager.getInstance().setIndexAtPlayer(target);
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        Set<String> possibleTargets = buildPossibleTargets(callerName);

        //remove the player with the card "immune" against the one playing this action if not null
        possibleTargets.remove((String) args.get("protectedPlayer"));

        return !possibleTargets.isEmpty();
    }

    private Set<String> buildPossibleTargets(String callerName) {
        PlayerManager pManager = PlayerManager.getInstance();

        Set<String> possibleTargets;

        if (requirement == null) {
            possibleTargets = pManager.getInGamePlayers();
        } else {
            possibleTargets = switch (requirement) {
                case "unrevealed" -> pManager.getUnrevealedPlayers();
                case "cards" -> pManager.getPlayersWithHand();
                default -> throw new IllegalArgumentException("ChooseNextPlayer: unsupported requirement");
            };
        }
        possibleTargets.remove(callerName);//a player can never choose himself as the target

        return possibleTargets;
    }
}
