package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.managers.PlayerManager;
import fr.utt.lo02.witchhunt.player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public final class GUI implements IOInterface {

    private final JFrame frame;
    private JPanel panel;

    public GUI() {
        frame = new JFrame();
        frame.setTitle("WitchHunt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);//center the frame on screen
        panel = new JPanel();
        frame.add(panel);
        frame.setVisible(true);
    }

    private void switchPanel(JPanel newPanel) {
        frame.remove(panel);
        frame.add(newPanel);
        frame.pack();
        frame.invalidate();
        frame.revalidate();

        panel = newPanel;
    }

    @Override
    public void clear() {
        JPanel panel = new JPanel();

        JLabel l = new JLabel("Nothing to display for now.");
        panel.setSize(l.getSize());
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
        PlayerManager pManager = PlayerManager.getInstance();

        JPanel panel = new JPanel();
        JPanel players = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        players.setLayout(new BoxLayout(players, BoxLayout.X_AXIS));

        for (String pName : pManager.getAllPlayers()) {
            Player p = pManager.getByName(pName);
            players.add(p.getPanel());
        }

        panel.add(players);

        switchPanel(panel);
    }

    @Override
    public void printInfo(String msg) {
    }

    @Override
    public void printError(String msg) {

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
