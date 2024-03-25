package com.example.groypaldaemon.model;


import com.example.groypaldaemon.utils.DateRangeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscription {
    private String id;
    private String paymentId; // todo understand if paypal sends string or long
    private String customId; // ID che collega tutte le classi figlio a UserSubscription
    private String userId;
    private Date startDate;
    private Subscription subscriptionData;
    private List<DateRange> deactivationRanges;
    private String orderData;

    public boolean isExpired(){
        // all this shit is done to ensure that the subscription expires at least at the end of the day.
        // Basically, user has max 1 day more than the duration as present
        //TODO da testare
        Calendar c = Calendar.getInstance();
        c.setTime(this.startDate);
        Integer integerValue = Math.toIntExact(this.subscriptionData.getDuration());
        c.add(Calendar.DATE, integerValue);
        // ignoring ranges that are included in other ranges to skip days that doesn't need to be included
        Integer differencesBetweenRanges = DateRangeUtils.getTotalSuspendedDays(this.deactivationRanges);
        c.add(Calendar.DATE, differencesBetweenRanges);
        Date expiringDate = c.getTime(); //data di scadenza
        Date today = new Date();
        c.setTime(today);
        c.add(Calendar.DATE, 1);
        today = c.getTime();
        return today.after(expiringDate);
    }
}
