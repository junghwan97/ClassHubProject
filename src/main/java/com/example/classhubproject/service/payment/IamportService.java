package com.example.classhubproject.service.payment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.siot.IamportRestClient.response.Prepare;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.siot.IamportRestClient.IamportClient;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IamportService {

    @Value("${iamport.key}")
    private String apiKey;

    @Value("${iamport.secret}")
    private String secretKey;

    private final IamportClient client;

    public String getToken() {
        try {
            URL url = new URL("https://api.iamport.kr/users/getToken");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            // 요청 방식 설정
            conn.setRequestMethod("POST");

            // Content-Type과 Accept 헤더 설정
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            // 해당 연결을 출력 스트림(요청)으로 사용
            conn.setDoOutput(true);

            // JSON 객체에 해당 API가 필요로 하는 데이터 추가
            JsonObject json = new JsonObject();
            json.addProperty("imp_key", apiKey);
            json.addProperty("imp_secret", secretKey);

            // 출력 스트림으로 해당 conn에 요청
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(json.toString()); // JSON 객체를 문자열 형태로 HTTP 요청 본문에 추가
            bw.flush();
            bw.close();

            // 입력 스트림으로 conn 요청에 대한 응답 반환
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            Gson gson = new Gson(); // 응답 데이터를 자바 객체로 변환하기 위한 Gson 객체 생성
            String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();
            String accessToken = gson.fromJson(response, Map.class).get("access_token").toString();
            br.close();

            conn.disconnect();

            return accessToken;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public IamportResponse<Payment> paymentByImpUid(String impUid) {

        try {
            return client.paymentByImpUid(impUid);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public Payment cancelPaymentByImpUid(CancelData cancelData) {
        try {
            IamportResponse<Payment> response = client.cancelPaymentByImpUid(cancelData);

            if (response.getCode() != 0) {
                throw new RuntimeException("결제 취소 요청이 실패했습니다.");
            }

            return response.getResponse();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public IamportResponse<Prepare> getPrepare(String merchantUid) {
        try {
            return client.getPrepare(merchantUid);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Prepare postPrepare(String merchantUid, BigDecimal amount) {
        PrepareData prepareData = new PrepareData(merchantUid, amount);
        try {
            return client.postPrepare(prepareData).getResponse();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
