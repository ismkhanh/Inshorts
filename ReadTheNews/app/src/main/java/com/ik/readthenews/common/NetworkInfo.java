package com.ik.readthenews.common;

import android.arch.lifecycle.LiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


public class NetworkInfo extends LiveData<Boolean> {

    public Context mContext;
    private static NetworkInfo mNetworkInfo;
    private BroadcastReceiver mReceiver;

    private NetworkInfo (Context mContext) {
        this.mContext = mContext;
    }

    public static NetworkInfo getInstance(Context context){
        if (mNetworkInfo == null) {
            mNetworkInfo = new NetworkInfo(context);
        }

        return mNetworkInfo;
    }

    @Override
    protected void onActive() {
        super.onActive();
        if (mReceiver == null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    setValue(Utils.isOnline(mContext));
                }
            };
            mContext.registerReceiver(mReceiver, intentFilter);
        }
    }

    @Override
    protected void onInactive() {
        super.onInactive();

        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }
}
