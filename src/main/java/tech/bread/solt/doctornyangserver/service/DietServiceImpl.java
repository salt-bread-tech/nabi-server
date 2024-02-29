package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.response.GetCalorieInformResponse;

@Service
@RequiredArgsConstructor
public class DietServiceImpl implements DietService {
    @Override
    public GetCalorieInformResponse getCalorieInform(String name) {
        return null;
    }
}
