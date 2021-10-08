package fr.utt.lo02.witchhunt.cli;

import java.io.IOException;

public class Utils {

    private static boolean windowsConsole;

    public static void setRunningOnWindows(){
        windowsConsole = (System.console() != null && System.getProperty("os.name").contains("Windows"));
    }

    public static void resetScreen(){
        if(windowsConsole){
            try {
                //run cls command
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (IOException | InterruptedException ignored){}
        } else {
            /*
            \033[H moves the cursor at the top left corner of the screen or console.
            \033[2J clears the screen from the cursor to the end of the screen.
             */
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    public static void coloredOutput(String str, ANSIColor color){
        coloredOutput(str, new ANSIColor[]{color});
    }

    public static void coloredOutput(String str, ANSIColor[] colors){
        if(windowsConsole){
            System.out.println(str);
        } else {
            String output = "";
            for (ANSIColor color : colors) {
                output = output.concat(color.toString());
            }
            output = output + str + ANSIColor.RESET;
            System.out.println(output);
        }
    }

    public static void errorOutput(String str){
        if(windowsConsole){
            System.out.println("Erreur: " + str);
        } else {
            System.out.println(ANSIColor.WHITE_BACKGROUND.toString() + ANSIColor.RED_BOLD + "Erreur: " + str + ANSIColor.RESET);
        }
    }
}
