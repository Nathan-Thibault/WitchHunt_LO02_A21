package fr.utt.lo02.witchhunt;

import fr.utt.lo02.witchhunt.cli.CommandListener;
import fr.utt.lo02.witchhunt.cli.Utils;

public class WitchHunt {

    public static void main(String[] args){
        Utils.setRunningOnWindows();

        CommandListener commandListener = new CommandListener(System.in);
        commandListener.start();
    }
}
