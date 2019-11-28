package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Model.MissedDay;
import com.example.Sim.Model.Month;
import com.example.Sim.Model.MonthNames;
import com.example.Sim.Model.RowData;
import com.example.Sim.services.DataGenerator;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.joda.time.Duration;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Service
public class StartController implements Initializable, DialogController {

    private Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
    private Font small = new Font(Font.FontFamily.HELVETICA, 6);
    private Font headerFont = new Font(Font.FontFamily.HELVETICA, 12);
    private Font footer = new Font(Font.FontFamily.HELVETICA, 11);
    private ScreensConfiguration screens;
    private FXMLDialog dialog;
    @FXML
    TextField dyzurTextField;
    @FXML
    TextField worktimeTextField;
    @FXML
    TextField nameSurnameTextField;
    @FXML
    TextField orgTextField;
    @FXML
    TextField extraDaysTextField;
    @FXML
    TextField extraCodesTextField;
    @FXML
    TextField extraHolidaysTextField;
    @FXML
    CheckBox LQuatro;
    @FXML
    DatePicker datePicker;

    static final Integer CELL_HEIGH = 15;
    static final String TITLE = "KARTA EWIDENCJI CZASU PRACY";
    static final String NAME = "Nazwisko i imie:";
    static final String ORG = "Kom. organiz.:";
    static final String FOOTER1 = "W przypadku nieobecności wprowadzić kod wg poniższego wykazu:\n" +
            "NN - nieobecność nieusprawiedliwiona\n" +
            "NU - nieobecność usprawiedliwiona\n" +
            "UW - urlop wypoczynkowy\n" +
            "USZ - urlop szkoleniowy\t\n" ;
    static final String FOOTER2 = "UB - Urlop Bezplatny";
    static final String FOOTER3 = "Podpis Kierownika komórki org.\n";
    static final String FOOTER4 = "CH - choroba\n" +
            "OP - opieka\n" +
            "Uwych - Urlop wychowawczy\n" +
            "UM - urlop macierzyński\n" +
            "MD - matka/dziecko\n";


