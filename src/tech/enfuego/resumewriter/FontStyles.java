package tech.enfuego.resumewriter;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;

/**
 * In this class we define the font styles for different sections of the resume
 * @author abhishek
 *
 */
public class FontStyles {

	/**
	 * iText has a limited support for font Types
	 * We use the Calibri font type which is not available in iText. 
	 * <p>
	 * Thus we have create the font using the calibri.ttf file.
	 * .ttf files are available for many fonts.
	 * 
	 */
	public static BaseFont calibri = null;

	/**
	 * load the calibri.ttf file and create the font
	 */
	static {
		try 
		{
			String CALIBRI_FILE = FontStyles.class.getResource("/fonts/calibri.ttf").getPath();
			calibri = BaseFont.createFont(CALIBRI_FILE, "CP1251", BaseFont.EMBEDDED);
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Define various styles using the font type, size and colo
	 */
    public final static Font firstLastName = new Font(calibri, 20, Font.BOLD, BaseColor.BLACK);
    public final static Font contactAddressInfo = new Font(calibri, 10, Font.NORMAL, BaseColor.BLACK);
    public final static Font sectionHeading = new Font(calibri, 12, Font.BOLD, BaseColor.BLACK);
    public final static Font normalText = new Font(calibri, 10, Font.NORMAL, BaseColor.BLACK);
    public final static Font nomadSkills = new Font(calibri, 10, Font.BOLD, BaseColor.BLACK);
    public final static Font employerName = new Font(calibri, 11, Font.NORMAL, BaseColor.BLUE);
    public final static Font workExpDate = new Font(calibri, 10, Font.BOLD, BaseColor.BLACK);
    public final static Font schoolName = employerName;
    public final static Font schoolDate = workExpDate;
    public final static Font jobTitle = new Font(calibri, 11, Font.BOLDITALIC, BaseColor.BLACK);
    public final static Font accTitle = new Font(calibri, 10, Font.BOLDITALIC, BaseColor.BLACK);
    public final static Font degreeName = new Font(calibri, 10, Font.BOLDITALIC, BaseColor.BLACK);



    
    
    
    
    
    
    
    

	
	


	//private static Font firstLastName = new Font(FontFactory.getFont("", size));


}
