package activities;

/**
 * Created by Emil
 * This class will hold in memory, for UI use, the information to be displayed to the user
 * Updated
 * Added additional fields required for AsapRow Popup
 * * Updated
 * Added visibility for complex name
 */
public class AsapRow {
    private String distance_km_label;
    private float distance_km;
    private String starts_in_min_label;
    private float starts_in_min;
    private String complex_name;
    private String complex_desc;
    private String complex_type;
    private String complex_number;
    private boolean hasWashroom;
    private boolean hasChangeroom;
    private boolean hasRentals;
    private boolean hasVisibleComplex = true;
    private boolean isWhite = false;
    private String  activity;
    private String  timetable;
    private String lat;
    private String lon;

    public String getDistance_km_label() {
        return distance_km_label;
    }

    public void setDistance_km_label(String distance_km_label) {
        this.distance_km_label = distance_km_label;
    }

    public String getStarts_in_min_label() {
        return starts_in_min_label;
    }

    public void setStarts_in_min_label(String starts_in_min_label) {
        this.starts_in_min_label = starts_in_min_label;
    }

    public String getComplex_name() {
        return complex_name;
    }

    public void setComplex_name(String complex_name) {
        this.complex_name = complex_name;
    }

    public String getComplex_desc() {
        return complex_desc;
    }

    public void setComplex_desc(String complex_desc) {
        this.complex_desc = complex_desc;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getTimetable() {
        return timetable;
    }

    public void setTimetable(String timetable) {
        this.timetable = timetable;
    }

    public float getStarts_in_min() {
        return starts_in_min;
    }

    public void setStarts_in_min(float starts_in_min) {
        this.starts_in_min = starts_in_min;
    }

    public float getDistance_km() {
        return distance_km;
    }

    public void setDistance_km(float distance_km) {
        this.distance_km = distance_km;
    }

    public String getComplex_type() {
        return complex_type;
    }

    public void setComplex_type(String complex_type) {
        this.complex_type = complex_type;
    }

    public boolean isHasWashroom() {
        return hasWashroom;
    }

    public void setHasWashroom(boolean hasWashroom) {
        this.hasWashroom = hasWashroom;
    }

    public boolean isHasChangeroom() {
        return hasChangeroom;
    }

    public void setHasChangeroom(boolean hasChangeroom) {
        this.hasChangeroom = hasChangeroom;
    }

    public boolean isHasRentals() {
        return hasRentals;
    }

    public void setHasRentals(boolean hasRentals) {
        this.hasRentals = hasRentals;
    }

    public boolean hasVisibleComplex() {
        return hasVisibleComplex;
    }

    public void setComplexVisibility(boolean complexVisibility) {
        this.hasVisibleComplex = complexVisibility;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setIsWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public String getComplex_number() {
        return complex_number;
    }

    public void setComplex_number(String complex_number) {
        this.complex_number = complex_number;
    }

    // ---------------------


}
