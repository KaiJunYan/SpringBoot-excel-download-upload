package com.test.AnnotionsWay;

import java.io.Serializable;
import java.util.Date;

public class EmployeeParam implements Serializable {

    /**
     * 字段描述: [字段功能描述]
     */
    private static final long serialVersionUID = 1L;

    @ExcelColumn(columnName = "企业名称")
    private String qymc;

    @ExcelColumn(columnName = "部门名称")
    private String bm;

    @ExcelColumn(columnName = "职务")
    private String zw;

    @ExcelColumn(columnName = "姓名")
    private String xm;

    @ExcelColumn(columnName = "手机号码")
    private String sjhm;

    @ExcelColumn(columnName = "电子邮箱")
    private String yx;

    @ExcelColumn(columnName = "出生日期")
    private Date csrq;

    @ExcelColumn(columnName = "性别")
    private String xb;

    @ExcelColumn(columnName = "身份证号码")
    private String sfzhm;

    @ExcelColumn(columnName = "民族")
    private String mz;

    @ExcelColumn(columnName = "籍贯")
    private String jg;

    @ExcelColumn(columnName = "户籍地址")
    private String hjdz;

    @ExcelColumn(columnName = "家庭住址")
    private String jtzz;

    @ExcelColumn(columnName = "婚育状况")
    private String hyzk;

    @ExcelColumn(columnName = "学历")
    private String xl;

    @ExcelColumn(columnName = "毕业院校")
    private String byxx;

    @ExcelColumn(columnName = "所学专业")
    private String sxzz;

    @ExcelColumn(columnName = "毕业时间")
    private Date bysj;

    @ExcelColumn(columnName = "启用状态")
    private String zt;

    public String getQymc() {
        return qymc;
    }

    public void setQymc(String qymc) {
        this.qymc = qymc;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getZw() {
        return zw;
    }

    public void setZw(String zw) {
        this.zw = zw;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getSjhm() {
        return sjhm;
    }

    public void setSjhm(String sjhm) {
        this.sjhm = sjhm;
    }

    public String getYx() {
        return yx;
    }

    public void setYx(String yx) {
        this.yx = yx;
    }

    public Date getCsrq() {
        return csrq;
    }

    public void setCsrq(Date csrq) {
        this.csrq = csrq;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getSfzhm() {
        return sfzhm;
    }

    public void setSfzhm(String sfzhm) {
        this.sfzhm = sfzhm;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
    }

    public String getJg() {
        return jg;
    }

    public void setJg(String jg) {
        this.jg = jg;
    }

    public String getHjdz() {
        return hjdz;
    }

    public void setHjdz(String hjdz) {
        this.hjdz = hjdz;
    }

    public String getJtzz() {
        return jtzz;
    }

    public void setJtzz(String jtzz) {
        this.jtzz = jtzz;
    }

    public String getHyzk() {
        return hyzk;
    }

    public void setHyzk(String hyzk) {
        this.hyzk = hyzk;
    }

    public String getXl() {
        return xl;
    }

    public void setXl(String xl) {
        this.xl = xl;
    }

    public String getByxx() {
        return byxx;
    }

    public void setByxx(String byxx) {
        this.byxx = byxx;
    }

    public String getSxzz() {
        return sxzz;
    }

    public void setSxzz(String sxzz) {
        this.sxzz = sxzz;
    }

    public Date getBysj() {
        return bysj;
    }

    public void setBysj(Date bysj) {
        this.bysj = bysj;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

}
