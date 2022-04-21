package io.github.derkrischan.pdftest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Test;

import io.github.derkrischan.pdftest.image.MetricRectangle;
import io.github.derkrischan.pdftest.page.Orientation;
import io.github.derkrischan.pdftest.page.PaperSize;

/**
 * Tests for PDF text verification.
 * 
 * @author krischan
 *
 */
public class PdfPageAssertionTest {

	@Test
	public void testAssertPfdPageSize() {
		PdfAssertions.assertThat(ClassLoader.getSystemResourceAsStream("pdf/dummy.pdf")).page(1).hasPaperSize(PaperSize.A4);
	}

	@Test
	public void testAssertPfdPaperSizeWithCustomTolerance() {
		PdfAssertions.assertThat(ClassLoader.getSystemResourceAsStream("pdf/dummy.pdf")).page(1).hasPaperSize(PaperSize.A4.getRectangle(), 0.5f, 0.5f);
	}
	
	@Test
	public void givenPortraitOrientedPdf_shouldMatchPortraitExpectation() {
		PdfAssertions.assertThat(ClassLoader.getSystemResourceAsStream("pdf/dummy.pdf")).page(1).hasPageOrientation(Orientation.PORTRAIT);
	}

	@Test(expected=AssertionError.class)
	public void givenLandscapeOrientedPdf_shouldFailPortraitExpectation() {
		PdfAssertions.assertThat(ClassLoader.getSystemResourceAsStream("pdf/dummy_landscape.pdf")).page(1).hasPageOrientation(Orientation.PORTRAIT);
	}
	
	@Test
	public void givenLandscapeOrientedPdf_shouldMatchLandscapeExpectation() {
		PdfAssertions.assertThat(ClassLoader.getSystemResourceAsStream("pdf/dummy_landscape.pdf")).page(1).hasPageOrientation(Orientation.LANDSCAPE);
	}

	@Test(expected=AssertionError.class)
	public void givenPortraitOrientedPdf_shouldFailLandscapeExpectation() {
		PdfAssertions.assertThat(ClassLoader.getSystemResourceAsStream("pdf/dummy.pdf")).page(1).hasPageOrientation(Orientation.LANDSCAPE);
	}
	
	@Test
    public void givenLandscapeOrientedPdf_shouldMatchLandscapeExpectationForEveryPage() {
        PdfAssertions.assertThat(ClassLoader.getSystemResourceAsStream("pdf/2_page_dummy.pdf")).eachPage(p -> p.hasPageOrientation(Orientation.PORTRAIT));
    }
	
	@Test(expected=AssertionError.class)
    public void givenTwoPagePdfWithDifferentTexts_shouldFailMatchTextInEveryPage() {
        PdfAssertions.assertThat(ClassLoader.getSystemResourceAsStream("pdf/2_page_dummy.pdf")).eachPage(p -> p.textInRegion(MetricRectangle.create(0, 0, 164, 310)).contains("Page 1"));
    }
	
	@Test
    public void givenTwoPagePdfWithSameTextPart_shouldMatchTextInEveryPage() {
        PdfAssertions.assertThat(ClassLoader.getSystemResourceAsStream("pdf/2_page_dummy.pdf")).eachPage(p -> p.textInRegion(MetricRectangle.create(0, 0, 164, 310)).contains("2_page_dummy.md"));
    }
	

	//TODO   https://www.tutorialkart.com/pdfbox/pdfbox-split-pdf-document-into-multiple-pdfs/
	@Test
    public void NewPrintPageSample() {
		
         InputStream iStream= ClassLoader.getSystemResourceAsStream("pdf/facture_sample.pdf");
		
		

        PDDocument documentFull =null;
		try {

			  // load pdf file
			  documentFull = PDDocument.load(iStream);
	
	 
	          // instantiating Splitter
	          Splitter splitter = new Splitter();
	           
	          // split the pages of a PDF document
	          List<PDDocument> Pages = splitter.split(documentFull);
	 
	       
	      	int pageNumber = 1;
	          for (PDDocument pageDocument : Pages) {
	        	  
	        		String textPageContent = new PDFTextStripper().getText(pageDocument);
	        		
	        		/*
	        		  String[] splittab = textPageContent.split("");
	        		  for (int i = 0; i < splittab.length; i++) {
	        			 String valeur = splittab[i];
	        			 System.out.println("zzz Valeur num "+ i +" ="+ valeur); 
					   }
					   */

	        		  System.out.println("***** Page Num "+ pageNumber + " : ");
				      System.out.println(textPageContent);
				      pageNumber = pageNumber +1;
				
			 }
			
	      
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
	
    
}
