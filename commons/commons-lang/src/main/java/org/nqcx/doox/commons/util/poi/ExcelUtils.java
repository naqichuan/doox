package org.nqcx.doox.commons.util.poi;

import org.apache.poi.ss.usermodel.*;
import org.nqcx.doox.commons.util.date.DateFormatUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelUtils {

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
            e.printStackTrace();
        }
        return wb;
    }

    public static Workbook getWorkbook(String file) {
        Workbook wb = null;
        try {
            File f = new File(file);
            wb = WorkbookFactory.create(f);
        } catch (IOException e) {
            e.printStackTrace();
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
        List<Object[]> dataList = new ArrayList<Object[]>();
        Sheet sheet = wb.getSheetAt(sheetindex);
        // 遍历该行所有的行,j表示行数 getPhysicalNumberOfRows行的总数
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            try {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                int cols = (int) row.getLastCellNum();
                if (cols <= 0) {
                    continue;
                }
                Object[] objectArray = new Object[cols];
                for (int j = 0; j < objectArray.length; j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        objectArray[j] = getCellValue(cell);
                    } else {
                        objectArray[j] = null;
                    }
                }
                dataList.add(objectArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dataList;
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
