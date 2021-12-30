package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.managers.PlayerManager;
import fr.utt.lo02.witchhunt.player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public final class GUI implements IOInterface {
    private final JFrame frame;
    private final JPanel playersPanel;
    private final JTextArea info;
    private final JPanel actionPanel;
    private JFrame popup;

    public GUI() {
        frame = new JFrame();
        playersPanel = new JPanel();
        actionPanel = new JPanel();
        info = new JTextArea();

        CardManager.getInstance().createViews();

        playersPanel.setLayout(new GridLayout(3, 2, 5, 5));

        BorderLayout layout = new BorderLayout();
        layout.setVgap(20);
        actionPanel.setLayout(layout);

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
            playersPanel.add(new PlayerView(p));
        }
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
        int confirmDialog = JOptionPane.showConfirmDialog(popup, msg, "WitchHunt", JOptionPane.OK_CANCEL_OPTION);
        if (confirmDialog == JOptionPane.OK_OPTION)
            IOController.getInstance().stopWaiting();
    }

    @Override
    public void displayGameInfos() {
        JPanel panel = new JPanel();
        BorderLayout layout = new BorderLayout();

        layout.setHgap(20);
        layout.setVgap(20);
        panel.setLayout(layout);

        JScrollPane infoScroll = new JScrollPane(info);
        infoScroll.setPreferredSize(new Dimension(400, 100));

        panel.add(infoScroll, BorderLayout.SOUTH);
        panel.add(actionPanel, BorderLayout.EAST);
        panel.add(playersPanel, BorderLayout.CENTER);

        frame.setMinimumSize(new Dimension(1200, 800));
        switchPanel(panel);
    }

    @Override
    public void playerInfos(String playerName) {
        //TODO
    }

    @Override
    public void printInfo(String msg) {
        info.setText(msg);
        frame.pack();
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
    public <T> T readFromSet(Set<T> set, String msg) {
        JLabel label = new JLabel(msg);
        label.setFont(label.getFont().deriveFont(15F));
        @SuppressWarnings("unchecked")
        JList<T> list = new JList<>((T[]) set.toArray());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        JButton button = new JButton("OK");

        button.addActionListener(e -> IOController.getInstance().read("from_set", list.getSelectedValue()));

        actionPanel.removeAll();
        actionPanel.add(label, BorderLayout.NORTH);
        actionPanel.add(list, BorderLayout.CENTER);
        actionPanel.add(button, BorderLayout.SOUTH);
        actionPanel.invalidate();
        actionPanel.validate();

        return null;
    }
}
