package com.example.Sim.services;

import com.example.Sim.Model.MissedDay;
import com.example.Sim.Model.Month;
import com.example.Sim.Model.RowData;
import com.example.Sim.Model.WorkHours;
import org.joda.time.Duration;
import org.joda.time.LocalTime;

import java.time.DayOfWeek;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class DataGenerator {
    public Month generateMonth(YearMonth yearMonthObject, String godzinyPracy, List<Integer> dyzuryList, List<MissedDay> missedDayList, List<Integer> extraFreeDays, Boolean isL4) {

        int daysInMonth = yearMonthObject.lengthOfMonth();
        List<RowData> rowDataList = new ArrayList<>();
        for (Integer i = 1; i <= daysInMonth; i++) {
            Boolean isFree = checkIfFree(yearMonthObject, i, extraFreeDays);
            RowData rowData;
            if (isFree) {
                rowData = new RowData(isFree, false, false, i.toString(), "", "", null, null, "", "", "", "", "", "");
                if (dyzuryList.contains(i)) {
                    rowData = new RowData(isFree, true, false, i.toString(), "", "0800-", Duration.ZERO, Duration.standardDays(1), "", "", "", "", "", "");
                }
                if (dyzuryList.contains(i - 1)) {
                    rowData = new RowData(isFree, false, true, i.toString(), "", "0800", Duration.ZERO, Duration.ZERO, "", "", "", "", "", "");
                    }
            } else {
                WorkHours workHours = calculateWorkHours(godzinyPracy);
                if (isL4) {
                    rowData = new RowData(isFree, false, false, i.toString(), "CH", godzinyPracy, workHours.getWorkTime(), Duration.ZERO, "", "", "", "", "", "");
                    rowDataList = handleMissedDays(rowDataList, missedDayList);
                } else {
                    rowData = new RowData(isFree, false, false, i.toString(), "", godzinyPracy, Duration.ZERO, workHours.getWorkTime(), "", "", "", "", "", "");
                }
                if (dyzuryList.contains(i)) {
                    rowData = new RowData(isFree, true, false, i.toString(), "", "0730-", Duration.ZERO, Duration.standardDays(1), "", "", "", "", "", "");
                }
                if (dyzuryList.contains(i - 1)) {
                    if (yearMonthObject.atDay(i).getDayOfWeek() == DayOfWeek.MONDAY)
                        rowData = new RowData(isFree, false, true, i.toString(), "", "0800", Duration.ZERO, Duration.ZERO, "", "", "", "", "", "");
                    else
                        rowData = new RowData(isFree, false, true, i.toString(), "", "0800", Duration.ZERO, Duration.ZERO, "", "", "", "", "", "");
                }
            }
            /*if(i == 4 || i ==6 || i ==12 || i ==14 )
                rowData = addOvertime(rowData,1);
            if(i == 5)
                rowData = addOvertime(rowData,2);*/

            rowDataList.add(rowData);

        }
        rowDataList = handleMissedDays(rowDataList, missedDayList);
        return new Month(rowDataList);
    }

    private RowData addOvertime(RowData rowData, Integer overhoursCount) {
        String godzinyPracy = rowData.getGodzPracy();
        Integer integer = Integer.parseInt(godzinyPracy.substring(godzinyPracy.length()-3,godzinyPracy.length()-2));
        integer += overhoursCount;
        godzinyPracy = godzinyPracy.substring(0,godzinyPracy.length()-3) + integer.toString() + godzinyPracy.substring(godzinyPracy.length()-2);
        rowData.setGodzPracy(godzinyPracy);
        WorkHours workHours = calculateWorkHours(godzinyPracy);
        rowData.setIloscGodzPracy(workHours.getWorkTime());
        rowData.setSumNadgodz(overhoursCount.toString());
        return rowData;
    }

    private List<RowData> handleMissedDays(List<RowData> rowDataList, List<MissedDay> missedDayList) {
        missedDayList.stream().forEach(missedDay -> {
            int dayOfMonth = missedDay.getDayNumber() - 1;
            rowDataList.get(dayOfMonth).setIloscGodzNieob(rowDataList.get(dayOfMonth).getIloscGodzPracy());
            rowDataList.get(dayOfMonth).setIloscGodzPracy(Duration.ZERO);
            rowDataList.get(dayOfMonth).setKodNieob(missedDay.getReasonCode());
        });
        return rowDataList;
    }

    private WorkHours calculateWorkHours(String godzinyPracy) {
        Integer startMin = Integer.parseInt(godzinyPracy.substring(2, 4));
        Integer startHour = Integer.parseInt(godzinyPracy.substring(0, 2));
        Integer endHour = Integer.parseInt(godzinyPracy.substring(5, 7));
        Integer endMin = Integer.parseInt(godzinyPracy.substring(7, 9));

        return new WorkHours(new LocalTime(startHour, startMin), new LocalTime(endHour, endMin));
    }

    private Boolean checkIfFree(YearMonth yearMonthObject, Integer i, List<Integer> extraFreeDays) {
        DayOfWeek currentDayOfWeek = yearMonthObject.atDay(i).getDayOfWeek();
        if (currentDayOfWeek == DayOfWeek.SATURDAY || currentDayOfWeek == DayOfWeek.SUNDAY)
            return true;
        else if (extraFreeDays.contains(i)) {
            return true;
        } else
            return false;
    }

}
