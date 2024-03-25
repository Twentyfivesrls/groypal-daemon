package com.example.groypaldaemon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateRange implements Comparable<DateRange>{
    private Date startDate;
    private Date endDate;
    @Override
    public int compareTo(DateRange other) {
        int startComparison = this.startDate.compareTo(other.startDate);
        if (startComparison != 0) {
            return startComparison; // Se le date di inizio sono diverse, restituisci la comparazione tra di loro
        } else {
            return this.endDate.compareTo(other.endDate); // Altrimenti, confronta le date di fine
        }
    }
}
