package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.RoundManager;
import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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

        ArrayList<String> possibleTargets;
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

        String target = caller.choosePlayerFrom(possibleTargets);

        effect.setTarget(target);
        RoundManager.getInstance().setIndexAtPlayer(target);
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        if (requirement != null)
            return requirement.equals("cards") && !PlayerManager.getInstance().getPlayersWithHand().isEmpty();
        else
            return true;
    }
}
