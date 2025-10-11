package com.qzt.common.core.util;


import com.xiaoleilu.hutool.captcha.AbstractCaptcha;
import com.xiaoleilu.hutool.util.ImageUtil;
import com.xiaoleilu.hutool.util.RandomUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

public class CircleCaptchay extends AbstractCaptcha {
    private static final long serialVersionUID = -7096627300356535494L;

    public CircleCaptchay(int width, int height) {
        this(width, height, 5);
    }

    public CircleCaptchay(int width, int height, int codeCount) {
        this(width, height, codeCount, 15);
    }

    public CircleCaptchay(int width, int height, int codeCount, int interfereCount) {
        super(width, height, codeCount, interfereCount);
    }

    @Override
    public void createCode() {
        this.code = RandomUtil.randomNumbers(this.codeCount);
        this.createImage(this.code);
    }

    public void createImage(String code) {
        this.image = new BufferedImage(this.width, this.height, 1);
        Graphics2D g = ImageUtil.createGraphics(this.image, Color.WHITE);
        g.setFont(this.font);
        int len = code.length();
        int w = this.width / len;

        for (int i = 0; i < len; ++i) {
            AlphaComposite ac3 = AlphaComposite.getInstance(3, 0.9F);
            g.setComposite(ac3);
            g.setColor(ImageUtil.randomColor());
            g.drawString(String.valueOf(code.charAt(i)), i * w, 25);
        }

        this.drawInterfere(g);
    }

    private void drawInterfere(Graphics2D g) {
        ThreadLocalRandom random = RandomUtil.getRandom();

        for (int i = 0; i < this.interfereCount; ++i) {
            g.setColor(ImageUtil.randomColor(random));
            g.drawOval(random.nextInt(this.width), random.nextInt(this.height), random.nextInt(this.height >> 1), random.nextInt(this.height >> 1));
        }

    }
}
