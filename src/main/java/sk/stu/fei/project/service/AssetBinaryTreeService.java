package sk.stu.fei.project.service;

import lombok.NonNull;
import sk.stu.fei.project.domain.AssetTree;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Queue;

public interface AssetBinaryTreeService {
    boolean buildTree(@NonNull AssetTree assetTree);

}
