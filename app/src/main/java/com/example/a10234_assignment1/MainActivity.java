package com.example.a10234_assignment1;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.imageview.ShapeableImageView;

public class MainActivity extends AppCompatActivity {

    private static final int ANIMATION_DURATION = 1000;

    ShapeableImageView image1;
    ShapeableImageView image2;
    ShapeableImageView image3;
    AppCompatButton button;
    private Animation fadeOutAnimation;
    private Animation fadeInAnimation;
    private int currentImageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void hideImages() {
        switch (currentImageIndex) {
            case 0:
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null && wifiManager.isWifiEnabled()) {
                    // WiFi is enabled
                    handleWithoutAnimation(image1, "fadeOutAnimation", null);
//                    handleWithAnimation(image1, View.VISIBLE, fadeOutAnimation);
                } else {
                    // WiFi is not enabled
                    handleWithoutAnimation(image1, "fadeInAnimation", "Hint - WIFI");
//                    handleWithAnimation(image1, View.INVISIBLE, fadeInAnimation);
                }
                break;
            case 1:
                NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
                if (nfcAdapter != null && nfcAdapter.isEnabled()) {
                    // NFC is enabled
                    handleWithoutAnimation(image2, "fadeOutAnimation", null);
//                    handleWithAnimation(image2, View.VISIBLE, fadeOutAnimation);
                } else {
                    // NFC is not enabled
                    handleWithoutAnimation(image2, "fadeInAnimation", "Hint - NFC");
//                    handleWithAnimation(image2, View.INVISIBLE, fadeInAnimation);
                }
                break;
            case 2:
                currentImageIndex++;
                break;
            default:
                Toast.makeText(this, "All images are visible", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void handleWithoutAnimation(ShapeableImageView image, String animation, String text) {
        if (image.getVisibility() == View.VISIBLE && animation.equals("fadeOutAnimation")) {
            image.setVisibility(View.INVISIBLE);
        } else if (animation.equals("fadeInAnimation")) {
            if (image.getVisibility() == View.INVISIBLE) {
                image.setVisibility(View.VISIBLE);
            }
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
        doSomethingAfter();
    }

    private void handleWithAnimation(ShapeableImageView image, int status, Animation animation) {
        if (image.getVisibility() == status) {
            image.startAnimation(animation);
        } else {
            doSomethingAfter();
        }
    }

    private void doSomethingAfter() {
        currentImageIndex++;
        if (currentImageIndex < 3) {
            hideImages();
        }
    }

    private void initViews() {
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        button = findViewById(R.id.centerButton);

        button.setOnClickListener(v -> {
            currentImageIndex = 0;
            hideImages();
        });

        fadeOutAnimation();

        fadeInAnimation();
    }

    private void fadeInAnimation() {
        fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimation.setDuration(ANIMATION_DURATION);
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ShapeableImageView nextShapeableImageView = getShapeableImageView();
                if (nextShapeableImageView != null) {
                    nextShapeableImageView.setVisibility(View.VISIBLE);
                }
                doSomethingAfter();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void fadeOutAnimation() {
        fadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        fadeOutAnimation.setDuration(ANIMATION_DURATION);
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ShapeableImageView nextShapeableImageView = getShapeableImageView();
                if (nextShapeableImageView != null) {
                    nextShapeableImageView.setVisibility(View.INVISIBLE);
                }
                doSomethingAfter();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private ShapeableImageView getShapeableImageView() {
        switch (currentImageIndex) {
            case 0:
                return image1;
            case 1:
                return image2;
            case 2:
                return image3;
            default:
                return null;
        }
    }
}