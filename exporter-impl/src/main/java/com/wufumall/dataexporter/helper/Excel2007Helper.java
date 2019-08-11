package com.wufumall.dataexporter.helper;

import org.apache.commons.lang.StringUtils;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * ExcelHelper po
 */
public class Excel2007Helper {

    private SXSSFWorkbook workbook;
    private SXSSFSheet sheet;
    private SXSSFRow row;
    private SXSSFCell cell;
    private XSSFCellStyle cellStyle;
    private XSSFCellStyle headerStyle;

    /**
     * 新建一个Excel文件
     * @param sheetName
     */
    public Excel2007Helper(String sheetName) {
        workbook = new SXSSFWorkbook();
        sheet = (SXSSFSheet) workbook.createSheet(sheetName);
        this.initStyle();
    }

    /**
     * 初始化样式
     */
    private void initStyle() {
        //设置表头样式
        headerStyle = (XSSFCellStyle) workbook.createCellStyle();

        //设置单元格样式
        cellStyle = (XSSFCellStyle) workbook.createCellStyle();
    }

    /**
     * 获取Workbook
     * @return
     */
    public SXSSFWorkbook getWorkbook() {
        return workbook;
    }

    /**
     * 获取Sheet
     *
     * @return
     */
    public SXSSFSheet getSheet() {
        return sheet;
    }

    /**
     * 获取行
     * @param rownum
     * @return
     */
    public SXSSFRow getRow(int rownum) {
        return (SXSSFRow) sheet.getRow(rownum);
    }

    /**
     * 获取单元格
     * @param rownum
     * @param column
     * @return
     */
    public SXSSFCell getCell(int rownum, short column) {
        row = (SXSSFRow) sheet.getRow(rownum);
        if (row == null) {
            return null;
        }
        return (SXSSFCell) row.getCell(column);
    }

    /**
     * 获取单元格的值（字符串类型）
     * @param rownum
     * @param column
     * @return
     */
    public String getCellStringValue(int rownum, short column) {
        cell = this.getCell(rownum, column);
        if (cell == null) {
            return null;
        }

        String value = null;
        int type = cell.getCellType();
        if (type == XSSFCell.CELL_TYPE_NUMERIC) {
            BigDecimal decimal = new BigDecimal(cell.getNumericCellValue());
            value = decimal.toPlainString();
        } else if (type == XSSFCell.CELL_TYPE_STRING) {
            value = cell.getRichStringCellValue().getString();
        } else if (type == XSSFCell.CELL_TYPE_BLANK) {
            value = "";
        } else if (type == XSSFCell.CELL_TYPE_BOOLEAN) {
            value = String.valueOf(cell.getBooleanCellValue());
        }
        if (value != null) {
            value = value.trim();
        }
        return value;
    }

    /**
     * 获取单元格的值（Integer类型）
     * @param rownum
     * @param column
     * @return
     */
    public Integer getCellIntegerValue(int rownum, short column) {
        cell = this.getCell(rownum, column);
        if (cell == null) {
            return null;
        }

        Integer value = null;
        int type = cell.getCellType();
        if (type == XSSFCell.CELL_TYPE_NUMERIC) {
            BigDecimal decimal = new BigDecimal(cell.getNumericCellValue());
            value = decimal.intValue();
        } else if (type == XSSFCell.CELL_TYPE_STRING) {
            String strValue = cell.getRichStringCellValue().getString();
            if (StringUtils.isNumeric(strValue)) {
                value = Integer.parseInt(strValue);
            }
        }

        return value;
    }

    /**
     * 获取单元格的值（Double类型）
     * @param rowNum
     * @param column
     * @return
     */
    public Double getCellNumericValue(int rowNum, short column) {
        cell = this.getCell(rowNum, column);
        if (cell == null) {
            return null;
        }

        Double value = null;
        int type = cell.getCellType();
        if (type == XSSFCell.CELL_TYPE_NUMERIC) {
            value = cell.getNumericCellValue();
        } else if (type == XSSFCell.CELL_TYPE_STRING) {
            String strValue = cell.getRichStringCellValue().getString();
            try {
                value = Double.parseDouble(strValue);
            } catch (NumberFormatException e) {
            }
        }

        return value;
    }

    /**
     * 获取总行数（包括标题）
     * @return
     */
    public int getTotalRowCount() {
        return sheet.getLastRowNum() + 1;
    }

    /**
     * 获取总列数
     * 如果第一行为空，返回-1
     * @return
     */
    public int getTotalCellCount() {
        row = (SXSSFRow) sheet.getRow(0);
        if (row == null) {
            return -1;
        }
        return row.getLastCellNum();
    }

    /**
     * 设置表头
     * @param column
     * @param width
     * @param title
     * @throws Exception
     */
    public void setHeader(short column, int width, String title) throws Exception {
        row = (SXSSFRow) ((sheet.getRow(0) == null) ? sheet.createRow(0) : sheet.getRow(0));
        cell = (SXSSFCell) ((row.getCell(column) == null) ? row.createCell(column) : row.getCell(column));

        cell.setCellStyle(headerStyle);
        cell.setCellValue(new XSSFRichTextString(title));

        sheet.setDefaultColumnStyle(column, cellStyle);
        sheet.setColumnWidth(column, (short) (width * 1000));
    }


    /**
     * 设置单元格的值
     * @param rownum
     * @param column
     * @param value
     */
    public void setCellValue(int rownum, int column, Object value) {
        row = (SXSSFRow) ((sheet.getRow(rownum) == null) ? sheet.createRow(rownum) : sheet.getRow(rownum));
        cell = (SXSSFCell) ((row.getCell(column) == null) ? row.createCell(column) : row.getCell(column));

        if (value instanceof Calendar) {
            cell.setCellValue((Calendar) value);
        } else if (value instanceof Date) {
            cell.setCellValue(new XSSFRichTextString(value + ""));
        } else if (value instanceof Number) {
            cell.setCellValue(Double.parseDouble(value+""));
        } else if (value instanceof String) {
            cell.setCellValue(new XSSFRichTextString(value + ""));
        } else {
            cell.setCellValue(value+"");
        }
    }

    /**
     * 合并单元格
     * @param rowFrom
     * @param colFrom
     * @param rowTo
     * @param colTo
     */
    public void addMergedRegion(int rowFrom, short colFrom, int rowTo, short colTo) {
        sheet.addMergedRegion(new CellRangeAddress(rowFrom, rowTo, colFrom, colTo));
    }

    /**
     * 将Excel输出到OutputStream
     * @param out
     * @throws java.io.IOException
     */
    public void write(OutputStream out) throws IOException {
        workbook.write(out);
    }

    
    
    public void flushToTemp() {
        try {
            sheet.flushRows();
        } catch (Exception e) {
            throw new RuntimeException("使用POI新特性,刷新数据到硬盘,失败,{" + e.getMessage() + "}");
        }
    }

}
