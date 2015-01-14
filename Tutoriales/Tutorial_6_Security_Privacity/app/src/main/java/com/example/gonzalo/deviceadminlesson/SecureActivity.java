package com.example.gonzalo.deviceadminlesson;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Gonzalo on 14/01/2015.
 */
public class SecureActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();
        // Check to see if the device is properly secured as per the policy.  Send user
        // back to policy set up screen if necessary.
        Policy policy = new Policy(this);
        policy.readFromLocal();
        if (!policy.isDeviceSecured()) {
            Intent intent = new Intent();
            intent.setClass(this, PolicySetupActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_secure);
    }
}
