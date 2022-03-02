package tech.grasshopper.pdf.section.details;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import tech.grasshopper.pdf.annotation.FileAnnotation;
import tech.grasshopper.pdf.annotation.file.FileAnnotationStore;
import tech.grasshopper.pdf.font.ReportFont;
import tech.grasshopper.pdf.structure.Display;
import tech.grasshopper.pdf.structure.PageCreator;
import tech.grasshopper.pdf.structure.TableCreator;
import tech.grasshopper.pdf.structure.cell.TextFileLinkCell;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class RestAssuredDisplay extends Display {

	@Getter
	private float finalY;

	private List<Map<String, String>> data;

	protected FileAnnotationStore fileAnnotations;

	private static final float GAP = 10f;

	@Override
	public void display() {
		data.forEach(d -> {

			TableBuilder tableBuilder = Table.builder().addColumnsOfWidth(80f, 50f, 250f, 80f, 300f).borderWidth(1f)
					.borderColor(Color.GRAY).horizontalAlignment(HorizontalAlignment.LEFT)
					.verticalAlignment(VerticalAlignment.MIDDLE)

					.addRow(Row.builder().fontSize(13)
							.add(TextCell.builder().font(ReportFont.BOLD_ITALIC_FONT).text(d.get("Method")).build())
							.add(TextCell.builder().font(ReportFont.BOLD_ITALIC_FONT).text(d.get("Status Code"))
									.build())
							.add(TextCell.builder().fontSize(10).text(d.get("Endpoint")).colSpan(3)
									.font(ReportFont.ITALIC_FONT).build())
							.build())

					.addRow(Row.builder().fontSize(12).font(ReportFont.ITALIC_FONT)
							.add(TextCell.builder().text("Request").build()).add(createDataFileDisplay("Request", d, 2))
							.add(TextCell.builder().text("Response").build())
							.add(createDataFileDisplay("Response", d, 1)).build());

			TableCreator tableCreator = TableCreator.builder().tableBuilder(tableBuilder).document(document)
					.startX(CONTENT_START_X).startY(ylocation).endY(DETAILED_CONTENT_END_Y).repeatRows(2)
					.pageSupplier(PageCreator.builder().document(document).build()
							.landscapePageWithHeaderAndNumberSupplier(DetailedSection.SECTION_TITLE))
					.build();
			tableCreator.displayTable();

			finalY = tableCreator.getFinalY();

			ylocation = finalY - GAP;
		});
	}

	private AbstractCell createDataFileDisplay(String reqres, Map<String, String> data, int colSpan) {
		String[] types = { "Body", "Headers", "Cookies" };

		List<FileAnnotation> annotations = new ArrayList<>();
		StringBuffer textSbr = new StringBuffer();

		for (String type : types) {
			if (data.containsKey(reqres + " " + type)) {
				annotations.add(FileAnnotation.builder().text(type).link(data.get(reqres + " " + type)).build());
				textSbr.append(type).append("  ");
			}
		}

		if (annotations.isEmpty())
			return TextCell.builder().text("").colSpan(colSpan).build();
		else {
			fileAnnotations.addFileAnnotations(annotations);
			return TextFileLinkCell.builder().text(textSbr.toString()).annotations(annotations).colSpan(colSpan)
					.build();
		}
	}
}
