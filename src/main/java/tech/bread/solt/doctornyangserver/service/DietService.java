package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.response.GetCalorieInformResponse;

public interface DietService {
    GetCalorieInformResponse getCalorieInform(String name);
}
