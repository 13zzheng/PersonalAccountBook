package stu.edu.cn.zing.personalbook.finanicalreport;

/**
 * Created by Administrator on 2017/5/9.
 */

public interface IFinanicalReportActivity {

    void showData();
    void showNoData();

    void setTotalInput(float input);
    void setTotalOutput(float output);
    void setBudgetEnable(boolean b);
    void setBudgetPercentage(float percentage);
    void setOverageValue(float overageValue);
    void setOveragePercentage(float overagePercentage);
    void setMostOutputDay(int month, int day);
    void setMostOutputValue(float value);
    void setAverageDayInput(float input);
    void setAverageDayOutput(float output);
    void setMostRateType(String type);
    void setMostRateTypeNumber(int number);
    void setMostRateMember(String member);
    void setMostRateMemberNumber(int number);
    void setMostPaymentType(String type);
    void setMostPaymentTypeValue(float value);
    void setMostPaymentMember(String member);
    void setMostPaymentMemberValue(float value);

}
