package sk.stu.fei.project.domain.binary_tree;

import lombok.Getter;
import sk.stu.fei.project.domain.Node;

import java.math.BigDecimal;

@Getter
public abstract class OptionBinaryTree {

    protected Node root;

    public OptionBinaryTree(){
        this.root = new Node(new BigDecimal(0));
    }

}
