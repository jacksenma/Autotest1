package com.nj.ts.autotest.email;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.nj.ts.autotest.smb.SmbServer;

import java.io.IOException;
import java.util.ArrayList;

import javax.mail.MessagingException;

/**
 * Created by root on 10/17/17.
 */

public class MailServer extends IntentService {

    private static final String TAG = "MailServer";
    public static final String ACTION_SEND_MAIL = "action_send_mail";

    public static final String STATUS_MAIL_SERVICE = "STATUS_MAIL_SERVICE";

    /**
     * Send email interface
     * @param context
     * @param subject
     * @param content
     * @param attachments
     */
    public static void sendMail(Context context, String subject, String content, ArrayList<Attachment> attachments) {
        Log.d(TAG, "sendMail: subject = " + subject + ", content =  " + content);
        Intent intent = new Intent(context, MailServer.class);
        intent.setAction(ACTION_SEND_MAIL);
        intent.putExtra("subject", subject);
        intent.putExtra("content", content);
        intent.putExtra("attachments", attachments);
        context.startService(intent);
    }

    private LocalBroadcastManager mLBM;

    public enum StatusService {
        STARTED, STOPPED
    }

    public MailServer() {
        super("MailServer");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLBM = LocalBroadcastManager.getInstance(this);
        sendServiceStatus(StatusService.STARTED);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) return;
        String action = intent.getAction();
        Log.d(TAG, "onHandleIntent: action = " + action);
        switch (action) {
            case ACTION_SEND_MAIL:
                String subject = intent.getExtras().getString("subject");
                String content = intent.getExtras().getString("content");
                ArrayList<Attachment> attachments = (ArrayList<Attachment>) intent.getExtras().getSerializable("attachments");

                Mail mail = new Mail(subject, content);
                if (null != attachments) {
                    mail.addAttachment(attachments);
                }
                try {
                    mail.send();
                } catch (IOException e) {
                    Log.d(TAG, "IOException: e " + Log.getStackTraceString(e));
                    e.printStackTrace();
                } catch (MessagingException e) {
                    Log.d(TAG, "MessagingException: e " + Log.getStackTraceString(e));
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sendServiceStatus(StatusService.STOPPED);
    }

    public void sendServiceStatus(StatusService status) {
        Intent intent = new Intent(STATUS_MAIL_SERVICE);
        intent.putExtra("status", status);
        mLBM.sendBroadcast(intent);
    }
}
