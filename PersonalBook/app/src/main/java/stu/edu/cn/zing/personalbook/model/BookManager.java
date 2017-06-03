package stu.edu.cn.zing.personalbook.model;

import android.content.Intent;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import stu.edu.cn.zing.personalbook.FindListenerUI;
import stu.edu.cn.zing.personalbook.SaveListenerUI;
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.BookItem;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemInfo;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemTitle;
import stu.edu.cn.zing.personalbook.bmobclass.BookMonthFinancial;
import stu.edu.cn.zing.personalbook.bmobclass.BookPayment;
import stu.edu.cn.zing.personalbook.bmobclass.BookPaymentType;

/**
 * Created by Administrator on 2017/4/29.
 */

public class BookManager implements IBookManager{

    private String bookPaymentObjecId = null;
    private String bookItemInfoObjectId = null;
    private String bookItemTitleObjectId = null;

    private boolean flag1 = false;
    private boolean flag2 = false;

    @Override
    public void addBookItem(final BookItemTitle itemTitle, BookPayment bookPayment, BookItemInfo itemInfo, final SaveListenerUI saveListenerUI) {
        Log.i("BookItem","开始保存");
        final BookItem bookItem = new BookItem();

        bookPaymentObjecId = null;
        bookItemInfoObjectId = null;
        bookItemTitleObjectId = null;

        saveBookPayment(bookPayment, new SaveListenerUI() {
            @Override
            public void onSucessUI() {
                if (isAllSave()) {
                    //如果全部添加成功，再添加bookitem
                    saveBookItem(bookItem, saveListenerUI);
                }
            }

            @Override
            public void onFailUI() {

            }
        });


        saveBookItemInfo(itemInfo, new SaveListenerUI() {
            @Override
            public void onSucessUI() {
                if (isAllSave()) {
                    //如果全部添加成功，再添加bookitem
                    saveBookItem(bookItem, saveListenerUI);
                }
            }

            @Override
            public void onFailUI() {

            }
        });

        queryBookItemTitle(itemTitle, new FindListenerUI<BookItemTitle>() {
            @Override
            public void onSucess(List<BookItemTitle> list) {
                if (list.size() > 0) {
                    //已经存在，不再添加
                    Log.i("BookItem", "Title已存在");
                    bookItemTitleObjectId = list.get(0).getObjectId();

                    if (isAllSave()) {
                        //如果全部添加成功，再添加bookitem
                        saveBookItem(bookItem, saveListenerUI);
                    }

                } else if (list.size() == 0) {
                    //不存在，添加
                    Log.i("BookItem", "Title不存在，开始添加");
                    saveBookItemTitle(itemTitle, new SaveListenerUI() {
                        @Override
                        public void onSucessUI() {
                            if (isAllSave()) {
                                //如果全部添加成功，再添加bookitem
                                saveBookItem(bookItem, saveListenerUI);
                            }
                        }

                        @Override
                        public void onFailUI() {

                        }
                    });
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    @Override
    public void updateBookItem(BookItem bookItem, final SaveListenerUI saveListenerUI) {
        bookItem.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    saveListenerUI.onSucessUI();
                } else {
                    saveListenerUI.onFailUI();
                }
            }
        });
    }

    @Override
    public void deleteBookItem(final BookItem bookItem, final SaveListenerUI saveListener) {
        bookItem.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //删除成功
                    caculateDelete(bookItem.getItemTitle(), bookItem.getPayment(), new SaveListenerUI() {
                        @Override
                        public void onSucessUI() {
                            saveListener.onSucessUI();
                        }

                        @Override
                        public void onFailUI() {

                        }
                    });
                }else {
                    //删除失败
                    saveListener.onFailUI();
                }
            }
        });
        Log.i("DELETE", "delete");
        bookItem.getPayment().delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("DELETE", "payment");
                }
            }
        });
        bookItem.getItemInfo().delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("DELETE", "itemInfo");
                }
            }
        });

    }

    private void caculateDelete(BookItemTitle itemTitle, BookPayment bookPayment, final SaveListenerUI saveListenerUI) {
        flag1 = false;
        flag2 = false;

        caculateTitleTotalPayDelete(itemTitle, bookPayment, saveListenerUI);
        caculateMonthFinancialDelete(itemTitle.getYear(), itemTitle.getMonth(), bookPayment, saveListenerUI);


    }

    private void caculateTitleTotalPayDelete(BookItemTitle itemTitle, final BookPayment bookPayment, final SaveListenerUI saveListenerUI) {
        Log.i("BookItem","开始计算Title");
        queryBookItemTitle(itemTitle, new FindListenerUI<BookItemTitle>() {
            @Override
            public void onSucess(List<BookItemTitle> list) {
                if (list.size() > 0) {
                    //已经存在，修改更新
                    BookItemTitle title = list.get(0);

                    if (bookPayment.getPayType() == BookPaymentType.TYPE_POSITIVE) {
                        //收入：大于0
                        title.setTotalPay(title.getTotalPay() - bookPayment.getPay());
                    } else if (bookPayment.getPayType() == BookPaymentType.TYPE_NEGATIVE) {
                        //支出：小于0
                        title.setTotalPay(title.getTotalPay() + bookPayment.getPay());
                    }

                    //更新Title
                    title.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                flag1 = true;
                                Log.i("BookItem","计算Title且更新完成");
                                if (flag1 && flag2) {
                                    saveListenerUI.onSucessUI();
                                }
                            }
                        }
                    });
                } else {
                    //不存在

                }

            }

            @Override
            public void onFail() {

            }
        });

    }


    private void caculateMonthFinancialDelete(final int year, final int month, final BookPayment bookPayment, final SaveListenerUI saveListenerUI) {
        Log.i("BookItem","开始计算BookMonthFinancial");
        queryBookMonthFinancial(year, month, Book.getCurrentBook(), new FindListenerUI<BookMonthFinancial>() {
            @Override
            public void onSucess(List<BookMonthFinancial> list) {
                if (list.size() > 0) {
                    //存在
                    BookMonthFinancial bookMonthFinancial = list.get(0);

                    if (bookPayment.getPayType() == BookPaymentType.TYPE_POSITIVE) {
                        //收入：大于0
                        bookMonthFinancial.setMonthInput(bookMonthFinancial.getMonthInput() - bookPayment.getPay());
                    } else if (bookPayment.getPayType() == BookPaymentType.TYPE_NEGATIVE) {
                        //支出：小于0
                        bookMonthFinancial.setMonthOutput(bookMonthFinancial.getMonthOutput() - bookPayment.getPay());
                    }

                    if (bookMonthFinancial.getBudgetEnable()) {
                        //如果预算设置开启
                        bookMonthFinancial.setBudgetOverageValue(bookMonthFinancial.getBudgetValue() - bookMonthFinancial.getMonthOutput());
                    }

                    //更新
                    bookMonthFinancial.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                flag2 = true;
                                Log.i("BookItem","计算BookMonthFinancial且更新完成");
                                if (flag1 && flag2) {
                                    saveListenerUI.onSucessUI();
                                }
                            }
                        }
                    });
                }else {
                    //不存在
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    @Override
    public void searchBookItem(List<QueryCriteria> queryCriterias) {

        List<BmobQuery<BookItem>> querys = new ArrayList<>();
        for (int i = 0; i < queryCriterias.size(); i++) {
            BmobQuery query = new BmobQuery();
            QueryCriteria queryCriteria = queryCriterias.get(i);

            if (queryCriteria.getValueType() == QueryCriteria.VALUE_TYPE_INT) {
                query.addWhereEqualTo(queryCriteria.getName(), Integer.valueOf(queryCriteria.getValue()));
            } else if (queryCriteria.getValueType() == QueryCriteria.VALUE_TYPE_STRING) {
                query.addWhereEqualTo(queryCriteria.getName(), queryCriteria.getValue());
            }

            querys.add(query);
        }
        BmobQuery mainQuery = new BmobQuery();
        mainQuery.and(querys);


    }

    private void saveBookItem(final BookItem bookItem, final SaveListenerUI saveListenerUI) {

        BmobQuery<BookItemInfo> q1 = new BmobQuery<>();
        q1.getObject(bookItemInfoObjectId, new QueryListener<BookItemInfo>() {
            @Override
            public void done(BookItemInfo bookItemInfo, BmobException e) {
                if (e == null) {
                    bookItem.setItemInfo(bookItemInfo);
                    Log.i("BookItem", "BookItemInfo设置成功");
                    if (isBookItemSet(bookItem)) {
                        bookItem.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    Log.i("BookItem","BookItem保存成功");
                                    saveListenerUI.onSucessUI();
                                } else {
                                    Log.e("BookItem","保存失败"+ e.getErrorCode());
                                }
                            }
                        });
                    }
                }else {
                    Log.i("BookItem", "BookItemInfo设置失败" + e.getErrorCode());
                }
            }
        });

        BmobQuery<BookPayment> q2 = new BmobQuery<>();
        q2.getObject(bookPaymentObjecId, new QueryListener<BookPayment>() {
            @Override
            public void done(BookPayment bookPayment, BmobException e) {
                if (e == null) {
                    bookItem.setPayment(bookPayment);
                    Log.i("BookItem", "BookPayment设置成功");
                    if (isBookItemSet(bookItem)) {
                        bookItem.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    Log.i("BookItem","BookItem保存成功");
                                    saveListenerUI.onSucessUI();
                                }else {
                                    Log.e("BookItem","保存失败"+  e.getErrorCode());
                                }
                            }
                        });
                    }
                }else {
                    Log.i("BookItem", "BookPayment设置失败"+ e.getErrorCode());
                }
            }
        });


        BmobQuery<BookItemTitle> q3 = new BmobQuery<>();
        q3.getObject(bookItemTitleObjectId, new QueryListener<BookItemTitle>() {
            @Override
            public void done(BookItemTitle bookItemTitle, BmobException e) {
                if (e == null) {
                    bookItem.setItemTitle(bookItemTitle);
                    Log.i("BookItem", "BookItemTitle设置成功");
                    if (isBookItemSet(bookItem)) {
                        bookItem.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    Log.i("BookItem", "BookItem保存成功");
                                    saveListenerUI.onSucessUI();
                                }else {
                                    Log.e("BookItem","保存失败" +  e.getErrorCode());
                                }
                            }
                        });
                    }
                }else {
                    Log.i("BookItem", "BookItemTitle设置失败"+ e.getErrorCode());
                }
            }
        });
    }

    public void saveBookPayment(BookPayment bookPayment, final SaveListenerUI saveListenerUI) {
        bookPayment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    //添加成功
                    bookPaymentObjecId = s;
                    Log.i("bookPaymentObjecId", bookPaymentObjecId);
                    Log.i("BookItem", "BookPayment保存成功");
                    saveListenerUI.onSucessUI();
                } else {
                    saveListenerUI.onFailUI();
                }
            }
        });
    }

    public void saveBookItemInfo(BookItemInfo bookItemInfo, final SaveListenerUI saveListenerUI) {
        bookItemInfo.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    //添加成功
                    bookItemInfoObjectId = s;
                    Log.i("bookItemInfoObjectId", bookItemInfoObjectId);
                    Log.i("BookItem", "BookItemInfo保存成功");
                    saveListenerUI.onSucessUI();
                }
            }
        });
    }

    @Override
    public void queryBookItemTitle(final BookItemTitle bookItemTitle, final FindListenerUI<BookItemTitle> findListenerUI) {
        BmobQuery<BookItemTitle> query = new BmobQuery<>();

        query.addWhereEqualTo("year", bookItemTitle.getYear());
        query.addWhereEqualTo("month", bookItemTitle.getMonth());
        query.addWhereEqualTo("day", bookItemTitle.getDay());
        query.addWhereEqualTo("week", bookItemTitle.getWeek());
        query.addWhereEqualTo("book", bookItemTitle.getBook());

        query.findObjects(new FindListener<BookItemTitle>() {
            @Override
            public void done(List<BookItemTitle> list, BmobException e) {
                if (e == null) {
                    //查询成功
                    findListenerUI.onSucess(list);

                }
            }
        });
    }

    public void queryBookItemTitle(int year, int month, Book book, final FindListenerUI findListenerUI) {
        BmobQuery<BookItemTitle> query = new BmobQuery<>();

        query.addWhereEqualTo("year", year);
        query.addWhereEqualTo("month", month);
        query.addWhereEqualTo("book", book);


        query.findObjects(new FindListener<BookItemTitle>() {
            @Override
            public void done(List<BookItemTitle> list, BmobException e) {
                if (e == null) {
                    //查询成功
                    findListenerUI.onSucess(list);

                }
            }
        });
    }

    private void saveBookItemTitle(BookItemTitle bookItemTitle, final SaveListenerUI saveListenerUI) {
        bookItemTitle.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i("BookItem","BookItemTitle保存成功");
                    //添加成功
                    bookItemTitleObjectId = s;
                    Log.i("bookItemTitleObjectId", bookItemTitleObjectId);

                    saveListenerUI.onSucessUI();
                } else {
                    saveListenerUI.onFailUI();
                }
            }
        });
    }

    private boolean isAllSave(){
        if (bookPaymentObjecId != null && bookItemInfoObjectId != null && bookItemTitleObjectId != null) {
            Log.i("BookItem", "BookItem三个子项全都保存成功");
            return true;
        }
        return false;
    }

    private boolean isBookItemSet(BookItem bookItem){
        if (bookItem.getItemInfo() != null && bookItem.getPayment() != null && bookItem.getItemTitle() != null) {
            Log.i("BookItem", "BookItem三个子项全部设置成功");
            return true;
        }
        return false;
    }

    @Override
    public void queryBookMonthFinancial(int year, int month, Book book, final FindListenerUI findListenerUI) {
        BmobQuery<BookMonthFinancial> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("year", year);
        bmobQuery.addWhereEqualTo("month", month);
        bmobQuery.addWhereEqualTo("book", book);

        bmobQuery.findObjects(new FindListener<BookMonthFinancial>() {
            @Override
            public void done(List<BookMonthFinancial> list, BmobException e) {
                if (e == null ) {
                    findListenerUI.onSucess(list);
                }
            }
        });
    }
}
