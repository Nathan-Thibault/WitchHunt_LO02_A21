package fr.utt.lo02.witchhunt.card.effect;

import fr.utt.lo02.witchhunt.RoundManager;
import fr.utt.lo02.witchhunt.card.effect.action.Action;
import fr.utt.lo02.witchhunt.card.effect.condition.Condition;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class CardEffect {

    private final EffectType type;
    private final ArrayList<Action> actions;
    private final ArrayList<Condition> conditions;

    private String targetName;

    //TODO: make a builder for CardEffect ?
    public CardEffect(EffectType type, Action action) {
        this(type, new ArrayList<>(List.of(action)), null);
    }

    public CardEffect(EffectType type, Action action, Condition condition) {
        this(type, new ArrayList<>(List.of(action)), new ArrayList<>(List.of(condition)));
    }

    public CardEffect(EffectType type, Action firstAction, Action secondAction) {
        this(type, new ArrayList<>(List.of(firstAction, secondAction)), null);
    }

    public CardEffect(EffectType type, Action firstAction, Action secondAction, Condition condition) {
        this(type, new ArrayList<>(List.of(firstAction, secondAction)), new ArrayList<>(List.of(condition)));
    }

    public CardEffect(EffectType type, ArrayList<Action> actions, ArrayList<Condition> conditions) throws NullPointerException {
        this.type = Objects.requireNonNull(type, "CardEffect constructor: type can't be null.");
        this.actions = Objects.requireNonNull(actions, "CardEffect constructor: actions can't be null.");
        this.conditions = conditions;
    }

    public boolean isPlayable(String callerName, String accuserName) {
        Player caller = PlayerManager.getInstance().getByName(callerName);

        if (conditions != null) {
            for (Condition condition : conditions) {
                if (!condition.verify(caller)) {
                    return false;
                }
            }
        }

        for (Action action : actions) {
            if (!action.isExecutable(callerName, buildArgs(action, accuserName))) {
                return false;
            }
        }

        return true;
    }

    public boolean isPlayable(String callerName) {
        return isPlayable(callerName, null);
    }

    public void play(String callerName, String accuserName) {
        for (Action action : actions) {
            action.execute(callerName, buildArgs(action, accuserName));
        }

        RoundManager.getInstance().next();
    }

    public void play(String caller) {
        play(caller, null);
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

    public String getDescription() {
        StringBuilder sb = new StringBuilder();

        for (Action action : actions) {
            sb.append(action.getDescription());
            sb.append("\n");
        }

        if (conditions != null) {
            sb.append("Condition(s):\n");
            for (Condition condition : conditions) {
                sb.append(condition.getDescription());
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    private HashMap<String, Object> buildArgs(Action action, String accuserName) {
        HashMap<String, Object> args = new HashMap<>();

        switch (action.getClass().getSimpleName()) {
            case "ChooseNextPlayer", "LookAtIdentity", "RandomlyTakeCardFrom", "MustAccuse" -> args.put("effect", this);
            case "MakeAccuserDiscard", "TakeFromAccuser" -> args.put("accuserName", accuserName);
            default -> args = null;
        }

        return args;
    }
}
