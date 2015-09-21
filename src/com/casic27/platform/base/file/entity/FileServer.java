package com.casic27.platform.base.file.entity;

import org.apache.commons.lang.StringUtils;

import com.casic27.platform.core.entity.BaseEntity;

public class FileServer  extends BaseEntity {
	/**
     * FTP默认的端口号
     */
    public static final int DEFAULT_FTP_PORT = 21;
    
    /**
     * FTP服务器的URL
     */
    private String ftpUrl;
    
    /**
     * FTP服务器的IP地址
     */
    private String ftpIp;
    
    /**
     * FTP服务器的端口号
     */
    private int ftpPort;
    
    /**
     * FTP服务器登录用户名
     */
    private String ftpUserName;
    
    /**
     * FTP服务器登录密码
     */
    private String ftpPassword;
    
    /**
     * FTP服务器的上传文件所在的根目录
     */
    private String ftpRootDir;
    
    /**
     * HTTP文件服务器的URL
     */
    private String httpUrl;
    
    /**
     * HTTP文件服务器的根目录
     */
    private String httpRootDir;
    
    /**
     * 取得文件服务器的URL
     * 
     * @return
     */
    public String getFileServerUrl(){
        StringBuffer result = new StringBuffer();
                     result.append(httpUrl).append("/").append(ftpRootDir).append("/");

        return result.toString();
    }
    
    /**
     * 取得上传文件在FTP服务器上的完整路径：<FTP根路径>/<文件相对路径>
     * 
     * @param relativePath
     * @return
     */
    public String getFtpFilePath(String relativePath){
        
        StringBuffer result = new StringBuffer();
                     result.append(ftpRootDir).append("/").append(relativePath);
                     
        return result.toString();
    }

    public String getFtpUrl() {
        return ftpUrl;
    }

    /**
     * 设置FTP的URL，同时解析出其IP地址和端口号
     * 
     * @param ftpUrl FTP请求的URL：<协议>://<ip地址>:<端口>
     */
    public void setFtpUrl(String ftpUrl) {
        
        if(StringUtils.isNotEmpty(ftpUrl)){
            
            int port = DEFAULT_FTP_PORT; 
            
            int ipStartPos = ftpUrl.lastIndexOf("/");
            int ipEndPos = ftpUrl.lastIndexOf(":");
            if(ipStartPos == -1)
                ipStartPos = 0;
            if(ipEndPos == -1)
                ipEndPos = ftpUrl.length();
            else
                port = Integer.valueOf(ftpUrl.substring(ipEndPos + 1));
            
            this.ftpPort = port;
            this.ftpIp = ftpUrl.substring(ipStartPos+1, ipEndPos);
        }
        
        this.ftpUrl = ftpUrl;
    }

    public String getFtpUserName() {
        return ftpUserName;
    }

    public void setFtpUserName(String ftpUserName) {
        this.ftpUserName = ftpUserName;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getFtpRootDir() {
        return ftpRootDir;
    }

    public void setFtpRootDir(String ftpRootDir) {
        this.ftpRootDir = ftpRootDir;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getHttpRootDir() {
        return httpRootDir;
    }

    public void setHttpRootDir(String httpRootDir) {
        this.httpRootDir = httpRootDir;
    }

    /**
     * @return the ftpIp
     */
    public String getFtpIp()
    {
        return ftpIp;
    }

    /**
     * @return the ftpPort
     */
    public int getFtpPort()
    {
        return ftpPort;
    }
}
