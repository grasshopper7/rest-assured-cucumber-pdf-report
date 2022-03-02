package tech.grasshopper.pdf;

import java.io.File;

import tech.grasshopper.pdf.annotation.file.FileAnnotationProcessor;
import tech.grasshopper.pdf.annotation.file.FileAnnotationStore;
import tech.grasshopper.pdf.data.ReportData;
import tech.grasshopper.pdf.section.details.RestAssuredDetailedSection;

public class RestAssuredPdfCucumberReport extends PDFCucumberReport {

	protected FileAnnotationStore fileAnnotations;

	public RestAssuredPdfCucumberReport(ReportData reportData, File reportFile) {
		super(reportData, reportFile);
		this.fileAnnotations = new FileAnnotationStore();

		// No zoomed media display required
		reportConfig.setDisplayExpanded(false);
	}

	protected void createDetailedSection() {
		if (reportConfig.isDisplayDetailed())
			RestAssuredDetailedSection.builder().displayData(reportData.getFeatureData()).reportConfig(reportConfig)
					.document(document).fileAnnotations(fileAnnotations).build().createSection();
	}

	protected void processAnnotations() {
		super.processAnnotations();
		FileAnnotationProcessor.builder().document(document).reportFile(reportFile).annotations(fileAnnotations).build()
				.processAnnotations();
	}
}
