package sk.stu.fei.project.domain;

public class AssetMovement {
    public double up;
    public double down;
    public double probability;
    public double T;

    public double volatility;
    public double timePeriod;
    public int steps;
    public double interest;

    public AssetMovement(double volatility, double timePeriod, double interest, int steps){
        this.volatility = volatility;
        this.timePeriod = timePeriod;
        this.interest = interest;
        this.steps = steps;
    }
}
