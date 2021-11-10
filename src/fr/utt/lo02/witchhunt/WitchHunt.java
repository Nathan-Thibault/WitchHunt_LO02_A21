package fr.utt.lo02.witchhunt;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.player.PlayerManager;
import fr.utt.lo02.witchhunt.player.strategy.Strategy;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class WitchHunt {

    public static void main(String[] args) {
        createPlayers();
        CardManager.getInstance();//create cards
        RoundManager.getInstance().startNewRound();
    }

    private static void createPlayers() {
        Utils.resetScreen();
        System.out.println("Choose the number of physical players you want.");

        int p = Utils.readIntBetween(0, 6);

        Utils.resetScreen();
        Scanner sc = new Scanner(System.in);
        PlayerManager pManager = PlayerManager.getInstance();

        for (int i = 1; i <= p; i++) {
            System.out.println("Enter the name of the player ".concat(Integer.toString(i)).concat(" :"));
            try {
                pManager.addPhysicalPlayer(sc.nextLine());
                System.out.println("Player successfully added.");
            } catch (IllegalStateException | NoSuchElementException e) {
                System.err.println("An exception occurred. Please try again.");
                i--;
            }
        }

        Utils.resetScreen();
        System.out.println("Now, choose the number of artificial players you want.");

        int a = Utils.readIntBetween(Math.max(3 - p, 0), 6 - p);

        for (int i = 0; i < a; i++) {
            Utils.resetScreen();
            System.out.println("Choose strategies for artificial player ".concat(Integer.toString(i)));

            HashMap<Strategy.StrategyType, Class<? extends Strategy>> strategies = new HashMap<>();
            for(Strategy.StrategyType sType : Strategy.StrategyType.values()){
                strategies.put(sType, Utils.readStrategy(sType));
            }

            pManager.createArtificialPlayer(strategies);
        }

        Utils.resetScreen();
        StringBuffer sb = new StringBuffer();
        sb.append("Players in game: ");
        for (String player : pManager.getAllPlayers()) {
            sb.append(player);
            sb.append(", ");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));//remove useless last comma
        System.out.println(sb);
    }
}
