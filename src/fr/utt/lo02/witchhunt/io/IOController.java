package fr.utt.lo02.witchhunt.io;

import java.util.ArrayList;

public class IOController {

    private static IOController instance;

    private ArrayList<IOInterface> interfaces = new ArrayList<>();

    private IOController(){}

    public static IOController getInstance(){
        if(instance == null){
            instance = new IOController();
        }
        return instance;
    }

    public int readIntBetween(int min, int max){

    }
}
