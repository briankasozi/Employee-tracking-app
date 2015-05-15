package com.bbh.ets.reports;


import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bursys.baaja.basis.constants.MiscConstants;
import com.bursys.baaja.basis.utils.BaajaConfig;

/**
 *  This utitlity class provides many simple utility functions
 * to suppor the use of Jasper reports functionality
 */
public class JasperReportHelper {
	protected static Log log = LogFactory.getLog(JasperReportHelper.class);

	public static final String TASK_PRINT = "print";

	public static final String TASK_PDF = "pdf";

	public static final String TASK_XML = "xml";

	public static final String TASK_XML_EMBED = "xmlEmbed";

	public static final String TASK_HTML = "html";

	public static final String TASK_CSV = "csv";

	public static final String TASK_XLS = "xls";
	
	public static final String TASK_VIEW = "view";

	public static final String TASK_EXPORT = "export";
	
	

	/**
	 * This method allows to fill large reports by serializing the parts of the
	 * report to disk so that memory size does not become an issue. This should
	 * not be used for small reports, only for large reports spanning across
	 * hundreds of pages.
	 * 
	 * @param fileName -
	 *            jasper file name to be used
	 * @param con -
	 *            pass in a valid sql connection after obtaining from the data
	 *            source
	 * @param virtualizer -
	 *            pass in a file virtualizer and remember to cleanup after you
	 *            are done
	 * @return JasperPrint is returned that can be output to whatever format
	 *         desired easily using the standard methods
	 * @throws JRException
	 */
	public static JasperPrint fillLargeReport(String fileName, Connection con,
			JRFileVirtualizer virtualizer) throws JRException {
		long start = System.currentTimeMillis();
		// Preparing parameters
		Map parameters = new HashMap();
		parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
		JasperPrint jasperPrint = JasperFillManager.fillReport(fileName,
				parameters, con);
		virtualizer.setReadOnly(true);

		log.debug("Filling time for report file " + fileName + " is: "
				+ (System.currentTimeMillis() - start));
		return jasperPrint;
	}

	public static JasperPrint fillReportWithArrayBean(String fileName,
			Collection data, HashMap parameters) throws JRException {
		long start = System.currentTimeMillis();
		// Preparing parameters

		JasperPrint jasperPrint = JasperFillManager.fillReport(fileName,
				parameters, new JRBeanArrayDataSource(data.toArray()));

		log.debug("Filling time for report file " + fileName + " is: "
				+ (System.currentTimeMillis() - start));
		return jasperPrint;
	}

	/**
	 * This method can be used to persist report files to a disk in different
	 * formats. The file format can be specified using one of the constants
	 * specified in JasperReportHelper
	 * 
	 * @param taskName
	 *            Format for the output, e.g., csv, excel, pdf, etc.
	 * @param jasperPrint
	 *            input the JasperPrint object after filling the report.
	 * @param outFileName
	 *            output filename where it needs to be persisted.
	 * @throws JRException
	 */
	public static void saveReportToFile(String taskName,
			JasperPrint jasperPrint, String outFileName) throws JRException {

		if (TASK_PDF.equals(taskName)) {
			exportPDF(outFileName, jasperPrint);
		} else if (TASK_XML.equals(taskName)) {
			exportXML(outFileName, jasperPrint, false);
		} else if (TASK_XML_EMBED.equals(taskName)) {
			exportXML(outFileName, jasperPrint, true);
		} else if (TASK_HTML.equals(taskName)) {
			exportHTML(outFileName, jasperPrint);
		} else if (TASK_CSV.equals(taskName)) {
			exportCSV(outFileName + ".csv", jasperPrint);
		} else if (TASK_EXPORT.equals(taskName)) {
			exportPDF(outFileName + ".pdf", jasperPrint);
			exportXML(outFileName + ".jrpxml", jasperPrint, false);
			exportHTML(outFileName + ".html", jasperPrint);
			exportCSV(outFileName + ".csv", jasperPrint);
		}
	}