    public StartController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        datePicker.setValue(localDate);

    }

    public void generatePDF() {

        try {
            Document document = new Document(PageSize.A4, 20f, 20f, 20f, 20f);
            String fileName;
            java.time.Month monthNumber = datePicker.getValue().getMonth();
            MonthNames monthName = new MonthNames();

            fileName = nameSurnameTextField.getText() + " ";
            fileName += monthName.getMonth(monthNumber.getValue())   + datePicker.getValue().getYear() + ".pdf";

            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            document.open();

            PdfPTable table = new PdfPTable(12);
            table.setWidthPercentage(95);
            table.setTotalWidth(new float[]{20, 40, 45, 40, 40, 60, 50, 30, 30, 40, 30, 100});
            table.setLockedWidth(true);

            table.setPaddingTop(3);
            addDocumentHeader(document);
            addTableHeader(table);
            fillTable(table);
            document.add(table);
            addFooter(document);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void addFooter(Document document) throws DocumentException {
        document.add(new Paragraph(FOOTER1, footer));
        Paragraph content = new Paragraph(FOOTER2 +
                "                                                                                          "
                + FOOTER3, footer);
        document.add(content);
        document.add(new Paragraph(FOOTER4, footer));
    }

    private void addDocumentHeader(Document document) throws DocumentException {
        Paragraph titleParagraph = new Paragraph(TITLE, headerFont);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(titleParagraph);
        document.add(new Paragraph(NAME + nameSurnameTextField.getText(), headerFont));
        document.add(new Paragraph(ORG + orgTextField.getText(), headerFont));
        java.time.Month monthNumber = datePicker.getValue().getMonth();
        MonthNames monthName = new MonthNames();

        Paragraph monthParagraph = new Paragraph(monthName.getMonth(monthNumber.getValue()) + " " + datePicker.getValue().getYear() + "     ", headerFont);
        monthParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(monthParagraph);
        document.add(new Paragraph(" ", new Font(Font.FontFamily.HELVETICA, 4)));


    }

    private void fillTable(PdfPTable table) {
        DataGenerator dataGenerator = new DataGenerator();
        List<Integer> dyzurList = new ArrayList<>();
        List<Integer> extraFreeList = new ArrayList<>();
        List<MissedDay> missedDayList = new ArrayList<>();
        if (!dyzurTextField.getText().isEmpty()) {
            List<String> dyzurListString = Arrays.asList(dyzurTextField.getText().split("\\s+"));
            dyzurListString.stream().forEach(dyzur -> dyzurList.add(Integer.parseInt(dyzur)));
        }
        if (!extraDaysTextField.getText().isEmpty()) {
            missedDayList = createMissedDayList();
        }
        if (!extraHolidaysTextField.getText().isEmpty()) {
            List<String> extraFreeListString = Arrays.asList(extraHolidaysTextField.getText().split("\\s+"));
            extraFreeListString.stream().forEach(dyzur -> extraFreeList.add(Integer.parseInt(dyzur)));
        }

        YearMonth yearMonthObject = YearMonth.of(datePicker.getValue().getYear(), datePicker.getValue().getMonth());
        Month currentMonth = dataGenerator.generateMonth(yearMonthObject, worktimeTextField.getText(), dyzurList,missedDayList,extraFreeList,LQuatro.isSelected());
        currentMonth.getRowDataList().stream().forEach(row -> addRow(table, row));
    }



    private List<MissedDay> createMissedDayList() {
        List<MissedDay> missedDayList = new ArrayList<>();
        List<String> extraDaysListString = Arrays.asList(extraDaysTextField.getText().split("\\s+"));
        List<String> extraCodeListString = Arrays.asList(extraCodesTextField.getText().split("\\s+"));
        for (Integer i = 0; i < extraDaysListString.size(); i++) {
            MissedDay missedDay = new MissedDay(Integer.parseInt(extraDaysListString.get(i)),extraCodeListString.get(i));
            missedDayList.add(missedDay);
        }
        return missedDayList;
    }

    public void quit() {
        Runtime.getRuntime().exit(0);
    }


    private void addRow(PdfPTable table, RowData row) {
        addRowCell(table, row.getLp(), row.getIsFree());
        addRowCell(table, row.getKodNieob(), row.getIsFree());

        PdfPCell cell = new PdfPCell();
        cell.setFixedHeight(CELL_HEIGH);
        setColor(cell, row.getIsFree());

        if (row.getIsFree() && !row.getIsDyzur() && !row.getIsAfterDyzur()) {
            table.addCell(cell);
            table.addCell(cell);
            table.addCell(cell);

        } else {
            cell = new PdfPCell(createTimePhrase(row.getGodzPracy(), row.getIsDyzur(), row.getIsAfterDyzur()));
            setColor(cell, row.getIsFree());
            table.addCell(cell);
            cell = new PdfPCell(formatTime(row.getIloscGodzNieob()));
            setColor(cell, row.getIsFree());
            table.addCell(cell);
            cell = new PdfPCell(formatTime(row.getIloscGodzPracy()));
            setColor(cell, row.getIsFree());
            table.addCell(cell);

        }


        addRowCell(table, row.getGotowosc(), row.getIsFree());
        addRowCell(table, row.getWezwanie(), row.getIsFree());
        addRowCell(table, row.getDod50(), row.getIsFree());

        addRowCell(table, row.getDod100(), row.getIsFree());
        addRowCell(table, row.getSumNadgodz(), row.getIsFree());
        addRowCell(table, row.getDod20(), row.getIsFree());
        addRowCell(table, "", row.getIsFree());
    }

    private void setColor(PdfPCell cell, Boolean isFree) {
        if (isFree) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        }
    }

    private Paragraph formatTime(Duration duration) {
        if (duration.equals(Duration.ZERO)) {
            return new Paragraph("");
        }
        String minutes = String.format("%02d", duration.toPeriod().getMinutes());
        String hours = String.format("%02d", duration.toPeriod().getHours());

        Chunk startMinutes = new Chunk(minutes, small);
        startMinutes.setTextRise(6);

        Paragraph paragraph = new Paragraph();
        paragraph.add(new Phrase(hours, font));
        paragraph.add(startMinutes);
        return paragraph;
    }

    private void addRowCell(PdfPTable table, String text, Boolean isFree) {
        PdfPCell header = new PdfPCell(new Phrase(text, font));
        setColor(header, isFree);
        header.setFixedHeight(CELL_HEIGH);
        table.addCell(header);
    }

    private Paragraph createTimePhrase(String godzPracy, Boolean isDyzur, Boolean isAfterDyzur) {
        if (godzPracy == null) {
            return new Paragraph();
        }
        Chunk startMinutes = new Chunk(godzPracy.substring(2, 4), small);
        startMinutes.setTextRise(6);

        Paragraph paragraph = new Paragraph();

        if (isAfterDyzur) {
            paragraph.add(new Phrase("-", font));
        }
        paragraph.add(new Phrase(godzPracy.substring(0, 2), font));
        paragraph.add(startMinutes);
        if (isDyzur) {
            paragraph.add(new Phrase("-", font));
        }
        //add end time if its not dyzur
        if (!isDyzur && !isAfterDyzur) {
            Chunk endMinutes = new Chunk(godzPracy.substring(7, 9), small);
            endMinutes.setTextRise(6);
            paragraph.add(new Phrase("-", font));
            paragraph.add(new Phrase(godzPracy.substring(5, 7), font));
            paragraph.add(endMinutes);
        }
        return paragraph;
    }

    private void addTableHeader(PdfPTable table) {
        addTableMainHeader(table);
        addTableSubHeader(table);
    }

    private void addTableMainHeader(PdfPTable table) {
        addHeaderCell(table, "Lp.", 1, 2);
        addHeaderCell(table, "Kod nieob.*", 1, 2);
        addHeaderCell(table, "Godziny pracy od-do", 1, 2);
        addHeaderCell(table, "Ilosc godzin", 2, 1);
        addHeaderCell(table, "Gotowosc pod tel. od-do", 1, 2);
        addHeaderCell(table, "Wezwanie od-do", 1, 2);
        addHeaderCell(table, "Dyzur Medyczny", 4, 1);
        addHeaderCell(table, "Podpis", 1, 2);
    }

    private void addHeaderCell(PdfPTable table, String text, int colSpan, int rowSpan) {
        PdfPCell header;
        header = new PdfPCell();
        header.setBorderWidth(1);
        header.setPhrase(new Phrase(text, FontFactory.getFont(FontFactory.COURIER, 8)));
        header.setColspan(colSpan);
        header.setRowspan(rowSpan);

        table.addCell(header);
    }

    private void addTableSubHeader(PdfPTable table) {
        addHeaderCell(table, "Nieob.", 1, 1);
        addHeaderCell(table, "Pracy", 1, 1);
        addHeaderCell(table, "Dod. 50%", 1, 1);
        addHeaderCell(table, "Dod. 100%", 1, 1);
        addHeaderCell(table, "Suma nadgodz", 1, 1);
        addHeaderCell(table, "Dod. 20%", 1, 1);
    }
}
