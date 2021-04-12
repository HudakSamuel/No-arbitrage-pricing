package sk.stu.fei.project.service.binary_tree_builder.floating_lookback_builder;

import lombok.NonNull;
import sk.stu.fei.project.domain.AssetTree;
import sk.stu.fei.project.domain.binary_tree.lookback.floating_lookback.CallFloatingLookbackBinaryTree;
import sk.stu.fei.project.domain.binary_tree.lookback.floating_lookback.PutFloatingLookbackBinaryTree;

public interface FloatingLookbackBinaryTreeService {
    void buildCallFloatingLookbackBinaryTree(@NonNull CallFloatingLookbackBinaryTree callFloatingLookbackBinaryTree, @NonNull AssetTree assetTree);

    void buildPutFloatingLookbackBinaryTree(@NonNull PutFloatingLookbackBinaryTree putFloatingLookbackBinaryTree, @NonNull AssetTree assetTree);
}
