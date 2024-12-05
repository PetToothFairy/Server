package com.example.server.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.example.server.model.User;
import com.example.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ToothService {
    private final UserRepository userRepository;

    Map<String, String> toothPartEngToKor = new HashMap<>();

    @PostConstruct
    private void init() {
        toothPartEngToKor.put("UNDER_FRONT", "아랫쪽 앞니");
        toothPartEngToKor.put("UP_FRONT", "윗쪽 앞니");
        toothPartEngToKor.put("UNDER_RIGHT_CANINE", "아래쪽 오른쪽 송곳니");
        toothPartEngToKor.put("UP_RIGHT_CANINE", "위쪽 오른쪽 송곳니");
        toothPartEngToKor.put("UNDER_RIGHT_MOLAR_OUTSIDE", "아랫쪽 오른쪽 어금니 바깥쪽");
        toothPartEngToKor.put("UP_RIGHT_MOLAR_OUTSIDE", "윗쪽 오른쪽 어금니 바깥쪽");
        toothPartEngToKor.put("UP_LEFT_MOLAR_CHEWING_SIDE", "윗쪽 왼쪽 어금니 씹는쪽");
        toothPartEngToKor.put("UP_RIGHT_MOLAR_CHEWING_SIDE", "윗쪽 오른쪽 어금니 씹는쪽");
        toothPartEngToKor.put("DOWN_RIGHT_MOLAR_CHEWING_SIDE", "아랫쪽 오른쪽 어금니 씹는쪽");
        toothPartEngToKor.put("DOWN_LEFT_MOLAR_CHEWING_SIDE", "아랫쪽 왼쪽 어금니 씹는쪽");
        toothPartEngToKor.put("UP_LEFT_MOLAR_OUTSIDE", "윗쪽 왼쪽 어금니 바깥쪽");
        toothPartEngToKor.put("UNDER_LEFT_MOLAR_OUTSIDE", "아랫쪽 왼쪽 어금니 바깥쪽");
        toothPartEngToKor.put("UNDER_LEFT_CANINE", "아랫쪽 왼쪽 송곳니");
        toothPartEngToKor.put("UP_LEFT_CANINE", "윗쪽 왼쪽 송곳니");
    }

    public String toothPartEngToKorFunction(String englishPart) {
        return toothPartEngToKor.get(englishPart);
    }

    public String evaluationPercentValue(Double percent) {
        if (100 == percent)
            return "적절해요.";
        else if (70 <= percent)
            return "주의해요.";
        return "미흡해요.";
    }

    public User getToothSeqByUserId(Long userId){
        return userRepository.getUserInformationById(userId);
    }

    public void setSeqAndDateByUserId(Long userId, int toothSeq, LocalDate toothDateRenew) {
        userRepository.updateSeqAndDateByUserId(userId, toothSeq, toothDateRenew);
    }
}
