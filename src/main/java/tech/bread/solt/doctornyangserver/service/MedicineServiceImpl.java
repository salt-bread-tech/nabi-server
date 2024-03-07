package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.dto.response.GetMedicineDescriptionResponse;
import tech.bread.solt.doctornyangserver.util.KeySet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    final private String REQUEST_URL = "https://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?serviceKey=" + KeySet.MEDICINE_API_KEY.getKey();

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

}
