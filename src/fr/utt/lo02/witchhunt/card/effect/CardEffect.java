package fr.utt.lo02.witchhunt.card.effect;

import fr.utt.lo02.witchhunt.managers.RoundManager;
import fr.utt.lo02.witchhunt.card.effect.actions.Action;
import fr.utt.lo02.witchhunt.card.effect.conditions.Condition;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * The <b>CardEffect</b> class represents an effect on a {@link fr.utt.lo02.witchhunt.card.RumourCard}.
 * <p>
 * A <b>CardEffect</b> can only be of one {@link EffectType},
 * is composed of at least one {@link Action} and can have none or many {@link Condition}.
 * The class offers methods to check if the <b>CardEffect</b> is playable and methods to play it.
 */
public final class CardEffect {

    /**
     * The type of the effect.
     * <p>
     * The {@link EffectType} determines in which situations the <b>CardEffect</b> can be played.
     * It also identifies the <b>CardEffect</b> on a card, as every card as one effect of each type.
     */
    private final EffectType type;

    /**
     * List of the actions constituting the <b>CardEffect</b>.
     * <p>
     * There can be as many {@link Action} as wanted but at least one is needed.
     */
    private final ArrayList<Action> actions;

    /**
     * List of the conditions constituting the <b>CardEffect</b>.
     * <p>
     * There can be as many {@link Condition} as wanted.
     * If there is none, this field should be <code>null</code>.
     */
    private final ArrayList<Condition> conditions;

    /**
     * Name of the player targeted by the <b>CardEffect</b>.
     * <p>
     * Some actions are dependant of each other and an action may need
     * a target previously chosen in another action. This field stores
     * the name of the target chosen in a first action, so it can be
     * retrieved by a second action later.
     */
    private String targetName;

    /**
     * Constructs a new <b>CardEffect</b>.
     *
     * @param type       type of the effect
     * @param actions    list of actions to constitute the effect
     * @param conditions list of conditions to constitute the effect, may be <code>null</code>
     * @throws NullPointerException if the type or the actions list are <code>null</code>
     */
    public CardEffect(EffectType type, ArrayList<Action> actions, ArrayList<Condition> conditions) throws NullPointerException {
        this.type = Objects.requireNonNull(type, "CardEffect constructor: type can't be null.");
        this.actions = Objects.requireNonNull(actions, "CardEffect constructor: actions can't be null.");
        this.conditions = conditions;
    }

    /**
     * Checks if the <b>CardEffect</b> is playable.
     * <p>
     * Will first check if all the conditions (if any) are met.
     * Then, will check if all the actions constituting the effect can be executed.
     *
     * @param callerName  name of the player who posses the card containing this effect
     * @param accuserName name of the player who accused the possessor of the card containing this effect
     * @return <code>true</code> if the effect can be played, <code>false</code> otherwise
     */
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

    /**
     * Checks if the <b>CardEffect</b> is playable.
     *
     * @param callerName name of the player who posses the card containing this effect
     * @return <code>true</code> if the effect can be played, <code>false</code> otherwise
     * @see CardEffect#isPlayable(String, String)
     */
    public boolean isPlayable(String callerName) {
        return isPlayable(callerName, null);
    }

    /**
     * Plays the <b>CardEffect</b>.
     * <p>
     * Executes all actions constituting the effect.
     *
     * @param callerName  name of the player who posses the card containing this effect
     * @param accuserName name of the player who accused the possessor of the card containing this effect
     */
    public void play(String callerName, String accuserName) {
        for (Action action : actions) {
            action.execute(callerName, buildArgs(action, accuserName));
        }

        RoundManager.getInstance().next();
    }

    /**
     * Plays the <b>CardEffect</b>.
     *
     * @param callerName name of the player who posses the card containing this effect
     * @see CardEffect#play(String, String)
     */
    public void play(String callerName) {
        play(callerName, null);
    }

    /**
     * Sets the target of the <b>CardEffect</b>.
     * <p>
     * This method allows en action to set the target for another action to retrieve it later.
     *
     * @param playerName the players name to target
     */
    public void setTarget(String playerName) {
        targetName = playerName;
    }

    /**
     * Gets the target of the <b>CardEffect</b>.
     * <p>
     * This method allows an action to get the target set by another, already executed action.
     *
     * @return the players name targeted
     */
    public String getTarget() {
        return targetName;
    }

    /**
     * Gets the type of the <b>CardEffect</b>.
     *
     * @return the type
     */
    public EffectType getType() {
        return this.type;
    }

    /**
     * Gets the description of the <b>CardEffect</b>.
     * <p>
     * Concatenates the description of all the actions constituting the effect,
     * well as the description of all the conditions, if any.
     *
     * @return the full description
     */
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
