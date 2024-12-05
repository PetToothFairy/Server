package com.example.server.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.Response.BaseResponse;
import com.example.server.Response.CException;
import com.example.server.Response.ErrorBase;
import com.example.server.Response.SuccessBase;
import com.example.server.jwt.JwtTokenService;
import com.example.server.model.ToothDataAnalyzer;
import com.example.server.model.User;
import com.example.server.model.ToothDataAnalyzer.ToothReport;
import com.example.server.service.InvalidTokenService;
import com.example.server.service.ToothService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tooth")
public class ToothController {
    private final JwtTokenService jwtTokenService;
    private final InvalidTokenService invalidTokenService;
    private final ToothService toothService;
    private final Double totalCnt = 500.0;

    @GetMapping("")
    public ResponseEntity<?> mypageGetinfo
                                        (
                                            @RequestHeader("AccessToken") String AccessToken,
                                            @RequestBody List<String> Instructions
                                        ) { 
        // 1. RefreshToken Valid?
        try {
            if(jwtTokenService.validateAccessToken(AccessToken) == false) {
                throw new CException(ErrorBase.INVALID_TOKEN);
            }
        } catch (Exception e) {
            throw new CException(ErrorBase.INVALID_TOKEN);
        }

        // 2. Check userId
        Long userId;
        try {
            userId = jwtTokenService.extractIdFromAccessToken(AccessToken);
            if(invalidTokenService.existsById(userId) == false) {
                throw new CException(ErrorBase.INVALID_TOKEN);
            }
        } catch (Exception e) {
            throw new CException(ErrorBase.INVALID_TOKEN);
        }
        
        // 3. Count Frequency
        Map<String, Integer> frequency = new HashMap<>();
    
        for (String instruction : Instructions) {
            frequency.put(instruction, frequency.getOrDefault(instruction, 0) + 1);
        }

        List<ToothReport> toothReports = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : frequency.entrySet()) {
            ToothReport toothReport = new ToothReport();
            System.out.println(entry.getKey() + ": " + entry.getValue());
            String koreanPart = toothService.toothPartEngToKorFunction(entry.getKey());
            if(koreanPart == null) {
                continue;
            }
            String percentStr = String.format("%.2f", entry.getValue() / totalCnt);
            if(100 < Double.parseDouble(percentStr))
                percentStr = "100.00";
            toothReport.setName(koreanPart);
            toothReport.setPercent(percentStr);
            toothReport.setDescription(toothService.evaluationPercentValue(Double.parseDouble(percentStr)));
            toothReports.add(toothReport);
        }

        // 4. Get Sequence Data
        User user = null;
        try {
            user = toothService.getToothSeqByUserId(userId);
        } catch (Exception e) {
            throw new CException(ErrorBase.INTERNAL_SERVER_ERROR);
        }

        // 5. Check Date if Date yesterday or after, change value
        LocalDate userDateRenew = user.getToothDateRenew();
        LocalDate nowDate = LocalDate.now();
        if(!userDateRenew.isEqual(nowDate)) {
            if(userDateRenew.plusDays(1).isEqual(nowDate)) {
                // Seq += 1
                user.setToothSeq(user.getToothSeq() + 1);
            }
            else if (userDateRenew.plusDays(1).isAfter(nowDate)) {
                // init Seq = 0
                user.setToothSeq(0);
            }

            // 6. 모든 값 저장
            try {
                toothService.setSeqAndDateByUserId(userId, user.getToothSeq(), nowDate);
            } catch(Exception e) {
                throw new CException(ErrorBase.INTERNAL_SERVER_ERROR);
            }
        }
        

        

        ToothDataAnalyzer toothDataAnalyzer = new ToothDataAnalyzer();
        toothDataAnalyzer.setReports(toothReports);
        toothDataAnalyzer.setSeq(user.getToothSeq());

        return ResponseEntity
            .status(SuccessBase.SUCCESS.getStatus())
            .body(BaseResponse.success(SuccessBase.SUCCESS, toothDataAnalyzer));
    }
}
