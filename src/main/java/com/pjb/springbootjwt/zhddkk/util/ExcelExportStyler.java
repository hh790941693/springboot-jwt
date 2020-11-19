package com.pjb.springbootjwt.zhddkk.util;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import cn.afterturn.easypoi.excel.export.styler.AbstractExcelExportStyler;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;

/**
 * excel 通用样式
 * 
 * @author Administrator
 *
 */
public class ExcelExportStyler extends AbstractExcelExportStyler implements IExcelExportStyler {

    public ExcelExportStyler(Workbook workbook) {
        super.createStyles(workbook);
    }

    @Override
    public CellStyle getHeaderStyle(short headerColor) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        titleStyle.setFont(font);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        //titleStyle.setFillForegroundColor(IndexedColors.CORAL.getIndex());
        //titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        titleStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        titleStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        titleStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return titleStyle;
    }

    @Override
    public CellStyle getTitleStyle(short color) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font font = this.workbook.createFont();
        font.setBold(true);
        titleStyle.setFont(font);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        titleStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        titleStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        titleStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setWrapText(true);
        return titleStyle;
    }

    @Override
    public CellStyle stringSeptailStyle(Workbook workbook, boolean isWarp) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setDataFormat(STRING_FORMAT);
        if (isWarp) {
            style.setWrapText(true);
        }
        return style;
    }

    @Override
    public CellStyle stringNoneStyle(Workbook workbook, boolean isWarp) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setDataFormat(STRING_FORMAT);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        if (isWarp) {
            style.setWrapText(true);
        }
        return style;
    }
}