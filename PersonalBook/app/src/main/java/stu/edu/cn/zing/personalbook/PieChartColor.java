package stu.edu.cn.zing.personalbook;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;

/**
 * Created by Administrator on 2017/5/13.
 */

public class PieChartColor {

    private List<Integer> pieInputColors;
    private List<Integer> pieOuputColors;

    private  Context context;

    public PieChartColor (Context context) {
        this.context = context;
        initInputColors();
        initOutputColors();
    }


    public  int getInputColor(int index) {

        return pieInputColors.get(index);
    }

    public int getOutputColor(int index) {
        return pieOuputColors.get(index);
    }

    private   void  initInputColors() {

        pieInputColors = new ArrayList<>();
        pieInputColors.add(context.getColor(R.color.pie_input_color1));
        pieInputColors.add(context.getColor(R.color.pie_input_color7));
        pieInputColors.add(context.getColor(R.color.pie_input_color3));
        pieInputColors.add(context.getColor(R.color.pie_input_color8));
        pieInputColors.add(context.getColor(R.color.pie_input_color5));
        pieInputColors.add(context.getColor(R.color.pie_input_color6));
        pieInputColors.add(context.getColor(R.color.pie_input_color4));
        pieInputColors.add(context.getColor(R.color.pie_input_color2));
        pieInputColors.add(context.getColor(R.color.pie_input_color9));
        pieInputColors.add(context.getColor(R.color.pie_input_color10));


    }

    private void initOutputColors() {
        pieOuputColors = new ArrayList<>();

        pieOuputColors.add(context.getColor(R.color.pie_output_color1));
        pieOuputColors.add(context.getColor(R.color.pie_output_color2));
        pieOuputColors.add(context.getColor(R.color.pie_output_color3));
        pieOuputColors.add(context.getColor(R.color.pie_output_color4));
        pieOuputColors.add(context.getColor(R.color.pie_output_color5));
        pieOuputColors.add(context.getColor(R.color.pie_output_color6));
        pieOuputColors.add(context.getColor(R.color.pie_output_color7));
        pieOuputColors.add(context.getColor(R.color.pie_output_color8));
        pieOuputColors.add(context.getColor(R.color.pie_output_color9));
        pieOuputColors.add(context.getColor(R.color.pie_output_color10));
    }
}
