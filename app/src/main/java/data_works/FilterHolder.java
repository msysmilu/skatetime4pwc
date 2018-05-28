package data_works;

import cfg.Cfg;

/**
 * Created
 * Updated Added get and set to shared prefs
 */
public final class FilterHolder {
    public static boolean hasIndoor = true;
    public static boolean hasOutdoor = true;
    public static boolean hasWashroom = false;
    public static boolean hasChangeroom = false;
    public static boolean hasRentals = false;
    public static boolean hasMorning = true;
    public static boolean hasAfternoon = true;
    public static boolean hasEvening = true;

    public static void saveAllFiltersToSharedPrefs(){
        Cfg.FilterChoice.setFilterChosen("filter.hasIndoor",hasIndoor);
        Cfg.FilterChoice.setFilterChosen("filter.hasOutdoor",hasOutdoor);
        Cfg.FilterChoice.setFilterChosen("filter.hasWashroom",hasWashroom);
        Cfg.FilterChoice.setFilterChosen("filter.hasChangeroom",hasChangeroom);
        Cfg.FilterChoice.setFilterChosen("filter.hasRentals",hasRentals);
        Cfg.FilterChoice.setFilterChosen("filter.hasMorning",hasMorning);
        Cfg.FilterChoice.setFilterChosen("filter.hasAfternoon",hasAfternoon);
        Cfg.FilterChoice.setFilterChosen("filter.hasEvening",hasEvening);

    }

    public static void loadAllFiltersFromSharedPrefs(){
        hasIndoor = Cfg.FilterChoice.getFilter("filter.hasIndoor");
        hasOutdoor = Cfg.FilterChoice.getFilter("filter.hasOutdoor");
        hasWashroom = Cfg.FilterChoice.getFilter("filter.hasWashroom");
        hasChangeroom = Cfg.FilterChoice.getFilter("filter.hasChangeroom");
        hasRentals = Cfg.FilterChoice.getFilter("filter.hasRentals");
        hasMorning = Cfg.FilterChoice.getFilter("filter.hasMorning");
        hasAfternoon = Cfg.FilterChoice.getFilter("filter.hasAfternoon");
        hasEvening = Cfg.FilterChoice.getFilter("filter.hasEvening");
    }
   /*public FilterHolder(){
        this.hasIndoor = true;
        this.hasOutdoor = true;
        this.hasWashroom = true;
        this.hasChangeroom = true;
        this.hasRentals = true;
        this.hasMorning = true;
        this.hasAfternoon = true;
        this.hasEvening = true;
    }

    public FilterHolder(boolean HasIndoor, boolean HasOutdoor, boolean HasWashroom, boolean HasChangeroom, boolean HasRentals, boolean HasMorning, boolean HasAfternoon, boolean HasEvening){
        this.hasIndoor = HasIndoor;
        this.hasOutdoor = HasOutdoor;
        this.hasWashroom = HasWashroom;
        this.hasChangeroom = HasChangeroom;
        this.hasRentals = HasRentals;
        this.hasMorning = HasMorning;
        this.hasAfternoon = HasAfternoon;
        this.hasEvening = HasEvening;
    }*/
}