package sk.stu.fei.project.service.asset_movement;

import lombok.NonNull;
import sk.stu.fei.project.domain.AssetMovement;

public class AssetMovementImpl implements AssetMovementService{
    java.util.logging.Logger logger =  java.util.logging.Logger.getLogger(this.getClass().getName());

    public boolean initAssetMovementParameters(@NonNull AssetMovement assetMovement){
        createAndSetT(assetMovement);
        if (isArbitrageConditionMet(assetMovement)){
            createAndSetUpAndDownModifiers(assetMovement);
            createAndSetProbability(assetMovement);

            logger.info("Asset Movement parameters initialized successfully");
            return true;
        }

        logger.severe("No-arbitrage condition is not met");
        return false;
    }

    private boolean isArbitrageConditionMet(AssetMovement assetMovement){
        boolean firstPart = (-assetMovement.volatility) < (assetMovement.interest * Math.sqrt(assetMovement.T));
        boolean secondPart = (assetMovement.interest * Math.sqrt(assetMovement.T)) < (assetMovement.volatility);
        logger.info(String.valueOf(firstPart));
        logger.info(String.valueOf(secondPart));

        return firstPart && secondPart;
    }

    private void createAndSetT(AssetMovement assetMovement){
        assetMovement.T = assetMovement.timePeriod / assetMovement.steps;
    }

    private void createAndSetUpAndDownModifiers(AssetMovement assetMovement){
        assetMovement.up = calculateUpMovement(assetMovement.volatility, assetMovement.T);
        assetMovement.down = calculateDownMovement(assetMovement.volatility, assetMovement.T);
    }

    private double calculateUpMovement(double volatility, double T){
        double up = Math.exp(volatility * Math.sqrt(T));

        logger.info("Asset up movement is: " + up);
        return up;
    }

    private double calculateDownMovement(double volatility, double T){
        double down = Math.exp(- volatility * Math.sqrt(T));

        logger.info("Asset down movement is: " + down);
        return down;
    }


    private void createAndSetProbability(AssetMovement assetMovement){
        double numerator = Math.exp(assetMovement.interest * (assetMovement.T)) - assetMovement.down;
        double denominator = assetMovement.up - assetMovement.down;
        assetMovement.probability = numerator / denominator;

        logger.info("Probability is: " + assetMovement.probability);
    }
}
