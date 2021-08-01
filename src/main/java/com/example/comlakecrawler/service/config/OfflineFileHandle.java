package com.example.comlakecrawler.service.config;

import com.example.comlakecrawler.service.uploader.UploadCenter;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.impl.FileVolumeManager;
import com.github.junrar.rarfile.FileHeader;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class OfflineFileHandle {
    private boolean isExit;

    public OfflineFileHandle() {
        isExit = false;
    }
    public void onStop(){
        isExit = true;
    }
    public String fileFormat(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
    }
    public void unpacking(String targetZippedFile, String token, String link){
        System.out.println("Begin unpacking....");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (!isExit){
                    try {
                        File targetFile = new File(targetZippedFile);
                        String source = targetFile.getAbsolutePath();
                        String fileFormat = fileFormat(targetZippedFile);
                        System.out.println(fileFormat);
                        if (!fileFormat.equals("zip")&&!fileFormat.equals("rar")){
                            System.out.println("Cancel unzipping process");
                            new UploadCenter().uploadTryOut(targetFile,token,link);
                            onStop();
                            return;
                        }
                        String des = source.replace("."+fileFormat,"");
                        File file = new File(des);
                        if (!file.exists()){
                            file.mkdirs();
                        }
                        switch (fileFormat){
                            case "zip":
                                byte []bytes = new byte[1024];
                                FileInputStream fInputStream = new FileInputStream(targetFile);
                                ZipInputStream zipInputStream = new ZipInputStream(fInputStream);
                                ZipEntry entry ;
                                while ((entry = zipInputStream.getNextEntry()) !=null){
                                    StringBuffer buffer = new StringBuffer();
                                    buffer.append(des);
                                    String finalPath = buffer.append(File.separator+entry.getName()).toString();
                                    File newBie = new File(finalPath);
                                    if (entry.isDirectory()){
                                        if (!newBie.isDirectory()&&!newBie.mkdirs()){
                                            throw new IOException("Failed to create directory"+newBie);
                                        }
                                    }else {
                                        File parent = newBie.getParentFile();
                                        if (!parent.isDirectory()&&!parent.mkdirs()){
                                            throw new IOException("Failed to create directory"+parent);
                                        }
                                        FileOutputStream fOS = new FileOutputStream(newBie);
                                        int length;
                                        while ((length = zipInputStream.read(bytes))!=-1){
                                            fOS.write(bytes,0,length);
                                        }
                                        fOS.close();
                                    }
                                    new UploadCenter().uploadTryOut(newBie,token,link);
                                }
                                zipInputStream.closeEntry();
                                zipInputStream.close();
                                System.out.println("Successfully unzipped");
                                break;
                            case "rar":
                                Archive archive = new Archive(new FileVolumeManager(targetFile));
                                if (archive!=null){
                                    archive.getMainHeader().print();
                                    FileHeader fileHeader = archive.nextFileHeader();
                                    while (fileHeader!=null){
                                        File out = new File(des
                                                + fileHeader.getFileNameString().trim());
                                        FileOutputStream fOS = new FileOutputStream(out);
                                        archive.extractFile(fileHeader,fOS);
                                        fOS.close();
                                        new UploadCenter().uploadTryOut(out,token,link);
                                        fileHeader = archive.nextFileHeader();
                                    }
                                }
                                break;
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (RarException e) {
                        e.printStackTrace();
                    } finally {
                        onStop();
                    }
                }
            }
        };
        runnable.run();
    }
}
