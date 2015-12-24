/*
 * Copyright 2012-2014 Daniel Serdyukov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.angelectro.zahittalipov.weather;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.ContentResolver;
import android.os.Bundle;
import android.util.Log;

/**
 * @author Daniel Serdyukov
 */
public class AppDelegate extends Application {

    public static final String ACCOUNT_TYPE = "com.angelectro.zahittalipov.weather.account";
    public static final String AUTHORITY = "com.angelectro.zahittalipov.weather";

    public static Account sAccount;

    @Override
    public void onCreate() {
        Log.d("weather","onCreate App delegate");
        super.onCreate();
        final AccountManager am = AccountManager.get(this);
        if (sAccount == null) {
            sAccount = new Account("weather", ACCOUNT_TYPE);
        }
        if (am.addAccountExplicitly(sAccount, getPackageName(), new Bundle())) {
            ContentResolver.setSyncAutomatically(sAccount, AUTHORITY, true);
        }
    }

}
