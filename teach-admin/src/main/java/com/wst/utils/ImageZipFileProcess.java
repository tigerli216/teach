/*
 * ImageZipFileProcess.java Created on 2017年11月25日 上午11:49:50
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
package com.wst.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import com.hiwi.common.util.file.FileUtils;

/**
 * 文件压缩与解压缩工具
 * 
 * @author <a href="mailto:lanwc@hiwitech.com">lanweicheng</a>
 * @version 1.0
 */
public final class ImageZipFileProcess {

    /**
     * 递归取到当前目录所有文件
     * 
     * @param dir
     * @return
     */
    public static List<String> getFiles(String dir) {
        List<String> lstFiles = null;
        if (lstFiles == null) {
            lstFiles = new ArrayList<String>();
        }
        File file = new File(dir);
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                lstFiles.add(f.getAbsolutePath());
                lstFiles.addAll(getFiles(f.getAbsolutePath()));
            } else {
                String str = f.getAbsolutePath();
                lstFiles.add(str);
            }
        }
        return lstFiles;
    }

    /**
     * 文件名处理
     * 
     * @param dir
     * @param path
     * @return
     */
    public static String getFilePathName(String dir, String path) {
        String p = path.replace(dir + File.separator, "");
        p = p.replace("\\", "/");
        return p;
    }

    /**
     * 把文件压缩成zip格式
     * 
     * @param files
     *            需要压缩的文件
     * @param zipFilePath
     *            压缩后的zip文件路径 ,如"D:/test/aa.zip";
     */
    public static void compressFilesZip(String[] files, String zipFilePath, String dir) {
        if (files == null || files.length <= 0) {
            return;
        }
        ZipArchiveOutputStream zaos = null;
        try {
            File zipFile = new File(zipFilePath);
            zaos = new ZipArchiveOutputStream(zipFile);
            zaos.setUseZip64(Zip64Mode.AsNeeded);
            // 将每个文件用ZipArchiveEntry封装
            // 再用ZipArchiveOutputStream写到压缩文件中
            for (String strfile : files) {
                File file = new File(strfile);
                if (file != null) {
                    String name = getFilePathName(dir, strfile);
                    ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, name);
                    zaos.putArchiveEntry(zipArchiveEntry);
                    if (file.isDirectory()) {
                        continue;
                    }
                    InputStream is = null;
                    try {
                        is = new BufferedInputStream(new FileInputStream(file));
                        byte[] buffer = new byte[1024];
                        int len = -1;
                        while ((len = is.read(buffer)) != -1) {
                            // 把缓冲区的字节写入到ZipArchiveEntry
                            zaos.write(buffer, 0, len);
                        }
                        zaos.closeArchiveEntry();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        if (is != null)
                            is.close();
                    }

                }
            }
            zaos.finish();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (zaos != null) {
                    zaos.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * 把zip文件解压到指定的文件夹
     * 
     * @param zipFilePath
     *            zip文件路径
     * @param saveFileDir
     *            解压后的文件存放路径
     */
    public static void unzip(String zipFilePath, String saveFileDir) {
        if (!saveFileDir.endsWith("\\") && !saveFileDir.endsWith("/")) {
            saveFileDir += File.separator;
        }
        File dir = new File(saveFileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(zipFilePath);
        if (file.exists()) {
            InputStream is = null;
            ZipArchiveInputStream zais = null;
            try {
                is = new FileInputStream(file);
                zais = new ZipArchiveInputStream(is);
                ArchiveEntry archiveEntry = null;
                while ((archiveEntry = zais.getNextEntry()) != null) {
                    // 获取文件名
                    String entryFileName = archiveEntry.getName();
                    // 构造解压出来的文件存放路径
                    String entryFilePath = saveFileDir + entryFileName;
                    OutputStream os = null;
                    try {
                        // 把解压出来的文件写到指定路径
                        File entryFile = new File(entryFilePath);
                        if (entryFileName.endsWith("/")) {
                            entryFile.mkdirs();
                        } else {
                            os = new BufferedOutputStream(new FileOutputStream(entryFile));
                            byte[] buffer = new byte[1024];
                            int len = -1;
                            while ((len = zais.read(buffer)) != -1) {
                                os.write(buffer, 0, len);
                            }
                        }
                    } catch (IOException e) {
                        throw new IOException(e);
                    } finally {
                        if (os != null) {
                            os.flush();
                            os.close();
                        }
                    }

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (zais != null) {
                        zais.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 把图片zip文件解压到指定的文件夹,并返回图片URL
     * 
     * @param zipIo
     *            流式压缩文件
     * @param saveFileDir
     *            解压后的文件存放路径
     * @return
     */
    public static List<String> unzipImageFile(InputStream zipIo) {
        if (zipIo == null) {
            return null;
        }

        List<String> unzipFilePaths = new ArrayList<String>();

        ZipArchiveInputStream zais = null;
        try {
            zais = new ZipArchiveInputStream(zipIo);
            ArchiveEntry archiveEntry = null;
            while ((archiveEntry = zais.getNextEntry()) != null) {
                // 获取文件名
                String entryFileName = archiveEntry.getName();
                
                // 拼接完整路径
                Calendar c = Calendar.getInstance();
                String directory = "/" + c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/"
                        + c.get(Calendar.DATE) + "/";

                String imgPath = FileUtils.getImgPath() + directory;
                File dstFile = new File(imgPath);
                if (!dstFile.exists()) {
                    dstFile.mkdirs();
                }
                // 文件名
                String fileSuffix = entryFileName.substring(entryFileName.lastIndexOf(".") + 1); // 文件名后缀
                String newFileName = c.getTimeInMillis() + "." + fileSuffix;
                
                // 构造解压出来的文件存放路径
                String entryFilePath = imgPath + newFileName;
                OutputStream os = null;
                try {
                    // 把解压出来的文件写到指定路径
                    File entryFile = new File(entryFilePath);
                    os = new BufferedOutputStream(new FileOutputStream(entryFile));
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    while ((len = zais.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }
                    
                    unzipFilePaths.add(FileUtils.getImgPrefix() + directory + newFileName);
                } catch (IOException e) {
                    throw new IOException(e);
                } finally {
                    if (os != null) {
                        os.flush();
                        os.close();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (zais != null) {
                    zais.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        return unzipFilePaths;
    }
}
