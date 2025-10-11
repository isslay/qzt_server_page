package com.qzt.common.swjtu.tts.impl;

import com.qzt.common.swjtu.lang.LANG;
import com.qzt.common.swjtu.tts.AbstractTTS;

public final class YoudaoTTS extends AbstractTTS {
    private final static String url = "http://tts.youdao.com/fanyivoice";

    public YoudaoTTS() {
        super(url);
        setLangSupport();
    }

    @Override
    public void setLangSupport() {
        langMap.put(LANG.EN, "eng");
        langMap.put(LANG.JP, "jap");
        langMap.put(LANG.FRA, "fr");
        langMap.put(LANG.KOR, "ko");
    }

    @Override
    public void setFormData(LANG source, String text) {
        formData.put("word", text);
        formData.put("le", langMap.get(source));
        formData.put("keyfrom", "speaker-target");
    }
}
