package tech.grasshopper.pdf.annotation.file;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationFileAttachment;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class FileAnnotationProcessor {

	@NonNull
	private FileAnnotationStore annotations;

	private PDDocument document;

	private File reportFile;

	private static final Logger logger = Logger.getLogger(FileAnnotationProcessor.class.getName());

	public void processAnnotations() {
		annotations.getFileAnnotation().forEach(f -> {

			try {
				PDAnnotationFileAttachment fileAtt = new PDAnnotationFileAttachment();
				fileAtt.setRectangle(f.getRectangle());
				PDColor pdColor = new PDColor(new float[] { 0.5f, 0.5f, 0.5f }, PDDeviceRGB.INSTANCE);
				fileAtt.setColor(pdColor);

				PDEmbeddedFile ef = new PDEmbeddedFile(document,
						new FileInputStream(Paths.get(reportFile.getParent(), f.getLink()).toString()));
				ef.setSubtype("text/html; charset=UTF-8");
				ef.setModDate(new GregorianCalendar());
				ef.setCreationDate(new GregorianCalendar());

				PDComplexFileSpecification fs = new PDComplexFileSpecification();
				fs.setEmbeddedFile(ef);
				fs.setFile(f.getLink());

				fs.setFileUnicode(f.getLink());

				fileAtt.setFile(fs);

				f.getPage().getAnnotations().add(fileAtt);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Unable to create file annotation link", e);
			}
		});
	}
}
