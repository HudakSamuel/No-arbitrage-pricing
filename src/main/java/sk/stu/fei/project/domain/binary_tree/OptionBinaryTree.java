package sk.stu.fei.project.domain.binary_tree;

import lombok.Getter;
import sk.stu.fei.project.domain.AssetTree;
import sk.stu.fei.project.domain.Node;

import java.math.BigDecimal;

@Getter
public abstract class OptionBinaryTree {

    protected Node root;
    protected AssetTree assetTree;

    public OptionBinaryTree(AssetTree assetTree){
        this.root = new Node(new BigDecimal(0));
        this.assetTree = assetTree;
        generateEmptyBinaryTree();
    }

    private void generateEmptyBinaryTree(){
        createChildrenRecursive(root, 1);
    }

    private void createChildrenRecursive(Node current, int depth){
        current.left = new Node(new BigDecimal(0));
        current.right = new Node(new BigDecimal(0));

        if (depth != assetTree.getSteps()){
            createChildrenRecursive(current.left, depth + 1);
            createChildrenRecursive(current.right, depth + 1);
        }
    }


}
