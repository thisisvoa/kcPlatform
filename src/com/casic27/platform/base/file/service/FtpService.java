package com.casic27.platform.base.file.service;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.casic27.platform.base.file.config.FileServiceConfiguration;
import com.casic27.platform.base.file.entity.FileServer;
import com.casic27.platform.base.file.exception.FileUploadException;

import java.io.*;
import java.util.Enumeration;

public class FtpService {
    
    private static Log log = LogFactory.getLog(FtpService.class);
    
    /**
     * 上传到Ftp服务器上
     * 
     * @param relativePath 在ftp上的保存的相对路径（相对于系统指定的根路径），这里不包括文件名
     * @param fileName 在ftp上文件的名字
     * @param inputStream 要上传的文件流
     */
    public static boolean uploadFtp(String relativePath, String fileName, InputStream inputStream)throws FileUploadException{
        boolean  result =  false ;
        FileServiceConfiguration configuration = FileServiceConfiguration.getInstance();
        //如果没有FTP服务器的配置信息，则直接返回
        if(!configuration.isUseFtp())
            return result;
        FTPClient ftpClient = getFtpClient();//取得FTP客户端
        if(ftpClient == null)
            return result;
        try  {
            String ftpFilePath = configuration.getFileServer().getFtpFilePath(relativePath);
            changeWorkingDirectory(ftpClient, ftpFilePath);  //切换到要保存文件的目录
            result = ftpClient.storeFile(fileName, inputStream);  //保存输入流到FTP服务器上   
            ftpClient.logout();  //退出
        } catch  (IOException e) {
        	log.error(e);
        	throw new FileUploadException("读取文件失败!",e);
        } finally  {   
            if (ftpClient.isConnected()) {   
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }   
        }   
        
        return  result;   
    }
    
    /**
     * 下载FTP上的文件到本地
     * 
     * @param remotePath 在ftp上的保存的相对路径（相对于系统指定的根路径），这里不包括文件名
     * @param fileName 在ftp上文件的名字
     * @param localPath 保存到本地的路径
     * @return
     */
    public static boolean downFile(String remotePath, String fileName, String localPath)throws Exception {  
        boolean  result =  false ;
        FileServiceConfiguration configuration = FileServiceConfiguration.getInstance();
        
        //如果没有FTP服务器的配置信息，则直接返回
        if(!configuration.isUseFtp())
            return result;
        
        FTPClient ftpClient = getFtpClient();   //取得FTP客户端
        
        if(ftpClient == null)
            return result;
        
        try  {   
            String ftpFilePath = configuration.getFileServer().getFtpFilePath(remotePath);
            
            changeWorkingDirectory(ftpClient, ftpFilePath);  //切换到FTP服务器目录
            
            FTPFile[] ftpFileArray = ftpClient.listFiles();   
            
            if(ftpFileArray == null || ftpFileArray.length == 0)
                return result;
            
            for (FTPFile ftpFile : ftpFileArray){   
                if (ftpFile.getName().equals(fileName)){   
                    File zipFile = new File(localPath+ "/" + ftpFile.getName()); 
                    //如果指定文件的目录不存在,则创建之. 
                    File parent = zipFile.getParentFile(); 
                    if(!parent.exists()){ 
                        parent.mkdirs(); 
                    } 
                    OutputStream outputStream = new FileOutputStream(zipFile);    
                    ftpClient.retrieveFile(ftpFile.getName(), outputStream);   
                    
                    outputStream.close(); 
                    break;
                }   
            }   
               
            ftpClient.logout();  //退出
            
            result = true ;   
            
        } catch  (IOException e) {   
        	log.error(e);
        	throw e;
        } finally  {   
            
            if (ftpClient.isConnected()) {   
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }   
        }   
        
        return  result;  
    }
    
    /**
     * 删除文件
     * 
     * @param filePathName 在ftp上的保存的文件路径（相对于系统指定的根路径），这里包括文件名
     * @return
     */
    public static boolean deleteFile(String filePathName)throws FileUploadException{
        boolean  result =  false ;
        FileServiceConfiguration configuration = FileServiceConfiguration.getInstance();
        //如果没有FTP服务器的配置信息，则直接返回
        if(!configuration.isUseFtp())
            return result;
        
        FTPClient ftpClient = getFtpClient();   //取得FTP客户端
        
        if(ftpClient == null)
            return result;
        
        try  {   
            String ftpFilePath = configuration.getFileServer().getFtpFilePath(filePathName);
            result = ftpClient.deleteFile(ftpFilePath);  //删除存在FTP上的文件          
            ftpClient.logout();//退出
        }catch(IOException e) {   
        	log.error(e);
        	throw new FileUploadException("文件删除失败!");
        }finally{
            if (ftpClient.isConnected()) {   
            	try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }   
        }   
        
        return  result; 
    }
    
    /**
     * 切换到工作目录，如果目录不存在，则创建
     * 
     * @param ftpClient
     * @param dirPath
     * @return
     * @throws java.io.IOException
     */
    public static boolean changeWorkingDirectory(FTPClient ftpClient, String dirPath) throws IOException{
        
        boolean result = false;
        
        if(StringUtils.isEmpty(dirPath))
            return result;
        
        String[] filePathArray = dirPath.split("/");
        StringBuffer currentFilePath = new StringBuffer();
        for(String path : filePathArray){
            currentFilePath.append(path).append("/");
            //如果不存在，就创建目录
            if(!isExistedDir(ftpClient, currentFilePath.toString())){
                ftpClient.mkd(currentFilePath.toString());
            }
        }
        
        return ftpClient.changeWorkingDirectory(dirPath);  //切换到最后要求的目录
    }
    
