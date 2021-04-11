package sk.stu.fei.project.service;

import sk.stu.fei.project.domain.AssetTree;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Queue;

public interface AssetBinaryTreeService {
    boolean buildTree(AssetTree assetTree);
    Queue<BigDecimal> averagePriceLeafs(AssetTree assetTree);
    //void printPaths(Node node);
    //void printTree(OutputStreamWriter out, Node root) throws IOException;
}
