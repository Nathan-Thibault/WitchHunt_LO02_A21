package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.managers.PlayerManager;
import fr.utt.lo02.witchhunt.player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public final class GUI implements IOInterface {

    private final JPanel mainPanel;
    private final JPanel playersPanel;
    private final JFrame frame;

    public GUI() {
        frame = new JFrame();
        mainPanel = new JPanel();
        playersPanel = new JPanel();

        CardManager.getInstance().createViews();

        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.X_AXIS));
        PlayerManager pManager = PlayerManager.getInstance();
        for (String pName : pManager.getAllPlayers()) {
            Player p = pManager.getByName(pName);
            playersPanel.add(new PlayerView(p));
        }

        frame.setTitle("WitchHunt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);//center the frame on screen
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
        switchPanel(playersPanel);
    }

    @Override
    public void printInfo(String msg) {
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
