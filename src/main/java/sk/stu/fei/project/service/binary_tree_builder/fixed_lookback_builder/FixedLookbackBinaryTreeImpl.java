package sk.stu.fei.project.service.binary_tree_builder.fixed_lookback_builder;

import lombok.NonNull;
import sk.stu.fei.project.domain.AssetMovement;
import sk.stu.fei.project.domain.AssetTree;
import sk.stu.fei.project.domain.Node;
import sk.stu.fei.project.domain.binary_tree.lookback.fixed_lookback.CallFixedLookbackBinaryTree;
import sk.stu.fei.project.domain.binary_tree.lookback.fixed_lookback.PutFixedLookbackBinaryTree;
import sk.stu.fei.project.service.binary_tree_builder.BinaryTreeImpl;
import sk.stu.fei.project.service.utility.BigDecimalComparator;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Queue;

public class FixedLookbackBinaryTreeImpl extends BinaryTreeImpl implements FixedLookbackBinaryTreeService {

    private BigDecimalComparator bigDecimalComparator;

    public FixedLookbackBinaryTreeImpl(){
        bigDecimalComparator = new BigDecimalComparator();
    }

    public void buildCallFixedLookbackBinaryTree(@NonNull CallFixedLookbackBinaryTree callFixedLookbackBinaryTree, @NonNull AssetTree assetTree){
        super.generateEmptyBinaryTree(callFixedLookbackBinaryTree, assetTree.getAssetMovement().steps);

        Queue<BigDecimal> queue = createCallLeafValues(assetTree, callFixedLookbackBinaryTree.getStrikePrice());
        createNodeValuesRecursive(callFixedLookbackBinaryTree.getRoot(), queue, assetTree.getAssetMovement());
    }

    public void buildPutFixedLookbackBinaryTree(@NonNull PutFixedLookbackBinaryTree putFixedLookbackBinaryTree, @NonNull AssetTree assetTree){
        super.generateEmptyBinaryTree(putFixedLookbackBinaryTree, assetTree.getAssetMovement().steps);

        Queue<BigDecimal> queue = createPutLeafValues(assetTree, putFixedLookbackBinaryTree.getStrikePrice());
        createNodeValuesRecursive(putFixedLookbackBinaryTree.getRoot(), queue, assetTree.getAssetMovement());
    }

    private Queue<BigDecimal> createCallLeafValues(AssetTree assetTree, BigDecimal strikePrice){
        Queue<BigDecimal> queue = new LinkedList<BigDecimal>();
        addCallLeafValues(assetTree.getRoot(), queue, assetTree.getRoot().value, strikePrice);

        return queue;
    }

    private void addCallLeafValues(Node current, Queue<BigDecimal> queue, BigDecimal maxValue, BigDecimal strikePrice){
        if (bigDecimalComparator.isValueGreaterThanCurrent(current.value, maxValue)){
            maxValue = current.value;
        }

        if ((current.left == null) && (current.right == null)){


            BigDecimal subtracted = maxValue.subtract(strikePrice);
            BigDecimal optionLeafValue =  new BigDecimal(0).max(subtracted);
            queue.add(optionLeafValue);

            return;
        }


        addCallLeafValues(current.left, queue, maxValue, strikePrice);
        addCallLeafValues(current.right, queue, maxValue, strikePrice);

    }

    private void createNodeValuesRecursive(Node current, Queue<BigDecimal> queue, AssetMovement assetMovement){
        if ((current.left == null) && (current.right == null)){
            BigDecimal leafValue = queue.remove();
            current.value = leafValue;

            return;
        }

        createNodeValuesRecursive(current.left, queue, assetMovement);
        createNodeValuesRecursive(current.right, queue, assetMovement);
        current.value = super.calculateNodeValue(current, assetMovement);
    }


    private Queue<BigDecimal> createPutLeafValues(AssetTree assetTree, BigDecimal strikePrice){
        Queue<BigDecimal> queue = new LinkedList<BigDecimal>();
        addPutLeafValues(assetTree.getRoot(), queue, assetTree.getRoot().value, strikePrice);

        return queue;
    }

    private void addPutLeafValues(Node current, Queue<BigDecimal> queue, BigDecimal minValue, BigDecimal strikePrice){
        if (bigDecimalComparator.isValueSmallerThanCurrent(current.value, minValue)){
            minValue = current.value;
        }

        if ((current.left == null) && (current.right == null)){


            BigDecimal subtracted = strikePrice.subtract(minValue);
            BigDecimal optionLeafValue =  new BigDecimal(0).max(subtracted);
            queue.add(optionLeafValue);

            return;
        }


        addPutLeafValues(current.left, queue, minValue, strikePrice);
        addPutLeafValues(current.right, queue, minValue, strikePrice);

    }

}

