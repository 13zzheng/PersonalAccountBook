package stu.edu.cn.zing.personalbook.finanicalreport;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import stu.edu.cn.zing.personalbook.BookItems;
import stu.edu.cn.zing.personalbook.FindListenerUI;
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.BookItem;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemTitle;
import stu.edu.cn.zing.personalbook.bmobclass.BookMonthFinancial;
import stu.edu.cn.zing.personalbook.bmobclass.BookPaymentType;
import stu.edu.cn.zing.personalbook.model.BookManager;

/**
 * Created by Administrator on 2017/5/9.
 */

public class FinanicalReportData {

    private IFinanicalReportActivity iFinanicalReportActivity;
    private int year;
    private int month;
    private BookManager bookManager;
    private int dayNumber;

    private Map<String, Integer> typeNumberHashMap;
    private Map<String, Integer> memberNumberHashMap;
    private Map<String, Float> typePaymentHashMap;
    private Map<String, Float> memberPaymentHashMap;


    public FinanicalReportData(IFinanicalReportActivity iFinanicalReportActivity1) {
        this.iFinanicalReportActivity = iFinanicalReportActivity1;
    }

    public void setDate(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public void init() {
        bookManager = new BookManager();
        dayNumber = getDaysByYearMonth(year, month);
        typeNumberHashMap = new HashMap<>();
        memberNumberHashMap = new HashMap<>();
        typePaymentHashMap = new HashMap<>();
        memberPaymentHashMap = new HashMap<>();
        Log.i("DayNumber", dayNumber + "");
        initMonth();
        initTitleList();

    }

    private int getDaysByYearMonth(int year, int month) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());
        String[] d = date.split("-");
        String y = d[0];
        String m = d[1];
        String day = d[2];
        if (m.substring(0,1).equals("0")) {
            m = m.substring(1);
        }if (day.substring(0,1).equals("0")) {
            day = day.substring(1);
        }
        if (y.equals(String.valueOf(year)) && m.equals(String.valueOf(month))) {
            return Integer.valueOf(day);
        }


