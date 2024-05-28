package tech.bread.solt.doctornyangserver.service;

import tech.bread.solt.doctornyangserver.model.dto.response.HomeResponse;

import java.security.Principal;
import java.time.LocalDate;

public interface HomeService {
    HomeResponse getHomeData(LocalDate date, String id);
}
