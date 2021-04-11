package sk.stu.fei.project.domain;

import java.math.BigDecimal;

public class Node {
    public BigDecimal value;
    public Node left;
    public Node right;

    public Node(){
        this.value = null;
        this.left = null;
        this.right = null;
    }

    public Node(BigDecimal value){
        this.value = value;
        this.left = null;
        this.right = null;
    }


}
