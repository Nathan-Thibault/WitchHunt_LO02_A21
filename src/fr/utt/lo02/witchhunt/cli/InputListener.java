package fr.utt.lo02.witchhunt.cli;

import fr.utt.lo02.witchhunt.WitchHunt;

import java.io.InputStream;
import java.util.Scanner;

public class InputListener extends Thread{

    private final Scanner scanner;
    private boolean listening;

    public InputListener(InputStream inputStream){
        this.scanner = new Scanner(inputStream);
        listening = true;
    }

    @Override
    public void run(){
        while(listening){
            if(scanner.hasNextLine()){
                String input = scanner.nextLine();
                if(!input.equals("")){
                    WitchHunt.echo(input);
                }
            }
        }
        scanner.close();
    }

    public void stopListening() {
        listening = false;
    }
}
