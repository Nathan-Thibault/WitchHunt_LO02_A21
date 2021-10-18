package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.RoundManager;
import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.HashMap;
import java.util.Objects;

public final class ChooseNextPlayer extends Action{

    private final String requirement;

    public ChooseNextPlayer(String requirement){
        super("Choose next player.");

        this.requirement = requirement;
    }

    @Override
    public boolean execute(Player caller, HashMap<String, Object> args) {
        PlayerManager pManager = PlayerManager.getInstance();

        CardEffect effect = (CardEffect) Objects.requireNonNull(args.get("effect"), "ChooseNextPlayer : missing argument effect");

        String target;
        switch (requirement){
            case "unrevealed" -> target = caller.choosePlayerFrom(pManager.getUnrevealedPlayers());
            case "cards" -> {
                if(pManager.getPlayersWithUnrevealedCards().isEmpty())
                    return false;
                target = caller.choosePlayerFrom(pManager.getPlayersWithUnrevealedCards());
            }
            default -> target = caller.choosePlayerFrom(pManager.getInGamePlayers());
        }

        effect.setTarget(target);
        RoundManager.getInstance().setIndexAtPlayer(target);
        return true;
    }

    @Override
    public String cantExecute() {
        return "There is no players with cards in hand to choose.";
    }
}
