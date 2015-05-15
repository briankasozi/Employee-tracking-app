package com.bbh.ets.reports;

import java.util.HashMap;

import net.sf.jasperreports.engine.JasperPrint;

import com.bbh.ets.reports.JasperReportHelper;

/**
 * This class is used to transfer the byte data - transfer object
 * 
 */
public class ReportData {
    private byte[] byteData;
    private JasperPrint jasperPrint;
    //
    private String rptName;
    private String outputFormat;
    private String outputFileName;
    private HashMap params;
    private String dataSource;
    private String rptCompletePath;

    public ReportData() {
        // set the default output format to pdf
        outputFormat = JasperReportHelper.TASK_PDF;
        params = new HashMap();
        outputFileName = "report.pdf";
    }

    public byte[] getByteData() {
        return byteData;
    }

    public void setByteData(byte[] byteData) {
        this.byteData = byteData;
    }

    public String getRptName() {
        return rptName;
    }

    public void setRptName(String rptName) {
        this.rptName = rptName;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public HashMap getParams() {
        return params;
    }

    public void setParams(HashMap params) {
        this.params = params;
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getRptCompletePath() {
        return rptCompletePath;
    }

    public void setRptCompletePath(String rptCompletePath) {
        this.rptCompletePath = rptCompletePath;
    }
}
