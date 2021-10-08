package fr.utt.lo02.witchhunt;

import fr.utt.lo02.witchhunt.cli.CommandListener;
import fr.utt.lo02.witchhunt.cli.Utils;

public class WitchHunt {

    public static void main(String[] args){
        Utils.setWindowsConsole(System.console() != null && System.getProperty("os.name").contains("Windows"));

        CommandListener commandListener = new CommandListener(System.in);
        commandListener.start();
    }
}
