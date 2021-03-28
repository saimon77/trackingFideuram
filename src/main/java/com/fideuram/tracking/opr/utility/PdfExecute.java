package com.fideuram.tracking.opr.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.util.Base64;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.qrcode.ByteArray;

public class PdfExecute {
	
	private final static String FILE="C:\\Users\\user\\Documents\\clienti\\fideuram\\test\\prova.pdf";
	
	private static Font bigFont  = new Font(Font.FontFamily.TIMES_ROMAN, 18,  Font.BOLD);
	 private static Font redFont  = new Font(Font.FontFamily.TIMES_ROMAN, 12,  Font.NORMAL, BaseColor.RED);
	 private static Font subFont  = new Font(Font.FontFamily.TIMES_ROMAN, 16,  Font.BOLD);
	 private static Font smallBold  = new Font(Font.FontFamily.TIMES_ROMAN, 12,  Font.BOLD);
	 
	public static byte[] getPdfTest() {
		byte[] pdf=null;
		try {
		 Document document = new Document(PageSize.A4);
		   PdfWriter.getInstance(document, new FileOutputStream(FILE));
		   document.open();
		   aggiungiContenuto(document);
		   document.close();
		   File file=new File(FILE);
		   pdf=Base64.encodeBase64(FileUtils.readFileToByteArray(file));
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
		return pdf;
	}
	
	private static void aggiungiContenuto(Document document) throws DocumentException {

		  // Il secondo parametro è il numero di capitolo
		  Chapter chapter = new Chapter(new Paragraph("CONTENUTO PDF", bigFont), 1);

		  // Aggiunta al documento
		  document.add(chapter);


		  // Aggiunta al documento
		  document.add(chapter);
		 }

}
