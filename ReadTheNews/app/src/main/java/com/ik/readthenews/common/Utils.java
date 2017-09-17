package com.ik.readthenews.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ik.readthenews.R;
import com.ik.readthenews.repository.shared_pref.MySharedPref;
import com.jakewharton.rxbinding2.view.RxView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.function.Consumer;

import static com.ik.readthenews.common.SortOption.CATEGORY;
import static com.ik.readthenews.common.SortOption.NEW_TO_OLD;
import static com.ik.readthenews.common.SortOption.OLD_TO_NEW;
import static com.ik.readthenews.common.SortOption.PUBLISHER;

public class Utils {

    public static AlertDialog getAlertDialog(Context context, View dialogView,
                                             Consumer<SortOption> subscriber){

        //getting reference to radio group
        final RadioGroup rgSortOption = (RadioGroup) dialogView.findViewById(R.id.rg_sort_option);

        //getting reference to buttons
        Button btnApply = (Button) dialogView.findViewById(R.id.btn_filter_dialog_apply);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_filter_dialog_cancel);

        //get saved sort option
        checkSavedSortOption(getSortOption(context), rgSortOption);

        //creating the alert dialog instance with view passed
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();

        //apply button click listener
        RxView.clicks(btnApply).subscribe(v -> {
            int viewId = rgSortOption.getCheckedRadioButtonId();
            SortOption sortOption = getSortOptionByViewId(viewId);

            subscriber.accept(sortOption);
            dialog.cancel();
        });

        //cancel button click listener
        RxView.clicks(btnCancel).subscribe(v->dialog.cancel());

        //return the dialog instance
        return dialog;
    }


    public static View getAlertDialogView(Context context){

        return LayoutInflater.from(context)
                .inflate(R.layout.alert_dialog_view, null);
    }

    public static String getFormattedDate(Date date){
        if (date != null) {
            DateFormat displayFormat = new SimpleDateFormat("E MMMM d, yyyy", Locale.ENGLISH);
            return displayFormat.format(date);
        } else {
            return "NA";
        }
    }

    public static SortOption getSortOption(Context context){
        MySharedPref sharedPref = new MySharedPref(context);
        return sharedPref.getSortOption();
    }

    public static Boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    private static void checkSavedSortOption(SortOption sortOption, RadioGroup radioGroup){
        switch (sortOption) {
            case PUBLISHER: ((RadioButton)radioGroup.findViewById(R.id.rb_publisher)).setChecked(true);
                break;
            case CATEGORY: ((RadioButton)radioGroup.findViewById(R.id.rb_category)).setChecked(true);
                break;
            case OLD_TO_NEW: ((RadioButton)radioGroup.findViewById(R.id.rb_old_new)).setChecked(true);
                break;
            case NEW_TO_OLD: ((RadioButton)radioGroup.findViewById(R.id.rb_new_old)).setChecked(true);
                break;
        }
    }

    public static void saveSortOption(Context context, SortOption sortOption){
        MySharedPref sharedPref = new MySharedPref(context);
        sharedPref.saveSortOption(sortOption);
    }

    private static SortOption getSortOptionByViewId(int viewId){
        switch(viewId){
            case R.id.rb_publisher:
                return PUBLISHER;

            case R.id.rb_category:
                return CATEGORY;

            case R.id.rb_old_new:
                return OLD_TO_NEW;

            case R.id.rb_new_old:
                return NEW_TO_OLD;

            default:
                return NEW_TO_OLD;
        }
    }


}
