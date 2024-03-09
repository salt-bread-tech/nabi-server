package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.AddIngestionRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetCalorieInformResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetDietResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetIngestionTotalResponse;
import tech.bread.solt.doctornyangserver.model.entity.FoodInformation;
import tech.bread.solt.doctornyangserver.model.entity.Ingestion;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.FoodInformationRepo;
import tech.bread.solt.doctornyangserver.repository.IngestionRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;
import tech.bread.solt.doctornyangserver.util.KeySet;
import tech.bread.solt.doctornyangserver.util.Times;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DietServiceImpl implements DietService {
    final private String REQUEST_URL = "https://apis.data.go.kr/1471000/FoodNtrIrdntInfoService1/getFoodNtrItdntList1?ServiceKey=" + KeySet.CALORIE_API_KEY.getKey();

    private final UserRepo userRepo;
    private final FoodInformationRepo foodInformationRepo;
    private final IngestionRepo ingestionRepo;

    @Override
    public List<String> getFoodList(String name) {
        String url = REQUEST_URL + "&desc_kor=" + name + "&pageNo=1&numOfRows=10";
        List<String> result = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).parser(org.jsoup.parser.Parser.xmlParser()).get();
            Elements items = doc.select("item");

            for (Element item : items) {
                System.out.println("name: " + item.select("DESC_KOR").text());
                result.add(item.select("DESC_KOR").text());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public GetCalorieInformResponse getCalorieInform(String name, int num) {
        String url = REQUEST_URL + "&desc_kor=" + name + "&pageNo=1&numOfRows=10";
        GetCalorieInformResponse result = new GetCalorieInformResponse();

        try {
            Document doc = Jsoup.connect(url).parser(org.jsoup.parser.Parser.xmlParser()).get();
            Elements items = doc.select("item");
            Element item = items.get(num);

            System.out.println("name: " + item.select("DESC_KOR").text());
            result.setName(item.select("DESC_KOR").text());
            result.setServingSize(Double.parseDouble(item.select("SERVING_WT").text().replace("N/A", "200000")));
            result.setCalories(Double.parseDouble(item.select("NUTR_CONT1").text().replace("N/A", "200000")));
            result.setCarbohydrate(Double.parseDouble(item.select("NUTR_CONT2").text().replace("N/A", "200000")));
            result.setProtein(Double.parseDouble(item.select("NUTR_CONT3").text().replace("N/A", "200000")));
            result.setFat(Double.parseDouble(item.select("NUTR_CONT4").text().replace("N/A", "200000")));
            result.setSugars(Double.parseDouble(item.select("NUTR_CONT5").text().replace("N/A", "200000")));
            result.setSalt(Double.parseDouble(item.select("NUTR_CONT6").text().replace("N/A", "200000")));
            result.setCholesterol(Double.parseDouble(item.select("NUTR_CONT7").text().replace("N/A", "200000")));
            result.setSaturatedFattyAcid(Double.parseDouble(item.select("NUTR_CONT8").text().replace("N/A", "200000")));
            result.setTransFattyAcid(Double.parseDouble(item.select("NUTR_CONT9").text().replace("N/A", "200000")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public int addIngestion(AddIngestionRequest request) {
        int result = 0;
        Optional<User> optionalUser = userRepo.findById(request.getUid());

        if (optionalUser.isPresent()) {
            FoodInformation foodInformation = FoodInformation.builder()
                    .name(request.getFoodName())
                    .servingSize(request.getServingSize())
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
                        .times(Times.ofOrdinal(9+request.getTimes()))
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
    public List<GetDietResponse> getDiet(int uid, LocalDate date) {
        List<GetDietResponse> result = new ArrayList<>();
        Optional<User> optionalUser = userRepo.findById(uid);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Ingestion> ingestionList = ingestionRepo.findAllByUserUidAndDate(user, date);

            for (Ingestion ingestion : ingestionList) {
                result.add(GetDietResponse.builder()
                                .dietId(ingestion.getId())
                                .foodId(ingestion.getFoodId().getFoodId())
                                .times(ingestion.getTimes())
                                .name(ingestion.getFoodId().getName())
                                .servingSize(ingestion.getFoodId().getServingSize())
                                .calories(ingestion.getFoodId().getCalories())
                                .carbohydrate(ingestion.getFoodId().getCarbohydrate())
                                .protein(ingestion.getFoodId().getProtein())
                                .fat(ingestion.getFoodId().getFat())
                                .build());
            }
        }

        return result;
    }

    @Override
    public GetIngestionTotalResponse getIngestionTotal(int uid, LocalDate date) {
        GetIngestionTotalResponse result;
        Optional<User> optionalUser = userRepo.findById(uid);

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

                switch (ingestion.getTimes()) {
                    case BREAKFAST -> breakfastKcal += food.getCalories();
                    case LUNCH -> lunchKcal += food.getCalories();
                    case DINNER -> dinnerKcal += food.getCalories();
                    case SNACK -> snackKcal += food.getCalories();
                    default -> System.out.println("times error");
                }

                totalCarbohydrate += food.getCarbohydrate();
                totalProtein += food.getProtein();
                totalFat += food.getFat();
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

}
