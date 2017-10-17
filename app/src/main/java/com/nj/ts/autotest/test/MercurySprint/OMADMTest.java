package com.nj.ts.autotest.test.MercurySprint;

import com.nj.ts.autotest.util.Info;

/**
 * Created by ts on 17-10-17.
 */

public class OMADMTest {
    public void GetDeviceID(){
        Info.sendResult(true,"获取设备id");
    }
    public void GetManufactureName(){
        Info.sendResult(false,"获取到的ManufactureName的信息为空");
    }
    public void GetModelName(){
        Info.sendResult(true,"获取模型id");
    }
    public void GetLanguage(){
        Info.sendResult(true,"获取语言");
    }
    public void GetSoftwareVersion(){
        Info.sendResult(true,"获取软件版本");
    }
    public void GetFirmwareVersion(){
        Info.sendResult(true,"获取固件版本");
    }
    public void GetHardwareVersion(){
        Info.sendResult(true,"获取硬件版本");
    }
}
