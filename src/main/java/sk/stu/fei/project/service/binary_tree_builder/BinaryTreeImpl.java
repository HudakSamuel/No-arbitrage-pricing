package sk.stu.fei.project.service.binary_tree_builder;

import ch.obermuhlner.math.big.BigDecimalMath;
import sk.stu.fei.project.domain.AssetMovement;
import sk.stu.fei.project.domain.Node;
import sk.stu.fei.project.domain.binary_tree.OptionBinaryTree;

import java.math.BigDecimal;
import java.math.MathContext;

public abstract class BinaryTreeImpl implements BinaryTreeService {
    MathContext precision = new MathContext(20);

    /**
     * Method generates empty tree by setting node values to 0
     * @param optionBinaryTree optionBinaryTree to generate values on
     * @param maxDepth height of the tree
     */
    protected void generateEmptyBinaryTree(OptionBinaryTree optionBinaryTree, int maxDepth){
        createChildrenRecursive(optionBinaryTree.getRoot(), 1, maxDepth);
    }

    /**
     * Method creates children nodes with value 0 by repeatedly calling itself
     * @param current current node to create children from
     * @param depth current depth in tree
     * @param maxDepth max available depth to traverse to
     */
    private void createChildrenRecursive(Node current, int depth, int maxDepth){
        current.left = new Node(new BigDecimal(0));
        current.right = new Node(new BigDecimal(0));

        if (depth != maxDepth){
            createChildrenRecursive(current.left, depth + 1, maxDepth);
            createChildrenRecursive(current.right, depth + 1, maxDepth);
        }
    }

    /**
     * Method calculates current node value based on risk-neutral probability and values of children
     * @param current node to calculate value for
     * @param assetMovement holds parameters to use for calculation
     * @return returns value of node
     */
    protected BigDecimal calculateNodeValue(Node current, AssetMovement assetMovement){
        BigDecimal nodeValueUp = current.left.value;
        BigDecimal nodeValueDown = current.right.value;
        BigDecimal probabilityUp = assetMovement.probability;
        BigDecimal probabilityDown = new BigDecimal(1).subtract(assetMovement.probability);

        BigDecimal firstPart = BigDecimalMath.exp(BigDecimal.ZERO.subtract(assetMovement.interest).multiply(assetMovement.T), precision);
        BigDecimal secondPart = nodeValueUp.multiply(probabilityUp);
        BigDecimal thirdPart = probabilityDown.multiply(nodeValueDown);
        BigDecimal result = firstPart.multiply((secondPart.add(thirdPart)));

        return result;
    }
}
