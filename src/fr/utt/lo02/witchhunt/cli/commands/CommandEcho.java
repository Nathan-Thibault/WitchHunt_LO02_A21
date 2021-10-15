package fr.utt.lo02.witchhunt.cli.commands;

public final class CommandEcho extends AbstractCommand{
    @Override
    public boolean run(String[] args) {
        if(args.length > 0){
            System.out.println("echo -> " + String.join(" ", args));
            return true;
        }
        return false;
    }

    @Override
    public void printUsage() {
        System.out.println("Utilisation de echo:\necho <text>");
    }
}
