package com.zzx.factorytest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.zzx.factorytest.R;

public class ScreenCanvasView extends View {

    /*******************  **********************/
    public static final int MODE_COLOR_BLACK = 1;
    public static final int MODE_COLOR_GREEN = 2;
    public static final int MODE_COLOR_PURPLE = 3;// �Ϻ�
    public static final int MODE_COLOR_RED = 4;
    public static final int MODE_COLOR_WHITE = 5;
    public static final int MODE_COLOR_YELLOW = 6;
    public static final int MODE_COLOR_BLUE = 7;

    /********************  *************************/
    public static final int MODE_GEOMETRY_WHITE_BLACK = 8;
    public static final int MODE_GEOMETRY_BLUE_BLACK = 9;
    public static final int MODE_GEOMETRY_GREEN_BLACK = 10;
    public static final int MODE_GEOMETRY_RED_BLACK = 11;

    /********************  ****************/
    public static final int MODE_MIX_1DOT = 12;
    // public static final int MODE_1DOT_1BLACK = 13;
    public static final int MODE_MIX_1DOT_1WHITE = 14;
    public static final int MODE_MIX_1H = 15;
    public static final int MODE_MIX_1VT = 16;
    public static final int MODE_MIX_2DOT = 17;
    public static final int MODE_MIX_2DOT_1BLACK = 18;
    public static final int MODE_MIX_2H = 19;
    public static final int MODE_MIX_2V = 20;
    public static final int MODE_MIX_3DOT = 21;
    public static final int MODE_MIX_3DOT_1BLACK = 22;
    public static final int MODE_MIX_MIX_3V = 23;
    public static final int MODE_MIX_1DOT_1BLACK = 24;
    public static final int MODE_MIX_1V = 25;
    public static final int MODE_MIX_3DOT_1WHITE = 26;
    public static final int MODE_MIX_3H = 27;
    public static final int MODE_MIX_2DOT_1WHITE = 28;
    public static final int MODE_MIX_3V = 29;

    /************
     * �׶Ȳ���
     *************/
    public static final int MODE_STEP_H_16 = 30;
    public static final int MODE_STEP_H_32 = 31;
    public static final int MODE_STEP_H_64 = 32;
    public static final int MODE_STEP_V_16 = 33;
    public static final int MODE_STEP_V_32 = 34;
    public static final int MODE_STEP_V_64 = 35;
    public static final int MODE_STEP_V_8 = 36;
    public static final int MODE_STEP_H_8 = 37;

    private int currentMode = 0;
    private int currentIndex = 0;

    private int screenWidthPixels;
    private int screenHeightPixels;

