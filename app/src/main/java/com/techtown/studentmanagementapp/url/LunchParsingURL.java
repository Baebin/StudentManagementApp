package com.techtown.studentmanagementapp.url;

import android.util.Log;

import com.techtown.studentmanagementapp.entity.Food;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LunchParsingURL {
    static public String TAG = "LaunchParsingURL";

    // NEIS API
    // https://open.neis.go.kr/portal/data/service/selectServicePage.do?page=1&rows=10&sortColumn=&sortDirection=&infId=OPEN17320190722180924242823&infSeq=1

    private URL url;

    // 서식 형태
    static String UTF = "UTF-8";

    // 파싱 타입
    static String Type = "Json";

    // 시도교육청코드
    static String ATPT_OFCDC_SC_CODE = "J10";
    // 표준학교코드
    static String SD_SCHUL_CODE = "7530929";

    // 급식 일자
    private String MLSV_YMD = null;
    private String MLSV_FROM_YMD = null;
    private String MLSV_TO_YMD = null;

    public LunchParsingURL(String MLSV_YMD) {
        this.MLSV_YMD = MLSV_YMD;
    }

    public LunchParsingURL(String MLSV_YMD, String MLSV_FROM_YMD, String MLSV_TO_YMD) {
        this.MLSV_YMD = MLSV_YMD;
        this.MLSV_FROM_YMD = MLSV_FROM_YMD;
        this.MLSV_TO_YMD = MLSV_TO_YMD;
    }

    private BufferedReader br;
    private String result;

    public void init() throws IOException {
        Log.d(TAG, "init()");

        String url_cash
                = "https://open.neis.go.kr/hub/mealServiceDietInfo?"
                + "Type=" + Type
                + "&ATPT_OFCDC_SC_CODE=" + ATPT_OFCDC_SC_CODE
                + "&SD_SCHUL_CODE=" + SD_SCHUL_CODE
                + "&MLSV_YMD=" + MLSV_YMD;

        if (MLSV_TO_YMD != null && MLSV_FROM_YMD != null) {
            url_cash +=
                    "&MLSV_FROM_YMD=" + MLSV_FROM_YMD
                            + "&MLSV_TO_YMD=" + MLSV_TO_YMD;
        }

        url = new URL(url_cash);

        Log.d(TAG, "url created");

        new Thread(() -> {
            try {
                br = new BufferedReader(new InputStreamReader(url.openStream(), UTF));
                result = br.readLine();
                Log.d(TAG, "br created: " + result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // URL 내용 리턴
    public String getContents() throws IOException {
        Log.d(TAG, "getContents()");

        // Test
        if (true)
        return null;
        return br.readLine();
    }

    /*
    public String getContents() throws IOException {
        Log.d(TAG, "getContents()");

        String url_cash
                = "https://open.neis.go.kr/hub/mealServiceDietInfo?"
                + "Type=" + Type
                + "&ATPT_OFCDC_SC_CODE=" + ATPT_OFCDC_SC_CODE
                + "&SD_SCHUL_CODE=" + SD_SCHUL_CODE
                + "&MLSV_YMD=" + MLSV_YMD;

        if (MLSV_TO_YMD != null && MLSV_FROM_YMD != null) {
            url_cash +=
                    "&MLSV_FROM_YMD=" + MLSV_FROM_YMD
                            + "&MLSV_TO_YMD=" + MLSV_TO_YMD;
        }

        url = new URL(url_cash);

        Log.d(TAG, "url created");

        BufferedReader br
                = new BufferedReader(new InputStreamReader(url.openStream(), UTF));

        Log.d(TAG, "br created");

        return br.readLine();
    }
     */

    // 급식 정보 리턴
    public Map<Integer, List<String>> getResult() throws IOException, ParseException, JSONException, org.json.simple.parser.ParseException {
        Log.d(TAG, "getResult()");

        //String result = getContents();
        //Log.d(TAG, "contents loaded");

        // 출력값
        Map<Integer, List<String>> foodMap = new HashMap<>();

        // JSON 파싱
        JSONParser jsonParser = new JSONParser();
        Log.d(TAG, "JSONParser created");
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
        Log.d(TAG, "JsonObject created");

        //JSONObject mealServiceDietInfo = (JSONObject) jsonObject.get("mealServiceDietInfo");
        org.json.simple.JSONArray jsonArray = (org.json.simple.JSONArray) jsonObject.get("mealServiceDietInfo");

        // 검색 실패
        if (!checkCode(jsonArray.get(0))) {
            Log.d(TAG, "checkCode(): Failed");
            return null;
        }

        // 급식 정보
        JSONObject row = (JSONObject) jsonArray.get(1);
        org.json.simple.JSONArray row_array = (JSONArray) row.get("row");

        // 요일별 급식
        for (int i = 0; i < row_array.size(); i++) {
            JSONObject out = (JSONObject) row_array.get(i);
            List<String> foods = Food.getFoods(out.get("DDISH_NM"));

            foodMap.put(i, foods);
            Log.d(TAG, (i+1) + "." + foods);
        }

        return foodMap;
    }

    // 수신 체크
    private boolean checkCode(Object object) throws JSONException {
        JSONObject head = (JSONObject) object;
        org.json.simple.JSONArray head_array = (org.json.simple.JSONArray) head.get("head");

        // {"list_total_count":5},{"RESULT":{"MESSAGE":"정상 처리되었습니다.","CODE":"INFO-000"}}
        JSONObject RESULT_Array = (JSONObject) head_array.get(1);
        System.out.println(RESULT_Array);

        // {"RESULT":{"MESSAGE":"정상 처리되었습니다.","CODE":"INFO-000"}}
        JSONObject RESULT = (JSONObject) RESULT_Array.get("RESULT");
        String CODE = RESULT.get("CODE").toString();

        if (CODE.equalsIgnoreCase("INFO-000")) {
            return true;
        }

        Log.d(TAG, "checkCode(): " + false);
        return false;
    }

    public String getMLSV_YMD() {
        return MLSV_YMD;
    }

    public void setMLSV_YMD(String MLSV_YMD) {
        this.MLSV_YMD = MLSV_YMD;
    }

    public String getMLSV_FROM_YMD() {
        return MLSV_FROM_YMD;
    }

    public void setMLSV_FROM_YMD(String MLSV_FROM_YMD) {
        this.MLSV_FROM_YMD = MLSV_FROM_YMD;
    }

    public String getMLSV_TO_YMD() {
        return MLSV_TO_YMD;
    }

    public void setMLSV_TO_YMD(String MLSV_TO_YMD) {
        this.MLSV_TO_YMD = MLSV_TO_YMD;
    }
}