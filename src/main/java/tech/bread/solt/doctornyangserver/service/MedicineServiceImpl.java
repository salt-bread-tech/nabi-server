package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterMedicineRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetMedicineDescriptionResponse;
import tech.bread.solt.doctornyangserver.model.dto.response.GetMedicineResponse;
import tech.bread.solt.doctornyangserver.model.entity.Dosage;
import tech.bread.solt.doctornyangserver.model.entity.Medicine;
import tech.bread.solt.doctornyangserver.model.entity.Prescription;
import tech.bread.solt.doctornyangserver.model.entity.User;
import tech.bread.solt.doctornyangserver.repository.DosageRepo;
import tech.bread.solt.doctornyangserver.repository.MedicineRepo;
import tech.bread.solt.doctornyangserver.repository.PrescriptionRepo;
import tech.bread.solt.doctornyangserver.repository.UserRepo;
import tech.bread.solt.doctornyangserver.util.KeySet;
import tech.bread.solt.doctornyangserver.util.Times;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    final private String REQUEST_URL = "https://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?serviceKey=" + KeySet.MEDICINE_API_KEY.getKey();
    final private UserRepo userRepo;
    final private PrescriptionRepo prescriptionRepo;
    final private MedicineRepo medicineRepo;
    final private DosageRepo dosageRepo;

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
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public int register(RegisterMedicineRequest request) {
        Prescription prescription;
        Medicine medicine;
        int pId, mId;
        Optional<User> optionalUser = userRepo.findById(request.getId());
        Optional<Prescription> optionalPrescription;
        List<Integer> ordinals = new ArrayList<>();

        if (optionalUser.isPresent()) {
            prescription = Prescription.builder()
                    .userUid(optionalUser.get())
                    .name(request.getStartDate().toString() + " 처방전")
                    .date(request.getStartDate())
                    .build();
            pId = prescriptionRepo.save(prescription).getId();
            optionalPrescription = prescriptionRepo.getPrescriptionById(pId);

            if (optionalPrescription.isPresent()) {
                medicine = Medicine.builder()
                        .prescriptionId(optionalPrescription.get())
                        .medicineName(request.getMedicineName())
                        .dailyDosage(request.getDaily())
                        .totalDosage(request.getTotal())
                        .onceDosage(request.getOnce())
                        .medicineDosage(request.getDosage())
                        .build();

                mId = medicineRepo.save(medicine).getId();
                Optional<Medicine> m = medicineRepo.findById(mId);
                Medicine medicineToRegister = m.get();

                int doseDays = medicineToRegister.getTotalDosage() / medicineToRegister.getDailyDosage();
                if (medicineToRegister.getTotalDosage() % medicineToRegister.getDailyDosage() > 0)
                    doseDays += 1;
                String desc = medicineToRegister.getMedicineDosage();

                if (desc.contains("식후")) {
                    switch (medicine.getOnceDosage()) {
                        case 1:
                            switch (medicine.getDailyDosage()) {
                                case 3:
                                    ordinals.add(1);
                                    ordinals.add(3);
                                    ordinals.add(5);
                                    break;
                                case 2:
                                    ordinals.add(1);
                                    ordinals.add(3);
                                    break;
                                case 1:
                                    ordinals.add(1);
                                    break;
                                default:
                                    System.out.println("연산 오류");
                                    break;
                            }
                            break;
                        case 2:
                            switch (medicine.getDailyDosage()){
                                case 6:
                                    ordinals.add(1);
                                    ordinals.add(3);
                                    ordinals.add(5);
                                    break;
                                case 4:
                                    ordinals.add(1);
                                    ordinals.add(3);
                                    break;
                                case 2:
                                    ordinals.add(1);
                                    break;
                                default:
                                    System.out.println("연산 오류");
                                    break;
                            }
                            break;
                        default:
                            System.out.println("연산 오류");
                            break;
                    }

                } else if (desc.contains("식전")) {
                    switch (medicine.getOnceDosage()) {
                        case 1:
                            switch (medicine.getDailyDosage()) {
                                case 3:
                                    ordinals.add(0);
                                    ordinals.add(2);
                                    ordinals.add(4);
                                    break;
                                case 2:
                                    ordinals.add(0);
                                    ordinals.add(4);
                                    break;
                                case 1:
                                    ordinals.add(0);
                                    break;
                                default:
                                    System.out.println("연산 오류");
                                    break;
                            }
                            break;
                        case 2:
                            switch (medicine.getDailyDosage()){
                                case 6:
                                    ordinals.add(0);
                                    ordinals.add(2);
                                    ordinals.add(4);
                                    break;
                                case 4:
                                    ordinals.add(0);
                                    ordinals.add(2);
                                    break;
                                case 2:
                                    ordinals.add(0);
                                    break;
                                default:
                                    System.out.println("연산 오류");
                                    break;
                            }
                            break;
                        default:
                            System.out.println("연산 오류");
                            break;
                    }
                }

                if (desc.contains("취침"))
                    ordinals.add(6);

                int count = 0;
                for (int i = 0; i < doseDays; i++) {
                    for (int j = 0; j < (medicine.getDailyDosage() / medicine.getOnceDosage()); j++) {
                        count++;
                        dosageRepo.save(Dosage.builder()
                                .date(request.getStartDate().plusDays(i))
                                .userUid(optionalUser.get())
                                .times(Times.ofOrdinal(ordinals.get(j)))
                                .medicineId(medicineToRegister)
                                .medicineTaken(false).build());
                        if (count >= (medicine.getTotalDosage() / medicine.getOnceDosage()))
                            break;
                    }
                }
                System.out.println("약품 정보 및 복용 일정 등록 완료");
                return 200;
            }
            System.out.println("처방전을 찾을 수 없습니다.");
            return 300;
        }
        System.out.println("유저 정보를 찾을 수 없습니다.");
        return 100;
    }

    public List<GetMedicineResponse> getMedicines(String id) {
        Optional<User> u = userRepo.findById(id);
        List<GetMedicineResponse> responses = new ArrayList<>();
        GetMedicineResponse response;
        if(u.isPresent()){
            List<Prescription> p = prescriptionRepo.getPrescriptionsByUserUid(u.get());
            if (p.isEmpty()) {
                System.out.println("등록된 처방전이 없습니다.");
                return responses;
            }
            for (Prescription prescription : p) {
                List<Medicine> medicines = medicineRepo.findAllByPrescriptionId(prescription);
                for (Medicine m : medicines){
                    response = GetMedicineResponse.builder()
                            .id(m.getId())
                            .name(m.getMedicineName())
                            .total(m.getTotalDosage())
                            .daily(m.getDailyDosage())
                            .once(m.getOnceDosage())
                            .desc(m.getMedicineDosage()).build();
                    responses.add(response);
                }
            }
            System.out.println(u.get().getNickname() + "의 의약품 정보");
        }
        return responses;
    }
}
