package com.mercadolibre.ipinfo.utils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IpUtilsTest {

    @Test
    public void isValidIp() {
        Assert.assertTrue(IpUtils.isValidIp("192.168.10.10"));
        Assert.assertTrue(IpUtils.isValidIp("1.1.1.1"));
        Assert.assertTrue(IpUtils.isValidIp("255.255.255.255"));
        Assert.assertTrue(IpUtils.isValidIp("0.0.0.0"));
    }

    @Test
    public void isNotValidIp_MoreThan4Seccions() {
        Assert.assertFalse(IpUtils.isValidIp("125.125.125.125.125"));
    }

    @Test
    public void isNotValidIp_NegativeValuePosition0() {
        Assert.assertFalse(IpUtils.isValidIp("-1.10.10.10"));
    }

    @Test
    public void isNotValidIp_NegativeValuePosition1() {
        Assert.assertFalse(IpUtils.isValidIp("10.-10.10.10"));
    }

    @Test
    public void isNotValidIp_NegativeValuePosition2() {
        Assert.assertFalse(IpUtils.isValidIp("10.10.-10.10"));
    }

    @Test
    public void isNotValidIp_NegativeValuePosition3() {
        Assert.assertFalse(IpUtils.isValidIp("10.10.10.-10"));
    }

    @Test
    public void isNotValidIp_GreaterThanMax_Position0() {
        Assert.assertFalse(IpUtils.isValidIp("256.10.10.10"));
    }

    @Test
    public void isNotValidIp_GreaterThanMax_Position1() {
        Assert.assertFalse(IpUtils.isValidIp("10.256.10.10"));
    }

    @Test
    public void isNotValidIp_GreaterThanMax_Position2() {
        Assert.assertFalse(IpUtils.isValidIp("10.10.256.10"));
    }

    @Test
    public void isNotValidIp_GreaterThanMax_Position3() {
        Assert.assertFalse(IpUtils.isValidIp("10.10.10.256"));
    }

    @Test
    public void isNotValidIp_EmptyValue() {
        Assert.assertFalse(IpUtils.isValidIp(""));
    }

    @Test
    public void isNotValidIp_NullValue() {
        Assert.assertFalse(IpUtils.isValidIp(null));
    }


}
