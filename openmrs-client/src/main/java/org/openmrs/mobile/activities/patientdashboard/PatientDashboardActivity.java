/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.mobile.activities.patientdashboard;

import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.ACBaseActivity;
import org.openmrs.mobile.activities.patientdashboard.charts.PatientChartsFragment;
import org.openmrs.mobile.activities.patientdashboard.charts.PatientDashboardChartsPresenter;
import org.openmrs.mobile.activities.patientdashboard.details.PatientDashboardDetailsPresenter;
import org.openmrs.mobile.activities.patientdashboard.details.PatientDetailsFragment;
import org.openmrs.mobile.activities.patientdashboard.diagnosis.PatientDashboardDiagnosisPresenter;
import org.openmrs.mobile.activities.patientdashboard.diagnosis.PatientDiagnosisFragment;
import org.openmrs.mobile.activities.patientdashboard.visits.PatientDashboardVisitsPresenter;
import org.openmrs.mobile.activities.patientdashboard.visits.PatientVisitsFragment;
import org.openmrs.mobile.activities.patientdashboard.vitals.PatientDashboardVitalsPresenter;
import org.openmrs.mobile.activities.patientdashboard.vitals.PatientVitalsFragment;
import org.openmrs.mobile.utilities.ApplicationConstants;
import org.openmrs.mobile.utilities.TabUtil;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class PatientDashboardActivity extends ACBaseActivity {

    public PatientDashboardContract.PatientDashboardMainPresenter mPresenter;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);
        getSupportActionBar().setElevation(0);
        Bundle patientBundle = savedInstanceState;
        if (null != patientBundle) {
            patientBundle.getString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE);
        } else {
            patientBundle = getIntent().getExtras();
        }
        mId = String.valueOf(patientBundle.get(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE));
        initViewPager(new PatientDashboardPagerAdapter(getSupportFragmentManager(), this, mId));
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        attachPresenterToFragment(fragment);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, mId);
    }

    @Override
    public void onConfigurationChanged(final Configuration config) {
        super.onConfigurationChanged(config);
        TabUtil.setHasEmbeddedTabs(getSupportActionBar(), getWindowManager(),
            TabUtil.MIN_SCREEN_WIDTH_FOR_PATIENTDASHBOARDACTIVITY);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.patient_dashboard_menu, menu);
        return true;
    }

    private void initViewPager(PatientDashboardPagerAdapter adapter) {
        final ViewPager viewPager = findViewById(R.id.pager);
        TabLayout tabHost = findViewById(R.id.tabhost);

        viewPager.setAdapter(adapter);
        tabHost.setupWithViewPager(viewPager);
    }

    private void attachPresenterToFragment(Fragment fragment) {
        Bundle patientBundle = getIntent().getExtras();
        String id = String.valueOf(patientBundle.get(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE));
        if (fragment instanceof PatientDetailsFragment) {
            mPresenter = new PatientDashboardDetailsPresenter(id, ((PatientDetailsFragment) fragment));
        } else if (fragment instanceof PatientDiagnosisFragment) {
            mPresenter = new PatientDashboardDiagnosisPresenter(id, ((PatientDiagnosisFragment) fragment));
        } else if (fragment instanceof PatientVisitsFragment) {
            mPresenter = new PatientDashboardVisitsPresenter(id, ((PatientVisitsFragment) fragment));
        } else if (fragment instanceof PatientVitalsFragment) {
            mPresenter = new PatientDashboardVitalsPresenter(id, ((PatientVitalsFragment) fragment));
        } else if (fragment instanceof PatientChartsFragment) {
            mPresenter = new PatientDashboardChartsPresenter(id, ((PatientChartsFragment) fragment));
        }
    }

}
