package com.example.flashlight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout backgroundColor;
    private ImageView backgroundImage;
    private SwitchCompat mySwitch;
    private static final int CAMERA_REQUEST = 50;
    int[] color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        backgroundColor = findViewById(R.id.mainLayout);
        backgroundImage = findViewById(R.id.image_view_torch);
        mySwitch = findViewById(R.id.switch_button);
        color = new int[] {Color.BLACK, Color.WHITE};

        final boolean isEnabled = ContextCompat.checkSelfPermission
                (this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        final boolean hasCameraFlash = getPackageManager().hasSystemFeature
                (PackageManager.FEATURE_CAMERA_FLASH);

        camPermissionRequest();
        backgroundColor.setBackgroundColor(color[0]);

        mySwitch.setChecked(!isEnabled);
        mySwitch.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(hasCameraFlash&&isEnabled&&isChecked){
                            flashLightOn();
                        }else if(!hasCameraFlash) {
                            Toast.makeText(MainActivity.this,
                                    "No flash available on your device",
                                    Toast.LENGTH_SHORT).show();
                        }else if (!isEnabled){
                            Toast.makeText(MainActivity.this,
                                    "No permission to use camera",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            flashLightOff();
                        }
                    }
                });
    }

    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        backgroundImage.setImageResource(R.drawable.torch_on);
        backgroundColor.setBackgroundColor(color[1]);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {

        }
    }

    private void flashLightOff () {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        backgroundImage.setImageResource(R.drawable.torch6);
        backgroundColor.setBackgroundColor(color[0]);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void camPermissionRequest(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {Manifest.permission.CAMERA}, CAMERA_REQUEST);
    }
}