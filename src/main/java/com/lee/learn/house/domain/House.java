package com.lee.learn.house.domain;

public class House {
    private Long id;
    private String title;
    private String xiaoQu;
    private Integer price;
    private Integer junJia;
    private String jingJiRen;
    private String huXing;
    private String louCeng;
    private Double jianZhuMianJi;
    private String huXingJieGou;
    private Double shiNeiMianJi;
    private String jianZhuLeiXing;
    private String chaoXiang;
    private String jianZhuJieGou;
    private String zhuangXiu;
    private String tiHuBiLi;
    private String gongNuanFangShi;
    private String dianTi;
    private String chanQuan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getXiaoQu() {
        return xiaoQu;
    }

    public void setXiaoQu(String xiaoQu) {
        this.xiaoQu = xiaoQu;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getJunJia() {
        return junJia;
    }

    public void setJunJia(Integer junJia) {
        this.junJia = junJia;
    }

    public String getJingJiRen() {
        return jingJiRen;
    }

    public void setJingJiRen(String jingJiRen) {
        this.jingJiRen = jingJiRen;
    }

    public String getHuXing() {
        return huXing;
    }

    public void setHuXing(String huXing) {
        this.huXing = huXing;
    }

    public String getLouCeng() {
        return louCeng;
    }

    public void setLouCeng(String louCeng) {
        this.louCeng = louCeng;
    }

    public Double getJianZhuMianJi() {
        return jianZhuMianJi;
    }

    public void setJianZhuMianJi(Double jianZhuMianJi) {
        this.jianZhuMianJi = jianZhuMianJi;
    }

    public String getHuXingJieGou() {
        return huXingJieGou;
    }

    public void setHuXingJieGou(String huXingJieGou) {
        this.huXingJieGou = huXingJieGou;
    }

    public Double getShiNeiMianJi() {
        return shiNeiMianJi;
    }

    public void setShiNeiMianJi(Double shiNeiMianJi) {
        this.shiNeiMianJi = shiNeiMianJi;
    }

    public String getJianZhuLeiXing() {
        return jianZhuLeiXing;
    }

    public void setJianZhuLeiXing(String jianZhuLeiXing) {
        this.jianZhuLeiXing = jianZhuLeiXing;
    }

    public String getChaoXiang() {
        return chaoXiang;
    }

    public void setChaoXiang(String chaoXiang) {
        this.chaoXiang = chaoXiang;
    }

    public String getJianZhuJieGou() {
        return jianZhuJieGou;
    }

    public void setJianZhuJieGou(String jianZhuJieGou) {
        this.jianZhuJieGou = jianZhuJieGou;
    }

    public String getZhuangXiu() {
        return zhuangXiu;
    }

    public void setZhuangXiu(String zhuangXiu) {
        this.zhuangXiu = zhuangXiu;
    }

    public String getTiHuBiLi() {
        return tiHuBiLi;
    }

    public void setTiHuBiLi(String tiHuBiLi) {
        this.tiHuBiLi = tiHuBiLi;
    }

    public String getGongNuanFangShi() {
        return gongNuanFangShi;
    }

    public void setGongNuanFangShi(String gongNuanFangShi) {
        this.gongNuanFangShi = gongNuanFangShi;
    }

    public String getDianTi() {
        return dianTi;
    }

    public void setDianTi(String dianTi) {
        this.dianTi = dianTi;
    }

    public String getChanQuan() {
        return chanQuan;
    }

    public void setChanQuan(String chanQuan) {
        this.chanQuan = chanQuan;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("House{");
        sb.append("title='").append(title).append('\'');
        sb.append(", xiaoQu='").append(xiaoQu).append('\'');
        sb.append(", price=").append(price);
        sb.append(", junJia=").append(junJia);
        sb.append(", jingJiRen='").append(jingJiRen).append('\'');
        sb.append(", huXing='").append(huXing).append('\'');
        sb.append(", louCeng='").append(louCeng).append('\'');
        sb.append(", jianZhuMianJi=").append(jianZhuMianJi);
        sb.append(", huXingJieGou='").append(huXingJieGou).append('\'');
        sb.append(", shiNeiMianJi=").append(shiNeiMianJi);
        sb.append(", jianZhuLeiXing='").append(jianZhuLeiXing).append('\'');
        sb.append(", chaoXiang='").append(chaoXiang).append('\'');
        sb.append(", jianZhuJieGou='").append(jianZhuJieGou).append('\'');
        sb.append(", zhuangXiu='").append(zhuangXiu).append('\'');
        sb.append(", tiHuBiLi='").append(tiHuBiLi).append('\'');
        sb.append(", gongNuanFangShi='").append(gongNuanFangShi).append('\'');
        sb.append(", dianTi='").append(dianTi).append('\'');
        sb.append(", chanQuan='").append(chanQuan).append('\'');
        sb.append('}');
        return sb.toString();
    }
}