package sk.stu.fei.project.service;

import sk.stu.fei.project.domain.AssetTree;
import sk.stu.fei.project.domain.AssetMovement;
import sk.stu.fei.project.domain.Node;
import sk.stu.fei.project.service.utility.BigDecimalComparator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;

public class AssetBinaryTreeImpl implements AssetBinaryTreeService{
    java.util.logging.Logger logger =  java.util.logging.Logger.getLogger(this.getClass().getName());

    private List<Node> existingNodes;
    private BigDecimalComparator bigDecimalComparator;

    public AssetBinaryTreeImpl() {
        this.bigDecimalComparator = new BigDecimalComparator();
    }

    public boolean buildTree(AssetTree assetTree) {
        AssetMovement assetMovement = createAssetMovementModifiers(assetTree.getVolatility(), assetTree.getTimePeriod(), assetTree.getSteps());

        generateNodes(assetTree.getRoot(), assetTree.getSteps(), assetMovement);
        createProbability(assetMovement, assetTree);

        return true;
    }

    private AssetMovement createAssetMovementModifiers(double volatility, double timePeriod, int steps){
        AssetMovement assetMovement = new AssetMovement();
        assetMovement.up = calculateUpMovement(volatility, timePeriod, steps);
        assetMovement.down = calculateDownMovement(volatility, timePeriod, steps);

        return assetMovement;
    }

    private void createProbability(AssetMovement assetMovement, AssetTree assetTree){
        double T = assetTree.getTimePeriod() / assetTree.getSteps();
        double numerator = Math.exp(assetTree.getInterest() * (T)) - assetMovement.down;
        double denominator = assetMovement.up - assetMovement.down;
        double result = numerator / denominator;
        assetTree.setProbability(result);

        logger.info("Probability is: " + result);
    }

    private double calculateUpMovement(double volatility, double timePeriod, int steps){
        double up = Math.exp(volatility * Math.sqrt(timePeriod / steps));

        logger.info("Asset up movement is: " + up);
        return up;
    }

    private double calculateDownMovement(double volatility, double timePeriod, int steps){
        double down = Math.exp(- volatility * Math.sqrt(timePeriod / steps));

        logger.info("Asset down movement is: " + down);
        return down;
    }

    private void generateNodes(Node root, int treeMaxHeight, AssetMovement assetMovement){
        this.existingNodes = new ArrayList<Node>();
        createValueAndAddNodeRecursive(root,1, treeMaxHeight, assetMovement);
    }

    private void createValueAndAddNodeRecursive(Node current, int depth, int treeMaxHeight, AssetMovement assetMovement){
        BigDecimal nodeUpValue = current.value.multiply(BigDecimal.valueOf(assetMovement.up));
        BigDecimal nodeDownValue = current.value.multiply(BigDecimal.valueOf(assetMovement.down));

        Node nodeUp = addNode(current, nodeUpValue);
        Node nodeDown = addNode(current, nodeDownValue);

        if (depth != treeMaxHeight){
            createValueAndAddNodeRecursive(nodeUp,depth + 1, treeMaxHeight, assetMovement);
            createValueAndAddNodeRecursive(nodeDown, depth + 1, treeMaxHeight, assetMovement);
        }
    }

    private Node addNode(Node current, BigDecimal value){
        Node node = connectNodeIfValueExists(current, value);
        if (node != null){
            return node;
        }

        return addNodeRecursive(current, value);
    }

    private Node addNodeRecursive(Node current, BigDecimal value){

        if (current == null){
            Node newNode = new Node(value);
            this.existingNodes.add(newNode);
            return newNode;
        }

        if (bigDecimalComparator.isValueGreaterThanCurrent(value, current.value)) {
            current.left = addNodeRecursive(current.left, value);
            return current.left;

        } else if (bigDecimalComparator.isValueSmallerThanCurrent(value, current.value)) {
            current.right = addNodeRecursive(current.right, value);
            return current.right;
        }

        return current;
    }


    private Node connectNodeIfValueExists(Node current, BigDecimal value){

        for (Node node : this.existingNodes) {
            if (node.value.equals(value)) {
                if (bigDecimalComparator.isValueGreaterThanCurrent(value, current.value)) {
                    current.left = node;
                    return current.left;

                } else {
                    current.right = node;
                    return  current.right;
                }
            }
        }

        return null;
    }


    public Queue<BigDecimal> averagePriceLeafs(AssetTree assetTree){
        Queue<BigDecimal> queue = new LinkedList<BigDecimal>();
        recursivePathSum(assetTree.getRoot(), queue, new BigDecimal(0), assetTree.getSteps() + 1);
        return queue;
    }


    public void recursivePathSum(Node current, Queue<BigDecimal> queue, BigDecimal pathSum, int maxTreeHeight){
        if ((current.left == null) && (current.right == null)){
            pathSum = pathSum.add(current.value);
            BigDecimal leafValue = pathSum.divide(BigDecimal.valueOf(maxTreeHeight), 5, RoundingMode.HALF_UP);
            queue.add(leafValue);
            return;
        }
        pathSum = pathSum.add(current.value);

        recursivePathSum(current.left, queue, pathSum, maxTreeHeight);
        recursivePathSum(current.right, queue, pathSum, maxTreeHeight);

    }

}
