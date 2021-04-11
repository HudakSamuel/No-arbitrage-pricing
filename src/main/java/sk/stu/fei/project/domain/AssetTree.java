package sk.stu.fei.project.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
public class AssetTree{

    private Node root;
    private AssetMovement assetMovement;

    public AssetTree(BigDecimal currentValue, AssetMovement assetMovement){
        this.root = new Node(currentValue);
        this.assetMovement = assetMovement;
    }
}
