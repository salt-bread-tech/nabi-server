package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.ShowWeightManagementRequest;
import tech.bread.solt.doctornyangserver.model.entity.User;

import java.util.List;

public interface WeightManagementTipService {
    List<String> showWeightManageTip(ShowWeightManagementRequest request);
}
