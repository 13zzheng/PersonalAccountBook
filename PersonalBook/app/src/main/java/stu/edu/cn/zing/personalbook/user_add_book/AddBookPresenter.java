package stu.edu.cn.zing.personalbook.user_add_book;

import android.app.PendingIntent;
import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import stu.edu.cn.zing.personalbook.FindListenerUI;
import stu.edu.cn.zing.personalbook.SaveListenerUI;
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.BookItem;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemInfo;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemTitle;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemType;
import stu.edu.cn.zing.personalbook.bmobclass.BookMonthFinancial;
import stu.edu.cn.zing.personalbook.bmobclass.BookPayment;
import stu.edu.cn.zing.personalbook.bmobclass.BookPaymentType;
import stu.edu.cn.zing.personalbook.bmobclass.User;
import stu.edu.cn.zing.personalbook.model.BookManager;
import stu.edu.cn.zing.personalbook.model.IBookManager;
import stu.edu.cn.zing.personalbook.model.IQuerier;
import stu.edu.cn.zing.personalbook.model.Querier;

/**
 * Created by Administrator on 2017/3/28.
 */

public class AddBookPresenter {

    private IAddBookView iAddBookView;
    private IBookManager iBookManager;

    private boolean flag1 = false;
    private boolean flag2 = false;

    private boolean updateFlag1 = false;
    private boolean updateFlag2 = false;
    private boolean updateFlag3 = false;

    public AddBookPresenter(IAddBookView iAddBookView) {
        this.iAddBookView = iAddBookView;
        iBookManager = new BookManager();
    }

    public void SaveBookItem(final BookItemTitle itemTitle, final BookPayment bookPayment, BookItemInfo itemInfo) {
        iBookManager.addBookItem(itemTitle, bookPayment, itemInfo, new SaveListenerUI() {
            @Override
            public void onSucessUI() {
                caculateFinancial(itemTitle, bookPayment, new SaveListenerUI() {
                    @Override
                    public void onSucessUI() {
                        Log.i("BookItem","BookItem保存成功，返回");
                        iAddBookView.saveBookItemSuccess();
                    }

                    @Override
                    public void onFailUI() {

                    }
                });
            }

            @Override
            public void onFailUI() {

            }
        });
    }

    public void updateBookItem(BookItem bookItem) {
        iBookManager.deleteBookItem(bookItem, new SaveListenerUI() {
            @Override
            public void onSucessUI() {
                iAddBookView.save();
            }

            @Override
            public void onFailUI() {

            }
        });
    }

