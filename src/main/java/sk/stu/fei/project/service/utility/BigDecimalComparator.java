package sk.stu.fei.project.service.utility;

import java.math.BigDecimal;

public class BigDecimalComparator {

    /**
     * Method compares if one BigDecimal number is greater than the other
     * @param value value to compare to
     * @param currentValue current value
     * @return true if greater, false otherwise
     */
    public boolean isValueGreaterThanCurrent(BigDecimal value, BigDecimal currentValue){
        int comparison = value.compareTo(currentValue);

        return comparison > 0;
    }


    /**
     * Method compares if one BigDecimal number is smaller than the other
     * @param value value to compare to
     * @param currentValue current value
     * @return true if smaller, false otherwise
     */
    public boolean isValueSmallerThanCurrent(BigDecimal value, BigDecimal currentValue){
        int comparison = value.compareTo(currentValue);

        return comparison < 0;
    }
}
