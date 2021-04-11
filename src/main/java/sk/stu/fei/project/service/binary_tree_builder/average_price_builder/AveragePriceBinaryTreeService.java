package sk.stu.fei.project.service.binary_tree_builder.average_price_builder;

import lombok.NonNull;
import sk.stu.fei.project.domain.AssetTree;
import sk.stu.fei.project.domain.binary_tree.average_price.CallAverageBinaryTree;
import sk.stu.fei.project.domain.binary_tree.average_price.PutAverageBinaryTree;

public interface AveragePriceBinaryTreeService {
    void buildCallAveragePriceBinaryTree(@NonNull CallAverageBinaryTree callAverageBinaryTree, @NonNull AssetTree assetTree);

    void buildPutAveragePriceBinaryTree(@NonNull PutAverageBinaryTree callAverageBinaryTree, @NonNull AssetTree assetTree);
}
