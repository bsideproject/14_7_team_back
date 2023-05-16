package com.mineservice.domain.user.domain;


/**
 * 요일구분
 */
public enum DayType {
    WEEKDAY("평일", 10),
    MON("월", 20),
    TUE("화", 30),
    WED("수", 40),
    THU("목", 50),
    FRI("금", 60),
    SAT("토", 70),
    SUN("일", 80),
    WEEKEND("주말", 90),
    EVERYDAY("매일", 100);

    private final String code;
    private final int order;

    DayType(String code, int order) {
        this.code = code;
        this.order = order;
    }

    public static DayType get(String code) {
        for (DayType element : values()) {
            if (element.code.equalsIgnoreCase(code)) {
                return element;
            }
        }
        return null;
    }

    public static DayType lookup(String code) {
        for (DayType element : DayType.values()) {
            if (element.code.equalsIgnoreCase(code)) {
                return element;
            }
        }
        return null;
    }


    public String getCode() {
        return code;
    }

    public int getOrder() {
        return order;
    }
}
