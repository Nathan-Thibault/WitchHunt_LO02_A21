package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.card.RumourCard;
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

        discarded.setLayout(new BorderLayout());

        BorderLayout layout = new BorderLayout();
        layout.setVgap(20);
        action.setLayout(layout);

        info.setBorder(new LineBorder(new Color(125, 125, 125)));
        info.setEditable(false);

        frame.setTitle("WitchHunt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void startGame() {
        //TODO: do that more properly
        PlayerManager pManager = PlayerManager.getInstance();
        for (String pName : pManager.getAllPlayers()) {
            Player p = pManager.getByName(pName);

            PlayerView playerView = new PlayerView(p);
            p.addPropertyChangeListener(new PlayerListener(playerView));

            players.add(playerView);
        }

        displayGameInfos();
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

        if (discardedCards.isEmpty()) {
            JLabel discardedLabel = new JLabel("There is no discarded cards.");
            discardedLabel.setFont(discardedLabel.getFont().deriveFont(20F));

            discarded.add(discardedLabel, BorderLayout.NORTH);
        } else {
            CardManager cManager = CardManager.getInstance();

            JLabel discardedLabel = new JLabel("Discarded cards:");
            discardedLabel.setFont(discardedLabel.getFont().deriveFont(20F));

            JPanel cards = new JPanel();
            cards.setLayout(new GridLayout(3, 2, 2, 2));

            for (String cardName : discardedCards) {
                RumourCard card = cManager.getByName(cardName);
                cards.add(card.getCardView());
            }

            discarded.add(discardedLabel, BorderLayout.NORTH);
            discarded.add(cards, BorderLayout.CENTER);
        }

        discarded.invalidate();
        discarded.validate();
    }

    private void switchPanel(JPanel panel) {
        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void clear() {
        if (popup != null)
            popup.dispose();

        if (gameStarted)
            return;

        JPanel panel = new JPanel();

        JLabel l = new JLabel("Nothing to display for now.");
        panel.add(l);

        frame.setMinimumSize(new Dimension(300, 75));
        switchPanel(panel);
    }

    @Override
    public void titleScreen() {
        JPanel panel = new JPanel();
        JButton button = new JButton();

        try {
            BufferedImage img = ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/images/title_screen.png")));
            ImageIcon imgIcon = new ImageIcon(img);
            button.setIcon(imgIcon);

            panel.setSize(img.getWidth(), img.getHeight());
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

        IOController.getInstance().stopWaiting();//executed only after JOptionPane is closed
    }

    @Override
    public void displayGameInfos() {
        if (gameStarted) {
            frame.pack();
            return;
        }

        JPanel panel = new JPanel();
        BorderLayout layout = new BorderLayout();

        layout.setHgap(20);
        layout.setVgap(20);
        panel.setLayout(layout);

        JScrollPane infoScroll = new JScrollPane(info);
        infoScroll.setPreferredSize(new Dimension(400, 100));

        panel.add(infoScroll, BorderLayout.SOUTH);
        panel.add(action, BorderLayout.WEST);
        panel.add(currentPlayer, BorderLayout.CENTER);
        panel.add(players, BorderLayout.NORTH);
        panel.add(discarded, BorderLayout.EAST);

        frame.setMinimumSize(new Dimension(1300, 800));
        switchPanel(panel);
    }

    @Override
    public void playerInfos(String playerName, String msg) {
        CardManager cManager = CardManager.getInstance();

        currentPlayer.removeAll();

        JLabel name = new JLabel(playerName);
        name.setFont(name.getFont().deriveFont(30F));
        name.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel message = new JLabel();
        message.setFont(message.getFont().deriveFont(15F));
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setText(msg);

        JPanel hand = new JPanel();
        hand.setLayout(new FlowLayout());

        for (String cardName : PlayerManager.getInstance().getByName(playerName).getHand()) {
            CardView original = cManager.getByName(cardName).getCardView();
            CardView cardView = new CardView(original, 200);
            cardView.update(true);

            hand.add(cardView);
        }

        currentPlayer.add(name);
        currentPlayer.add(message);
        currentPlayer.add(hand);

        currentPlayer.invalidate();
        currentPlayer.validate();
    }

    @Override
    public void printInfo(String msg) {
        info.setText(info.getText() + "\n" + msg);
    }

    @Override
    public int readIntBetween(int min, int max, String message) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(message);
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, min);
        JButton button = new JButton("OK");

        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        button.addActionListener(e -> IOController.getInstance().read("int", slider.getValue()));

        BorderLayout layout = new BorderLayout();
        layout.setVgap(20);
        panel.setLayout(layout);
        panel.add(label, BorderLayout.NORTH);
        panel.add(slider, BorderLayout.CENTER);
        panel.add(button, BorderLayout.SOUTH);

        switchPanel(panel);

        return 0;
    }

    @Override
    public String readName(int playerNum) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter the name of the player " + playerNum + ":");
        JTextField text = new JTextField("Player " + playerNum);
        JButton button = new JButton("OK");

        button.addActionListener(e -> IOController.getInstance().read("name", text.getText()));

        BorderLayout layout = new BorderLayout();
        layout.setVgap(20);
        panel.setLayout(layout);
        panel.add(label, BorderLayout.NORTH);
        panel.add(text, BorderLayout.CENTER);
        panel.add(button, BorderLayout.SOUTH);

        switchPanel(panel);

        return null;
    }

    @Override
    public <T> T readFromSet(Set<T> set, String msg) {
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

        return null;
    }
}
