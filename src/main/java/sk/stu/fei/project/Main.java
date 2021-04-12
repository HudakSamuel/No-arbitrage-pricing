package sk.stu.fei.project;

import jdk.nashorn.internal.codegen.CompilerConstants;
import sk.stu.fei.project.domain.AssetMovement;
import sk.stu.fei.project.domain.AssetTree;
import sk.stu.fei.project.domain.binary_tree.average_price.AveragePriceBinaryTree;
import sk.stu.fei.project.domain.binary_tree.average_price.CallAverageBinaryTree;
import sk.stu.fei.project.domain.binary_tree.average_price.PutAverageBinaryTree;
import sk.stu.fei.project.domain.binary_tree.lookback.floating_lookback.CallFloatingLookbackBinaryTree;
import sk.stu.fei.project.domain.binary_tree.lookback.floating_lookback.FloatingLookbackBinaryTree;
import sk.stu.fei.project.domain.binary_tree.lookback.floating_lookback.PutFloatingLookbackBinaryTree;
import sk.stu.fei.project.service.AssetBinaryTreeImpl;
import sk.stu.fei.project.service.AssetBinaryTreeService;
import sk.stu.fei.project.service.asset_movement.AssetMovementImpl;
import sk.stu.fei.project.service.asset_movement.AssetMovementService;
import sk.stu.fei.project.service.binary_tree_builder.average_price_builder.AveragePriceBinaryTreeImpl;
import sk.stu.fei.project.service.binary_tree_builder.average_price_builder.AveragePriceBinaryTreeService;
import sk.stu.fei.project.service.binary_tree_builder.floating_lookback_builder.FloatingLookbackBinaryTreeImpl;
import sk.stu.fei.project.service.binary_tree_builder.floating_lookback_builder.FloatingLookbackBinaryTreeService;
import sk.stu.fei.project.service.tree_printer.AssetTreePrinter;
import sk.stu.fei.project.service.tree_printer.OptionTreePrinter;


import java.io.IOException;
import java.math.BigDecimal;

public class Main{
    public static void main(String[] args) throws IOException {
        AssetMovementService assetMovementService = new AssetMovementImpl();
        AssetBinaryTreeService assetBinaryTreeService = new AssetBinaryTreeImpl();
        AveragePriceBinaryTreeService averagePriceBinaryTreeService = new AveragePriceBinaryTreeImpl();
        AssetTreePrinter<AssetTree> assetTreePrinter = new AssetTreePrinter<AssetTree>();
        OptionTreePrinter optionTreePrinter = new OptionTreePrinter();
        FloatingLookbackBinaryTreeService floatingLookbackBinaryTreeService = new FloatingLookbackBinaryTreeImpl();

        AssetMovement assetMovement = new AssetMovement(0.1, 3, 0.01, 3);

        if (assetMovementService.initAssetMovementParameters(assetMovement)){
            AssetTree assetTree = new AssetTree(new BigDecimal(110), assetMovement);
            assetBinaryTreeService.buildTree(assetTree);


            assetTreePrinter.print(assetTree);

            // FLOATING LOOKBACK //
            CallFloatingLookbackBinaryTree callFloatingLookbackBinaryTree = new CallFloatingLookbackBinaryTree();
            PutFloatingLookbackBinaryTree putFloatingLookbackBinaryTree = new PutFloatingLookbackBinaryTree();

            floatingLookbackBinaryTreeService.buildCallFloatingLookbackBinaryTree(callFloatingLookbackBinaryTree, assetTree);
            floatingLookbackBinaryTreeService.buildPutFloatingLookbackBinaryTree(putFloatingLookbackBinaryTree, assetTree);

            optionTreePrinter.print(callFloatingLookbackBinaryTree);
            System.out.println();
            optionTreePrinter.print(putFloatingLookbackBinaryTree);


            // AVERAGE LOOKBACK //
//            CallAverageBinaryTree callAverageBinaryTree = new CallAverageBinaryTree(new BigDecimal(110));
//            PutAverageBinaryTree putAverageBinaryTree = new PutAverageBinaryTree(new BigDecimal(110));
//
//            averagePriceBinaryTreeService.buildCallAveragePriceBinaryTree(callAverageBinaryTree, assetTree);
//            averagePriceBinaryTreeService.buildPutAveragePriceBinaryTree(putAverageBinaryTree, assetTree);
//
//            optionTreePrinter.print(callAverageBinaryTree);
//            System.out.println();
//            optionTreePrinter.print(putAverageBinaryTree);
        }


    }
}
