package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.AddIngestionRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdateIngestionRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetCalorieInformResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetDietResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetIngestionTotalResponse;

import java.time.LocalDate;
import java.util.List;

public interface DietService {

    List<GetCalorieInformResponse> getFoodList(String name);

    GetCalorieInformResponse getCalorieInform(String name, int num);

    int addIngestion(AddIngestionRequest request, String id);

    List<GetDietResponse> getDiet(String id, LocalDate date);

    GetIngestionTotalResponse getIngestionTotal(String id, LocalDate date);

    int updateIngestion(UpdateIngestionRequest request);
    int deleteIngestion(int ingestionId);
}
