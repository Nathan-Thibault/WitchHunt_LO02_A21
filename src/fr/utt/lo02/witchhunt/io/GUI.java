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
    private final JPanel mainPanel;
    private final JPanel playersPanel;
    private final JTextArea info;

    public GUI() {
        frame = new JFrame();
        mainPanel = new JPanel();
        playersPanel = new JPanel();
        info = new JTextArea();

        CardManager.getInstance().createViews();

        playersPanel.setLayout(new GridLayout(3, 2, 5, 5));
        PlayerManager pManager = PlayerManager.getInstance();
        for (String pName : pManager.getAllPlayers()) {
            Player p = pManager.getByName(pName);
            playersPanel.add(new PlayerView(p));
        }

        info.setBorder(new LineBorder(new Color(125, 125, 125)));
        info.setEditable(false);

        frame.setTitle("WitchHunt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private void switchPanel(JPanel newPanel) {
        mainPanel.removeAll();
        mainPanel.add(newPanel);
        mainPanel.invalidate();
        mainPanel.validate();
        frame.pack();
    }

    @Override
    public void clear() {
        JPanel panel = new JPanel();

        JLabel l = new JLabel("Nothing to display for now.");
        panel.add(l);

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
    public void pause() {
    }

    @Override
    public void displayGameInfos() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(playersPanel);
        panel.add(info);
        switchPanel(panel);
    }

    @Override
    public void printInfo(String msg) {
        info.setText(msg);
        frame.pack();
    }

    @Override
    public int readIntBetween(int min, int max) {
        return 0;
    }

    @Override
    public <T> T readFromSet(Set<T> list) {
        return null;
    }
}
