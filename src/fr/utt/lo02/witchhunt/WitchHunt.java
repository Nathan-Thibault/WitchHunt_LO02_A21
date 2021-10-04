package fr.utt.lo02.witchhunt;

import fr.utt.lo02.witchhunt.cli.InputListener;

public class WitchHunt {

    public static void main(String[] args){
        InputListener inputListener = new InputListener(System.in);
        inputListener.start();
    }

    public static void echo(String str){
        System.out.println("echo-> " + str);
    }
}