    public ScreenCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
//		screenWidthPixels = this.getScreenWidth();
//		screenHeightPixels = this.getScreenHeight();
    }

    /**
     * ���Ե�ǰ��ʾ��ͼƬ����
     *
     * @param picMode
     */
    public void setCurrentMode(int picMode, int index) {
        this.currentMode = picMode;
        this.currentIndex = index;
        this.invalidate();
    }

    public ScreenCanvasView(Context context) {
        this(context, null);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        screenWidthPixels = getMeasuredWidth();
        screenHeightPixels = getMeasuredHeight();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 1.����ͬ���͵�ͼƬ
        switch (currentMode) {
            /****************** ��ɫ��ʼ ****************/
            case MODE_COLOR_BLACK:
                drawColorPic(canvas, Color.BLACK);
                break;
            case MODE_COLOR_BLUE:
                drawColorPic(canvas, Color.BLUE);
                break;
            case MODE_COLOR_GREEN:
                drawColorPic(canvas, Color.GREEN);
                break;
            case MODE_COLOR_RED:
                drawColorPic(canvas, Color.RED);
                break;
            case MODE_COLOR_WHITE:
                drawColorPic(canvas, Color.WHITE);
                break;
            case MODE_COLOR_YELLOW:
                drawColorPic(canvas, Color.YELLOW);
                break;
            case MODE_COLOR_PURPLE:
                drawColorPic(canvas, Color.rgb(255, 0, 255));
                break;
            /******************* ���β��Կ�ʼ ************************/
            case MODE_GEOMETRY_WHITE_BLACK:
            case MODE_GEOMETRY_GREEN_BLACK:
            case MODE_GEOMETRY_RED_BLACK:
            case MODE_GEOMETRY_BLUE_BLACK:
                drawGeometryPic(canvas, currentMode);
                break;
            /**************************** ������Կ�ʼ ****************************/
            case MODE_MIX_1DOT:
            case MODE_MIX_2DOT:
            case MODE_MIX_3DOT:
            case MODE_MIX_1DOT_1BLACK:
            case MODE_MIX_2DOT_1BLACK:
            case MODE_MIX_3DOT_1BLACK:
            case MODE_MIX_1DOT_1WHITE:
            case MODE_MIX_2DOT_1WHITE:
            case MODE_MIX_3DOT_1WHITE:
            case MODE_MIX_1H:
            case MODE_MIX_2H:
            case MODE_MIX_3H:
            case MODE_MIX_1V:
            case MODE_MIX_2V:
            case MODE_MIX_3V:
                // TODO
                drawMixPic(canvas, currentMode);
                break;
            case MODE_STEP_H_8:
            case MODE_STEP_H_16:
            case MODE_STEP_H_32:
            case MODE_STEP_H_64:
            case MODE_STEP_V_8:
            case MODE_STEP_V_16:
            case MODE_STEP_V_32:
            case MODE_STEP_V_64:
                drawStepPic(canvas, currentMode);
            default:
                break;
        }

        // 2.����������
        drawIndexNumer(canvas, currentIndex);
        super.onDraw(canvas);


    }

    private void drawIndexNumer(Canvas canvas, int index) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(getResources().getDimension(R.dimen.screenCanvasLabelSize));
        canvas.drawText(index + "", 10, 150, paint);
    }

    /**
     * ���Ƶ�ɫͼƬ
     *
     * @param canvas
     * @param color
     */
    private void drawColorPic(Canvas canvas, int color) {
        canvas.drawColor(color);
    }

    /**
     * ���Ƽ��β���ͼ
     *
     * @param canvas
     * @param mode
     */
    private void drawGeometryPic(Canvas canvas, int mode) {
        int offsetW = 27;
        int offsetH = 18;
        screenWidthPixels = getWidth();
        screenHeightPixels = getHeight();
        // ��16��26+2���
        float rectWidth = screenWidthPixels / offsetW;// С����Ŀ��
        float rectHeight = screenHeightPixels / offsetH;// С����ĸ߶�
        float startX = rectWidth / 2;
        float startY = 0;
        Paint paint = new Paint();
        canvas.drawColor(Color.BLACK);// ��ɫ����
        switch (mode) {
            case MODE_GEOMETRY_WHITE_BLACK:
                paint.setColor(Color.WHITE);
                break;
            case MODE_GEOMETRY_RED_BLACK:
                paint.setColor(Color.RED);
                break;
            case MODE_GEOMETRY_GREEN_BLACK:
                paint.setColor(Color.GREEN);
                break;
            case MODE_GEOMETRY_BLUE_BLACK:
                paint.setColor(Color.BLUE);
                break;
            default:
                break;

        }

        for (int j = 0; j < offsetW + 1; j++) {
            canvas.drawLine(startX, 0, startX, screenHeightPixels, paint);// ����
            startX += rectWidth;
        }
        for (int i = 0; i < offsetH + 1; i++) {
            canvas.drawLine(0, startY, screenWidthPixels, startY, paint);// ����
            startY += rectHeight;
        }

    }

    /**
     * �������ͼƬ
     *
     * @param canvas
     * @param mode
     */
    private void drawMixPic(Canvas canvas, int mode) {
        long time = SystemClock.currentThreadTimeMillis();


        Bitmap bitmap = Bitmap.createBitmap(this.screenWidthPixels,
                this.screenHeightPixels, Config.ARGB_8888);
        int x, y;
        if (mode == MODE_MIX_1DOT) {

            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {
                    x = i / 1;
                    y = j / 1;
                    if ((x + y) % 2 == 0) {
                        bitmap.setPixel(i, j, Color.WHITE);
                    } else {
                        bitmap.setPixel(i, j, Color.BLACK);
                    }
                }
            }
        } else if (mode == MODE_MIX_2DOT) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {
                    x = i / 2;
                    y = j / 2;
                    if ((x + y) % 2 == 0) {
                        bitmap.setPixel(i, j, Color.WHITE);
                    } else {
                        bitmap.setPixel(i, j, Color.BLACK);
                    }
                }
            }
        } else if (mode == MODE_MIX_3DOT) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {
                    x = i / 3;
                    y = j / 3;
                    if ((x + y) % 2 == 0) {
                        bitmap.setPixel(i, j, Color.WHITE);
                    } else {
                        bitmap.setPixel(i, j, Color.BLACK);
                    }
                }
            }
        } else if (mode == MODE_MIX_1DOT_1BLACK) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {

                    if (i % 2 == 0 && j % 2 == 0) {
                        bitmap.setPixel(i, j, Color.BLACK);
                    } else {
                        bitmap.setPixel(i, j, Color.WHITE);
                    }
                }
            }
        } else if (mode == MODE_MIX_2DOT_1BLACK) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {

                    if (i % 3 == 0 && j % 3 == 0) {
                        bitmap.setPixel(i, j, Color.BLACK);
                    } else {
                        bitmap.setPixel(i, j, Color.WHITE);
                    }
                }
            }
        } else if (mode == MODE_MIX_3DOT_1BLACK) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {

                    if (i % 4 == 0 && j % 4 == 0) {
                        bitmap.setPixel(i, j, Color.BLACK);
                    } else {
                        bitmap.setPixel(i, j, Color.WHITE);
                    }
                }
            }
        } else if (mode == MODE_MIX_1DOT_1WHITE) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {

                    if (i % 2 == 0 && j % 2 == 0) {
                        bitmap.setPixel(i, j, Color.WHITE);
                    } else {
                        bitmap.setPixel(i, j, Color.BLACK);
                    }
                }
            }
        } else if (mode == MODE_MIX_2DOT_1WHITE) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {

                    if (i % 3 == 0 && j % 3 == 0) {
                        bitmap.setPixel(i, j, Color.WHITE);
                    } else {
                        bitmap.setPixel(i, j, Color.BLACK);
                    }
                }
            }
        } else if (mode == MODE_MIX_3DOT_1WHITE) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {

                    if (i % 4 == 0 && j % 4 == 0) {
                        bitmap.setPixel(i, j, Color.WHITE);
                    } else {
                        bitmap.setPixel(i, j, Color.BLACK);
                    }
                }
            }
        } else if (mode == MODE_MIX_1H) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {

                    if (j % 2 == 0) {
                        bitmap.setPixel(i, j, Color.WHITE);
                    } else {
                        bitmap.setPixel(i, j, Color.BLACK);
                    }
                }
            }
        } else if (mode == MODE_MIX_2H) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {

                    if (j % 3 == 0) {
                        bitmap.setPixel(i, j, Color.WHITE);
                    } else {
                        bitmap.setPixel(i, j, Color.BLACK);
                    }
                }
            }
        } else if (mode == MODE_MIX_3H) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {

                    if (j % 4 == 0) {
                        bitmap.setPixel(i, j, Color.WHITE);
                    } else {
                        bitmap.setPixel(i, j, Color.BLACK);
                    }
                }
            }
        } else if (mode == MODE_MIX_1V) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {

                    if (i % 2 == 0) {
                        bitmap.setPixel(i, j, Color.WHITE);
                    } else {
                        bitmap.setPixel(i, j, Color.BLACK);
                    }
                }
            }
        } else if (mode == MODE_MIX_2V) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {

                    if (i % 3 == 0) {
                        bitmap.setPixel(i, j, Color.WHITE);
                    } else {
                        bitmap.setPixel(i, j, Color.BLACK);
                    }
                }
            }
        } else if (mode == MODE_MIX_3V) {
            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {

                    if (i % 4 == 0) {
                        bitmap.setPixel(i, j, Color.WHITE);
                    } else {
                        bitmap.setPixel(i, j, Color.BLACK);
                    }
                }
            }
        }
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
        bitmap.recycle();
        System.out.println("drawMixPic = " + (SystemClock.currentThreadTimeMillis() - time));
    }

    private void drawStepPic(Canvas canvas, int mode) {
        int rectWidth;// ���ƿ��
        int colorGap;// ��ɫֵ���
        Paint paint = new Paint();
        paint.setStyle(Style.FILL);
        if (mode == MODE_STEP_H_8) {
            int colorStep = 8;
            rectWidth = this.screenHeightPixels / colorStep;
            colorGap = 255 / (colorStep - 1);
            int color = 255;
            for (int i = 0; i < colorStep; i++) {
                paint.setColor(Color.rgb(color, color, color));
                canvas.drawRect(0, i * rectWidth, this.screenWidthPixels,
                        (i + 1) * rectWidth, paint);
                color -= colorGap;
            }

        } else if (mode == MODE_STEP_H_16) {
            int colorStep = 16;
            rectWidth = this.screenHeightPixels / colorStep;
            colorGap = 255 / (colorStep - 1);
            int color = 255;
            for (int i = 0; i < colorStep; i++) {
                paint.setColor(Color.rgb(color, color, color));
                canvas.drawRect(0, i * rectWidth, this.screenWidthPixels,
                        (i + 1) * rectWidth, paint);
                color -= colorGap;
            }
        } else if (mode == MODE_STEP_H_32) {
            int colorStep = 32;
            rectWidth = this.screenHeightPixels / colorStep;
            colorGap = 255 / (colorStep - 1);
            int color = 255;
            for (int i = 0; i < colorStep; i++) {
                paint.setColor(Color.rgb(color, color, color));
                canvas.drawRect(0, i * rectWidth, this.screenWidthPixels,
                        (i + 1) * rectWidth, paint);
                color -= colorGap;
            }
        } else if (mode == MODE_STEP_H_64) {
            int colorStep = 64;
            rectWidth = this.screenHeightPixels / colorStep;
            colorGap = 255 / (colorStep - 1);
            int color = 255;
            for (int i = 0; i < colorStep; i++) {
                paint.setColor(Color.rgb(color, color, color));
                canvas.drawRect(0, i * rectWidth, this.screenWidthPixels,
                        (i + 1) * rectWidth, paint);
                color -= colorGap;
            }
        } else if (mode == MODE_STEP_V_8) {
            int colorStep = 8;
            rectWidth = this.screenWidthPixels / colorStep;
            colorGap = 255 / (colorStep - 1);
            int color = 255;
            for (int i = 0; i < colorStep; i++) {
                paint.setColor(Color.rgb(color, color, color));
                canvas.drawRect(i * rectWidth, 0, (i + 1) * rectWidth,
                        this.screenHeightPixels, paint);
                color -= colorGap;
            }
        } else if (mode == MODE_STEP_V_16) {
            int colorStep = 16;
            rectWidth = this.screenWidthPixels / colorStep;
            colorGap = 255 / (colorStep - 1);
            int color = 255;
            for (int i = 0; i < colorStep; i++) {
                paint.setColor(Color.rgb(color, color, color));
                canvas.drawRect(i * rectWidth, 0, (i + 1) * rectWidth,
                        this.screenHeightPixels, paint);
                color -= colorGap;
            }
        } else if (mode == MODE_STEP_V_32) {
            int colorStep = 32;
            rectWidth = this.screenWidthPixels / colorStep;
            colorGap = 255 / (colorStep - 1);
            int color = 255;
            for (int i = 0; i < colorStep; i++) {
                paint.setColor(Color.rgb(color, color, color));
                canvas.drawRect(i * rectWidth, 0, (i + 1) * rectWidth,
                        this.screenHeightPixels, paint);
                color -= colorGap;
            }
        } else if (mode == MODE_STEP_V_64) {
            int colorStep = 64;
            rectWidth = this.screenWidthPixels / colorStep;
            colorGap = 255 / (colorStep - 1);
            int color = 255;
            for (int i = 0; i < colorStep; i++) {
                paint.setColor(Color.rgb(color, color, color));
                canvas.drawRect(i * rectWidth, 0, (i + 1) * rectWidth,
                        this.screenHeightPixels, paint);
                color -= colorGap;
            }
        }

    }

    public int getScreenWidth() {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public int getScreenHeight() {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

}
