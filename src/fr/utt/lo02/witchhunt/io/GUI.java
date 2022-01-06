package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.card.RumourCard;
import fr.utt.lo02.witchhunt.io.listener.CardListener;
import fr.utt.lo02.witchhunt.io.listener.CardManagerListener;
import fr.utt.lo02.witchhunt.io.listener.PlayerListener;
import fr.utt.lo02.witchhunt.io.view.CardView;
import fr.utt.lo02.witchhunt.io.view.PlayerView;
import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.managers.PlayerManager;
import fr.utt.lo02.witchhunt.player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class GUI implements IOInterface {
    private final JFrame frame;
    private final JPanel currentPlayer;
    private final JPanel players;
    private final JPanel discarded;
    private final JPanel action;
    private final JTextArea info;
    private JFrame popup;

    boolean gameStarted = false;

    public GUI() {
        CardManager.getInstance().addPropertyChangeListener(new CardManagerListener(this));

        frame = new JFrame();
        currentPlayer = new JPanel();
        players = new JPanel();
        discarded = new JPanel();
        action = new JPanel();
        info = new JTextArea();

        players.setLayout(new FlowLayout());

        currentPlayer.setLayout(new BoxLayout(currentPlayer, BoxLayout.Y_AXIS));
        currentPlayer.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentPlayer.setBorder(new LineBorder(new Color(175, 0, 50)));

        discarded.setLayout(new BorderLayout(1, 20));

        action.setLayout(new BorderLayout(1, 20));

        info.setBorder(new LineBorder(new Color(125, 125, 125)));
        info.setEditable(false);

        frame.setTitle("WitchHunt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void startGame() {//TODO: do that more properly
        PlayerManager pManager = PlayerManager.getInstance();
        for (String pName : pManager.getAllPlayers()) {
            Player p = pManager.getByName(pName);

            PlayerView playerView = new PlayerView(p);
            p.addPropertyChangeListener(new PlayerListener(playerView));

            players.add(playerView);
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));

        JScrollPane infoScroll = new JScrollPane(info);
        infoScroll.setPreferredSize(new Dimension(400, 100));

        JPanel northPanel = new JPanel();
        JLabel playersLabel = new JLabel("All players :");
        playersLabel.setFont(playersLabel.getFont().deriveFont(20F));

        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.add(playersLabel);
        northPanel.add(players);

        mainPanel.add(infoScroll, BorderLayout.SOUTH);
        mainPanel.add(action, BorderLayout.WEST);
        mainPanel.add(currentPlayer, BorderLayout.CENTER);
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(discarded, BorderLayout.EAST);

        switchPanel(mainPanel);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        gameStarted = true;
    }

    public void createCardViews() {
        CardManager cManager = CardManager.getInstance();

        for (String cardName : cManager.getAll()) {
            RumourCard card = cManager.getByName(cardName);

            CardView cardView = new CardView(card);
            card.addPropertyChangeListener(new CardListener(cardView));

            card.setCardView(cardView);
        }
    }

    public void updateDiscarded(HashSet<String> discardedCards) {
        discarded.removeAll();

        JLabel discardedLabel = new JLabel("There's no discarded cards.");
        discardedLabel.setFont(discardedLabel.getFont().deriveFont(20F));

        if (!discardedCards.isEmpty()) {
            CardManager cManager = CardManager.getInstance();

            discardedLabel.setText("Discarded cards:");

            JPanel cards = new JPanel();
            cards.setLayout(new GridLayout(3, 2, 2, 2));

            for (String cardName : discardedCards) {
                RumourCard card = cManager.getByName(cardName);
                cards.add(card.getCardView());
            }

            discarded.add(cards, BorderLayout.CENTER);
        }

        discarded.add(discardedLabel, BorderLayout.NORTH);
        discarded.invalidate();
        discarded.validate();

        SwingUtilities.updateComponentTreeUI(frame);
    }

    private void switchPanel(JPanel panel) {
        frame.setContentPane(panel);
        SwingUtilities.updateComponentTreeUI(frame);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void clear() {
        if (popup != null) {
            popup.dispose();
            popup = null;
        }
    }

    @Override
    public void titleScreen() {
        JPanel panel = new JPanel();
        JButton button = new JButton();

        try {
            BufferedImage img = ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/images/title_screen.png")));
            ImageIcon imgIcon = new ImageIcon(img);
            button.setIcon(imgIcon);
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }

        button.setText("Click to continue.");
        button.addActionListener(e -> IOController.getInstance().stopWaiting());

        panel.add(button);
        switchPanel(panel);
    }

    @Override
    public void pause(String msg) {
        if (popup != null)
            popup.dispose();

        popup = new JFrame();
        JOptionPane.showMessageDialog(popup, msg, "WitchHunt", JOptionPane.INFORMATION_MESSAGE);

        //executed only after JOptionPane is closed
        if (popup != null) IOController.getInstance().stopWaiting();
    }

    @Override
    public void playerInfos(String playerName, String msg) {
        CardManager cManager = CardManager.getInstance();

        currentPlayer.removeAll();

        JLabel name = new JLabel(playerName);
        name.setFont(name.getFont().deriveFont(30F));

        JLabel message = new JLabel();
        message.setFont(message.getFont().deriveFont(15F));
        message.setText(msg);

        JPanel hand = new JPanel();
        hand.setLayout(new FlowLayout());

        for (String cardName : PlayerManager.getInstance().getByName(playerName).getHand()) {
            CardView original = cManager.getByName(cardName).getCardView();
            CardView cardViewCopy = new CardView(original, 200);
            cardViewCopy.update(true);

            hand.add(cardViewCopy);
        }

        currentPlayer.add(name);
        currentPlayer.add(message);
        currentPlayer.add(hand);

        currentPlayer.invalidate();
        currentPlayer.validate();

        SwingUtilities.updateComponentTreeUI(frame);
    }

    @Override
    public void printInfo(String msg) {
        info.setText(info.getText() + "\n" + msg);
    }

    @Override
    public int readIntBetween(int min, int max, String msg) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(msg);
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, min);
        JButton button = new JButton("OK");

        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        button.addActionListener(e -> IOController.getInstance().read("int", slider.getValue()));

        panel.setLayout(new BorderLayout(1, 20));
        panel.add(label, BorderLayout.NORTH);
        panel.add(slider, BorderLayout.CENTER);
        panel.add(button, BorderLayout.SOUTH);

        switchPanel(panel);

        return 0;
    }

    @Override
    public boolean yesOrNo(String yesMsg, String noMsg, String msg) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(msg);
        JButton noButton = new JButton(noMsg);
        JButton yesButton = new JButton(yesMsg);

        noButton.addActionListener(e -> IOController.getInstance().read("boolean", false));
        yesButton.addActionListener(e -> IOController.getInstance().read("boolean", true));

        panel.setLayout(new BorderLayout(10, 20));
        panel.add(label, BorderLayout.NORTH);
        panel.add(yesButton, BorderLayout.CENTER);
        panel.add(noButton, BorderLayout.WEST);

        switchPanel(panel);

        return false;
    }

    @Override
    public String readName(int playerNum) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter the name of the player " + playerNum + ":");
        JTextField text = new JTextField("Player " + playerNum);
        JButton button = new JButton("OK");

        button.addActionListener(e -> IOController.getInstance().read("name", text.getText()));

        panel.setLayout(new BorderLayout(1, 20));
        panel.add(label, BorderLayout.NORTH);
        panel.add(text, BorderLayout.CENTER);
        panel.add(button, BorderLayout.SOUTH);

        switchPanel(panel);

        return null;
    }

    @Override
    public <T> T readFromSet(Set<T> set, String msg) {
        msg = "<html>" + msg.replace("\n", "<br>") + "</html>";
        JLabel label = new JLabel(msg);
        label.setFont(label.getFont().deriveFont(15F));
        @SuppressWarnings("unchecked")
        JList<T> list = new JList<>((T[]) set.toArray());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        JButton button = new JButton("OK");

        button.addActionListener(e -> IOController.getInstance().read("from_set", list.getSelectedValue()));

        action.removeAll();
        action.add(label, BorderLayout.NORTH);
        action.add(list, BorderLayout.CENTER);
        action.add(button, BorderLayout.SOUTH);
        action.invalidate();
        action.validate();

        if (!gameStarted)
            switchPanel(action);
        else
            SwingUtilities.updateComponentTreeUI(frame);

        return null;
    }
}
