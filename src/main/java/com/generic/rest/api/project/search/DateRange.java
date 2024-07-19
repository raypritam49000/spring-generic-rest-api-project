package com.generic.rest.api.project.search;

import org.apache.commons.lang3.time.DateUtils;

import java.time.Instant;
import java.util.Date;

public class DateRange {

    private Date startDate;

    private Date endDate;

    public DateRange() {
    }

    private DateRange(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static DateRange create(Date startDate, Date endDate, boolean useDayInclusiveTime) {
        DateRange dateRange = createConstrainedDateRange(new DateRange(startDate, endDate));

        if (useDayInclusiveTime) {
            dateRange.setStartDate(getDayInclusiveDate(dateRange.getStartDate(), true));
            dateRange.setEndDate(getDayInclusiveDate(dateRange.getEndDate(), false));
        }

        return dateRange;
    }

    private static Date getDayInclusiveDate(Date value, boolean startOfDay) {
        if (value == null) {
            return null;
        }

        if (startOfDay) {
            value = DateUtils.setHours(value, 0);
            value = DateUtils.setMinutes(value, 0);
            value = DateUtils.setSeconds(value, 0);
        } else {
            value = DateUtils.setHours(value, 23);
            value = DateUtils.setMinutes(value, 59);
            value = DateUtils.setSeconds(value, 59);
        }

        return value;
    }

    private static DateRange createConstrainedDateRange(DateRange value) {

        if (!isValid(value)) {
            return value;
        }

        if (value.startDate == null) {
            value.startDate = new Date(0);
        }

        if (value.endDate == null) {
            value.endDate = DateUtils.addYears(new Date(), 100);
        }

        return value;
    }

    public boolean containsInRange(Date value) {
        return value != null && value.after(startDate) && value.before(endDate);
    }

    public boolean containsInRange(Instant value) {
        return containsInRange(Date.from(value));
    }

    private static boolean isValid(DateRange value) {
        return value.startDate != null || value.endDate != null;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "DateRange{" + "startDate=" + startDate + ", endDate=" + endDate + '}';
    }


}