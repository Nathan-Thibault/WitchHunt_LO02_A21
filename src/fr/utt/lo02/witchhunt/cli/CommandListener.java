package fr.utt.lo02.witchhunt.cli;

import fr.utt.lo02.witchhunt.cli.commands.AbstractCommand;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Scanner;

public class CommandListener extends Thread{

    private final Scanner scanner;
    private boolean listening;

    public CommandListener(InputStream inputStream){
        this.scanner = new Scanner(inputStream);
        listening = true;
    }

    @Override
    public void run(){
        while(listening){
            if(scanner.hasNextLine()){
                String input = scanner.nextLine();
                if(!input.equals("")){
                    Utils.resetScreen();
                    readInput(input);
                }
            }
        }
        scanner.close();
    }

    public void stopListening() {
        listening = false;
    }

    public static void readInput(String input){
        String[] args = input.split(" ");
        String commandName = args[0].substring(0,1).toUpperCase() + args[0].substring(1).toLowerCase();//capitalize first letter only
        args = Arrays.copyOfRange(args, 1, args.length);//remove command name from args

        try{
            //get class by its name then instantiate it
            Class<? extends AbstractCommand> commandClass = Class.forName("fr.utt.lo02.witchhunt.cli.commands.Command" + commandName).asSubclass(AbstractCommand.class);
            AbstractCommand command = commandClass.getDeclaredConstructor().newInstance();

            if(!command.run(args)){
                Utils.errorOutput("La commande n'a pas pu être executée");
                command.printUsage();
            }
        } catch (ClassNotFoundException e) {
            Utils.errorOutput("Commande non reconnue");
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
