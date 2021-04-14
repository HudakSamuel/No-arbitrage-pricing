package sk.stu.fei.project.service.binary_tree_builder.fixed_lookback_builder;

import lombok.NonNull;
import sk.stu.fei.project.domain.AssetTree;
import sk.stu.fei.project.domain.binary_tree.lookback.fixed_lookback.CallFixedLookbackBinaryTree;
import sk.stu.fei.project.domain.binary_tree.lookback.fixed_lookback.PutFixedLookbackBinaryTree;

public interface FixedLookbackBinaryTreeService {
    void buildCallFixedLookbackBinaryTree(@NonNull CallFixedLookbackBinaryTree callFixedLookbackBinaryTree, @NonNull AssetTree assetTree);

    void buildPutFixedLookbackBinaryTree(@NonNull PutFixedLookbackBinaryTree putFixedLookbackBinaryTree, @NonNull AssetTree assetTree);
}
