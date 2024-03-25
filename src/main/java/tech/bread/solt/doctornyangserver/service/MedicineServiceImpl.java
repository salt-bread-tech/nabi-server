package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.request.RegisterMedicineRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetMedicineDescriptionResponse;
import tech.bread.solt.doctornyangserver.model.entity.Medicine;
import tech.bread.solt.doctornyangserver.model.entity.Prescription;
import tech.bread.solt.doctornyangserver.model.entity.User;
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
public class MedicineServiceImpl implements MedicineService {

    final private String REQUEST_URL = "https://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?serviceKey=" + KeySet.MEDICINE_API_KEY.getKey();
    final private UserRepo userRepo;
    final private PrescriptionRepo prescriptionRepo;
    final private MedicineRepo medicineRepo;

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
    public int registerMedicine(RegisterMedicineRequest request) {
        Prescription prescription;
        Medicine medicine;
        int pId;
        Optional<User> optionalUser = userRepo.findById(request.getUid());
        Optional<Prescription> optionalPrescription;

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

                medicineRepo.save(medicine);
                System.out.println("약품 정보 등록 완료");
                return 200;
            }
            System.out.println("처방전을 찾을 수 없습니다.");
            return 300;
        }
        System.out.println("유저 정보를 찾을 수 없습니다.");
        return 100;
    }
}