	public static byte[] export(String taskName, JasperPrint jasperPrint)
			throws JRException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		if (TASK_XML.equals(taskName)) {
			baos = exportToXml(jasperPrint, false);
		} else if (TASK_XML_EMBED.equals(taskName)) {
			baos = exportToXml(jasperPrint, true);
		} else if (TASK_HTML.equals(taskName)) {
			baos = exportToHTML(jasperPrint);
		} else if (TASK_CSV.equals(taskName)) {
			baos = exportToCsv(jasperPrint);
		} else if (TASK_XLS.equals(taskName)) {
			exportToXLS(jasperPrint);
		} else {
			// generate the pdf by default if no valid taskname provided.
			baos = exportToPDF(jasperPrint);
		}
		return baos.toByteArray();
	}

	/**
	 * This methods takes in a jasperprint object and converts into the byte
	 * array forr pdf output
	 * 
	 * @param jasperPrint
	 * @return
	 * @throws JRException
	 */
	public static ByteArrayOutputStream exportToPDF(JasperPrint jasperPrint)
			throws JRException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JRExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		exporter.exportReport();
		return baos;
	}

	/**
	 * This methods takes in a jasperprint object and converts into the byte
	 * array for excel output
	 * 
	 * @param jasperPrint
	 * @return
	 * @throws JRException
	 */
	public static ByteArrayOutputStream exportToXLS(JasperPrint jasperPrint)
			throws JRException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		
		JRExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);		
		exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE); 
		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		
		exporter.exportReport();
		return baos;
	}

	/**
	 * This methods takes in a jasperprint object and converts into the byte
	 * array forr HTML output
	 * 
	 * @param jasperPrint
	 * @return
	 * @throws JRException
	 */
	public static ByteArrayOutputStream exportToHTML(JasperPrint jasperPrint)
			throws JRException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JRExporter exporter = new JRHtmlExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		exporter.setParameter(JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.TRUE);
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.FALSE);
		exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
		
		exporter.exportReport();
		return baos;
	}

	public static ByteArrayOutputStream exportToCsv(JasperPrint jasperPrint)
			throws JRException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JRExporter exporter = new JRCsvExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		exporter.exportReport();
		return baos;
	}

	public static ByteArrayOutputStream exportToXml(JasperPrint jasperPrint,
			boolean embedded) throws JRException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JRExporter exporter = new JRXmlExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		exporter.exportReport();
		return baos;
	}

	private static void exportCSV(String outFileName, JasperPrint jasperPrint)
			throws JRException {
		long start = System.currentTimeMillis();
		JRCsvExporter exporter = new JRCsvExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
		exporter.exportReport();
		log.debug("CSV creation time : "+ (System.currentTimeMillis() - start));
	}

	

	private static void exportHTML(String outFileName, JasperPrint jasperPrint)
			throws JRException {
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToHtmlFile(jasperPrint, outFileName);
		log.debug("HTML creation time : "
				+ (System.currentTimeMillis() - start));
	}

	private static void exportXML(String outFileName, JasperPrint jasperPrint,
			boolean embedded) throws JRException {
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToXmlFile(jasperPrint, outFileName,
				embedded);
		log
				.debug("XML creation time : "
						+ (System.currentTimeMillis() - start));
	}

	private static void exportPDF(String outFileName, JasperPrint jasperPrint)
			throws JRException {
		long start = System.currentTimeMillis();
		JasperExportManager.exportReportToPdfFile(jasperPrint, outFileName);
		log
				.debug("PDF creation time : "
						+ (System.currentTimeMillis() - start));
	}

	public static JRFileVirtualizer getVirtualSpace() {
		BaajaConfig baajaCfg = BaajaConfig.getInstance();
		JRFileVirtualizer virtualizer = new JRFileVirtualizer(baajaCfg
				.getIntegerProperty(MiscConstants.JASPER_VIRTUAL_MAX_SIZE),
				baajaCfg.getProperty(MiscConstants.JASPER_VIRTUAL_DIR));
		return virtualizer;
	}

	public static JRFileVirtualizer getVirtualSpace(String tmpPath) {
		BaajaConfig baajaCfg = BaajaConfig.getInstance();
		JRFileVirtualizer virtualizer = new JRFileVirtualizer(
				Integer
						.getInteger(
								baajaCfg
										.getAppProperty(MiscConstants.JASPER_VIRTUAL_MAX_SIZE))
						.intValue(), tmpPath);
		return virtualizer;
	}

	public static void cleanVirtualSpace(JRFileVirtualizer virtualizer) {
		virtualizer.cleanup();
	}
	/**
	 * @see : This method takes a arrayList containing JasperPrint objects and concat them into single report
	 * as specified format(pdf , excel or csv) on the basis of provided outPutType.
	 * @param jPrintList : containing JasperPrint objects.
	 * @param outPutType - pdf = 1 , excel =2 or csv = 3
	 * @return byte []
	 * @throws JRException 
	 */
	
	public static byte [] concatJasperPrints(ArrayList<JasperPrint>jPrintList,int outPutType) throws JRException{
		
		JRExporter exporter = null;
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(); 
	
		if(outPutType == 1)
			exporter = new JRPdfExporter();
		else if(outPutType == 3)
	        exporter = new JRCsvExporter();
		else if(outPutType == 2)
			exporter = new JRXlsExporter();
		else exporter = new JRPdfExporter();
		// for PDF ....and CSV out put..
		exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jPrintList); 
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteStream); 
		// for excel format....
		if(outPutType == 2){
			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE); 
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);	
		}
		exporter.exportReport(); 
		byte[] results = byteStream.toByteArray();
		
		return results;
	}

	
	
	
	
	
	
	
	
	
}
