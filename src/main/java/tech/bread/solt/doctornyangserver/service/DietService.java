package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.AddIngestionRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetCalorieInformResponse;

import java.util.List;

public interface DietService {

    List<String> getFoodList(String name);

    GetCalorieInformResponse getCalorieInform(String name, int num);

    int addIngestion(AddIngestionRequest request);

}
