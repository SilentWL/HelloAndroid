package com.example.administrator.wechatrob;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Iterator;
import java.util.List;

import utils.HongbaoSignature;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class RobMoneyService extends AccessibilityService{
    private static final String WECHAT_CHAT_LIST_LUCKRECEIVE_ID = "com.tencent.mm:id/ba";
    private static final String WECHAT_LUCKMONEY_OPENRECEIVE_ID = "com.tencent.mm:id/b5d";
    private static final String WECHAT_CONTRACT_LIST_ITEM = "com.tencent.mm:id/c9";
    private static final String WECHAT_LUCKMONEY_GENERAL_ACTIVITY = "LauncherUI";
    private static final String WECHAT_NOTIFICATION_TIP = "[微信红包]";
    private static final String WECHAT_CHAT_ITEM_TEXT = "com.tencent.mm:id/bf";

    private String currentActivityName = WECHAT_LUCKMONEY_GENERAL_ACTIVITY;
    private boolean  hasNotification = false;
    private boolean  openingLuckReceive = false;
    private HongbaoSignature signature = new HongbaoSignature();
    private int hasCode = 0;
    private int hasLuckReceiveCount = 0;
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int exentType = event.getEventType();
        String className = event.getClassName().toString();
        setCurrentActivityName(event);
        switch (exentType){
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                List<CharSequence> texts = event.getText();
                if (!texts.isEmpty()){
                    for (Iterator iterator = texts.iterator();iterator.hasNext();){
                        String content = iterator.next().toString();
                        if (content.contains(WECHAT_NOTIFICATION_TIP)){
                            if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification){
                                Notification notification = (Notification)event.getParcelableData();
                                PendingIntent pendingIntent = notification.contentIntent;
                                try{
                                    pendingIntent.send();
                                    hasNotification = true;
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (className.equals("com.tencent.mm.ui.LauncherUI") || className.equals("android.widget.TextView")){
                    getPacket(event);
                }else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")){
                    openPacket(event);
                }
                break;
        }

    }
    private boolean checkNodeInfo(AccessibilityNodeInfo LastRedReceiveNodeInfo) {
        AccessibilityNodeInfo NodeInfo = LastRedReceiveNodeInfo.getParent().getParent();
        int nodeInfoCount = NodeInfo.getChildCount();

        for (int i = nodeInfoCount - 1; i >= 0; i--){
            List<AccessibilityNodeInfo> childNodeInfos1 = NodeInfo.getChild(i).findAccessibilityNodeInfosByViewId(WECHAT_CHAT_LIST_LUCKRECEIVE_ID);

            if (childNodeInfos1.size() > 0){
                if (i < nodeInfoCount - 1) {
                    for (int j = i + 1; i < nodeInfoCount - 1; j++) {
                        List<AccessibilityNodeInfo> childNodeInfos2 = NodeInfo.getChild(j).findAccessibilityNodeInfosByText("你领取了");

                        if (childNodeInfos2.size() > 0){
                            for (AccessibilityNodeInfo childNodeInfo2 : childNodeInfos2) {
                                if (childNodeInfo2.findAccessibilityNodeInfosByText("的红包").size() > 0)
                                {
                                    return false;
                                }
                            }
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
    private boolean watchList(AccessibilityEvent event) {
        AccessibilityNodeInfo eventSource = event.getSource();
        // Not a message
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED || eventSource == null)
            return false;

        List<AccessibilityNodeInfo> nodes = eventSource.findAccessibilityNodeInfosByViewId(WECHAT_CHAT_LIST_LUCKRECEIVE_ID);
        //增加条件判断currentActivityName.contains(WECHAT_LUCKMONEY_GENERAL_ACTIVITY)
        //避免当订阅号中出现标题为“[微信红包]拜年红包”（其实并非红包）的信息时误判
        if (!nodes.isEmpty() && currentActivityName.contains(WECHAT_LUCKMONEY_GENERAL_ACTIVITY)) {
            AccessibilityNodeInfo lastNode = getTheLastNode(eventSource, WECHAT_CHAT_LIST_LUCKRECEIVE_ID);
            if (lastNode != null) {
                int temphashCode = lastNode.hashCode();
                if (temphashCode != this.hasCode || hasNotification) {
                    this.hasCode = temphashCode;
                    if (lastNode.isClickable()) {
                        lastNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    } else {
                        recycle(lastNode);
                    }
                }
            }
        }
        return false;
    }
    private void getPacket(AccessibilityEvent event){
        AccessibilityNodeInfo rootNodeInfo = (/*event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED*/false) ? event.getSource() : getRootInActiveWindow();
        if (rootNodeInfo != null){
                AccessibilityNodeInfo lastNode = getTheLastNode(rootNodeInfo, WECHAT_CHAT_LIST_LUCKRECEIVE_ID);
                if (lastNode != null) {
                    boolean needClick = false;
                    int temphashCode = lastNode.hashCode();

                    if (!openingLuckReceive && (temphashCode != this.hasCode || hasNotification || checkNodeInfo(lastNode))) {
                        this.hasCode = temphashCode;
                        needClick = true;
                    }
//                    else if (hasLuckReceiveCount !=  nodes.size()){
//                        hasLuckReceiveCount = nodes.size();
//                        needClick = true;
//                    }
                    if (needClick){
                        if (lastNode.isClickable()) {
                            lastNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        } else {
                            recycle(lastNode);
                        }
                        openingLuckReceive = true;
                    }
                }
//                List<AccessibilityNodeInfo> nodeInfos = rootNodeInfo.findAccessibilityNodeInfosByViewId();
//                for (AccessibilityNodeInfo nodeInfo : nodeInfos) {
//                    if (nodeInfo.isClickable()) {
//                        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                    } else {
//                        recycle(rootNodeInfo);
//                    }
//                }
        }
        hasNotification = false;
    }
    private void openPacket(AccessibilityEvent event){
        AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();

        if (rootNodeInfo != null){
            List<AccessibilityNodeInfo> nodeInfos = rootNodeInfo.findAccessibilityNodeInfosByViewId(WECHAT_LUCKMONEY_OPENRECEIVE_ID);
            for (AccessibilityNodeInfo nodeInfo : nodeInfos){
                if (nodeInfo.isClickable()) {
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                else{
                    recycle(rootNodeInfo);
                }
            }
            openingLuckReceive = false;
            //performGlobalAction(GLOBAL_ACTION_BACK);
        }
    }

    public void recycle(AccessibilityNodeInfo info) {
        if (info.getChildCount() == 0) {
            if (info.isClickable()) {
                info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            else {
                AccessibilityNodeInfo parent = info.getParent();
                while (parent != null) {
                    if (parent.isClickable()) {
                        parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        break;
                    }
                    parent = parent.getParent();
                }
            }
        } else {
            for (int i = 0; i < info.getChildCount(); i++) {
                if(info.getChild(i)!=null){
                    recycle(info.getChild(i));
                }
            }
        }
    }
    private void setCurrentActivityName(AccessibilityEvent event) {
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return;
        }

        try {
            ComponentName componentName = new ComponentName(
                    event.getPackageName().toString(),
                    event.getClassName().toString()
            );

            getPackageManager().getActivityInfo(componentName, 0);
            currentActivityName = componentName.flattenToShortString();
        } catch (PackageManager.NameNotFoundException e) {
            currentActivityName = WECHAT_LUCKMONEY_GENERAL_ACTIVITY;
        }
    }
    private AccessibilityNodeInfo getTheLastNode(AccessibilityNodeInfo rootNodeInfo, String resId) {
        int bottom = 0;
        AccessibilityNodeInfo lastNode = null;

        List<AccessibilityNodeInfo> nodes = rootNodeInfo.findAccessibilityNodeInfosByViewId(resId);


        if (!nodes.isEmpty()) {
            AccessibilityNodeInfo node = nodes.get(nodes.size() - 1);
            if (node != null) {
                Rect bounds = new Rect();
                node.getBoundsInScreen(bounds);
                if (bounds.bottom > bottom) {
                    bottom = bounds.bottom;
                    lastNode = node;
                }
            }
        }
        return lastNode;
    }
    @Override
    public void onInterrupt() {

    }
}
