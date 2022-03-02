package tech.grasshopper.pdf.annotation.file;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import tech.grasshopper.pdf.annotation.FileAnnotation;

@Data
public class FileAnnotationStore {

	private List<FileAnnotation> fileAnnotation = new ArrayList<>();

	public void addFileAnnotation(FileAnnotation annotation) {
		fileAnnotation.add(annotation);
	}

	public void addFileAnnotations(List<FileAnnotation> annotations) {
		fileAnnotation.addAll(annotations);
	}
}
