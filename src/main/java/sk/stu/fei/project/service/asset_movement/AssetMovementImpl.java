package sk.stu.fei.project.service.asset_movement;

import ch.obermuhlner.math.big.BigDecimalMath;
import lombok.NonNull;
import sk.stu.fei.project.domain.AssetMovement;
import sk.stu.fei.project.service.utility.BigDecimalComparator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class AssetMovementImpl implements AssetMovementService{
    MathContext precision = new MathContext(20);
    java.util.logging.Logger logger =  java.util.logging.Logger.getLogger(this.getClass().getName());
    BigDecimalComparator bigDecimalComparator = new BigDecimalComparator();

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

        BigDecimal vol = BigDecimal.ZERO.subtract(assetMovement.volatility);
        boolean firstPart = bigDecimalComparator.isValueSmallerThanCurrent(vol, assetMovement.interest.multiply(BigDecimalMath.sqrt(assetMovement.T, precision)));
        boolean secondPart = bigDecimalComparator.isValueSmallerThanCurrent(assetMovement.interest.multiply(BigDecimalMath.sqrt(assetMovement.T, precision)), assetMovement.volatility);

        logger.info(String.valueOf(firstPart));
        logger.info(String.valueOf(secondPart));
        return firstPart && secondPart;
    }

    private void createAndSetT(AssetMovement assetMovement){
        assetMovement.T = assetMovement.timePeriod.divide(BigDecimal.valueOf(assetMovement.steps), precision);
    }

    private void createAndSetUpAndDownModifiers(AssetMovement assetMovement){
        assetMovement.up = calculateUpMovement(assetMovement.volatility, assetMovement.T);
        assetMovement.down = calculateDownMovement(assetMovement.volatility, assetMovement.T);
    }

    private BigDecimal calculateUpMovement(BigDecimal volatility, BigDecimal T){

        BigDecimal multiplier = volatility.multiply(BigDecimalMath.sqrt(T, precision), precision);
        BigDecimal up = BigDecimalMath.exp(multiplier, precision);

        logger.info("Asset up movement is: " + up);
        return up;
    }

    private BigDecimal calculateDownMovement(BigDecimal volatility, BigDecimal T){
        BigDecimal vol = BigDecimal.ZERO.subtract(volatility);
        BigDecimal multiplier = vol.multiply(BigDecimalMath.sqrt(T, precision), precision);
        BigDecimal down = BigDecimalMath.exp(multiplier, precision);

        logger.info("Asset down movement is: " + down);
        return down;
    }


    private void createAndSetProbability(AssetMovement assetMovement){

        BigDecimal numerator1 = BigDecimalMath.exp(assetMovement.interest.multiply(assetMovement.T), precision);
        BigDecimal numerator2 = numerator1.subtract(assetMovement.down);
        BigDecimal denominator = assetMovement.up.subtract(assetMovement.down);

        assetMovement.probability = numerator2.divide(denominator, precision);
        logger.info("Probability is: " + assetMovement.probability);
    }
}
