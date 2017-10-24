package com.nj.ts.autotest.testutil.MercurySprint;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MercuryOmaTestUtil {
    private static final String TAG = MercuryOmaTestUtil.class.getSimpleName();
    private static final String ACTION_START_OMA_TEST = "ts.intent.action.ActionOmaTester";
    private Context mContext;

    public MercuryOmaTestUtil(Context context) {
        mContext = context;
    }

    public void startTest() {
        Log.d(TAG, "ruan send oma test notification");
        Intent intent = new Intent();
        intent.addFlags(0x01000000);
        intent.setAction(ACTION_START_OMA_TEST);
        mContext.sendBroadcast(intent);
    }

    /**
     * @return all test items
     */
    public String[] getTestItems() {
        return new String[]{
                "testIplHfaNeedStart",
                "testIplIsMobileDataEnabled",
                "testIplGetDeviceID",
                "testIplGetManufactureName",
                "testIplGetModelName",
                "testIplGetLanguage",
                "testIplGetSoftwareVersion",
                "testIplGetFirmwareVersion",
                "testIplGetHardwareVersion",
                "testIplGetDeviceType",
                "testIplGetOEMname",
                "testIplLTEEnablementWrite",
                "testIplLTEEnablementRead",
                "testIplLTEBandWrite",
                "testIplLTEBandRead",
                "testIplSPAWrite",
                "testIplSPARead",
                "testIplSubscriberWrite",
                "testIplSubscriberRead",
                "testIplBandClassWrite",
                "testIplHfaNeedStart",
        };
    }


}
