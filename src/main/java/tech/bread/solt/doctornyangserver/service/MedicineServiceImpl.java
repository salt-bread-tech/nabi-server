package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.DosageRegisterRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterMedicineRequest;
import tech.bread.solt.doctornyangserver.model.dto.request.UpdateMedicineRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetMedicineDescriptionResponse;
import tech.bread.solt.doctornyangserver.model.entity.Dosage;
import tech.bread.solt.doctornyangserver.model.entity.Medicine;
import tech.bread.solt.doctornyangserver.model.entity.Prescription;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.DosageRepo;
import tech.bread.solt.doctornyangserver.repository.MedicineRepo;
import tech.bread.solt.doctornyangserver.repository.PrescriptionRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;
import tech.bread.solt.doctornyangserver.util.KeySet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineServiceImpl implements MedicineService {

    final private String REQUEST_URL = "https://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?serviceKey=" + KeySet.MEDICINE_API_KEY.getKey();
    final private UserRepo userRepo;
    final private PrescriptionRepo prescriptionRepo;
    final private MedicineRepo medicineRepo;
    final private DosageRepo dosageRepo;
    final private DosageService dosageService;

    @Override
    public List<String> getMedicineList(String name) {
        String url = REQUEST_URL + "&itemName=" + name + "&pageNo=1&numOfRows=10";
        List<String> result = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).parser(org.jsoup.parser.Parser.xmlParser()).get();
            Elements items = doc.select("item");

            for (Element item : items) {
                System.out.println("name: " + item.select("itemName").text());
                result.add(item.select("itemName").text());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public GetMedicineDescriptionResponse getMedicineDescription(String name, int num) {
        String url = REQUEST_URL + "&itemName=" + name + "&pageNo=1&numOfRows=10";
        GetMedicineDescriptionResponse result = new GetMedicineDescriptionResponse();

        try {
            Document doc = Jsoup.connect(url).parser(org.jsoup.parser.Parser.xmlParser()).get();
            Elements items = doc.select("item");
            Element item = items.get(num);

            System.out.println("name: " + item.select("itemName").text());

            result.setItemName(item.select("itemName").text());
            result.setEfcyQesitm(item.select("efcyQesitm").text());
            result.setUseMethodQesitm(item.select("useMethodQesitm").text());
            result.setAtpnWarnQesitm(item.select("atpnWarnQesitm").text());
            result.setAtpnQesitm(item.select("atpnQesitm").text());
            result.setIntrcQesitm(item.select("intrcQesitm").text());
            result.setSeQesitm(item.select("seQesitm").text());
            result.setDepositMethodQesitm(item.select("depositMethodQesitm").text());

        } catch (IOException e) {
            log.error("Request Valid Error");
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public int register(RegisterMedicineRequest request, String id) {
        Optional<User> optionalUser = userRepo.findById(id);
        Optional<Prescription> optionalPrescription = prescriptionRepo.getPrescriptionById(request.getPrescriptionId());
        String[] dosageText = {"식전", "식중", "식후", "상관 없음"};

        if (optionalUser.isPresent()) {
            if (optionalPrescription.isPresent()) {
                Prescription p = optionalPrescription.get();

                int once = request.getOnce();
                int daily = request.getTime().size() * once;
                int total = request.getDays() * daily;

                Medicine medicine = Medicine.builder()
                        .prescriptionId(p)
                        .medicineName(request.getMedicineName())
                        .dailyDosage(daily)
                        .totalDosage(total)
                        .onceDosage(once)
                        .medicineDosage(dosageText[request.getDosage()%4])
                        .breakfast(request.getTime().contains(0))
                        .lunch(request.getTime().contains(1))
                        .dinner(request.getTime().contains(2))
                        .beforeSleep(request.getTime().contains(3))
                        .build();
                medicineRepo.save(medicine);

                log.info("의약품 등록 완료");
                return 200;
            }
            log.warn("잘못된 처방전 아이디");
            return 300;
        }
        log.warn("잘못된 유저 정보");
        return 100;
    }

    @Override
    public int update(UpdateMedicineRequest request) {
        Optional<Medicine> optionalMedicine = medicineRepo.findById(request.getMedicineId());

        if (optionalMedicine.isPresent()) {
            Medicine medicine = optionalMedicine.get();
            medicine = medicineRepo.save(getMedicine(request, medicine));

            List<Dosage> dosages = dosageRepo.findByMedicineId(medicine);

            // 복용 일정이 등록되지 않은 경우
            if (dosages.isEmpty()) {
                log.info("복용 일정이 등록되지 않아 의약품 정보만 수정");
                return 201;
            }
            // 복용 일정이 등록되어 있는 경우: 복용 일정 수정
            dosageRepo.deleteAll(dosages);
            DosageRegisterRequest dosageRegisterRequest =
                    DosageRegisterRequest.builder()
                            .userId(request.getUserId())
                            .medicineId(medicine.getId())
                            .build();
            dosageService.register(dosageRegisterRequest);

            log.info("복용 일정 수정 성공");
            return 200;
        }
        log.warn("등록되지 않은 의약품");
        return 100;
    }

    @Override
    public boolean delete(int medicineId) {
        Optional<Medicine> optionalMedicine = medicineRepo.findById(medicineId);

        if (optionalMedicine.isPresent()) {
            Medicine deleteMedicine = optionalMedicine.get();
            medicineRepo.delete(deleteMedicine);
            log.info("의약품 삭제 성공");
            return true;
        }
        log.warn("잘못된 의약품 정보");
        return false;
    }

    private Medicine getMedicine(UpdateMedicineRequest request, Medicine medicine) {
        String[] dosageText = {"식전", "식중", "식후", "상관 없음"};
        int once = request.getOnce();
        int daily = request.getTime().size() * once;
        int total = request.getDays() * daily;

        // 의약품 정보 수정
        medicine.setMedicineName(request.getMedicineName());
        medicine.setOnceDosage(once);
        medicine.setDailyDosage(daily);
        medicine.setTotalDosage(total);
        medicine.setMedicineDosage(dosageText[request.getDosage() % 4]);
        medicine.setBreakfast(request.getTime().contains(0));
        medicine.setLunch(request.getTime().contains(1));
        medicine.setDinner(request.getTime().contains(2));
        medicine.setBeforeSleep(request.getTime().contains(3));

        return medicine;
    }
}
