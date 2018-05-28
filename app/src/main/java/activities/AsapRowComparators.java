package activities;

import java.util.Comparator;

/**
 * Created by Emil
 */
public class AsapRowComparators {
    public static Comparator<AsapRow> comparatorWrtAsapTime = new Comparator<AsapRow>() {
        @Override
        public int compare(AsapRow lhs, AsapRow rhs) {
            return (int) Math.signum(lhs.getStarts_in_min() - rhs.getStarts_in_min());
        }
    };

    public static Comparator<AsapRow> comparatorWrtStartsInAndThenDistanceAndThenName = new Comparator<AsapRow>() {
        @Override
        public int compare(AsapRow lhs, AsapRow rhs) {
            int sign = 0;
            //  by starting time
            if(0 == sign) sign = (int) Math.signum(
                    onlyPositiveNumbers(lhs.getStarts_in_min())
                            - onlyPositiveNumbers(rhs.getStarts_in_min())
            );
            //  by distance
            if(0 == sign) sign = (int) Math.signum(lhs.getDistance_km() - rhs.getDistance_km());
            //  by name
            if(0 == sign) sign = (int) Math.signum(lhs.getComplex_name().compareToIgnoreCase(rhs.getComplex_name()));
            return sign;
        }
    };

    public static Comparator<AsapRow> comparatorWrtDistanceAndThenStartsInAndThenName = new Comparator<AsapRow>() {
        @Override
        public int compare(AsapRow lhs, AsapRow rhs) {
            int sign = 0;
            // by distance
            if(0 == sign) sign = (int) Math.signum(lhs.getDistance_km() - rhs.getDistance_km());
            // by starting time
            if(0 == sign) sign = (int) Math.signum(
                    onlyPositiveNumbers(lhs.getStarts_in_min())
                            - onlyPositiveNumbers(rhs.getStarts_in_min())
            );
            // by name
            if(0 == sign) sign = (int) Math.signum(lhs.getComplex_name().compareToIgnoreCase(rhs.getComplex_name()));
            return sign;
        }
    };

    private static float onlyPositiveNumbers(float n){
        if(n<=0) return 0;
        return n;
    }

}

