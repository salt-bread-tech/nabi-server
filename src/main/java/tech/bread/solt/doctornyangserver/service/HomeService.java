package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.request.WidgetSequenceRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.HomeResponse;

import java.time.LocalDate;
import java.util.Map;

public interface HomeService {
    HomeResponse getHomeData(LocalDate date, String id);

    String modifyWidgetSequence(WidgetSequenceRequest request, String id);

    Map<String, String> getWidgetSequence(String id);
}
