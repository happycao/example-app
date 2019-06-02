package com.cl.testapp.dili.entity;

import java.io.Serializable;

/**
 * author : happyc
 * e-mail : bafs.jy@live.com
 * time   : 2019/05/29
 * desc   : 番剧
 * version: 1.0
 */
public class DBangumi implements Serializable {
    /**
     * id : 4781
     * reid : 3302
     * topid : 3302
     * sortrank : 50
     * typename : 战姬绝唱第五季
     * typedir : /anime/zhanjijuechang5
     * description : 战姬绝唱的第五季
     * keywords : 战姬绝唱第五季,战姬绝唱第五季全集,战姬绝唱第五季完整版
     * diqu : 日本
     * niandai : 2019年7月
     * kandian : 四月番拖到7月现在5月staff还没公布可能还会跳票
     * suoluetudizhi : http://www.dilidili.name/uploads/allimg/190516/312_1040347931.jpg
     * lanmujianjie : http://www.dilidili.name/uploads/allimg/190516/312_1040347931.jpg
     * biaoqian : 偶像|奇幻|搞笑|机战|音乐
     * zhuangtai : 连载中
     * genxinshijian : 随缘更新
     * staff :
     * cast : 水树奈奈
     * bbs_tid : 678299
     * latest :
     * is_like : false
     * share_url : http://share.dilidili.club/bangumi.html?bangumi_id=4781
     */
    private int id;
    private int reid;
    private int topid;
    private int sortrank;
    private String typename;
    private String typedir;
    private String description;
    private String keywords;
    private String diqu;
    private String niandai;
    private String kandian;
    private String suoluetudizhi;
    private String lanmujianjie;
    private String biaoqian;
    private String zhuangtai;
    private String genxinshijian;
    private String staff;
    private String cast;
    private int bbs_tid;
    private String latest;
    private boolean is_like;
    private String share_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReid() {
        return reid;
    }

    public void setReid(int reid) {
        this.reid = reid;
    }

    public int getTopid() {
        return topid;
    }

    public void setTopid(int topid) {
        this.topid = topid;
    }

    public int getSortrank() {
        return sortrank;
    }

    public void setSortrank(int sortrank) {
        this.sortrank = sortrank;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getTypedir() {
        return typedir;
    }

    public void setTypedir(String typedir) {
        this.typedir = typedir;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDiqu() {
        return diqu;
    }

    public void setDiqu(String diqu) {
        this.diqu = diqu;
    }

    public String getNiandai() {
        return niandai;
    }

    public void setNiandai(String niandai) {
        this.niandai = niandai;
    }

    public String getKandian() {
        return kandian;
    }

    public void setKandian(String kandian) {
        this.kandian = kandian;
    }

    public String getSuoluetudizhi() {
        return suoluetudizhi;
    }

    public void setSuoluetudizhi(String suoluetudizhi) {
        this.suoluetudizhi = suoluetudizhi;
    }

    public String getLanmujianjie() {
        return lanmujianjie;
    }

    public void setLanmujianjie(String lanmujianjie) {
        this.lanmujianjie = lanmujianjie;
    }

    public String getBiaoqian() {
        return biaoqian;
    }

    public void setBiaoqian(String biaoqian) {
        this.biaoqian = biaoqian;
    }

    public String getZhuangtai() {
        return zhuangtai;
    }

    public void setZhuangtai(String zhuangtai) {
        this.zhuangtai = zhuangtai;
    }

    public String getGenxinshijian() {
        return genxinshijian;
    }

    public void setGenxinshijian(String genxinshijian) {
        this.genxinshijian = genxinshijian;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public int getBbs_tid() {
        return bbs_tid;
    }

    public void setBbs_tid(int bbs_tid) {
        this.bbs_tid = bbs_tid;
    }

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
    }

    public boolean isIs_like() {
        return is_like;
    }

    public void setIs_like(boolean is_like) {
        this.is_like = is_like;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }
}
