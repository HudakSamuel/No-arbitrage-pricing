package sk.stu.fei.project.service.asset_movement;

import lombok.NonNull;
import sk.stu.fei.project.domain.AssetMovement;

public interface AssetMovementService {
    boolean initAssetMovementParameters(@NonNull AssetMovement assetMovement);
}
