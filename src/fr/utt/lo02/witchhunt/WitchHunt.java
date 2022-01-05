package fr.utt.lo02.witchhunt;

import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.io.IOController;
import fr.utt.lo02.witchhunt.managers.PlayerManager;
import fr.utt.lo02.witchhunt.managers.RoundManager;
import fr.utt.lo02.witchhunt.player.strategy.Strategy;
import fr.utt.lo02.witchhunt.player.strategy.StrategyEnum;
import fr.utt.lo02.witchhunt.player.strategy.identity.RandomIdentityStrategy;
import fr.utt.lo02.witchhunt.player.strategy.respond.RevealIfVillager;
import fr.utt.lo02.witchhunt.player.strategy.turn.AlwaysAccuse;

import java.util.HashMap;

public final class WitchHunt {

    private static boolean test;

    public static void main(String[] args) {
        test = false;

        for (String arg : args) {
            if (arg.equals("-test")) {
                createTestGame();
                test = true;
            }
        }

        IOController.getInstance().titleScreen();

        if (!test)
            createPlayers();

        CardManager.getInstance().createCards();
        PlayerManager.getInstance().shufflePlayers();

        IOController.getInstance().startGame();

        RoundManager.getInstance().startNewRound();
    }

    private static void createPlayers() {
        IOController io = IOController.getInstance();

        int p = io.readIntBetween(0, 6, "Choose the number of physical players you want.");

        PlayerManager pManager = PlayerManager.getInstance();

        for (int i = 1; i <= p; i++) {
            String name = io.readName(i);
            pManager.addPhysicalPlayer(name);
        }

        int a = io.readIntBetween(Math.max(3 - p, 0), 6 - p, "Now, choose the number of artificial players you want.");

        if (a > 0) {
            boolean chooseStrat = io.yesOrNo("Yes", "No, choose them at random",
                    "Do you want to choose the strategies of the artificial players ?");

            if (chooseStrat) {
                for (int i = 0; i < a; i++) {
                    io.printInfo("Choose strategies for artificial player ".concat(Integer.toString(i)));

                    HashMap<Strategy.StrategyType, Class<? extends Strategy>> strategies = new HashMap<>();

                    for (Strategy.StrategyType sType : Strategy.StrategyType.values()) {
                        StrategyEnum sEnum = io.readFromSet(StrategyEnum.getAllOfType(sType), "Select a strategy from the list bellow for the " + sType.getName() + ":");

                        strategies.put(sType, sEnum.getStrategyClass());
                    }

                    pManager.createArtificialPlayer(strategies);
                }
            } else {
                for (int i = 0; i < a; i++) {
                    HashMap<Strategy.StrategyType, Class<? extends Strategy>> strategies = new HashMap<>();

                    for (Strategy.StrategyType sType : Strategy.StrategyType.values()) {
                        StrategyEnum sEnum = Utils.randomFromSet(StrategyEnum.getAllOfType(sType));

                        strategies.put(sType, sEnum.getStrategyClass());
                    }

                    pManager.createArtificialPlayer(strategies);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Players in game: ");
        for (String player : pManager.getAllPlayers()) {
            sb.append(player);
            sb.append(", ");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));//remove useless last comma
        io.pause(sb.toString());
    }

    private static void createTestGame() {
        PlayerManager pManager = PlayerManager.getInstance();

        pManager.addPhysicalPlayer("Pierre");
        pManager.addPhysicalPlayer("Paul");
        pManager.addPhysicalPlayer("Jacques");

        HashMap<Strategy.StrategyType, Class<? extends Strategy>> strategies = new HashMap<>();
        strategies.put(Strategy.StrategyType.IDENTITY, RandomIdentityStrategy.class);
        strategies.put(Strategy.StrategyType.RESPOND, RevealIfVillager.class);
        strategies.put(Strategy.StrategyType.TURN, AlwaysAccuse.class);

        pManager.createArtificialPlayer(strategies);
        pManager.createArtificialPlayer(strategies);
    }

    public static boolean isTest() {
        return test;
    }
}
