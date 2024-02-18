package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.util.KeySet;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    final private String REQUEST_URL = "http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?serviceKey=" + KeySet.MEDICINE_API_KEY;

    @Override
    public String getMedicineDescription(String medicineName) {
        String result = "";
        String url = REQUEST_URL + "&itemName=" + medicineName;

        try {
            Document doc = Jsoup.connect(url).parser(org.jsoup.parser.Parser.xmlParser()).get();

            Elements items = doc.select("item");

            for (Element item : items) {
                String itemName = item.select("itemName").text();
                System.out.println("Item Name: " + itemName);

                String entpName = item.select("entpName").text();
                System.out.println("Enterprise Name: " + entpName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return result;
    }

}
