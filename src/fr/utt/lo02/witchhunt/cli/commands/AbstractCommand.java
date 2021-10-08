package fr.utt.lo02.witchhunt.cli.commands;

public abstract class AbstractCommand {
    public abstract boolean run(String[] args);
    public abstract void printUsage();
}
