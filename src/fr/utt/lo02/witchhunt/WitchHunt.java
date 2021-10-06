package fr.utt.lo02.witchhunt;

import fr.utt.lo02.witchhunt.cli.CommandListener;

public class WitchHunt {

    public static void main(String[] args){
        CommandListener commandListener = new CommandListener(System.in);
        commandListener.start();
    }
}
