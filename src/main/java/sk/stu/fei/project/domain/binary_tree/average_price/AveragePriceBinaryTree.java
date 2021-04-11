package sk.stu.fei.project.domain.binary_tree.average_price;

import lombok.Getter;
import sk.stu.fei.project.domain.AssetTree;
import sk.stu.fei.project.domain.Node;
import sk.stu.fei.project.domain.binary_tree.OptionBinaryTree;
import sk.stu.fei.project.service.AssetBinaryTreeImpl;
import sk.stu.fei.project.service.AssetBinaryTreeService;


import java.math.BigDecimal;
import java.util.Queue;

@Getter
public abstract class AveragePriceBinaryTree extends OptionBinaryTree {

    AssetBinaryTreeService assetBinaryTreeService = new AssetBinaryTreeImpl();
    protected BigDecimal strikePrice;

    AveragePriceBinaryTree(AssetTree assetTree, BigDecimal strikePrice) {
        super(assetTree);
        this.strikePrice = strikePrice;
    }

    public void buildAverageBinaryTree(){
        Queue<BigDecimal> queue = assetBinaryTreeService.averagePriceLeafs(assetTree);
        createValuesRecursive(root, queue);
    }

    abstract protected void createValuesRecursive(Node current, Queue<BigDecimal> queue);
}
