package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.RoundManager;
import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.card.IdentityCard;
import fr.utt.lo02.witchhunt.card.RumourCard;
import fr.utt.lo02.witchhunt.card.effect.EffectType;
import fr.utt.lo02.witchhunt.io.IOController;

import java.util.ArrayList;

public abstract class Player {

    protected final String name;
    protected int score;
    protected IdentityCard identityCard;
    protected ArrayList<String> ownedCards;

    public Player(String name) {
        this.name = name;
        identityCard = null;
        score = 0;
    }

    public void revealIdentity() {
        IOController io = IOController.getInstance();

        identityCard.setRevealed(true);
        io.printInfo(name.concat(" was a ").concat(identityCard.getIdentity().toString()).concat("!"));
        io.pause();
    }

    public void revealIdentity(String accuser) {
        RoundManager rManager = RoundManager.getInstance();
        PlayerManager pManager = PlayerManager.getInstance();
        IOController io = IOController.getInstance();

        identityCard.setRevealed(true);
        io.printInfo(name.concat(" was a ").concat(identityCard.getIdentity().toString()).concat("!"));

        if (identityCard.getIdentity() == Identity.WITCH) {
            pManager.getByName(accuser).addToScore(1);
            pManager.eliminate(name);
            io.printInfo(name.concat(" is out of the game until the end of the round.\n").concat(accuser).concat(" gains one point and takes another turn."));
            rManager.setIndexAtPlayer(accuser);
        } else {
            io.printInfo(accuser.concat(" gains no point and ").concat(name).concat(" takes next turn."));
            rManager.setIndexAtPlayer(name);
        }

        io.pause();
        rManager.next();
    }

    public void setIdentity(Identity identity) {
        identityCard = new IdentityCard(identity);
    }

    public IdentityCard getIdentityCard() {
        return identityCard;
    }

    public ArrayList<String> getHand() {
        return getRevealedCards(false);
    }

    public ArrayList<String> getPlayableCards(EffectType type, String accuser) {
        CardManager cManager = CardManager.getInstance();
        ArrayList<String> cards = new ArrayList<>();

        for (String cardName : getHand()) {
            RumourCard card = cManager.getByName(cardName);

            boolean playable = switch (type) {
                case HUNT -> card.canPlayHuntEffect(name);
                case WITCH -> card.canPlayWitchEffect(name, accuser);
            };

            if (playable) cards.add(cardName);
        }

        return cards;
    }

    public ArrayList<String> getPlayableCards() {
        return getPlayableCards(EffectType.HUNT, null);
    }

    public ArrayList<String> getPlayableCards(String accuser) {
        return getPlayableCards(EffectType.WITCH, accuser);
    }

    public ArrayList<String> getOwnedCards() {
        return ownedCards;
    }

    public ArrayList<String> getRevealedCards() {
        return getRevealedCards(true);
    }

    private ArrayList<String> getRevealedCards(boolean revealed) {
        CardManager cManager = CardManager.getInstance();

        ArrayList<String> cards = new ArrayList<>();
        for (String card : ownedCards) {
            boolean cr = cManager.getByName(card).isRevealed();
            //card revealed or not depending on if we want it to be or not
            if (!(cr || revealed) || (cr && revealed))
                cards.add(card);
        }

        return cards;
    }

    public void reset() {
        identityCard = null;
        ownedCards = CardManager.getInstance().dealHand();
    }

    public void addToScore(int addend) {
        score += addend;
    }

    public int getScore() {
        return score;
    }

    public abstract void playTurn();

    public abstract void respondAccusation(String accuser);

    public abstract void chooseIdentity();

    /**
     * @return true to reveal and false to discard
     */
    public abstract boolean chooseToRevealOrDiscard();

    public abstract String chooseCardFrom(ArrayList<String> listOfCardNames);

    public abstract String choosePlayerFrom(ArrayList<String> listOfPlayerNames);
}
