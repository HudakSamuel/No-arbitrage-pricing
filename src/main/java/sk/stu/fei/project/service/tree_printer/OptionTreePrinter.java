package sk.stu.fei.project.service.tree_printer;

import sk.stu.fei.project.domain.Node;
import sk.stu.fei.project.domain.binary_tree.OptionBinaryTree;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.MathContext;
import java.math.RoundingMode;

public class OptionTreePrinter extends TreePrinter{

    private OutputStreamWriter output;

    public OptionTreePrinter(){
        this.output = new OutputStreamWriter(System.out);
    }

    public void print(OptionBinaryTree optionBinaryTree) throws IOException{
        printTree(optionBinaryTree.getRoot());
        output.flush();
    }

    private void printTree(Node root) throws IOException {
        if (root.left != null) {
            printTree(root.left, false, "");
        }
        printNodeValue(root);
        if (root.right != null) {
            printTree(root.right, true, "");
        }
    }
    private void printNodeValue(Node current) throws IOException {
        MathContext m = new MathContext(3);
        if (current.value == null) {
            output.write("<null>");
        } else {
            output.write(current.value.round(m).toString());
        }
        output.write('\n');
    }
    // use string and not stringbuffer on purpose as we need to change the indent at each recursion
    private void printTree(Node current, boolean isRight, String indent) throws IOException {
        if (current.left != null) {
            printTree(current.left, false, indent + (isRight ? " |      " : "        "));
        }

        output.write(indent);
        if (isRight) {
            output.write(" \\");

        } else {
            output.write(" /");
        }
        output.write("----- ");
        printNodeValue(current);

        if (current.right != null) {
            printTree(current.right, true, indent + (isRight ? "        " : " |      "));
        }

    }

}
