package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.AddIngestionRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdateIngestionRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetCalorieInformResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetDietResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetIngestionTotalResponse;
import tech.bread.solt.doctornyangserver.model.entity.FoodInformation;
import tech.bread.solt.doctornyangserver.model.entity.Ingestion;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.FoodInformationRepo;
import tech.bread.solt.doctornyangserver.repository.IngestionRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;
import tech.bread.solt.doctornyangserver.util.IngestionTimes;
import tech.bread.solt.doctornyangserver.util.IngestionTimesConverter;
import tech.bread.solt.doctornyangserver.util.KeySet;
import tech.bread.solt.doctornyangserver.util.DosageTimes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DietServiceImpl implements DietService {
    final private String REQUEST_URL = "http://openapi.foodsafetykorea.go.kr/api/" + KeySet.CALORIE_API_KEY.getKey() + "/I2790/xml/1/10/";

    private final UserRepo userRepo;
    private final FoodInformationRepo foodInformationRepo;
    private final IngestionRepo ingestionRepo;

    @Override
    public List<GetCalorieInformResponse> getFoodList(String name) {
        String url = REQUEST_URL + "DESC_KOR=" + name;
        List<GetCalorieInformResponse> result = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).parser(org.jsoup.parser.Parser.xmlParser()).get();
            Elements items = doc.select("row");

            for (Element item : items) {
                result.add(GetCalorieInformResponse.builder()
                                .name(item.select("DESC_KOR").text())
                                .servingSize(parseDoubleOrDefault(item.select("SERVING_SIZE").text()))
                                .calories(parseDoubleOrDefault(item.select("NUTR_CONT1").text()))
                                .carbohydrate(parseDoubleOrDefault(item.select("NUTR_CONT2").text()))
                                .protein(parseDoubleOrDefault(item.select("NUTR_CONT3").text()))
                                .fat(parseDoubleOrDefault(item.select("NUTR_CONT4").text()))
                                .sugars(parseDoubleOrDefault(item.select("NUTR_CONT5").text()))
                                .salt(parseDoubleOrDefault(item.select("NUTR_CONT6").text()))
                                .cholesterol(parseDoubleOrDefault(item.select("NUTR_CONT7").text()))
                                .saturatedFattyAcid(parseDoubleOrDefault(item.select("NUTR_CONT8").text()))
                                .transFattyAcid(parseDoubleOrDefault(item.select("NUTR_CONT9").text()))
                                .build());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public GetCalorieInformResponse getCalorieInform(String name, int num) {
        String url = REQUEST_URL + "DESC_KOR=" + name;
        GetCalorieInformResponse result = new GetCalorieInformResponse();
        System.out.println(num);

        try {
            Document doc = Jsoup.connect(url).parser(org.jsoup.parser.Parser.xmlParser()).get();

            Elements items = doc.select("row");
            Element item = items.get(num);

            System.out.println("name: " + item.select("DESC_KOR").text());
            result.setName(item.select("DESC_KOR").text());

            result.setServingSize(parseDoubleOrDefault(item.select("SERVING_SIZE").text()));
            result.setCalories(parseDoubleOrDefault(item.select("NUTR_CONT1").text()));
            result.setCarbohydrate(parseDoubleOrDefault(item.select("NUTR_CONT2").text()));
            result.setProtein(parseDoubleOrDefault(item.select("NUTR_CONT3").text()));
            result.setFat(parseDoubleOrDefault(item.select("NUTR_CONT4").text()));
            result.setSugars(parseDoubleOrDefault(item.select("NUTR_CONT5").text()));
            result.setSalt(parseDoubleOrDefault(item.select("NUTR_CONT6").text()));
            result.setCholesterol(parseDoubleOrDefault(item.select("NUTR_CONT7").text()));
            result.setSaturatedFattyAcid(parseDoubleOrDefault(item.select("NUTR_CONT8").text()));
            result.setTransFattyAcid(parseDoubleOrDefault(item.select("NUTR_CONT9").text()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private static double parseDoubleOrDefault(String value) {
        if (value != null && !value.trim().isEmpty()) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                return 9999999.0;
            }
        }
        return 9999999.0;
    }

    @Override
    public int addIngestion(AddIngestionRequest request, String id) {
        int result = 0;
        Optional<User> optionalUser = userRepo.findById(id);

        if (optionalUser.isPresent()) {
            FoodInformation foodInformation = FoodInformation.builder()
                    .name(request.getFoodName())
                    .servingSize(request.getServingSize())
                    .totalIngestionSize(request.getTotalIngestionSize())
                    .calories(request.getCalories())
                    .carbohydrate(request.getCarbohydrate())
                    .protein(request.getProtein())
                    .fat(request.getFat())
                    .sugars(request.getSugars())
                    .salt(request.getSalt())
                    .cholesterol(request.getCholesterol())
                    .saturatedFattyAcid(request.getSaturatedFattyAcid())
                    .transFattyAcid(request.getTransFattyAcid())
                    .build();

            foodInformationRepo.save(foodInformation);
            System.out.println("음식 데이터 저장 성공");

            Optional<FoodInformation> optionalFoodInformation = foodInformationRepo.findById(foodInformation.getFoodId());

            if (optionalFoodInformation.isPresent()) {
                FoodInformation foodInformationResult = optionalFoodInformation.get();
                User user = optionalUser.get();

                Ingestion ingestion = Ingestion.builder()
                        .ingestionTimes(IngestionTimes.ofOrdinal(request.getTimes()))
                        .userUid(user)
                        .foodId(foodInformationResult)
                        .date(request.getDate())
                        .build();

                ingestionRepo.save(ingestion);
                result = 200;
            }
            else {
                System.out.println("음식 데이터가 제대로 저장되지 않음");
                result = 400;
            }
        }
        else {
            System.out.println("유저가 존재하지 않음");
            result = 400;
        }

        return result;
    }

    @Override
    public List<GetDietResponse> getDiet(String id, LocalDate date) {
        List<GetDietResponse> result = new ArrayList<>();
        Optional<User> optionalUser = userRepo.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Ingestion> ingestionList = ingestionRepo.findAllByUserUidAndDate(user, date);

            for (Ingestion ingestion : ingestionList) {
                FoodInformation f = ingestion.getFoodId();

                result.add(GetDietResponse.builder()
                                .ingestionId(ingestion.getId())
                                .foodId(ingestion.getFoodId().getFoodId())
                                .ingestionTimes(ingestion.getIngestionTimes().ordinal())
                                .name(f.getName())
                                .servingSize(f.getServingSize())
                                .totalIngestionSize(f.getTotalIngestionSize())
                                .calories(f.getCalories())
                                .carbohydrate(f.getCarbohydrate())
                                .protein(f.getProtein())
                                .fat(f.getFat())
                                .sugars(f.getSugars())
                                .salt(f.getSalt())
                                .cholesterol(f.getCholesterol())
                                .saturatedFattyAcid(f.getSaturatedFattyAcid())
                                .transFattyAcid(f.getTransFattyAcid())
                                .build());
            }
        }

        return result;
    }

    @Override
    public GetIngestionTotalResponse getIngestionTotal(String id, LocalDate date) {
        GetIngestionTotalResponse result;
        Optional<User> optionalUser = userRepo.findById(id);

        double totalKcal = 0;
        double breakfastKcal = 0;
        double lunchKcal = 0;
        double dinnerKcal = 0;
        double snackKcal = 0;
        double totalCarbohydrate = 0;
        double totalProtein = 0;
        double totalFat = 0;

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Ingestion> ingestionList = ingestionRepo.findAllByUserUidAndDate(user, date);

            for (Ingestion ingestion : ingestionList) {
                FoodInformation food = ingestion.getFoodId();

                switch (ingestion.getIngestionTimes()) {
                    case BREAKFAST -> breakfastKcal += food.getCalories();
                    case LUNCH -> lunchKcal += food.getCalories();
                    case DINNER -> dinnerKcal += food.getCalories();
                    case SNACK -> snackKcal += food.getCalories();
                    default -> System.out.println("times error");
                }

                if (food.getCarbohydrate() != 9999999.0) {
                    totalCarbohydrate += food.getCarbohydrate();
                }

                if (food.getProtein() != 9999999.0) {
                    totalProtein += food.getProtein();
                }

                if (food.getFat() != 9999999.0) {
                    totalFat += food.getFat();
                }
            }

            totalKcal = breakfastKcal + lunchKcal + dinnerKcal + snackKcal;
        }

        result = GetIngestionTotalResponse.builder()
                .totalKcal(totalKcal)
                .breakfastKcal(breakfastKcal)
                .lunchKcal(lunchKcal)
                .dinnerKcal(dinnerKcal)
                .snackKcal(snackKcal)
                .totalCarbohydrate(totalCarbohydrate)
                .totalProtein(totalProtein)
                .totalFat(totalFat)
                .build();

        return result;
    }

    @Override
    public int updateIngestion(UpdateIngestionRequest request) {
        Optional<Ingestion> optionalIngestion = ingestionRepo.findById(request.getIngestionId());
        IngestionTimes times = new IngestionTimesConverter().convertToEntityAttribute(request.getIngestionId());

        if (optionalIngestion.isPresent()){
            Ingestion ingestion = optionalIngestion.get();
            ingestion.setDate(request.getDate());
            ingestion.setIngestionTimes(times);
            ingestionRepo.save(ingestion);
            System.out.println("섭취 날짜, 시간대 수정 완료");

            Optional<FoodInformation> optionalFood = foodInformationRepo.findById(ingestion.getFoodId().getFoodId());

            if (optionalFood.isPresent()){
                FoodInformation food = optionalFood.get();
                food.setServingSize(request.getServingSize());
                food.setTotalIngestionSize(request.getTotalIngestionSize());
                food.setCalories(request.getCalories());
                food.setProtein(request.getProtein());
                food.setFat(request.getFat());
                food.setCarbohydrate(request.getCarbohydrate());
                food.setSugars(request.getSugars());
                food.setSalt(request.getSalt());
                food.setCholesterol(request.getCholesterol());
                food.setSaturatedFattyAcid(request.getSaturatedFattyAcid());
                food.setTransFattyAcid(request.getTransFattyAcid());
                foodInformationRepo.save(food);
                System.out.println("섭취 정보 수정 완료");

                return 200;
            }
            System.out.println("찾는 음식에 대한 정보가 없음");
            return 500;
        }
        System.out.println("음식 섭취 정보가 없음");
        return 400;
    }

    @Override
    public int deleteIngestion(int ingestionId) {
        Optional<Ingestion> ingestion = ingestionRepo.findById(ingestionId);

        if (ingestion.isPresent()) {
            Ingestion i = ingestion.get();
            ingestionRepo.delete(ingestion.get());
            foodInformationRepo.delete(i.getFoodId());
            System.out.println("섭취 정보 삭제");
            return 200;
        }
        System.out.println("찾는 정보가 없음");
        return 400;
    }
}
