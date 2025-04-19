public enum DeliveryWeekday {
    SUNDAY("sunday"),
    MONDAY("monday"),
    TUESDAY("tuesday"),
    WEDNESDAY("wednesday"),
    THURSDAY("thursday"),
    FRIDAY("friday"),
    SATURDAY("saturday");

    private final String day_name;

    DeliveryWeekday(String name) {
        this.day_name = name;
    }

    public String getDay_Name() {
        return day_name;
    }

    public static DeliveryWeekday fromInt(int index) {
        if (index < 0 || index >= values().length) {
            throw new IllegalArgumentException("Invalid index for DeliveryWeekday: " + index);
        }
        return values()[index];
    }

    public static DeliveryWeekday fromString(String input) {
        input = input.trim().toLowerCase();
        for (DeliveryWeekday day : values()) {
            if (day.name().equalsIgnoreCase(input) || day.day_name.equalsIgnoreCase(input)) {
                return day;
            }
        }
        throw new IllegalArgumentException("Unknown day: " + input);
    }
}
