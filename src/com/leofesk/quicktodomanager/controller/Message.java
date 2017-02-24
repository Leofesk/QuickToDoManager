package com.leofesk.quicktodomanager.controller;

import com.leofesk.quicktodomanager.model.Options;

public class Message {
    public static String getText(String key) {
        return Options.getTextByLang(key);
    }
}
