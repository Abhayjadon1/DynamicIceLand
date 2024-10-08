package com.miniature.dynamiciceland.lock.activites;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miniature.dynamiciceland.lock.SplashLaunchActivity;
import com.miniature.dynamiciceland.lock.adaptar.AppsRecyclerViewAdapter;
import com.miniature.dynamiciceland.lock.background.PrefManager;
import com.miniature.dynamiciceland.lock.entity.AppDetail;
import com.miniature.dynamiciceland.lock.entity.AppPackageList;
import com.miniature.dynamiciceland.lock.utils.Utils;
import com.miniature.dynamiciceland.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AppsFilterListActivity extends Activity {
    AppsRecyclerViewAdapter adapter;
    public HashMap<String, AppDetail> appDetailHashMap = new HashMap<>();
    
    public final ArrayList<AppDetail> applications = new ArrayList<>();
    
    public ArrayList<AppDetail> apps;

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.applist_activity);


        //fb ads call
        SplashLaunchActivity.FBInterstitialAdCall(this);


        //Mix Banner Ads Call
        RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.btm10);
        RelativeLayout adContainer2 = (RelativeLayout) findViewById(R.id.ads2);
        ImageView OwnBannerAds = (ImageView) findViewById(R.id.bannerads);
        SplashLaunchActivity.MixBannerAdsCall(this, adContainer, adContainer2, OwnBannerAds);


        loadApps();
        loadListView();
        addClickListener();
        AppPackageList selectedFilterPkg = PrefManager.getSelectedFilterPkg(this);
        if (selectedFilterPkg != null && this.apps != null) {
            for (int i = 0; i < selectedFilterPkg.appDetailList.size(); i++) {
                AppDetail appDetail = selectedFilterPkg.appDetailList.get(i);
                for (int i2 = 0; i2 < this.apps.size(); i2++) {
                    if (this.apps.get(i2).pkg.equals(appDetail.pkg) && this.apps.get(i2).activityInfoName.equals(appDetail.activityInfoName)) {
                        this.apps.get(i2).isSelected = true;
                    }
                }
            }
            this.adapter.notifyDataSetChanged();
        }
    }

    private void loadApps() {
        try {
            ArrayList<AppDetail> arrayList = this.apps;
            if (arrayList != null) {
                arrayList.clear();
            } else {
                this.apps = new ArrayList<>();
            }
            if (!this.appDetailHashMap.isEmpty()) {
                this.appDetailHashMap.clear();
            }
            UserManager userManager = (UserManager) getSystemService("user");
            LauncherApps launcherApps = (LauncherApps) getSystemService("launcherapps");
            Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
            intent.addCategory("android.intent.category.LAUNCHER");
            PackageManager packageManager = getPackageManager();
            for (ResolveInfo next : packageManager.queryIntentActivities(intent, 0)) {
                AppDetail appDetail = new AppDetail();
                appDetail.label = next.loadLabel(packageManager).toString();
                appDetail.pkg = next.activityInfo.packageName;
                appDetail.activityInfoName = next.activityInfo.name;
                appDetail.isSorted = false;
                this.apps.add(appDetail);
                this.appDetailHashMap.put(appDetail.label + appDetail.activityInfoName + appDetail.pkg, appDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void getFilteredList(String str) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.apps.size(); i++) {
            if (!(this.apps.get(i) == null || this.apps.get(i).label == null || (!this.apps.get(i).label.toUpperCase().startsWith(str) && !this.apps.get(i).label.toUpperCase().contains(str)))) {
                arrayList.add(this.apps.get(i));
            }
        }
        ArrayList<AppDetail> sortAppsAlphabetically = Utils.sortAppsAlphabetically(arrayList);
        this.applications.clear();
        this.applications.addAll(sortAppsAlphabetically);
        this.adapter.notifyDataSetChanged();
    }

    private void loadListView() {
        ((EditText) findViewById(R.id.search_field)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    AppsFilterListActivity.this.getFilteredList(editable.toString().toUpperCase());
                    return;
                }
                AppsFilterListActivity.this.apps.addAll(Utils.sortAppsAlphabetically(AppsFilterListActivity.this.apps));
                AppsFilterListActivity.this.applications.clear();
                AppsFilterListActivity.this.applications.addAll(AppsFilterListActivity.this.apps);
                AppsFilterListActivity.this.adapter.notifyDataSetChanged();
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_apps);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<AppDetail> arrayList = this.apps;
        arrayList.addAll(Utils.sortAppsAlphabetically(arrayList));
        this.applications.clear();
        this.applications.addAll(this.apps);
        AppsRecyclerViewAdapter appsRecyclerViewAdapter = new AppsRecyclerViewAdapter(this, this.applications, this.appDetailHashMap);
        this.adapter = appsRecyclerViewAdapter;
        recyclerView.setAdapter(appsRecyclerViewAdapter);
    }

    private void addClickListener() {
        findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AppPackageList appPackageList = new AppPackageList();
                appPackageList.appDetailList = new ArrayList<>();
                for (int i = 0; i < AppsFilterListActivity.this.apps.size(); i++) {
                    if (((AppDetail) AppsFilterListActivity.this.apps.get(i)).isSelected) {
                        appPackageList.appDetailList.add((AppDetail) AppsFilterListActivity.this.apps.get(i));
                    }
                }
                PrefManager.setFilterPkg(AppsFilterListActivity.this.getApplicationContext(), appPackageList);
                AppsFilterListActivity.this.finish();
            }
        });
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AppsFilterListActivity.this.finish();
            }
        });
    }
}
