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

    public void buildCallAveragePriceBinaryTree(@NonNull CallAverageBinaryTree callAverageBinaryTree, @NonNull AssetTree assetTree){
        super.generateEmptyBinaryTree(callAverageBinaryTree, assetTree.getAssetMovement().steps);

        Queue<BigDecimal> queue = createAveragePathValues(assetTree);
        createCallNodeValuesRecursive(callAverageBinaryTree.getRoot(), queue, callAverageBinaryTree.getStrikePrice(), assetTree.getAssetMovement());
    }

    public void buildPutAveragePriceBinaryTree(@NonNull PutAverageBinaryTree putAverageBinaryTree, @NonNull AssetTree assetTree){
        super.generateEmptyBinaryTree(putAverageBinaryTree, assetTree.getAssetMovement().steps);

        Queue<BigDecimal> queue = createAveragePathValues(assetTree);
        createPutNodeValuesRecursive(putAverageBinaryTree.getRoot(), queue, putAverageBinaryTree.getStrikePrice(), assetTree.getAssetMovement());
    }

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


    private BigDecimal calculateCallLeafValue(BigDecimal averagePathValue, BigDecimal strikePrice){
        return new BigDecimal(0).max(averagePathValue.subtract(strikePrice));
    }


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


    private BigDecimal calculatePutLeafValue(BigDecimal averagePathValue, BigDecimal strikePrice){
        return new BigDecimal(0).max(strikePrice.subtract(averagePathValue));
    }



    private Queue<BigDecimal> createAveragePathValues(AssetTree assetTree){
        Queue<BigDecimal> queue = new LinkedList<BigDecimal>();
        calculatePathSumAndAddAverageValue(assetTree.getRoot(), queue, new BigDecimal(0), assetTree.getAssetMovement().steps);

        return queue;
    }


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

    private BigDecimal calculateAverageValue(BigDecimal pathSum, int maxDepth){

        return pathSum.divide(BigDecimal.valueOf(maxDepth + 1), 5, RoundingMode.HALF_UP);
    }


}
