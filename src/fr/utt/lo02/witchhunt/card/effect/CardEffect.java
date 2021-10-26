package fr.utt.lo02.witchhunt.card.effect;

import fr.utt.lo02.witchhunt.card.effect.action.Action;
import fr.utt.lo02.witchhunt.card.effect.condition.Condition;
import fr.utt.lo02.witchhunt.player.PhysicalPlayer;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class CardEffect {

    private EffectType type;
    private ArrayList<Action> actions;
    private ArrayList<Condition> conditions;

    private String targetName;

    public CardEffect(EffectType type, Action action) {
        new CardEffect(type, new ArrayList<>(List.of(action)), null);
    }

    public CardEffect(EffectType type, Action action, Condition condition) {
        new CardEffect(type, new ArrayList<>(List.of(action)), new ArrayList<>(List.of(condition)));
    }

    public CardEffect(EffectType type, Action firstAction, Action secondAction) {
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(firstAction);
        actions.add(secondAction);

        new CardEffect(type, actions, null);
    }

    public CardEffect(EffectType type, Action firstAction, Action secondAction, Condition condition) {
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(firstAction);
        actions.add(secondAction);

        new CardEffect(type, actions, new ArrayList<>(List.of(condition)));
    }

    public CardEffect(EffectType type, ArrayList<Action> actions, ArrayList<Condition> conditions) throws NullPointerException {
        this.type = Objects.requireNonNull(type, "CardEffect constructor: type can't be null.");
        this.actions = Objects.requireNonNull(actions, "CardEffect constructor: actions can't be null.");
        this.conditions = conditions;
    }

    public boolean play(String caller) {
        return play(caller, null);
    }

    public boolean play(String callerName, String accuserName) {
        Player caller = PlayerManager.getInstance().getByName(callerName);

        if (conditions != null) {
            for (Condition condition : conditions) {
                if (!condition.verify(caller)) {
                    if (caller instanceof PhysicalPlayer)
                        System.out.println("This effect can't be played :\n".concat(condition.getDescription()));
                    return false;
                }
            }
        }

        for (Action action : actions) {
            HashMap<String, Object> args = new HashMap<>();

            switch (action.getClass().getName()) {
                case "ChooseNextPlayer", "LookAtIdentity", "RandomlyTakeCardFrom" -> args.put("effect", this);
                case "MakeAccuserDiscard" -> args.put("accuserName", accuserName);
                default -> args = null;
            }

            if (!action.execute(callerName, args)) {
                if (caller instanceof PhysicalPlayer)
                    System.out.println("This effect can't be played :\n".concat(action.cantExecute()));
                return false;
            }
        }

        return true;
    }

    public void setTarget(String playerName) {
        targetName = playerName;
    }

    public String getTarget() {
        return targetName;
    }

    public EffectType getType() {
        return this.type;
    }
}
