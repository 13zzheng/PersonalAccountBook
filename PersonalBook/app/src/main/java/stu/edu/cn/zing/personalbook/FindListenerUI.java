package stu.edu.cn.zing.personalbook;

import java.util.List;

/**
 * Created by Administrator on 2017/3/28.
 */

public interface FindListenerUI<T> {
    void onSucess(List<T> list);

    void onFail();
}
