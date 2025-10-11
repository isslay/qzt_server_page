package com.qzt.ump.server.message;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 导出管理控制器
 * </p>
 *
 * @author Xiaofei
 * @since 2018-07-12
 */
@Component
public class ExportUtils {

    @Autowired
    public ExportConfig exportConfig;

    /**
     * 导出通用方法
     *
     * @param title         表格第一行标题
     * @param templateName  模板文件名称
     * @param newFileName   新生成的文件名
     * @param mapList       数据集合 list中的map 必须是LinkedHashMap,按照put的先后排序   防止数据与标题头不对应
     * @param digitPosition 数字索引集合，用于处理导出的表格格式为数字类型
     * @param h             从表格第几行开始 0开始
     * @author Xiaofei
     * @date 2019-07-23
     */
    public void exportExcel(HttpServletResponse response, String title, String templateName, String newFileName, List<Map<String, Object>> mapList, Integer[] digitPosition, Integer h) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(new File(this.exportConfig.getCatalogue() + templateName));
            XSSFWorkbook workbook = new XSSFWorkbook(in);
            XSSFSheet sheet = workbook.getSheetAt(0);
            if (sheet != null) {
                XSSFRow row = sheet.getRow(0);
                if (row == null) {
                    row = sheet.createRow(0);
                }
                XSSFCell cell = row.getCell(0);
                if (cell == null) {
                    cell = row.createCell(0);
                }
                cell.setCellValue(title);
                List<Integer> integers = Arrays.asList(digitPosition);//数字格式索引
                for (int i = 0; i < mapList.size(); i++) {
                    Map<String, Object> map = mapList.get(i);
                    Object[] values = map.values().toArray();
                    row = sheet.createRow(i + h); //从第三行开始
                    //根据excel模板格式写入数据....
                    for (int k = 0; k < values.length; k++) {
                        double height= 400 ;
                        row.setHeight((short)height);
                        cell = row.getCell(k) == null ? row.createCell(k) : row.getCell(k);
                        if (integers.contains(k)) {//特殊标记索引特殊处理
                            BigDecimal money = values[k] == null ? BigDecimal.ZERO : ((BigDecimal) values[k]);
                            cell.setCellValue(money.doubleValue());
                        } else {
                            cell.setCellValue(values[k] == null ? "" : values[k].toString());
                        }
                    }
                }
            }
            String pageName = newFileName + System.currentTimeMillis() + ".xlsx";
            setResponseHeader(response, pageName);
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setResponseHeader(HttpServletResponse response, String fileName) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setDateHeader("Expires", 0);
    }

    /**
     * 根据当前row行，来创建index标记的列数,并赋值数据
     *
     * @author Xiaofei
     * @date 2019-07-23
     */
    private void createRowAndCell(Object obj, XSSFRow row, XSSFCell cell, int index) {
        cell = row.getCell(index) == null ? row.createCell(index) : row.getCell(index);
        cell.setCellValue(obj == null ? "" : obj.toString());
    }

}