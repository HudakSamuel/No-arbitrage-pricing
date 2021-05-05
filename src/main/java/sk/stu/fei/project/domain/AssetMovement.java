package sk.stu.fei.project.domain;

import java.math.BigDecimal;

public class AssetMovement {
    public BigDecimal up;
    public BigDecimal down;
    public BigDecimal probability;
    public BigDecimal T;

    public BigDecimal volatility;
    public BigDecimal timePeriod;
    public int steps;
    public BigDecimal interest;

    public AssetMovement(BigDecimal volatility, BigDecimal timePeriod, BigDecimal interest, int steps){
        this.volatility = volatility;
        this.timePeriod = timePeriod;
        this.interest = interest;
        this.steps = steps;
    }
}