    public void updateBookItem(final BookItem bookItem, BookItemTitle bookItemTitle, BookPayment bookPayment) {
        bookItem.getItemInfo().update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    updateFlag1 = true;
                    if (isAllUpdate()) {
                        iAddBookView.updateBookItemSuccess();
                    }
                }
            }
        });

        final BookItemTitle title = bookItem.getItemTitle();
        iBookManager.queryBookItemTitle(title, new FindListenerUI<BookItemTitle>() {
            @Override
            public void onSucess(List<BookItemTitle> list) {
                if (list.size() > 0 ) {
                    bookItem.setItemTitle(list.get(0));
                    bookItem.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                updateFlag2 = true;
                                if (isAllUpdate()) {
                                    iAddBookView.updateBookItemSuccess();
                                }
                            }
                        }
                    });
                } else {
                    title.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                bookItem.setItemTitle(title);
                                bookItem.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            updateFlag2 = true;
                                            if (isAllUpdate()) {
                                                iAddBookView.updateBookItemSuccess();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
            @Override
            public void onFail() {

            }
        });


        bookItem.getPayment().update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    updateFlag3 = true;
                    if (isAllUpdate()) {
                        iAddBookView.updateBookItemSuccess();
                    }
                }
            }
        });

        bookPayment.setPayType(-bookPayment.getPayType());

    }

    private boolean isAllUpdate() {
        return updateFlag1 && updateFlag2 && updateFlag3;
    }


    //账单保存完毕后计算收入支出等等财政情况
    private void caculateFinancial(BookItemTitle itemTitle, BookPayment bookPayment, final SaveListenerUI saveListenerUI) {

        flag1 = false;
        flag2 = false;
        caculateTitleTotalPay(itemTitle, bookPayment, saveListenerUI);
        caculateMonthFinancial(itemTitle.getYear(), itemTitle.getMonth(), bookPayment, saveListenerUI);

    }

    private void caculateTitleTotalPay(BookItemTitle itemTitle, final BookPayment bookPayment, final SaveListenerUI saveListenerUI) {
        Log.i("BookItem","开始计算Title");
        iBookManager.queryBookItemTitle(itemTitle, new FindListenerUI<BookItemTitle>() {
            @Override
            public void onSucess(List<BookItemTitle> list) {
                if (list.size() > 0) {
                    //已经存在，修改更新
                    BookItemTitle title = list.get(0);

                    if (bookPayment.getPayType() == BookPaymentType.TYPE_POSITIVE) {
                        //收入：大于0
                        title.setTotalPay(title.getTotalPay() + bookPayment.getPay());
                    } else if (bookPayment.getPayType() == BookPaymentType.TYPE_NEGATIVE) {
                        //支出：小于0
                        title.setTotalPay(title.getTotalPay() - bookPayment.getPay());
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
                            } else {
                                Log.i("BookItem","计算Title且更新出错" + e.getErrorCode());
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

    private void caculateMonthFinancial(final int year, final int month, final BookPayment bookPayment, final SaveListenerUI saveListenerUI) {
        Log.i("BookItem","开始计算BookMonthFinancial");
        iBookManager.queryBookMonthFinancial(year, month, Book.getCurrentBook(), new FindListenerUI<BookMonthFinancial>() {
            @Override
            public void onSucess(List<BookMonthFinancial> list) {
                if (list.size() > 0) {
                    //存在
                    BookMonthFinancial bookMonthFinancial = list.get(0);

                    if (bookPayment.getPayType() == BookPaymentType.TYPE_POSITIVE) {
                        //收入：大于0
                        bookMonthFinancial.setMonthInput(bookMonthFinancial.getMonthInput() + bookPayment.getPay());
                    } else if (bookPayment.getPayType() == BookPaymentType.TYPE_NEGATIVE) {
                        //支出：小于0
                        bookMonthFinancial.setMonthOutput(bookMonthFinancial.getMonthOutput() + bookPayment.getPay());
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
                            } else {
                                Log.i("BookItem","计算BookMonthFinancial且更新出错" + e.getErrorCode());
                            }
                        }
                    });
                }else {
                    //不存在，添加新的
                    BookMonthFinancial bookMonthFinancial = new BookMonthFinancial();
                    bookMonthFinancial.setYear(year);
                    bookMonthFinancial.setMonth(month);
                    bookMonthFinancial.setBook(Book.getCurrentBook());

                    //将该笔账单计算进去
                    if (bookPayment.getPayType() == BookPaymentType.TYPE_POSITIVE) {
                        //收入：大于0
                        bookMonthFinancial.setMonthInput(bookMonthFinancial.getMonthInput() + bookPayment.getPay());
                    } else if (bookPayment.getPayType() == BookPaymentType.TYPE_NEGATIVE) {
                        //支出：小于0
                        bookMonthFinancial.setMonthOutput(bookMonthFinancial.getMonthOutput() + bookPayment.getPay());
                    }

                    bookMonthFinancial.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                flag2 = true;
                                Log.i("BookItem","计算BookMonthFinancial且保存完成");
                                if (flag1 && flag2) {
                                    saveListenerUI.onSucessUI();
                                }
                            }
                        }
                    });

                }
            }

            @Override
            public void onFail() {

            }
        });
    }


}
