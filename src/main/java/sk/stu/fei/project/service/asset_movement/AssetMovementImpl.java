package sk.stu.fei.project.service.asset_movement;

import ch.obermuhlner.math.big.BigDecimalMath;
import lombok.NonNull;
import sk.stu.fei.project.domain.AssetMovement;
import sk.stu.fei.project.service.utility.BigDecimalComparator;

import java.math.BigDecimal;
import java.math.MathContext;

public class AssetMovementImpl implements AssetMovementService{
    MathContext precision = new MathContext(20);
    java.util.logging.Logger logger =  java.util.logging.Logger.getLogger(this.getClass().getName());
    BigDecimalComparator bigDecimalComparator = new BigDecimalComparator();


    /**
     * Method checks if no-arbitrage condition is met and initializes asset movement parameters necessary to create underlying asset tree
     * @param assetMovement assetMovement to set
     * @return true on successful initialization, false otherwise
     */
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

    /**
     * Method checks if no-arbitrage condition is met
     * @param assetMovement holds parameters to use in comparison
     * @return true if met, otherwise false
     */
    private boolean isArbitrageConditionMet(AssetMovement assetMovement){

        BigDecimal vol = BigDecimal.ZERO.subtract(assetMovement.volatility);
        boolean firstPart = bigDecimalComparator.isValueSmallerThanCurrent(vol, assetMovement.interest.multiply(BigDecimalMath.sqrt(assetMovement.T, precision)));
        boolean secondPart = bigDecimalComparator.isValueSmallerThanCurrent(assetMovement.interest.multiply(BigDecimalMath.sqrt(assetMovement.T, precision)), assetMovement.volatility);

        return firstPart && secondPart;
    }

    /**
     * Method calculates and sets assetMovement.T by dividing total time and number of steps
     * @param assetMovement holds parameters to use in calculation
     */
    private void createAndSetT(AssetMovement assetMovement){
        assetMovement.T = assetMovement.timePeriod.divide(BigDecimal.valueOf(assetMovement.steps), precision);
    }

    /**
     * Method calls private methods to set assetMovement.up and assetMovement.down
     * @param assetMovement holds parameters to use in calculation
     */
    private void createAndSetUpAndDownModifiers(AssetMovement assetMovement){
        assetMovement.up = calculateUpMovement(assetMovement.volatility, assetMovement.T);
        assetMovement.down = calculateDownMovement(assetMovement.volatility, assetMovement.T);
    }

    /**
     * Method calculates value of assetMovement.up
     * @param volatility volatility to base calculation on
     * @param T T also used in calculation
     * @return value of assetMovement.up
     */
    private BigDecimal calculateUpMovement(BigDecimal volatility, BigDecimal T){

        BigDecimal multiplier = volatility.multiply(BigDecimalMath.sqrt(T, precision), precision);
        BigDecimal up = BigDecimalMath.exp(multiplier, precision);

        logger.info("Asset up movement is: " + up);
        return up;
    }

    /**
     * Method calculates value of assetMovement.down
     * @param volatility volatility to base calculation on
     * @param T T also used in calculation
     * @return value of assetMovement.down
     */
    private BigDecimal calculateDownMovement(BigDecimal volatility, BigDecimal T){
        BigDecimal vol = BigDecimal.ZERO.subtract(volatility);
        BigDecimal multiplier = vol.multiply(BigDecimalMath.sqrt(T, precision), precision);
        BigDecimal down = BigDecimalMath.exp(multiplier, precision);

        logger.info("Asset down movement is: " + down);
        return down;
    }


    /**
     * Method creates and sets risk-neutral probability
     * @param assetMovement holds parameters to use in calculation
     */
    private void createAndSetProbability(AssetMovement assetMovement){

        BigDecimal numerator1 = BigDecimalMath.exp(assetMovement.interest.multiply(assetMovement.T), precision);
        BigDecimal numerator2 = numerator1.subtract(assetMovement.down);
        BigDecimal denominator = assetMovement.up.subtract(assetMovement.down);

        assetMovement.probability = numerator2.divide(denominator, precision);
        logger.info("Probability is: " + assetMovement.probability);
    }
}
