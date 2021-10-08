package fr.utt.lo02.witchhunt.cli;

public class Utils {
    public static void resetScreen(){
        /*
        \033[H moves the cursor at the top left corner of the screen or console.
        \033[2J clears the screen from the cursor to the end of the screen.
         */
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void coloredOutput(String str, ANSIColor color){
        System.out.println(color.toString() + str + ANSIColor.RESET);
    }

    public static void coloredOutput(String str, ANSIColor[] colors){
        String output = "";
        for (ANSIColor color : colors) {
            output = output.concat(color.toString());
        }
        output = output + str + ANSIColor.RESET.toString();
        System.out.println(output);
    }

    public static void errorOutput(String str){
        System.out.println(ANSIColor.WHITE_BACKGROUND.toString() + ANSIColor.RED_BOLD.toString() + str + ANSIColor.RESET.toString());
    }
}
