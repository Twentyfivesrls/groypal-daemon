package com.example.groypaldaemon.utils;


import com.example.groypaldaemon.model.DateRange;

import java.util.*;

public class DateRangeUtils {
    public static int getTotalSuspendedDays(List<DateRange> dateRanges) {
        if (dateRanges == null || dateRanges.isEmpty()) {
            return 0;
        }

        // Ordina la lista dei DateRange in base alla data di inizio
        Collections.sort(dateRanges);

        // Inizializza una lista per tenere traccia di tutti i giorni inclusi negli intervalli di sospensione
        Set<Date> suspendedDays = new HashSet<>();

        // Itera attraverso la lista dei DateRange e aggiungi tutti i giorni inclusi negli intervalli di sospensione
        for (DateRange range : dateRanges) {
            Date currentDate = range.getStartDate();
            Date endDate = range.getEndDate();

            while (!currentDate.after(endDate)) {
                suspendedDays.add(currentDate);
                currentDate = addDays(currentDate, 1);
            }
        }

        // Calcola la dimensione della lista dei giorni di sospensione
        return suspendedDays.size();
    }

    private static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }
}