        Calendar a = Calendar.getInstance();

        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    private void initMonth() {

        bookManager.queryBookMonthFinancial(year, month, Book.getCurrentBook(), new FindListenerUI<BookMonthFinancial>() {
            @Override
            public void onSucess(List<BookMonthFinancial> list) {
                if (list.size() > 0) {
                    iFinanicalReportActivity.showData();
                    BookMonthFinancial bookMonthFinancial = list.get(0);
                    float input = bookMonthFinancial.getMonthInput();
                    float output = bookMonthFinancial.getMonthOutput();
                    float budget = bookMonthFinancial.getBudgetValue();
                    iFinanicalReportActivity.setTotalInput(input);
                    iFinanicalReportActivity.setTotalOutput(output);
                    iFinanicalReportActivity.setBudgetEnable(bookMonthFinancial.getBudgetEnable());

                    float difference = input - output;
                    iFinanicalReportActivity.setBudgetPercentage(bookMonthFinancial.getBudgetOverageValue() / budget);
                    iFinanicalReportActivity.setOverageValue(difference);
                    iFinanicalReportActivity.setOveragePercentage(difference / input);

                    float averageInput = input / dayNumber;
                    float averageOutput = output / dayNumber;
                    iFinanicalReportActivity.setAverageDayInput(averageInput);
                    iFinanicalReportActivity.setAverageDayOutput(averageOutput);

                }else {
                    iFinanicalReportActivity.showNoData();
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    private void initTitleList() {
        final List<BookItems> bookItemsList = new ArrayList<>();
        bookManager.queryBookItemTitle(year, month, Book.getCurrentBook(),new FindListenerUI<BookItemTitle>() {
            @Override
            public void onSucess(final List<BookItemTitle> list) {
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        final BookItems bookItems = new BookItems();
                        bookItems.setTitle(list.get(i));
                        BmobQuery<BookItem> itemQuery = new BmobQuery<>();
                        itemQuery.addWhereEqualTo("itemTitle", list.get(i));
                        itemQuery.include("payment,itemTitle,itemInfo.itemType,itemInfo.itemTradingWay,itemInfo.itemMember");
                        itemQuery.findObjects(new FindListener<BookItem>() {
                            @Override
                            public void done(List<BookItem> list2, BmobException e) {
                                if (e == null) {
                                    float totalInput = 0;
                                    float totalOuput = 0;
                                    bookItems.setItems(list2);
                                    for (int j = 0; j < list2.size(); j++) {
                                        if (list2.get(j).getPayment().getPayType() == BookPaymentType.TYPE_POSITIVE) {
                                            totalInput += list2.get(j).getPayment().getPay();
                                        } else if (list2.get(j).getPayment().getPayType() == BookPaymentType.TYPE_NEGATIVE){
                                            totalOuput += list2.get(j).getPayment().getPay();
                                            String type = list2.get(j).getItemInfo().getItemType().getType();
                                            String member = list2.get(j).getItemInfo().getItemMember().getItemMember();
                                            if (typeNumberHashMap.containsKey(type)) {
                                                typeNumberHashMap.put(type, typeNumberHashMap.get(type) + 1);
                                            }else {
                                                typeNumberHashMap.put(type, 1);
                                            }

                                            if (memberNumberHashMap.containsKey(member)) {
                                                memberNumberHashMap.put(member, memberNumberHashMap.get(member) + 1);
                                            }else {
                                                memberNumberHashMap.put(member, 1);
                                            }

                                            if (typePaymentHashMap.containsKey(type)) {
                                                typePaymentHashMap.put(type, typePaymentHashMap.get(type) + list2.get(j).getPayment().getPay());
                                            } else {
                                                typePaymentHashMap.put(type, list2.get(j).getPayment().getPay());
                                            }

                                            if (memberPaymentHashMap.containsKey(member)) {
                                                memberPaymentHashMap.put(member, memberPaymentHashMap.get(member) + list2.get(j).getPayment().getPay());
                                            } else {
                                                memberPaymentHashMap.put(member, list2.get(j).getPayment().getPay());
                                            }
                                        }

                                    }

                                    bookItems.setTotalInput(totalInput);
                                    bookItems.setTotalOutput(totalOuput);

                                    bookItemsList.add(bookItems);
                                    if (bookItemsList.size() == list.size()) {
                                        //数据获取完毕

                                        Log.i("Size",typeNumberHashMap.size() + "");
                                        List<Map.Entry<String, Integer>> typeNumberList = new ArrayList<>(typeNumberHashMap.entrySet());
                                        List<Map.Entry<String, Integer>> memberNumberList = new ArrayList<>(memberNumberHashMap.entrySet());

                                        List<Map.Entry<String, Float>> typePaymentList = new ArrayList<>(typePaymentHashMap.entrySet());
                                        List<Map.Entry<String, Float>> memberPaymentList = new ArrayList<>(memberPaymentHashMap.entrySet());

                                        Collections.sort(typeNumberList, new HashMapComparatorByValue());
                                        Collections.sort(memberNumberList, new HashMapComparatorByValue());

                                        Collections.sort(typePaymentList, new HashMapComparatorByFloat());
                                        Collections.sort(memberPaymentList, new HashMapComparatorByFloat());

                                        iFinanicalReportActivity.setMostRateType(typeNumberList.get(0).getKey());
                                        iFinanicalReportActivity.setMostRateTypeNumber(typeNumberList.get(0).getValue());
                                        iFinanicalReportActivity.setMostRateMember(memberNumberList.get(0).getKey());
                                        iFinanicalReportActivity.setMostRateMemberNumber(memberNumberList.get(0).getValue());

                                        iFinanicalReportActivity.setMostPaymentType(typePaymentList.get(0).getKey());
                                        iFinanicalReportActivity.setMostPaymentTypeValue(typePaymentList.get(0).getValue());
                                        iFinanicalReportActivity.setMostPaymentMember(memberPaymentList.get(0).getKey());
                                        iFinanicalReportActivity.setMostPaymentMemberValue(memberPaymentList.get(0).getValue());

                                        Collections.sort(bookItemsList, new BookItemsComparatorByOutput());
                                        iFinanicalReportActivity.setMostOutputDay(bookItemsList.get(0).getTitle().getMonth(),
                                                bookItemsList.get(0).getTitle().getDay());
                                        iFinanicalReportActivity.setMostOutputValue(bookItemsList.get(0).getTotalOutput());
                                    }

                                }
                            }
                        });

                    }


                }
            }

            @Override
            public void onFail() {

            }
        });
    }

}
