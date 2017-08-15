package cn.com.unilever.www.unileverapp.config;

import java.util.ArrayList;

public class MyConfig {
    //问题
    public static ArrayList<String> problem = new ArrayList<>();
    //选择问题序号(有多余)
    public static ArrayList<Integer> sourceStrArray;
    //被提问人员id
    public static String id = null;
    //被提问人员name
    public static String name = null;
    //优
    public static int ExcellentNumber = 0;
    //良
    public static int FineNumber = 0;
    //差
    public static int DadNumber = 0;
    //异常类型
    public static String type = "TAG";
    //url
    public static String url = "http://192.168.10.21:8080/HiperMES";
    //登录url
    public static String loginurl = "http://192.168.10.21:8080/HiperMES/login.sp?method=appLogin&loginName=admin&password=admin";
    //SMAT 区域
    public static String area;
    //smat异常数量
    public static int num = 0;
    public static int on = 0;
    public static int ok = 0;
}