package sk.stu.fei.project.domain.binary_tree.lookback.fixed_lookback;

import lombok.Getter;
import sk.stu.fei.project.domain.binary_tree.lookback.LookbackBinaryTree;

import java.math.BigDecimal;

@Getter
public abstract class FixedLookbackBinaryTree extends LookbackBinaryTree {
    protected BigDecimal strikePrice;

    FixedLookbackBinaryTree(BigDecimal strikePrice) {
        this.strikePrice = strikePrice;
    }
}
