package sk.stu.fei.project;

import sk.stu.fei.project.domain.AssetTree;
import sk.stu.fei.project.domain.binary_tree.average_price.AveragePriceBinaryTree;
import sk.stu.fei.project.domain.binary_tree.average_price.CallAverageBinaryTree;
import sk.stu.fei.project.service.AssetBinaryTreeImpl;
import sk.stu.fei.project.service.AssetBinaryTreeService;
import sk.stu.fei.project.service.tree_printer.AssetTreePrinter;
import sk.stu.fei.project.service.tree_printer.OptionTreePrinter;

import java.io.IOException;
import java.math.BigDecimal;

public class Main{
    public static void main(String[] args) throws IOException {
        AssetTree assetTree = new AssetTree(0.1, new BigDecimal(110),3, 3, 0.1);
        AssetBinaryTreeService assetBinaryTreeService = new AssetBinaryTreeImpl();
        assetBinaryTreeService.buildTree(assetTree);

        AssetTreePrinter<AssetTree> assetTreePrinter = new AssetTreePrinter<AssetTree>();
        assetTreePrinter.print(assetTree);

        AveragePriceBinaryTree averagePriceBinaryTree = new CallAverageBinaryTree(assetTree, new BigDecimal(110));
        OptionTreePrinter optionTreePrinter = new OptionTreePrinter();

        averagePriceBinaryTree.buildAverageBinaryTree();
        optionTreePrinter.print(averagePriceBinaryTree);
        System.out.println(averagePriceBinaryTree.getRoot().value);

    }
}
