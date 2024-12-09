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
    public static class ToothReport implements Comparable<ToothReport>{
        private String name;
        private String percent;
        private String description;

        @Override
        public int compareTo(ToothReport other) {
            // null 체크
            if (this.name == null && other.name == null) {
                return 0;
            }
            if (this.name == null) {
                return -1;
            }
            if (other.name == null) {
                return 1;
            }
            
            // name을 기준으로 오름차순 정렬
            return this.name.compareTo(other.name);
        }
    }
}