    /**
     * 判断该目录是否存在
     * 
     * @param ftpClient
     * @param filePath
     * @return
     * @throws java.io.IOException
     */
    public static boolean isExistedDir(FTPClient ftpClient, String dirPath) throws IOException{
        if(StringUtils.isEmpty(dirPath))
            return false;
        
        return ftpClient.changeWorkingDirectory(dirPath);
    }
    
    /**
     * 取得FTP客户端，该客户端已经连接了FTP服务器
     * 
     * @return
     */
    public static FTPClient getFtpClient() {
        
        FTPClient ftpClient = null; 
        FileServiceConfiguration configuration = FileServiceConfiguration.getInstance();
        FileServer fileServer = configuration.getFileServer();
        ftpClient = new  FTPClient();
        try {
            //连接FTP服务器。如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
        	ftpClient.connect(fileServer.getFtpIp(), fileServer.getFtpPort());
        	ftpClient.login(fileServer.getFtpUserName(), fileServer.getFtpPassword()); //登录
        	ftpClient.setFileType(FTP.BINARY_FILE_TYPE); //设置二进制的文件类型，否则对于word等文件上传虽然成功，但是没办法打开，或者部署在LINUX环境下，图片失真
            int reply = ftpClient.getReplyCode();   
            if  (!FTPReply.isPositiveCompletion(reply)) {   
            	ftpClient.disconnect();   
            	ftpClient = null;
                return  ftpClient;   
            } 
        } catch (Exception e) {
        	ftpClient = null;
            e.printStackTrace();
        }
        
        return ftpClient;
    }
    
    /**
     * 取得远程FTP服务器上的文件到输出流中
     * 
     * @param relativePath
     * @param fileName
     * @param outputStream
     * @return
     * @throws Exception
     */
    public static boolean retrieveFile(String relativePath, String fileName, OutputStream outputStream) throws Exception {
        boolean result = false;
        FileServiceConfiguration configuration = FileServiceConfiguration.getInstance();
        //如果没有FTP服务器的配置信息，则直接返回
        if(!configuration.isUseFtp())
            return result;
        
        FTPClient ftpClient = getFtpClient();   //取得FTP客户端
        
        if(ftpClient == null)
            return result;
        
        try  {   
            String fileDir = configuration.getFileServer().getFtpFilePath(relativePath);  //文件的路径
            ftpClient.changeWorkingDirectory(fileDir);    //切换到要保存文件的目录
            ftpClient.retrieveFile(fileName, outputStream);
            ftpClient.logout();  //退出   
            result = true;
        } catch(IOException e) {   
            e.printStackTrace();   
        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }   
        }   
        
        return  result;   
    }
    
    /**
     * 取得远程FTP服务器上的文件
     * 
     * @param relativePath
     * @param fileName
     * @return
     * @throws Exception
     */
    public static InputStream getFileInputStream(String relativePath, String fileName) throws Exception {
        
        InputStream result = null;
        FileServiceConfiguration configuration = FileServiceConfiguration.getInstance();
        //如果没有FTP服务器的配置信息，则直接返回
        if(!configuration.isUseFtp())
            return result;
        
        FTPClient ftpClient = getFtpClient();   //取得FTP客户端
        
        if(ftpClient == null)
            return result;
        
        try  {   
            String fileDir = configuration.getFileServer().getFtpFilePath(relativePath);  //文件的路径
            ftpClient.changeWorkingDirectory(fileDir);    //切换到要保存文件的目录
            result = ftpClient.retrieveFileStream(fileName);
            ftpClient.logout();  //退出   
        } catch  (IOException e) {   
            e.printStackTrace();   
        } finally  {
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
        }
        
        return  result;   
    }
    
    /**
     * 解压zip文件
     * 
     * @param zipFileName 为要解压缩的zip为文件名，例：c:\\filename.zip
     * @param unzipFilePath 为解压缩后文件名，例：c:\\filename
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static void unzipFile(String zipFileName, String unzipFilePath) throws Exception {
        
        InputStream in = null; 
        FileOutputStream out = null; 
        ZipFile zipFile = null;
        try { 
            if(System.getProperty("os.name").toUpperCase().indexOf("WINDOWS")!=-1){//windows操作系统
                zipFile = new ZipFile(zipFileName);             
            }else{  //linux操作系统
                zipFile = new ZipFile(zipFileName, "GBK"); 
            }
            
            for(Enumeration entries = zipFile.getEntries(); entries.hasMoreElements();){ 
                ZipEntry entry = (ZipEntry)entries.nextElement();
                String unzipFileName = unzipFilePath + entry.getName();
                File file = new File(unzipFileName);
                if(entry.isDirectory()){
                    file.mkdirs(); 
                } else {
                    //如果指定文件的目录不存在,则创建之. 
                    File parent = file.getParentFile(); 
                    if(!parent.exists()){ 
                        parent.mkdirs(); 
                    } 

                    in = zipFile.getInputStream(entry); 

                    out = new FileOutputStream(file); 
                    byte[] c = new byte[1024];
                    int slen;        
                    
                    while((slen = in.read(c) ) > 0){ 
                        out.write(c , 0 , slen );
                    } 
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        } finally { 
            if (in != null) {
                in.close();        //解压完成后注意关闭输入流对象
            }
            if (out != null) { 
                out.close();      //解压完成后注意关闭输出流对象
            }
            if (zipFile != null){
            	zipFile.close();        //解压完成后注意关闭apache自带zip流对象
            }
        }  
    } 
}
