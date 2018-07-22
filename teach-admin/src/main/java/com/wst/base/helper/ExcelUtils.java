/*
 * ExcelUtils.java Created on 2017年11月15日 下午3:46:38
 * Copyright (c) 2017 HeWei Technology Co.Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wst.base.helper;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.util.DateUtils;

/**
 * 
 * @author <a href="mailto:lanwc@hiwitech.com">lanweicheng</a>
 * @version 1.0
 */
public class ExcelUtils {
    private static Logger log = Logger.getLogger(ExcelUtils.class);

    /**
     * 读取excel（poi）
     * 
     * @author liqg @creationDate. 2015-8-26 下午2:59:41
     * @param file
     *            excel文件
     * @param sheetNo
     *            工作表No，默认为第一个工作表
     * @param startRow
     *            开始行数，以1开始
     * @return
     */
    public static ResultDTO<List<List<String>>> readExcel(MultipartFile file, int sheetNo, int startRow) {
        ResultDTO<List<List<String>>> resultDto = new ResultDTO<>();
        List<List<String>> list = new ArrayList<List<String>>();
        if (file == null) {
            return resultDto.set(ResultCodeEnum.ERROR_HANDLE, "文件异常！请先选择文件！");
        } else {
            Workbook wb = null;
            String fileName = "";
            try {
                fileName = file.getOriginalFilename();
                if (fileName.endsWith("xls")) { // 解析xls格式
                    wb = new HSSFWorkbook(file.getInputStream());
                } else if (fileName.endsWith("xlsx")) { // 解析xlsx格式
                   // wb = new XSSFWorkbook(file.getInputStream());
                	//解决poi与excel版本不匹配问题
                    wb = WorkbookFactory.create(file.getInputStream());
                } else {
                    return resultDto.set(ResultCodeEnum.ERROR_HANDLE, "文件格式不正确。请上传xls、xlsx文件！");
                }

                if (wb != null) {
                    Sheet sheet = wb.getSheetAt(sheetNo);// 第一个工作表
                    if (sheet == null) {
                        return resultDto.set(ResultCodeEnum.ERROR_HANDLE, "第" + sheetNo + "工作表不存在！");
                    } else {
                        int totalRowNum = sheet.getLastRowNum();
                        if (totalRowNum + 2 - startRow <= 0) {
                            return resultDto.set(ResultCodeEnum.ERROR_HANDLE, "无数据！请先填写数据后重新上传！");
                        } else {
                            for (int rowNum = startRow - 1; rowNum <= totalRowNum; rowNum++) {
                                Row row = sheet.getRow(rowNum);
                                if (row != null) {
                                    List<String> rowList = new ArrayList<String>();

                                    for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
                                        Cell cell = row.getCell(cellNum);
                                        rowList.add(ExcelUtils.getStringCellValue(cell));
                                    }
                                    list.add(rowList);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("解析Excel异常。文件名：" + fileName, e);
                return resultDto.set(ResultCodeEnum.ERROR_HANDLE, "解析Excel失败！服务器异常！");
            } finally {
                try {
                    if (wb != null) {
                        wb.close();
                    }
                } catch (IOException e) {
                }
            }
        }

        return resultDto.set(ResultCodeEnum.SUCCESS, "", list);
    }

    /**
     * 获取单元格的字符值
     * 
     * @author liqg @creationDate. 2016-1-21 下午3:35:06
     * @param cell
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getStringCellValue(Cell cell) {
        String strCell = "";
        if (cell == null) {
            return "";
        }
        DecimalFormat df = new DecimalFormat("#.##");
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_FORMULA: // 公式
                // cell.getCellFormula();
                try {
                    /*
                     * 此处判断使用公式生成的字符串有问题，因为HSSFDateUtil.isCellDateFormatted(
                     * cell) 判断过程中cell .getNumericCellValue();方法会抛出java.lang.
                     * NumberFormatException异常
                     */

                    if (DateUtil.isCellDateFormatted(cell)) { //
                        Date date = cell.getDateCellValue();
                        strCell = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                        break;
                    } else {
                        strCell = String.valueOf(cell.getNumericCellValue());
                    }
                } catch (IllegalStateException e) {
                    strCell = String.valueOf(cell.getRichStringCellValue());
                }
                break;
            case Cell.CELL_TYPE_STRING: // 字符串
                strCell = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC: // 数值
                if (DateUtil.isCellDateFormatted(cell)) {
                    strCell = DateUtils.convertDateToString(cell.getDateCellValue(), DateUtils.YYYY_MM_DD_HH_MM_SS);
                    break;
                } else {
                    strCell = df.format(cell.getNumericCellValue()).toString();
//                    if (strCell.endsWith(".0")) {
//                        strCell = strCell.substring(0, strCell.lastIndexOf("."));
//                    }
                    break;
                }
            case Cell.CELL_TYPE_BOOLEAN: // boolean
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_BLANK: // 空白
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        return strCell;
    }
}
