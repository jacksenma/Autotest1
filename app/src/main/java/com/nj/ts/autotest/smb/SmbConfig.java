package com.nj.ts.autotest.smb;

import android.app.Application;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.ts.autotest.R;
import com.nj.ts.autotest.App;


import java.io.File;

public class SmbConfig {

    public static SmbConfig sSmbConfig;

    public static final String EXTERNAL = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final String DEFAULT_CONFIG_NAME = "config.xml";
    public static final String DEFAULT_DOWNLOAD_PATH = EXTERNAL + "/" + App.getContext().getResources().getString(R.string.app_name) + "/download/";

    private String account;
    private String password;
    private String ip;
    private String rootPath;
    private String configName = DEFAULT_CONFIG_NAME;
    private String downloadPath = DEFAULT_DOWNLOAD_PATH;

    public static SmbConfig getInstance() {
        if (sSmbConfig == null) {
            sSmbConfig = new SmbConfig();
        }
        return sSmbConfig;
    }

    private SmbConfig() {
        //need require storage permissions
        checkAppFolderExist();
    }

    private void checkAppFolderExist() {
        File appFolder = new File(DEFAULT_DOWNLOAD_PATH);
        if (!appFolder.exists()) {
            appFolder.mkdirs();
        }
    }

    public String getAccount() {
        return account;
    }

    /**
     *
     * Could be null
     * @param account
     * @return
     */
    public SmbConfig setAccount(@Nullable String account) {
        this.account = account;
        return this;
    }

    public String getPassword() {
        return password;
    }

    /**
     *
     * Could be null
     * @param password
     * @return
     */
    public SmbConfig setPassword(@Nullable String password) {
        this.password = password;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public SmbConfig setIp(@NonNull String ip) {
        this.ip = ip;
        return this;
    }

    public String getRootPath() {
        return rootPath;
    }

    public SmbConfig setRootPath(@NonNull String path) {
        this.rootPath = path;
        return this;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getRootUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append("smb://");
        if(account != null) {
            builder.append(account)
                    .append(":");
        }
        if(password != null) {
            builder.append(password)
                    .append("@");
        }
        if (TextUtils.isEmpty(ip)) {
            throw new NullPointerException("SmbConfig: IP CAN NOT BE NULL!");
        }
        builder.append(ip);

        if (TextUtils.isEmpty(rootPath)) {
            throw new NullPointerException("SmbConfig: PATH CAN NOT BE NULL!");
        }
        if (rootPath.startsWith("/"))
            builder.append(rootPath);
        else
            builder.append("/").append(rootPath);

        if (!rootPath.endsWith("/"))
            builder.append("/");
        return builder.toString();
    }

    public String getConfigUrl() {
        return getRootUrl() + configName;
    }
}
