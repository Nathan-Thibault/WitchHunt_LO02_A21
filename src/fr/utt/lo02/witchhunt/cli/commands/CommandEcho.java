package fr.utt.lo02.witchhunt.cli.commands;

import fr.utt.lo02.witchhunt.cli.ANSIColor;
import fr.utt.lo02.witchhunt.cli.Utils;

public class CommandEcho extends AbstractCommand{
    @Override
    public boolean run(String[] args) {
        if(args.length > 0){
            Utils.coloredOutput("echo -> " + String.join(" ", args), ANSIColor.CYAN);
            return true;
        }
        return false;
    }

    @Override
    public void printUsage() {
        Utils.coloredOutput("Utilisation de echo:\necho <text>", ANSIColor.YELLOW);
    }
}
