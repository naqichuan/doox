package org.nqcx.doox.commons.util.poi;

import org.apache.poi.ss.usermodel.*;
import org.nqcx.doox.commons.util.date.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ExcelUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * 加载数据
     *
     * @param inputStream inputStream
     * @param sheetindex  sheetindex
     * @return {@link List<Object[]>}
     * @author naqichuan 9/16/21 4:34 PM
     */
    public static List<Object[]> loadData(InputStream inputStream, int sheetindex) {
        Workbook workbook = getWorkbook(inputStream);
        return loadData(workbook, sheetindex);
    }

    public static List<Object[]> loadData(String file, int sheetindex) {
        Workbook wb = getWorkbook(file);
        return loadData(wb, sheetindex);
    }


    /**
     * 获取单元格内容
     *
     * @param cell
     * @return
     */
    private static Object getCellValue(Cell cell) {
        Object value = null;
        switch (cell.getCellType()) {
            case BLANK:
                value = "";
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = DateUtil.getJavaDate(cell.getNumericCellValue());
                    if (value != null) {
                        value = DateFormatUtils.format((Date) value, DateFormatUtils.DATE_FORMAT);
                    }
                } else {
                    value = cell.getNumericCellValue();
                }
                break;
            case STRING:
                value = cell.getRichStringCellValue().toString();
                break;
            case FORMULA:
                value = String.valueOf(cell.getNumericCellValue());
                if (value.equals("NaN")) {
                    // 如果获取的数据值为非法值,则转换为获取字符串
                    value = cell.getRichStringCellValue().toString();
                }
                break;
            case ERROR:
                // 故障
                value = "";
                break;
            default:
                value = cell.getRichStringCellValue().toString();
        }
        return value;
    }

    /**
     * 获取excel对象
     *
     * @param inputStream
     * @return
     */
    public static Workbook getWorkbook(InputStream inputStream) {
        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            LOGGER.error("", e);
        }
        return wb;
    }

    public static Workbook getWorkbook(String file) {
        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(new File(file));
        } catch (IOException e) {
            LOGGER.error("", e);
        }
        return wb;
    }

    /**
     * 加载数据
     *
     * @param wb
     * @param sheetindex
     * @return
     */
    private static List<Object[]> loadData(Workbook wb, int sheetindex) {
        List<Object[]> rows = new ArrayList<>();

        Sheet sheet = wb.getSheetAt(sheetindex);

        // sheet name
        String sheetName = sheet.getSheetName();
        LOGGER.info("Sheet name \"{}\"", sheetName);

        // 取标题，第一行
        Row titleRow = sheet.getRow(0);
        int cellCount = titleRow.getPhysicalNumberOfCells();

        int rowCount = sheet.getPhysicalNumberOfRows();
        // 遍历该行所有的行,j表示行数 getPhysicalNumberOfRows行的总数
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            try {
                Row sheetRow = sheet.getRow(rowIndex);
                if (sheetRow == null)
                    continue;

                Object[] row = new Object[cellCount + 1];
                Arrays.fill(row, null);
                row[0] = rowIndex;

                for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
                    Cell cell = sheetRow.getCell(cellIndex);
                    if (cell != null) {
                        row[cellIndex + 1] = getCellValue(cell);
                    }
                }

                rows.add(row);
            } catch (Exception e) {
                LOGGER.error("", e);
            }
        }
        return rows;
    }

    public static void writeData(Sheet sheet, Integer rowIndex, Integer column, String value) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        Cell cell = row.getCell(column);
        if (cell == null) {
            cell = row.createCell(column);
        }
        cell.setCellValue(value);
    }
}
