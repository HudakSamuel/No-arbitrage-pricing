package sk.stu.fei.project.service;

import lombok.NonNull;
import sk.stu.fei.project.domain.AssetTree;
import sk.stu.fei.project.domain.AssetMovement;
import sk.stu.fei.project.domain.Node;
import sk.stu.fei.project.service.utility.BigDecimalComparator;

import java.math.BigDecimal;
import java.util.*;
import java.util.List;

public class AssetBinaryTreeImpl implements AssetBinaryTreeService{

    private List<Node> existingNodes;
    private BigDecimalComparator bigDecimalComparator;

    public AssetBinaryTreeImpl() {
        this.bigDecimalComparator = new BigDecimalComparator();
    }

    public boolean buildTree(@NonNull AssetTree assetTree) {

        generateNodes(assetTree.getRoot(), assetTree.getAssetMovement());
        return true;
    }


    private void generateNodes(Node root, AssetMovement assetMovement){
        this.existingNodes = new ArrayList<Node>();
        createValueAndAddNodeRecursive(root,1, assetMovement);
    }

    private void createValueAndAddNodeRecursive(Node current, int depth, AssetMovement assetMovement){
        BigDecimal nodeUpValue = current.value.multiply(BigDecimal.valueOf(assetMovement.up));
        BigDecimal nodeDownValue = current.value.multiply(BigDecimal.valueOf(assetMovement.down));

        Node nodeUp = addNode(current, nodeUpValue);
        Node nodeDown = addNode(current, nodeDownValue);

        if (depth != assetMovement.steps){
            createValueAndAddNodeRecursive(nodeUp,depth + 1, assetMovement);
            createValueAndAddNodeRecursive(nodeDown, depth + 1, assetMovement);
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

}
