package sk.stu.fei.project.service.tree_printer;

import sk.stu.fei.project.domain.AssetTree;
import sk.stu.fei.project.domain.Node;
import sk.stu.fei.project.service.utility.BigDecimalComparator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class AssetTreePrinter<T extends AssetTree> extends TreePrinter{

    private BigDecimalComparator bigDecimalComparator;
    private final int nSPACES = 100;

    public AssetTreePrinter() {
        this.bigDecimalComparator = new BigDecimalComparator();
    }

    /**
     * Method prints binary tree to console
     * @param assetTree assetTree to print
     */
    public void print(AssetTree assetTree){
        LinkedHashSet<Node> nodesToPrint = new LinkedHashSet<Node>();
        nodesToPrint.add(assetTree.getRoot());

        printNodesRecursive(nodesToPrint, nSPACES);
    }

    /**
     * Method prints binary tree rows one by one
     * @param nodesToPrint contains nodes with their respective value
     * @param spaces number of spaces before first node value
     */
    private void printNodesRecursive(LinkedHashSet<Node> nodesToPrint, int spaces){
        if (nodesToPrint.size() == 0){
            return;
        }

        printNodeRow(nodesToPrint, spaces);

        LinkedHashSet<Node> newNodesToPrint = getChildrenNodes(nodesToPrint);
        nodesToPrint.clear();

        printNodesRecursive(newNodesToPrint, spaces - 3);
    }

    /**
     * Method prints one row of binary tree values including road junctions
     * @param nodesToPrint contains nodes with their respective value
     * @param spaces number of spaces before first node value
     */
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
            printRoadJunctions(nodesToPrint);
        }
        System.out.println();
    }

    /**
     * Method collects children nodes from parents
     * @param nodesToPrint parents to collect children from
     * @return returns filled set with children
     */
    private LinkedHashSet<Node> getChildrenNodes(LinkedHashSet<Node> nodesToPrint){
        LinkedHashSet<Node> newNodesToPrint = new LinkedHashSet<Node>();

        for(Node child : nodesToPrint){
            addChild(child, newNodesToPrint);
        }

        return newNodesToPrint;
    }

    /**
     * Method adds child to set
     * @param node parent node
     * @param nodesToPrint set to collect child
     */
    private void addChild(Node node, LinkedHashSet<Node> nodesToPrint){
        if (node.right != null){
            nodesToPrint.add(node.right);
        }

        if (node.left != null){
            nodesToPrint.add(node.left);
        }

    }

    /**
     * Method prints empty space to console
     * @param spaces number of spaces to print
     */
    private void printEmptySpace(int spaces){
        for(int i = 0; i < spaces; i++) {
            System.out.print(" ");
        }
    }

    /**
     * Method prints value of nodes to console
     * @param nodesToPrint set to print values from
     */
    private void printNodeValues(LinkedHashSet<Node> nodesToPrint){

        for(Node child : nodesToPrint){
            BigDecimal value = child.value.setScale(2, RoundingMode.HALF_EVEN);
            System.out.print(value + "  ");
        }

    }

    /**
     * Method prints road junctions to one row
     * @param nodesToPrint used to determine space after and between road junctions
     */
    private void printRoadJunctions(LinkedHashSet<Node> nodesToPrint) {
        ArrayList<Integer> roadJunctions = new ArrayList<>();
        ArrayList<Integer> spaceBetween = new ArrayList<>();
        for(Node child : nodesToPrint){
            this.determineSpaceAfterRoadJunction(child.value, roadJunctions, spaceBetween);
        }

        for (int i = 0; i < roadJunctions.size(); i++){
            System.out.print("/");
            printEmptySpace(spaceBetween.get(i));
            System.out.print("\\");
            printEmptySpace(roadJunctions.get(i));
        }

    }


    /**
     * Method checks if parent does not have any children
     * @param nodesToPrint nodes to check for children
     * @return returns true if parent does not have children, else false
     */
    private boolean checkIfLastChildren(LinkedHashSet<Node> nodesToPrint){
        Node first = nodesToPrint.stream().findFirst().get();

        if (first.left == null && first.right == null){
            return true;
        }

        return false;
    }


    /**
     * Method calculates space between and after road junctions based on node value size
     * Adds this space to array
     * @param nodeValue road junctions for this node
     * @param roadJunctions space after road junctions for each node
     * @param spaceBetween space between road junction for each node
     */
    private void determineSpaceAfterRoadJunction(BigDecimal nodeValue, ArrayList<Integer> roadJunctions, ArrayList<Integer> spaceBetween){
        int between;
        int spaceAfter;
        if (bigDecimalComparator.isValueSmallerThanCurrent(nodeValue, BigDecimal.TEN)){
            between = 1;
            spaceAfter = 3;

        }else{
            between = 3;
            spaceAfter = 0;
            BigDecimal value = BigDecimal.ONE;
            while(bigDecimalComparator.isValueSmallerThanCurrent(value, nodeValue)){
                value = value.multiply(BigDecimal.TEN);
                spaceAfter++;

            }

            if (bigDecimalComparator.isValueGreaterThanCurrent(nodeValue, new BigDecimal(10000))) {
                between = 4;
                spaceAfter--;
            }
        }

        roadJunctions.add(spaceAfter);
        spaceBetween.add(between);

    }

}
