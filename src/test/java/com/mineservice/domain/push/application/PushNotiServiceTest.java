package com.mineservice.domain.push.application;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@SpringBootTest
class PushNotiServiceTest {

    @Test
    void dateDiff() {
        LocalDate refDate = LocalDate.of(2022,4,5);

        LocalDate today = LocalDate.now();

        long between = ChronoUnit.DAYS.between(refDate, today);

        switch ((int) between) {
            case 0:
                System.out.println("오늘");
                return;
            case 1:
                System.out.println("어제");
                return;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                System.out.println(between + "일전");
                return;
            case 7:
                System.out.println("일주일전");
                return;
            default:
                System.out.println(refDate.format(DateTimeFormatter.ofPattern("M월 d일")));
                return;
        }


    }

}