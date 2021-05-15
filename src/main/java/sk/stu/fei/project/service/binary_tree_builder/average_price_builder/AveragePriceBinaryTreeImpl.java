package sk.stu.fei.project.service.binary_tree_builder.average_price_builder;

import lombok.NonNull;
import sk.stu.fei.project.domain.AssetMovement;
import sk.stu.fei.project.domain.AssetTree;
import sk.stu.fei.project.domain.Node;
import sk.stu.fei.project.domain.binary_tree.average_price.CallAverageBinaryTree;
import sk.stu.fei.project.domain.binary_tree.average_price.PutAverageBinaryTree;
import sk.stu.fei.project.service.binary_tree_builder.BinaryTreeImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.Queue;

public class AveragePriceBinaryTreeImpl extends BinaryTreeImpl implements AveragePriceBinaryTreeService {

    /**
     * Method builds binary tree for call average price option
     * @param callAverageBinaryTree tree to build
     * @param assetTree underlying asset tree to use
     */
    public void buildCallAveragePriceBinaryTree(@NonNull CallAverageBinaryTree callAverageBinaryTree, @NonNull AssetTree assetTree){
        super.generateEmptyBinaryTree(callAverageBinaryTree, assetTree.getAssetMovement().steps);

        Queue<BigDecimal> queue = createAveragePathValues(assetTree);
        createCallNodeValuesRecursive(callAverageBinaryTree.getRoot(), queue, callAverageBinaryTree.getStrikePrice(), assetTree.getAssetMovement());
    }

    /**
     * Method builds binary tree for put average price option
     * @param putAverageBinaryTree tree to build
     * @param assetTree underlying asset tree to use
     */
    public void buildPutAveragePriceBinaryTree(@NonNull PutAverageBinaryTree putAverageBinaryTree, @NonNull AssetTree assetTree){
        super.generateEmptyBinaryTree(putAverageBinaryTree, assetTree.getAssetMovement().steps);

        Queue<BigDecimal> queue = createAveragePathValues(assetTree);
        createPutNodeValuesRecursive(putAverageBinaryTree.getRoot(), queue, putAverageBinaryTree.getStrikePrice(), assetTree.getAssetMovement());
    }


    /**
     * Method creates node values for call average price binary tree by reaching end of the tree and setting leaf values
     * and then calculating rest of the node values
     * @param current node to create value for
     * @param queue holds sums of average path value
     * @param strikePrice strike price of option
     * @param assetMovement parameters necessary to calculate node value
     */
    private void createCallNodeValuesRecursive(Node current, Queue<BigDecimal> queue, BigDecimal strikePrice, AssetMovement assetMovement){
        if ((current.left == null) && (current.right == null)){
            BigDecimal averagePathValue = queue.remove();
            BigDecimal leafValue = calculateCallLeafValue(averagePathValue, strikePrice);
            current.value = leafValue;

            return;
        }

        createCallNodeValuesRecursive(current.left, queue, strikePrice, assetMovement);
        createCallNodeValuesRecursive(current.right, queue, strikePrice, assetMovement);
        current.value = super.calculateNodeValue(current, assetMovement);

    }


    /**
     * Method calculates call average price option leaf value
     * @param averagePathValue sum of average path value to use in calculation
     * @param strikePrice strike price of the option
     * @return returns value of the leaf
     */
    private BigDecimal calculateCallLeafValue(BigDecimal averagePathValue, BigDecimal strikePrice){
        return new BigDecimal(0).max(averagePathValue.subtract(strikePrice));
    }


    /**
     * Method creates node values for put average price binary tree by reaching end of the tree and setting leaf values
     * and then calculating rest of the node values
     * @param current node to create value for
     * @param queue holds sums of average path value
     * @param strikePrice strike price of option
     * @param assetMovement parameters necessary to calculate node value
     */
    private void createPutNodeValuesRecursive(Node current, Queue<BigDecimal> queue, BigDecimal strikePrice, AssetMovement assetMovement){
        if ((current.left == null) && (current.right == null)){
            BigDecimal averagePathValue = queue.remove();
            BigDecimal leafValue = calculatePutLeafValue(averagePathValue, strikePrice);
            current.value = leafValue;

            return;
        }

        createPutNodeValuesRecursive(current.left, queue, strikePrice, assetMovement);
        createPutNodeValuesRecursive(current.right, queue, strikePrice, assetMovement);
        current.value = super.calculateNodeValue(current, assetMovement);
    }


    /**
     * Method calculates put average price option leaf value
     * @param averagePathValue sum of average path value to use in calculation
     * @param strikePrice strike price of the option
     * @return returns value of the leaf
     */
    private BigDecimal calculatePutLeafValue(BigDecimal averagePathValue, BigDecimal strikePrice){
        return new BigDecimal(0).max(strikePrice.subtract(averagePathValue));
    }


    /**
     * Method creates queue to use for collection and calls method to start collecting
     * @param assetTree underlying asset tree to use in collection
     * @return returns filled queue
     */
    private Queue<BigDecimal> createAveragePathValues(AssetTree assetTree){
        Queue<BigDecimal> queue = new LinkedList<BigDecimal>();
        calculatePathSumAndAddAverageValue(assetTree.getRoot(), queue, new BigDecimal(0), assetTree.getAssetMovement().steps);

        return queue;
    }


    /**
     * Method traverses underlying asset tree paths and creates average path values
     * @param current current node
     * @param queue stores average path values
     * @param pathSum sum of node values
     * @param maxDepth max depth to traverse to
     */
    private void calculatePathSumAndAddAverageValue(Node current, Queue<BigDecimal> queue, BigDecimal pathSum, int maxDepth){
        if ((current.left == null) && (current.right == null)){
            pathSum = pathSum.add(current.value);
            BigDecimal averageValue = calculateAverageValue(pathSum, maxDepth);
            queue.add(averageValue);

            return;
        }
        pathSum = pathSum.add(current.value);

        calculatePathSumAndAddAverageValue(current.left, queue, pathSum, maxDepth);
        calculatePathSumAndAddAverageValue(current.right, queue, pathSum, maxDepth);

    }

    /**
     * Method calculates average value of path
     * @param pathSum sum of node values
     * @param maxDepth tree max height
     * @return returns average path value
     */
    private BigDecimal calculateAverageValue(BigDecimal pathSum, int maxDepth){

        return pathSum.divide(BigDecimal.valueOf(maxDepth + 1), 5, RoundingMode.HALF_UP);
    }


}
