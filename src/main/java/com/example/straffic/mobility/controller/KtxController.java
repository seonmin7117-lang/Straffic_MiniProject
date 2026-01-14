package com.example.straffic.mobility.controller;

import com.example.straffic.mobility.entity.KtxTrainEntity;
import com.example.straffic.mobility.repository.KtxTrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class KtxController {
    @Value("${kakao.js.key:}")
    private String kakaoJsKey;

    private final KtxTrainRepository ktxTrainRepository;

    @GetMapping("/ktx")
    public String ktxMain(Model model) {
        model.addAttribute("pageTitle", "KTX 예매");
        model.addAttribute("kakaoKey", kakaoJsKey);
        model.addAttribute("stations", getStations());
        model.addAttribute("trains", ktxTrainRepository.findByDepartureAndArrivalOrderByDepartureTimeAsc("서울", "부산"));
        return "mobility/ktx";
    }

    @GetMapping("/ktx/search")
    @ResponseBody
    public Map<String, Object> searchTrains(@RequestParam String departure,
                                            @RequestParam String arrival,
                                            @RequestParam String date,
                                            @RequestParam(defaultValue = "1") int passengers) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("departure", departure);
        result.put("arrival", arrival);
        result.put("date", date);
        result.put("passengers", passengers);

        List<KtxTrainEntity> trains = ktxTrainRepository.findByDepartureAndArrivalOrderByDepartureTimeAsc(departure, arrival);
        List<Map<String, Object>> trainList = new ArrayList<>();
        for (KtxTrainEntity t : trains) {
            Map<String, Object> train = new HashMap<>();
            train.put("trainNo", t.getTrainNo());
            train.put("departure", t.getDeparture());
            train.put("arrival", t.getArrival());
            train.put("departureTime", t.getDepartureTime());
            train.put("arrivalTime", t.getArrivalTime());
            train.put("duration", t.getDuration());
            train.put("price", String.format("%,d", t.getPrice()));
            train.put("availableSeats", t.getAvailableSeats());
            trainList.add(train);
        }
        result.put("trains", trainList);
        return result;
    }

    @GetMapping("/ktx/api/reserve")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> reserve(@RequestParam String trainNo,
                                                       @RequestParam String from,
                                                       @RequestParam String to,
                                                       @RequestParam String depTime,
                                                       @RequestParam String arrTime,
                                                       @RequestParam String seats,
                                                       @RequestParam String price) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("reservationId", "KTX" + System.currentTimeMillis());
        result.put("trainNo", trainNo);
        result.put("from", from);
        result.put("to", to);
        result.put("depTime", depTime);
        result.put("arrTime", arrTime);
        result.put("seats", seats);
        result.put("price", price);
        result.put("message", "예약이 완료되었습니다.");
        return ResponseEntity.ok(result);
    }

    private List<String> getStations() {
        return Arrays.asList("서울", "용산", "광명", "수원", "천안아산", "대전", "동대구", "부산");
    }
}
