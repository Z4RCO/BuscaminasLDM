package com.example.practica4.androidimpl;

import java.util.List;

import android.view.View.OnTouchListener;

import com.example.practica4.Input;


public interface TouchHandler extends OnTouchListener {
    public boolean isTouchDown(int pointer);

    public int getTouchX(int pointer);

    public int getTouchY(int pointer);

    public List<Input.TouchEvent> getTouchEvents();
}

