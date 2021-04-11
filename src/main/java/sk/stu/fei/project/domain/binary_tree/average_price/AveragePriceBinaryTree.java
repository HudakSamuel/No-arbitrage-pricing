package sk.stu.fei.project.domain.binary_tree.average_price;

import lombok.Getter;
import sk.stu.fei.project.domain.AssetTree;
import sk.stu.fei.project.domain.binary_tree.OptionBinaryTree;

import java.math.BigDecimal;


@Getter
public abstract class AveragePriceBinaryTree extends OptionBinaryTree {

    protected BigDecimal strikePrice;

    AveragePriceBinaryTree(BigDecimal strikePrice) {
        this.strikePrice = strikePrice;
    }
}
