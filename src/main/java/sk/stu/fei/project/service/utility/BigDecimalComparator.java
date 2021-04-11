package sk.stu.fei.project.service.utility;

import java.math.BigDecimal;

public class BigDecimalComparator {

    public boolean isValueGreaterThanCurrent(BigDecimal value, BigDecimal currentValue){
        int comparison = value.compareTo(currentValue);

        return comparison > 0;
    }

    public boolean isValueSmallerThanCurrent(BigDecimal value, BigDecimal currentValue){
        int comparison = value.compareTo(currentValue);

        return comparison < 0;
    }
}
