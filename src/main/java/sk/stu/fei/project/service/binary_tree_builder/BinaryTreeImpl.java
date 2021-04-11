package sk.stu.fei.project.service.binary_tree_builder;

import sk.stu.fei.project.domain.AssetMovement;
import sk.stu.fei.project.domain.Node;
import sk.stu.fei.project.domain.binary_tree.OptionBinaryTree;

import java.math.BigDecimal;

public abstract class BinaryTreeImpl implements BinaryTreeService {

    protected void generateEmptyBinaryTree(OptionBinaryTree optionBinaryTree, int maxDepth){
        createChildrenRecursive(optionBinaryTree.getRoot(), 1, maxDepth);
    }

    private void createChildrenRecursive(Node current, int depth, int maxDepth){
        current.left = new Node(new BigDecimal(0));
        current.right = new Node(new BigDecimal(0));

        if (depth != maxDepth){
            createChildrenRecursive(current.left, depth + 1, maxDepth);
            createChildrenRecursive(current.right, depth + 1, maxDepth);
        }
    }

    protected BigDecimal calculateNodeValue(Node current, AssetMovement assetMovement){
        BigDecimal nodeValueUp = current.left.value;
        BigDecimal nodeValueDown = current.right.value;
        BigDecimal probabilityUp = BigDecimal.valueOf(assetMovement.probability);
        BigDecimal probabilityDown = new BigDecimal(1).subtract(BigDecimal.valueOf(assetMovement.probability));

        BigDecimal firstPart = BigDecimal.valueOf(Math.exp(- assetMovement.interest * assetMovement.T));
        BigDecimal secondPart = nodeValueUp.multiply(probabilityUp);
        BigDecimal thirdPart = probabilityDown.multiply(nodeValueDown);
        BigDecimal result = firstPart.multiply((secondPart.add(thirdPart)));

        return result;
    }
}
