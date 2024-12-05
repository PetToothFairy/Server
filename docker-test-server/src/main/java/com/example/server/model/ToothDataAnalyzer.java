package com.example.server.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ToothDataAnalyzer {
    private List<ToothReport> reports;
    private int seq = 0;

    @Getter
    @Setter
    public static class ToothReport {
        private String name;
        private String percent;
        private String description;
    }
}
