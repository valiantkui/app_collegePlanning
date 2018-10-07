package com.planning.college.tools;

/**
 * Created by KUIKUI on 2018-05-23.
 */

public class Globle {
    //public static String url="http://test.j2eeall.com";
//    public static String HOST_PORT="http://test.j2eeall.com";
    public static String HOST_PORT="http://39.105.76.3:8080";
    public static String SUBJECT_INFO_URL="/cpServer/subject/downLoadImageByS_no";
    public static String MASTER_DIRECTIONS_URL="/cpServer/master/findMasterDirections";
    public static String JOB_DIRECTIONS_URL="/cpServer/job/findJobDirections";


    public static String GET_RESOURCES = "/cpServer/resource/findResourceByType";

    public static String htmlHead="<!DOCTYPE html>\n" +
            "<html>\n" +
            "\t<head>\n" +
            "\t\t<meta charset=\"utf-8\" />\n" +
            "\t\t<title></title>\n" +
            "\t\t\n" +
            "\t\t<style>\n" +
            "\t\t\timg{\n" +
            "\t\t\t\twidth: 100%;\n" +
            "\t\t\t}\n" +
            "\t\t\t\n" +
            "\t\t\t\n" +
            "\t\t</style>\n" +
            "\t</head>\n" +
            "\t<body>\n";


    public static String htmltail="\n\t</body>\n" +
            "</html>";

}
