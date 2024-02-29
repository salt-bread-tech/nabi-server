package tech.bread.solt.doctornyangserver.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.bread.solt.doctornyangserver.model.dto.request.ShowWeightManagementRequest;
import tech.bread.solt.doctornyangserver.service.WeightManagementTipService;

import java.util.List;

@RestController
@RequestMapping("/weight-management-tip")
public class WeightManagementTipController {
    final WeightManagementTipService weightManagementTipService;

    public WeightManagementTipController(WeightManagementTipService weightManagementTipService) {
        this.weightManagementTipService = weightManagementTipService;
    }

    @PostMapping("/show")
    public List<String> showWeightManagementTips(@RequestBody ShowWeightManagementRequest request) {
        return weightManagementTipService.showWeightManageTip(request);
    }
}
