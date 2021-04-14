package sk.stu.fei.project.domain.binary_tree.lookback.fixed_lookback;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PutFixedLookbackBinaryTree extends FixedLookbackBinaryTree{
    public PutFixedLookbackBinaryTree(BigDecimal strikePrice) {
        super(strikePrice);
    }
}
