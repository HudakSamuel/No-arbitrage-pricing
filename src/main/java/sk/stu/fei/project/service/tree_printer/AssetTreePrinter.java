package sk.stu.fei.project.service.tree_printer;

import sk.stu.fei.project.domain.AssetTree;
import sk.stu.fei.project.domain.Node;
import sk.stu.fei.project.service.utility.BigDecimalComparator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashSet;

public class AssetTreePrinter<T extends AssetTree> extends TreePrinter{

    private BigDecimalComparator bigDecimalComparator;
    private final int nSPACES = 100;
    private int spaceBetweenRoadJunction = 3;
    private int spaceAfterRoadJunction = 2;

    public AssetTreePrinter() {
        this.bigDecimalComparator = new BigDecimalComparator();
    }

    public void print(AssetTree assetTree){
        LinkedHashSet<Node> nodesToPrint = new LinkedHashSet<Node>();
        nodesToPrint.add(assetTree.getRoot());
        //determineSpaceAfterRoadJunction(assetTree.getRoot().value);

        printNodesRecursive(nodesToPrint, nSPACES);
    }

    private void printNodesRecursive(LinkedHashSet<Node> nodesToPrint, int spaces){
        if (nodesToPrint.size() == 0){
            return;
        }

        printNodeRow(nodesToPrint, spaces);

        LinkedHashSet<Node> newNodesToPrint = getChildrenNodes(nodesToPrint);
        nodesToPrint.clear();

        printNodesRecursive(newNodesToPrint, spaces - 3);
    }

    private void printNodeRow(LinkedHashSet<Node> nodesToPrint, int spaces){

        if (nodesToPrint.size() == 1){
            printEmptySpace(spaces);

        } else{
            printEmptySpace(spaces - 1);
        }
        printNodeValues(nodesToPrint);
        System.out.println();

        printEmptySpace(spaces - 1);

        if(!checkIfLastChildren(nodesToPrint)){
            printRoadJunctions(nodesToPrint.size());
        }
        System.out.println();
    }

    private LinkedHashSet<Node> getChildrenNodes(LinkedHashSet<Node> nodesToPrint){
        LinkedHashSet<Node> newNodesToPrint = new LinkedHashSet<Node>();

        for(Node child : nodesToPrint){
            addChild(child, newNodesToPrint);
        }

        return newNodesToPrint;
    }

    private void addChild(Node node, LinkedHashSet<Node> nodesToPrint){
        if (node.right != null){
            nodesToPrint.add(node.right);
        }

        if (node.left != null){
            nodesToPrint.add(node.left);
        }

    }

    private void printEmptySpace(int spaces){
        for(int i = 0; i < spaces; i++) {
            System.out.print(" ");
        }
    }

    private void printNodeValues(LinkedHashSet<Node> nodesToPrint){

        for(Node child : nodesToPrint){
            BigDecimal value = child.value.setScale(2, RoundingMode.HALF_EVEN);
            System.out.print(value + "  ");
        }

    }

    private void printRoadJunctions(int numberOfNodes) {
        for (int i = 0; i < numberOfNodes; i++) {

            System.out.print("/");
            printEmptySpace(spaceBetweenRoadJunction);
            System.out.print("\\");
            printEmptySpace(spaceAfterRoadJunction);
        }
    }


    private boolean checkIfLastChildren(LinkedHashSet<Node> nodesToPrint){
        Node first = nodesToPrint.stream().findFirst().get();

        if (first.left == null && first.right == null){
            return true;
        }

        return false;
    }


//    private void determineSpaceAfterRoadJunction(BigDecimal rootValue){
//        if (bigDecimalComparator.isValueGreaterThanCurrent(rootValue, new BigDecimal(1000))){
//            spaceAfterRoadJunction = 5;
//            spaceBetweenRoadJunction = 2;
//        }
//
//        else if (bigDecimalComparator.isValueGreaterThanCurrent(rootValue, new BigDecimal(100))){
//            spaceAfterRoadJunction = 3;
//        }
//
//    }

}
