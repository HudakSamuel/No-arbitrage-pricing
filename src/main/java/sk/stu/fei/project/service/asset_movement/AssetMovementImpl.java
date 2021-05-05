package sk.stu.fei.project.service.asset_movement;

import ch.obermuhlner.math.big.BigDecimalMath;
import lombok.NonNull;
import sk.stu.fei.project.domain.AssetMovement;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class AssetMovementImpl implements AssetMovementService{
    MathContext precision = new MathContext(10000);
    java.util.logging.Logger logger =  java.util.logging.Logger.getLogger(this.getClass().getName());

    public boolean initAssetMovementParameters(@NonNull AssetMovement assetMovement){
        if (isArbitrageConditionMet(assetMovement)){
            createAndSetT(assetMovement);
            createAndSetUpAndDownModifiers(assetMovement);
            createAndSetProbability(assetMovement);

            logger.info("Asset Movement parameters initialized successfully");
            return true;
        }

        logger.severe("No-arbitrage condition is not met");
        return false;
    }

    private boolean isArbitrageConditionMet(AssetMovement assetMovement){
//        boolean firstPart = (-assetMovement.volatility) < (assetMovement.interest * Math.sqrt(assetMovement.T));
//        boolean secondPart = (assetMovement.interest * Math.sqrt(assetMovement.T)) < (assetMovement.volatility);
//        logger.info(String.valueOf(firstPart));
//        logger.info(String.valueOf(secondPart));
//
//        return firstPart && secondPart;
        return true;
    }

    private void createAndSetT(AssetMovement assetMovement){
        //assetMovement.T = assetMovement.timePeriod / assetMovement.steps;
        assetMovement.T = assetMovement.timePeriod.divide(BigDecimal.valueOf(assetMovement.steps), RoundingMode.HALF_EVEN);
    }

    private void createAndSetUpAndDownModifiers(AssetMovement assetMovement){
        assetMovement.up = calculateUpMovement(assetMovement.volatility, assetMovement.T);
        assetMovement.down = calculateDownMovement(assetMovement.volatility, assetMovement.T);
    }

    private BigDecimal calculateUpMovement(BigDecimal volatility, BigDecimal T){


//        double up = Math.exp(volatility * Math.sqrt(T));
        BigDecimal multiplier = volatility.multiply(BigDecimalMath.sqrt(T, precision), precision);
        BigDecimal up = BigDecimalMath.exp(multiplier, precision);

        logger.info("Asset up movement is: " + up);
        return up;
    }

    private BigDecimal calculateDownMovement(BigDecimal volatility, BigDecimal T){
       // double down = Math.exp(- volatility * Math.sqrt(T));
        BigDecimal vol = BigDecimal.ZERO.subtract(volatility);
        BigDecimal multiplier = vol.multiply(BigDecimalMath.sqrt(T, precision), precision);
        BigDecimal down = BigDecimalMath.exp(multiplier, precision);

        logger.info("Asset down movement is: " + down);
        return down;
    }


    private void createAndSetProbability(AssetMovement assetMovement){
//        double numerator = Math.exp(assetMovement.interest * (assetMovement.T)) - assetMovement.down;
//        double denominator = assetMovement.up - assetMovement.down;
//        assetMovement.probability = numerator / denominator;
//
//        logger.info("Probability is: " + assetMovement.probability);
    }
}
