package com.bazhanpavel.sudoku;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener {

    String difficult;
    double x;
    double y;
    double x0;
    double y0;
    float x1;
    float y1;
    double angleI;
    int numb;
    String angleIstr;
    TextView currentCell;
    int[] numbers = new int[81];
    int[] rightNumbers =   {1,2,3,4,5,6,7,8,9,
                            4,5,6,7,8,9,1,2,3,
                            7,8,9,1,2,3,4,5,6,
                            2,3,4,5,6,7,8,9,1,
                            5,6,7,8,9,1,2,3,4,
                            8,9,1,2,3,4,5,6,7,
                            3,4,5,6,7,8,9,1,2,
                            6,7,8,9,1,2,3,4,5,
                            9,1,2,3,4,5,6,7,8};
    boolean[] maskNumbers = new boolean[81];
    TextView[] items = new TextView[81];




    //функции для перетасовки массива
    public static void swap_rows_small(int[] arr, int rowNum, int line1, int line2) {
        int buffer;
        for (int i = 27*rowNum; i < 27*rowNum+9; i++) {
            buffer = arr[i+line1*9];
            arr[i+line1*9] = arr[i + line2*9];
            arr[i + line2*9] = buffer;
        }
        return;
    }
    public static void swap_column_small(int[] arr, int colNum, int line1, int line2) {
        int buffer;
        for (int i = 3*colNum; i <= 3*colNum+72; i = i + 9) {
            buffer = arr[i+line1];
            arr[i+line1] = arr[i+line2];
            arr[i+line2] = buffer;
        }
        return;
    }
    public static void swap_rows_area(int[] arr, int row1, int row2) {
        int buffer;
        for (int i = 27*row1; i < 27*row1+27; i++) {
            buffer = arr[i];
            arr[i] = arr[i+27*(row2-row1)];
            arr[i+27*(row2-row1)] = buffer;
        }
        return;
    }
    public static void swap_column_area(int[] arr, int col1, int col2) {
        int buffer;
        for (int j = col1*3; j < col1*3+3; j++) {
            for (int i = j; i <= j+72; i=i+9) {
                buffer = arr[i];
                arr[i] = arr[i+3*(col2-col1)];
                arr[i+3*(col2-col1)] = buffer;
            }
        }
        return;
    }

    //функция перемешивания массива
    public static void remix(int[] arr) {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int aa = (int) Math.floor(Math.random()*3);
            int bb = (int) Math.floor(Math.random()*2);
            int cc = (int) Math.floor(Math.random()*2) + 1;
            //Log.d("myTag", aa + " " + bb + " " + cc);
            swap_rows_small(arr, aa, bb, cc);
            swap_column_small(arr, aa, bb, cc);
            swap_rows_area(arr, bb, cc);
            swap_column_area(arr, bb, cc);
        }
    }

    //функция для создания маски с открытыми/закрытыми числами
    public static void makemask(boolean[] arr, String diff) {
        if (diff.equals("easy")) {
            int[] indexarr = new int[4];
            for (int i = 0; i < 4; i++) {
                indexarr[i] = (int) Math.floor(Math.random()*81);
                Log.d("", indexarr[i]+"");
            }
            for (int i = 0; i < 81; i++) {
                for (int j = 0; j < 4; j++) {
                    if (i == indexarr[j]) { arr[i] = true; break; } else { arr[i] = false; }
                }
            }
            return;
        }

        if (diff.equals("hard")) {
            int[] indexarr = new int[20];
            for (int i = 0; i < 20; i++) {
                indexarr[i] = (int) Math.floor(Math.random()*81);
                Log.d("", indexarr[i]+"");
            }
            for (int i = 0; i < 81; i++) {
                for (int j = 0; j < 20; j++) {
                    if (i == indexarr[j]) { arr[i] = true; break; } else { arr[i] = false; }
                }
            }
        }
    }

    //функция для создания массива текущего состояния
    public static void makenumbers(int[] numbers, int[] right, boolean[] mask) {
        for (int i = 0; i < 81; i++) {
            if (mask[i] == true) { numbers[i] = 0; } else { numbers[i] = right[i]; }
        }
        return;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //устанавливаем сложность
        difficult = (String) getIntent().getStringExtra("DIFF");
        Log.e("qwe", difficult);
        Log.e("qwr", "easy");
        Log.e("qwe", (difficult.equals("easy")) + "");

        //формируем массив с выигрышной комбинацией чисел
        //рандомно перемешиваем массив
        remix(rightNumbers);

        //формирование маски с открытыми/закрытыми числами
        makemask(maskNumbers, difficult);

        //создание массива, содержащего текущее состояние решения
        makenumbers(numbers, rightNumbers, maskNumbers);

        //заполняем клетки таблицы числами из rightNumbers и назначаем им обработчик
        for (int i=0; i<81; i++) {
            String txtID;
            if (i<10) {
                txtID = "cell0" + i;
            } else {
                txtID = "cell" + i;
            }

            int resID = getResources().getIdentifier(txtID, "id", getPackageName());

            items[i] = (TextView) findViewById(resID);

            if (maskNumbers[i] == false) {
                items[i].setText(rightNumbers[i] + "");
            }
            if (maskNumbers[i] == true) {
                items[i].setText("");
                items[i].setOnTouchListener(this);
            }
        }
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        //получаем объекты с цифрами по id
        ImageView num01 = (ImageView) findViewById(R.id.num01);
        ImageView num01wh = (ImageView) findViewById(R.id.num01wh);
        ImageView num02 = (ImageView) findViewById(R.id.num02);
        ImageView num02wh = (ImageView) findViewById(R.id.num02wh);
        ImageView num03 = (ImageView) findViewById(R.id.num03);
        ImageView num03wh = (ImageView) findViewById(R.id.num03wh);
        ImageView num04 = (ImageView) findViewById(R.id.num04);
        ImageView num04wh = (ImageView) findViewById(R.id.num04wh);
        ImageView num05 = (ImageView) findViewById(R.id.num05);
        ImageView num05wh = (ImageView) findViewById(R.id.num05wh);
        ImageView num06 = (ImageView) findViewById(R.id.num06);
        ImageView num06wh = (ImageView) findViewById(R.id.num06wh);
        ImageView num07 = (ImageView) findViewById(R.id.num07);
        ImageView num07wh = (ImageView) findViewById(R.id.num07wh);
        ImageView num08 = (ImageView) findViewById(R.id.num08);
        ImageView num08wh = (ImageView) findViewById(R.id.num08wh);
        ImageView num09 = (ImageView) findViewById(R.id.num09);
        ImageView num09wh = (ImageView) findViewById(R.id.num09wh);


        TextView vw = (TextView) v;
        x = event.getX();
        y = event.getY();
        TextView numbv = (TextView) findViewById(R.id.numb);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: // нажатие
                    x0 = x;
                    y0 = y;
                    v.setBackgroundColor(0xFF555555);
                    vw.setTextColor(Color.WHITE);

                    int[] crds = new int[2];
                    v.getLocationOnScreen(crds);

                    TextView graycover = (TextView) findViewById(R.id.graycover);
                    graycover.setBackgroundColor(0x77000000);

                    //делаем объекты с цифрами видимыми
                    num01.setVisibility(View.VISIBLE);
                    num02.setVisibility(View.VISIBLE);
                    num03.setVisibility(View.VISIBLE);
                    num04.setVisibility(View.VISIBLE);
                    num05.setVisibility(View.VISIBLE);
                    num06.setVisibility(View.VISIBLE);
                    num07.setVisibility(View.VISIBLE);
                    num08.setVisibility(View.VISIBLE);
                    num09.setVisibility(View.VISIBLE);

                    //устанавливаем координаты
                    num01.setX(crds[0]); num01.setY(crds[1] - 120);
                    num01wh.setX(crds[0]); num01wh.setY(crds[1] - 120);
                    num02.setX(crds[0] + 77); num02.setY(crds[1] - 92);
                    num02wh.setX(crds[0] + 77); num02wh.setY(crds[1] - 92);
                    num03.setX(crds[0] + 118); num03.setY(crds[1] - 21);
                    num03wh.setX(crds[0] + 118); num03wh.setY(crds[1] - 21);
                    num04.setX(crds[0] + 104); num04.setY(crds[1] + 60);
                    num04wh.setX(crds[0] + 104); num04wh.setY(crds[1] + 60);
                    num05.setX(crds[0] + 41); num05.setY(crds[1] + 112);
                    num05wh.setX(crds[0] + 41); num05wh.setY(crds[1] + 112);
                    num06.setX(crds[0] - 41); num06.setY(crds[1] + 112);
                    num06wh.setX(crds[0] - 41); num06wh.setY(crds[1] + 112);
                    num07.setX(crds[0] - 104); num07.setY(crds[1] + 60);
                    num07wh.setX(crds[0] - 104); num07wh.setY(crds[1] + 60);
                    num08.setX(crds[0] - 118); num08.setY(crds[1] - 21);
                    num08wh.setX(crds[0] - 118); num08wh.setY(crds[1] - 21);
                    num09.setX(crds[0] - 77); num09.setY(crds[1] - 92);
                    num09wh.setX(crds[0] - 77); num09wh.setY(crds[1] - 92);

                    break;
                case MotionEvent.ACTION_MOVE: // движение
                    if (Math.sqrt((x - x0) * (x - x0) + (y - y0) * (y - y0)) >= 50) {
                        if (x > x0 && y <= y0) {
                            angleI = Math.toDegrees(Math.asin((x - x0) / (Math.sqrt(Math.pow(x - x0, 2) + Math.pow(y - y0, 2)))));
                        }
                        if (x >= x0 && y > y0) {
                            angleI = 90 + Math.toDegrees(Math.acos((x - x0) / (Math.sqrt(Math.pow(x - x0, 2) + Math.pow(y - y0, 2)))));
                        }
                        if (x < x0 && y >= y0) {
                            angleI = 90 + Math.toDegrees(Math.acos((x - x0) / (Math.sqrt(Math.pow(x - x0, 2) + Math.pow(y - y0, 2)))));
                        }
                        if (x <= x0 && y < y0) {
                            angleI = 360 + Math.toDegrees(Math.asin((x - x0) / (Math.sqrt(Math.pow(x - x0, 2) + Math.pow(y - y0, 2)))));
                        }

                        if (angleI <= 20 || angleI > 340) {
                            numb = 1;
                            num01wh.setVisibility(View.VISIBLE);
                            num02wh.setVisibility(View.GONE);
                            num03wh.setVisibility(View.GONE);
                            num04wh.setVisibility(View.GONE);
                            num05wh.setVisibility(View.GONE);
                            num06wh.setVisibility(View.GONE);
                            num07wh.setVisibility(View.GONE);
                            num08wh.setVisibility(View.GONE);
                            num09wh.setVisibility(View.GONE);
                        }
                        if (angleI <= 60 && angleI > 20) {
                            numb = 2;
                            num01wh.setVisibility(View.GONE);
                            num02wh.setVisibility(View.VISIBLE);
                            num03wh.setVisibility(View.GONE);
                            num04wh.setVisibility(View.GONE);
                            num05wh.setVisibility(View.GONE);
                            num06wh.setVisibility(View.GONE);
                            num07wh.setVisibility(View.GONE);
                            num08wh.setVisibility(View.GONE);
                            num09wh.setVisibility(View.GONE);
                        }
                        if (angleI <= 100 && angleI > 60) {
                            numb = 3;
                            num01wh.setVisibility(View.GONE);
                            num02wh.setVisibility(View.GONE);
                            num03wh.setVisibility(View.VISIBLE);
                            num04wh.setVisibility(View.GONE);
                            num05wh.setVisibility(View.GONE);
                            num06wh.setVisibility(View.GONE);
                            num07wh.setVisibility(View.GONE);
                            num08wh.setVisibility(View.GONE);
                            num09wh.setVisibility(View.GONE);
                        }
                        if (angleI <= 140 && angleI > 100) {
                            numb = 4;
                            num01wh.setVisibility(View.GONE);
                            num02wh.setVisibility(View.GONE);
                            num03wh.setVisibility(View.GONE);
                            num04wh.setVisibility(View.VISIBLE);
                            num05wh.setVisibility(View.GONE);
                            num06wh.setVisibility(View.GONE);
                            num07wh.setVisibility(View.GONE);
                            num08wh.setVisibility(View.GONE);
                            num09wh.setVisibility(View.GONE);
                        }
                        if (angleI <= 180 && angleI > 140) {
                            numb = 5;
                            num01wh.setVisibility(View.GONE);
                            num02wh.setVisibility(View.GONE);
                            num03wh.setVisibility(View.GONE);
                            num04wh.setVisibility(View.GONE);
                            num05wh.setVisibility(View.VISIBLE);
                            num06wh.setVisibility(View.GONE);
                            num07wh.setVisibility(View.GONE);
                            num08wh.setVisibility(View.GONE);
                            num09wh.setVisibility(View.GONE);
                        }
                        if (angleI <= 220 && angleI > 180) {
                            numb = 6;
                            num01wh.setVisibility(View.GONE);
                            num02wh.setVisibility(View.GONE);
                            num03wh.setVisibility(View.GONE);
                            num04wh.setVisibility(View.GONE);
                            num05wh.setVisibility(View.GONE);
                            num06wh.setVisibility(View.VISIBLE);
                            num07wh.setVisibility(View.GONE);
                            num08wh.setVisibility(View.GONE);
                            num09wh.setVisibility(View.GONE);
                        }
                        if (angleI <= 260 && angleI > 220) {
                            numb = 7;
                            num01wh.setVisibility(View.GONE);
                            num02wh.setVisibility(View.GONE);
                            num03wh.setVisibility(View.GONE);
                            num04wh.setVisibility(View.GONE);
                            num05wh.setVisibility(View.GONE);
                            num06wh.setVisibility(View.GONE);
                            num07wh.setVisibility(View.VISIBLE);
                            num08wh.setVisibility(View.GONE);
                            num09wh.setVisibility(View.GONE);
                        }
                        if (angleI <= 300 && angleI > 260) {
                            numb = 8;
                            num01wh.setVisibility(View.GONE);
                            num02wh.setVisibility(View.GONE);
                            num03wh.setVisibility(View.GONE);
                            num04wh.setVisibility(View.GONE);
                            num05wh.setVisibility(View.GONE);
                            num06wh.setVisibility(View.GONE);
                            num07wh.setVisibility(View.GONE);
                            num08wh.setVisibility(View.VISIBLE);
                            num09wh.setVisibility(View.GONE);
                        }
                        if (angleI <= 340 && angleI > 300) {
                            numb = 9;
                            num01wh.setVisibility(View.GONE);
                            num02wh.setVisibility(View.GONE);
                            num03wh.setVisibility(View.GONE);
                            num04wh.setVisibility(View.GONE);
                            num05wh.setVisibility(View.GONE);
                            num06wh.setVisibility(View.GONE);
                            num07wh.setVisibility(View.GONE);
                            num08wh.setVisibility(View.GONE);
                            num09wh.setVisibility(View.VISIBLE);
                        }

                        numbv.setTextColor(0xFFFFFFFF);
                        numbv.setText(numb + "");
                    } else {
                        numbv.setTextColor(0x00FFFFFF);
                        numbv.setText("");

                        //в мертвой зоне отключаем панель с числами
                        num01wh.setVisibility(View.GONE);
                        num02wh.setVisibility(View.GONE);
                        num03wh.setVisibility(View.GONE);
                        num04wh.setVisibility(View.GONE);
                        num05wh.setVisibility(View.GONE);
                        num06wh.setVisibility(View.GONE);
                        num07wh.setVisibility(View.GONE);
                        num08wh.setVisibility(View.GONE);
                        num09wh.setVisibility(View.GONE);
                    }

                    break;
                case MotionEvent.ACTION_UP: // отпускание
                    v.setBackgroundColor(0xFFBBBBBB);
                    vw.setTextColor(0xFF555555);
                    if (Math.sqrt((x - x0) * (x - x0) + (y - y0) * (y - y0)) >= 50) {
                        TextView currentCell = (TextView) v;
                        currentCell.setText(numb + "");
                        String strid = new String(currentCell.getResources().getResourceName(currentCell.getId()));
                        strid = strid.substring(30);
                        int intid = Integer.parseInt(strid);
                        numbers[intid] = numb;
                        if (Arrays.equals(numbers, rightNumbers)) {
                            TextView welldone = (TextView) findViewById(R.id.textView7);
                            welldone.setTextColor(0xFF00CC00);
                            for (int i = 0; i < 81; i++) {
                                items[i].setOnTouchListener(null);
                            }
                        }
                    }
                    TextView graycover2 = (TextView) findViewById(R.id.graycover);
                    graycover2.setBackgroundColor(0x00000000);
                    numbv.setTextColor(0x00FFFFFF);
                    numbv.setText("");

                    //отключаем панель выбора числа
                    num01.setVisibility(View.GONE);
                    num02.setVisibility(View.GONE);
                    num03.setVisibility(View.GONE);
                    num04.setVisibility(View.GONE);
                    num05.setVisibility(View.GONE);
                    num06.setVisibility(View.GONE);
                    num07.setVisibility(View.GONE);
                    num08.setVisibility(View.GONE);
                    num09.setVisibility(View.GONE);
                    num01wh.setVisibility(View.GONE);
                    num02wh.setVisibility(View.GONE);
                    num03wh.setVisibility(View.GONE);
                    num04wh.setVisibility(View.GONE);
                    num05wh.setVisibility(View.GONE);
                    num06wh.setVisibility(View.GONE);
                    num07wh.setVisibility(View.GONE);
                    num08wh.setVisibility(View.GONE);
                    num09wh.setVisibility(View.GONE);

                    break;
                case MotionEvent.ACTION_CANCEL:
                    v.setBackgroundColor(0x00555555);
                    vw.setTextColor(Color.WHITE);
                    numbv.setTextColor(0x00FFFFFF);
                    numbv.setText("");
                    break;
            }
        return true;
    }
}
