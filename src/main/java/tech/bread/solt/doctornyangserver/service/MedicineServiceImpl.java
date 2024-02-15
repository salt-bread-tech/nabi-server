package tech.bread.solt.doctornyangserver.service;

import lombok.RequiredArgsConstructor;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tech.bread.solt.doctornyangserver.util.KeySet;

import java.io.IOException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    final private String REQUEST_URL = "http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?serviceKey=" + KeySet.MEDICINE_API_KEY;

    @Override
    public String getMedicineDescription(String medicineName) {
        String result = "";

        RestTemplate restTemplate = new RestTemplate();
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = builder.build(new URL(REQUEST_URL+"&itemName="+medicineName));
            Element root = document.getRootElement();
            System.out.println(document);
        } catch (JDOMException | IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

}
