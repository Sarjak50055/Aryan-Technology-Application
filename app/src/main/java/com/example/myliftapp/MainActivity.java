package com.example.myliftapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private View joystickKnob;
    private FrameLayout joystickContainer;
    private Spinner wallSpinner;
    private Spinner textureSpinner;
    private Button btnApply;

    private float knobStartX, knobStartY;
    private int containerWidth, containerHeight;
    private int knobWidth, knobHeight;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        joystickKnob = findViewById(R.id.joystick_knob);
        joystickContainer = findViewById(R.id.joystick_container);
        wallSpinner = findViewById(R.id.wall_spinner);
        textureSpinner = findViewById(R.id.texture_spinner);
        btnApply = findViewById(R.id.btn_apply);

        // Setup WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        webView.loadUrl("file:///android_asset/3d_viewer.html");

        // Setup Spinners
        String[] walls = {"front", "back", "left", "right", "top", "bottom"};
        ArrayAdapter<String> wallAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, walls);
        wallSpinner.setAdapter(wallAdapter);

        String[] textures = {"metal", "glass", "wood"};
        ArrayAdapter<String> textureAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, textures);
        textureSpinner.setAdapter(textureAdapter);

        btnApply.setOnClickListener(v -> {
            String selectedWall = wallSpinner.getSelectedItem().toString();
            String selectedTexture = textureSpinner.getSelectedItem().toString();
            // Call JS function
            webView.evaluateJavascript("javascript:updateTexture('" + selectedWall + "', '" + selectedTexture + "')", null);
        });

        setupJoystick();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupJoystick() {
        joystickContainer.post(() -> {
            containerWidth = joystickContainer.getWidth();
            containerHeight = joystickContainer.getHeight();
            knobWidth = joystickKnob.getWidth();
            knobHeight = joystickKnob.getHeight();
            
            knobStartX = (containerWidth - knobWidth) / 2f;
            knobStartY = (containerHeight - knobHeight) / 2f;
        });

        joystickContainer.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    float x = event.getX() - knobWidth / 2f;
                    float y = event.getY() - knobHeight / 2f;
                    
                    // Constrain within the container
                    x = Math.max(0, Math.min(x, containerWidth - knobWidth));
                    y = Math.max(0, Math.min(y, containerHeight - knobHeight));

                    joystickKnob.setX(x);
                    joystickKnob.setY(y);

                    // Calculate rotation values (-1 to 1)
                    float deltaX = (x - knobStartX) / (containerWidth / 2f);
                    float deltaY = (y - knobStartY) / (containerHeight / 2f);
                    
                    // Send rotation to WebView
                    webView.evaluateJavascript("javascript:rotateCamera(" + deltaX + ", " + deltaY + ")", null);
                    return true;

                case MotionEvent.ACTION_UP:
                    // Reset knob to center
                    joystickKnob.setX(knobStartX);
                    joystickKnob.setY(knobStartY);
                    webView.evaluateJavascript("javascript:rotateCamera(0, 0)", null);
                    return true;
            }
            return false;
        });
    }
}
