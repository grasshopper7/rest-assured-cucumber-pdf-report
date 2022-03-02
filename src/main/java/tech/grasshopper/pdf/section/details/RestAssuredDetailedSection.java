package tech.grasshopper.pdf.section.details;

import java.util.List;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.annotation.file.FileAnnotationStore;
import tech.grasshopper.pdf.pojo.cucumber.AdditionalDataKey;
import tech.grasshopper.pdf.pojo.cucumber.Scenario;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class RestAssuredDetailedSection extends DetailedSection {

	protected FileAnnotationStore fileAnnotations;

	@SuppressWarnings("unchecked")
	@Override
	protected void scenarioAdditionalInfoDisplay(Scenario scenario) {
		RestAssuredDisplay featureDisplay = RestAssuredDisplay.builder().ylocation(ylocation).document(document)
				.reportConfig(reportConfig).fileAnnotations(fileAnnotations).data((List<Map<String, String>>) scenario
						.getAdditionalData().get(AdditionalDataKey.REST_ASSURED_DATA_KEY))
				.build();
		featureDisplay.display();

		ylocation = featureDisplay.getFinalY() - GAP;
	}
}
