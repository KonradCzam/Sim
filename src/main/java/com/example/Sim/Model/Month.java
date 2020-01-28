package com.example.Sim.Model;

import lombok.Getter;
import org.joda.time.Duration;

import java.util.List;

@Getter
public class Month {
    List<RowData> rowDataList;
    Duration totalWorkTime;
    Duration totalMissedTime;

    public Month(List<RowData> rowDataList) {
        this.rowDataList = rowDataList;
        Duration totalWorkTime = Duration.ZERO;
        Duration totalMissedTime = Duration.ZERO;
        for (int i = 0; i < rowDataList.size(); i++) {
            totalWorkTime = totalWorkTime.plus(rowDataList.get(i).getIloscGodzPracy());
            totalMissedTime = totalMissedTime.plus(rowDataList.get(i).getIloscGodzNieob());
        }
        RowData rowData = new RowData(false, false, false, "", "Razem", null, totalMissedTime, totalWorkTime, "", "", "", "", "", "");
        this.rowDataList.add(rowData);
    }
}
