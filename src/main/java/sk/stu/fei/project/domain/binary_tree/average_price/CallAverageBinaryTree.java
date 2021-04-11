package sk.stu.fei.project.domain.binary_tree.average_price;

import sk.stu.fei.project.domain.AssetTree;
import sk.stu.fei.project.domain.Node;

import java.math.BigDecimal;
import java.util.Queue;

public class CallAverageBinaryTree extends AveragePriceBinaryTree {

    public CallAverageBinaryTree(AssetTree assetTree, BigDecimal strikePrice) {
        super(assetTree, strikePrice);
    }


    @Override
    protected void createValuesRecursive(Node current, Queue<BigDecimal> queue){
        if ((current.left == null) && (current.right == null)){
            BigDecimal averagePathValue = queue.remove();
            BigDecimal leafValue = new BigDecimal(0).max(averagePathValue.subtract(strikePrice));
            current.value = leafValue;

            return;
        }

        createValuesRecursive(current.left, queue);
        createValuesRecursive(current.right, queue);
        double T = assetTree.getTimePeriod() / assetTree.getSteps();
        double multiplier =  Math.exp(- assetTree.getInterest() * T);
        BigDecimal multi = BigDecimal.valueOf(multiplier);
        BigDecimal prob = BigDecimal.valueOf(assetTree.getProbability());
        BigDecimal first = current.left.value.multiply(prob);
        BigDecimal second = new BigDecimal(1).subtract(prob);
        BigDecimal third = second.multiply(current.right.value);
        BigDecimal b = first.add(third);
        current.value = multi.multiply(b);

    }

}
