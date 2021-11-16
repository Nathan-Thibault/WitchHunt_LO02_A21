package fr.utt.lo02.witchhunt;

public enum Identity {
    VILLAGER("Villager", 1),
    WITCH("Witch", 2);

    private final String name;
    private final int code;

    Identity(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static Identity getByCode(int code) throws IllegalArgumentException {
        for (Identity id : Identity.values()) {
            if (id.getCode() == code)
                return id;
        }
        throw new IllegalArgumentException("StrategyEnum getByCode: couldn't find a Identity with this code");
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return name;
    }
}
