package sk.stu.fei.project.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
public class AssetTree{

    private Node root;
    private double volatility;
    private double timePeriod;
    private int steps;
    private double interest;
    @Setter
    private double probability;



    public AssetTree(double volatility, BigDecimal currentAssetValue, double timePeriod, int steps, double interest){
        this.root = new Node();
        this.volatility = volatility;
        this.root.value = currentAssetValue;
        this.timePeriod = timePeriod;
        this.steps = steps;
        this.interest = interest;
    }
}
