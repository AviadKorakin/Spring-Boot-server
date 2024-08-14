package com.example.menu_editor.Utilities;


import com.example.menu_editor.General.ObjectId;

public interface ChangeCallback {
    void change(MenuItem menuItem);
    void delete(MenuItem menuItem);
}