package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.ShowWeightManagementRequest;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.model.entity.WeightManagementTip;
import tech.bread.solt.doctornyangserver.repository.UserRepo;
import tech.bread.solt.doctornyangserver.repository.WeightManagementTipRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeightManagementTipServiceImpl implements WeightManagementTipService {
    private final UserRepo userRepo;
    private final WeightManagementTipRepo weightManagementTipRepo;
    @Override
    public List<String> showWeightManageTip(ShowWeightManagementRequest request) {
        Optional<User> optionalUser = userRepo.findById(request.getUserUid());
        List<WeightManagementTip> weightManagementTips;
        List<String> res = new ArrayList<>();

        if (optionalUser.isPresent()) {
            weightManagementTips = weightManagementTipRepo.findByBmiRangeId(optionalUser.get().getBmiRangeId());
            if (weightManagementTips.isEmpty()){
                res.add("유저의 BMI 값이 설정되어 있지 않습니다.");
                return res;
            }
            for (WeightManagementTip wmt : weightManagementTips)
                res.add(wmt.getTip());
            return res;
        }
        res.add("찾고자 하는 유저가 존재하지 않습니다.");
        return res;
    }
}
