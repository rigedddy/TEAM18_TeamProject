package com.example.myjavafx;

/**
 * Represents a weekly schedule with a time or activity string for each day of the week.
 */
public class WeeklySchedule {
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;

    /**
     * Constructs a WeeklySchedule with values for each day of the week.
     *
     * @param monday    Schedule for Monday.
     * @param tuesday   Schedule for Tuesday.
     * @param wednesday Schedule for Wednesday.
     * @param thursday  Schedule for Thursday.
     * @param friday    Schedule for Friday.
     * @param saturday  Schedule for Saturday.
     * @param sunday    Schedule for Sunday.
     */
    public WeeklySchedule(String monday, String tuesday, String wednesday, String thursday,
                          String friday, String saturday, String sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    /** @return the schedule for Monday */
    public String getMonday() {
        return monday;
    }

    /** @return the schedule for Tuesday */
    public String getTuesday() {
        return tuesday;
    }

    /** @return the schedule for Wednesday */
    public String getWednesday() {
        return wednesday;
    }

    /** @return the schedule for Thursday */
    public String getThursday() {
        return thursday;
    }

    /** @return the schedule for Friday */
    public String getFriday() {
        return friday;
    }

    /** @return the schedule for Saturday */
    public String getSaturday() {
        return saturday;
    }

    /** @return the schedule for Sunday */
    public String getSunday() {
        return sunday;
    }
}
