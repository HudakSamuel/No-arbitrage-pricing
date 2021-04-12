package sk.stu.fei.project.service.binary_tree_builder.floating_lookback_builder;

import lombok.NonNull;
import sk.stu.fei.project.domain.AssetMovement;
import sk.stu.fei.project.domain.AssetTree;
import sk.stu.fei.project.domain.Node;
import sk.stu.fei.project.domain.binary_tree.average_price.CallAverageBinaryTree;
import sk.stu.fei.project.domain.binary_tree.lookback.floating_lookback.CallFloatingLookbackBinaryTree;
import sk.stu.fei.project.domain.binary_tree.lookback.floating_lookback.FloatingLookbackBinaryTree;
import sk.stu.fei.project.domain.binary_tree.lookback.floating_lookback.PutFloatingLookbackBinaryTree;
import sk.stu.fei.project.service.binary_tree_builder.BinaryTreeImpl;
import sk.stu.fei.project.service.utility.BigDecimalComparator;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Queue;

public class FloatingLookbackBinaryTreeImpl extends BinaryTreeImpl implements FloatingLookbackBinaryTreeService{
    private BigDecimalComparator bigDecimalComparator;

    public FloatingLookbackBinaryTreeImpl(){
        bigDecimalComparator = new BigDecimalComparator();
    }

    public void buildCallFloatingLookbackBinaryTree(@NonNull CallFloatingLookbackBinaryTree callFloatingLookbackBinaryTree, @NonNull AssetTree assetTree){
        super.generateEmptyBinaryTree(callFloatingLookbackBinaryTree, assetTree.getAssetMovement().steps);

        Queue<BigDecimal> queue = createCallLeafValues(assetTree);
        createNodeValuesRecursive(callFloatingLookbackBinaryTree.getRoot(), queue, assetTree.getAssetMovement());
    }

    public void buildPutFloatingLookbackBinaryTree(@NonNull PutFloatingLookbackBinaryTree putFloatingLookbackBinaryTree, @NonNull AssetTree assetTree){
        super.generateEmptyBinaryTree(putFloatingLookbackBinaryTree, assetTree.getAssetMovement().steps);

        Queue<BigDecimal> queue = createPutLeafValues(assetTree);
        createNodeValuesRecursive(putFloatingLookbackBinaryTree.getRoot(), queue, assetTree.getAssetMovement());
    }

    private Queue<BigDecimal> createCallLeafValues(AssetTree assetTree){
        Queue<BigDecimal> queue = new LinkedList<BigDecimal>();
        addCallLeafValues(assetTree.getRoot(), queue, assetTree.getRoot().value);

        return queue;
    }

    private void addCallLeafValues(Node current, Queue<BigDecimal> queue, BigDecimal minValue){
        if ((current.left == null) && (current.right == null)){

            BigDecimal subtracted = current.value.subtract(minValue);
            BigDecimal optionLeafValue =  new BigDecimal(0).max(subtracted);
            queue.add(optionLeafValue);

            return;
        }

        if (bigDecimalComparator.isValueSmallerThanCurrent(current.value, minValue)){
            minValue = current.value;
        }

        addCallLeafValues(current.left, queue, minValue);
        addCallLeafValues(current.right, queue, minValue);

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


    private Queue<BigDecimal> createPutLeafValues(AssetTree assetTree){
        Queue<BigDecimal> queue = new LinkedList<BigDecimal>();
        addPutLeafValues(assetTree.getRoot(), queue, assetTree.getRoot().value);

        return queue;
    }

    private void addPutLeafValues(Node current, Queue<BigDecimal> queue, BigDecimal maxValue){
        if ((current.left == null) && (current.right == null)){

            BigDecimal subtracted = maxValue.subtract(current.value);
            BigDecimal optionLeafValue =  new BigDecimal(0).max(subtracted);
            queue.add(optionLeafValue);

            return;
        }

        if (bigDecimalComparator.isValueGreaterThanCurrent(current.value, maxValue)){
            maxValue = current.value;
        }

        addPutLeafValues(current.left, queue, maxValue);
        addPutLeafValues(current.right, queue, maxValue);

    }

}
