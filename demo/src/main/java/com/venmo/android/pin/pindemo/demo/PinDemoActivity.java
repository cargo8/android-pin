package com.venmo.android.pin.pindemo.demo;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.venmo.android.pin.PinFragment;
import com.venmo.android.pin.util.PinHelper;

import java.util.concurrent.TimeUnit;

public class PinDemoActivity extends Activity implements PinFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_demo);
        showPinFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pin_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            PinHelper.resetDefaultSavedPin(this);
            showPinFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValidated() {
        Toast.makeText(this, "Validated PIN!", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showPinFragment();
            }
        }, TimeUnit.SECONDS.toMillis(2));
    }

    @Override
    public void onPinCreated() {
        Toast.makeText(this, "Created PIN!", Toast.LENGTH_SHORT).show();
        showPinFragment();
    }

    private void showPinFragment() {
        Fragment toShow = PinHelper.hasDefaultPinSaved(this) ?
                PinFragment.newInstanceForVerification() :
                PinFragment.newInstanceForCreation();

        getFragmentManager().beginTransaction()
                .replace(R.id.root, toShow, toShow.getClass().getSimpleName())
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .commit();
    }

}
