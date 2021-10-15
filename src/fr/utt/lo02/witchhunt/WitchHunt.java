package fr.utt.lo02.witchhunt;

public class WitchHunt {

    public static void main(String[] args){
        Utils.setWindowsConsole(System.console() != null && System.getProperty("os.name").contains("Windows"));
    }
}
