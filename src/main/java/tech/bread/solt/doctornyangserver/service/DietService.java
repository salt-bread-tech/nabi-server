package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.AddIngestionRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdateIngestionRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetCalorieInformResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetDietResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetIngestionTotalResponse;

import java.time.LocalDate;
import java.util.List;

public interface DietService {

    List<String> getFoodList(String name);

    GetCalorieInformResponse getCalorieInform(String name, int num);

    int addIngestion(AddIngestionRequest request);

    List<GetDietResponse> getDiet(int uid, LocalDate date);

    GetIngestionTotalResponse getIngestionTotal(int uid, LocalDate date);

    int updateIngestion(UpdateIngestionRequest request);
}
