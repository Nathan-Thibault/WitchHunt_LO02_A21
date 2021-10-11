package fr.utt.lo02.witchhunt;

public enum Identity {
    VILLAGER("Villager"),
    WITCH("Witch");

    private final String name;

    Identity(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
