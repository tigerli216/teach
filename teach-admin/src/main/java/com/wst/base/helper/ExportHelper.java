/*
 * ExportHelper.java Created on 2017年10月31日 下午9:33:12
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
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.formula.functions.T;

import com.hiwi.common.util.CommonFuntions;

/**
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class ExportHelper {

	/**
	 * 封装请求参数
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> encReqParam(HttpServletRequest request) {
		Map<String, String[]> paramMap = request.getParameterMap();
		Map<String, String> resultMap = new HashMap<String, String>();
		if (paramMap != null) {
			for (String key : paramMap.keySet()) {
				if (paramMap.get(key) != null && paramMap.get(key).length > 0) {
					resultMap.put(key, paramMap.get(key)[0]);
				}
			}
		}
		return resultMap;
	}

	/**
	 * 导出时设置头部信息
	 * 
	 * @param string
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	public static void writerCsvFileSetHeaderForUTF8(String csvFileName, HttpServletResponse response)
			throws UnsupportedEncodingException {
		response.reset();
		response.setContentType("application/csv;charset=gbk");
		response.addHeader("Content-Disposition",
				"attachment; filename=\"" + new String(csvFileName.getBytes("UTF-8"), "iso8859-1") + "\"");
		response.setCharacterEncoding("gb2312");
	}

	/**
	 * 返回统一未分页json数据
	 * 
	 * @param string
	 * @param response
	 * @param isLast
	 *            是否是最后一行
	 */
	public static void writerCsvFileContent(String csvStr, HttpServletResponse response, boolean isLast) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(csvStr);
			out.flush();
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 最后一次追加导出才关闭输出流通道
			if (isLast && out != null) {
				out.flush();
				out.close();
			}
		}
	}

	

	/**
	 * 获得导出数据字符串
	 * 
	 * @param fileFirstLine
	 *            第一行标题
	 * @param exportDataList
	 *            导出的数据对象
	 * @param exportFieldName
	 *            导出的字段
	 * @return
	 */
	public static String getExportCsvStr(String fileFirstLine, List<Object> exportDataList, String[] exportFieldName) {
		if (exportDataList == null || exportDataList.isEmpty()) {
			return fileFirstLine;
		}

		StringBuilder sbStr = new StringBuilder(fileFirstLine);
		if (CommonFuntions.isNotEmptyObject(fileFirstLine)) {
			sbStr.append("\r\n");
		}

		for (Object obj : exportDataList) {
			for (String filedName : exportFieldName) {
				Object fieldValue = getProperty(obj, filedName);
				if (fieldValue == null) {
					fieldValue = "";
				}
				sbStr.append(fieldValue).append(",");
			}
			sbStr.append("\r\n");
		}

		return sbStr.toString();
	}

	/**
	 * 设定历史数据失效日期
	 * 
	 * @param hisEntity
	 */
	public static Object getProperty(Object entity, String propertyName) {
		try {
			return BeanUtils.getProperty(entity, propertyName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取 WorkBook
	 * @param title
	 * @param header
	 * @param tblContent
	 * @param dataset
	 * @return
	 */
	public static HSSFWorkbook exportExcel(String title, String header,String[] tblContent,List dataset)  
    {  
        // 声明一个工作薄  
        HSSFWorkbook workbook = new HSSFWorkbook();  
        // 生成一个表格  
        HSSFSheet sheet = workbook.createSheet(title);  
        // 设置表格默认列宽度为20个字节  
        sheet.setDefaultColumnWidth((short) 20);  
        /*// 生成一个样式  
        HSSFCellStyle style = workbook.createCellStyle();  
        // 设置这些样式  
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);  
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        // 生成一个字体  
        HSSFFont font = workbook.createFont();  
        font.setColor(HSSFColor.VIOLET.index);  
        font.setFontHeightInPoints((short) 12);  
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        // 把字体应用到当前的样式  
        style.setFont(font);  
        // 生成并设置另一个样式  
        HSSFCellStyle style2 = workbook.createCellStyle();  
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);  
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // 生成另一个字体  
        HSSFFont font2 = workbook.createFont();  
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
        // 把字体应用到当前的样式  
        style2.setFont(font2);  */
  
        /*// 声明一个画图的顶级管理器  
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();  
        // 定义注释的大小和位置,详见文档  
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,  
                0, 0, 0, (short) 4, 2, (short) 6, 5));  
        // 设置注释内容  
        comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));  
        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.  
        comment.setAuthor("leno");  
  */
        // 产生表格标题行  
        HSSFRow row = sheet.createRow(0); 
        String[] headers = header.split(",");
        for (short i = 0; i < headers.length; i++)  
        {  
            HSSFCell cell = row.createCell(i);  
//            cell.setCellStyle(style);  
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);  
            cell.setCellValue(text);  
        }  
  
        // 遍历集合数据，产生数据行
        int index = 0;
        if(dataset==null){
        	return workbook;
        }
        for(Object o:dataset){
        	index++;
        	short x = 0;
        	row = sheet.createRow(index);
        	for(String fieldName: tblContent){
        		HSSFCell cell = row.createCell(x);
        		x++;
//                cell.setCellStyle(style2); 
        		String getMethodName = "get"  
                        + fieldName.substring(0, 1).toUpperCase()  
                        + fieldName.substring(1); 
				try {
					Class tCls = o.getClass();
					Method getMethod = tCls.getMethod(getMethodName,  
					        new Class[]  
					        {});
					Object value = getMethod.invoke(o, new Object[]  
			                {});  
					if(value==null)
						cell.setCellValue("");
					else
			            cell.setCellValue(value.toString());
				}  
                catch (SecurityException e)  
                {  
                    e.printStackTrace();  
                }  
                catch (NoSuchMethodException e)  
                {  
                    e.printStackTrace();  
                }  
                catch (IllegalArgumentException e)  
                {  
                    e.printStackTrace();  
                }  
                catch (IllegalAccessException e)  
                {  
                    e.printStackTrace();  
                }  
                catch (InvocationTargetException e)  
                {  
                    e.printStackTrace();  
                }  
                
        	}
        }
            return workbook;
    }  
	
	/**
	 * 导出.xls文件设置头部信息
	 * @param xlsFileName
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	public static void writerXlsFileSetHeaderForUTF8(String xlsFileName, HttpServletResponse response)
			throws UnsupportedEncodingException {
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.addHeader("Content-Disposition",
				"attachment; filename=\"" + new String(xlsFileName.getBytes("UTF-8"), "iso8859-1") + "\"");
		response.setCharacterEncoding("utf8");
	}
	
	/**
	 * 返回所有数据
	 * @param workbook
	 * @param response
	 * @param isLast
	 */
	public static void writerXlsFileContent(HSSFWorkbook workbook, HttpServletResponse response, boolean isLast) {
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			workbook.write(out);
			out.flush();
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 最后一次追加导出才关闭输出流通道
			if (isLast && out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	

}
