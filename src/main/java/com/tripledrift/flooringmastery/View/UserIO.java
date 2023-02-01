package com.tripledrift.flooringmastery.View;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserIO
{
    void print(String msg);

    double readDouble(String prompt);

    double readDouble(String prompt, double min, double max);

    float readFloat(String prompt);

    float readFloat(String prompt, float min, float max);

    int readInt(String prompt);

    int readInt(String prompt, int min, int max);

    long readLong(String prompt);

    long readLong(String prompt, long min, long max);

    String readString(String prompt);

    LocalDate readDate(String prompt, boolean futurDate);

    String  readCustomerName(String prompt);

    BigDecimal readBigDecimal(String prompt);

    BigDecimal readArea(String prompt);

    String readYesOrNo(String msg);
}
