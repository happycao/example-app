package com.cl.testapp.dili.constant;

/**
 * author : Bafs
 * e-mail : bafs.jy@live.com
 * time   : 2018/09/04
 * desc   : 嘀哩嘀哩appApi
 * version: 1.0
 */
public class DApi {

    /* api地址 */
    private static String baseUrl = "http://go.dilidili.club";

    /* 番剧时间表 */
    public static String home = baseUrl + "/home";

    /* 分类 */
    public static String categories = baseUrl + "/categories";

    /* 分类番剧，category：%E5%A5%87%E5%B9%BB，size：15，page：1 */
    public static String bangumiByCategory = baseUrl + "/bangumi_by_category";

    /* 季度，arctype_id：4781 */
    public static String seasons = baseUrl + "/bangumi/seasons";

    /* 番剧剧集，arctype_id：4718，size：16，page：1 */
    public static String episodes = baseUrl + "/bangumi/episodes";

    /* 播放列表，archive_id：78816 */
    public static String playerUrl = baseUrl + "/playurls";

    /* 播放解析 */
    public static String httpPlayer = "http://play.jiningwanjun.com/ckm3u8.html?v=";
    public static String m3u8Player = "http://play.jiningwanjun.com/dpm3u8.html?v=";

}
