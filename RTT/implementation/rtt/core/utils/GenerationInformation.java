package rtt.core.utils;

import java.util.ArrayList;
import java.util.List;

import rtt.core.archive.logging.Detail;

public class GenerationInformation {

	public static class GenerationResult {
		public boolean noError = false;
		public boolean hasReplaced = false;
		public Throwable exception = null;

		public String suiteName;
		public String caseName;

		public GenerationResult(String suiteName, String caseName) {
			this.suiteName = suiteName;
			this.caseName = caseName;
		}

		public String getMessage() {

			String message = "Reference data for test [" + suiteName + "/"
					+ caseName + "] were ";

			if (noError) {
				if (hasReplaced) {
					message += "generated.";
				} else {
					message += "not changed.";
				}
				
			} else {
				message = "Error during generation of reference data";
				if (exception != null) {
					message += ": " + exception;
				}
			}

			return message;
		}

		public Integer getPriority() {
			Integer priority = 0;
			
			if (noError) {
				if (hasReplaced) {
					priority = 1;
				}
			} else {
				priority = 2;				
			}
			
			return priority;
		}
	}

	private boolean hasError = false;
	private List<GenerationResult> infoList;

	public GenerationInformation() {
		infoList = new ArrayList<GenerationInformation.GenerationResult>();
	}

	public boolean hasErrors() {
		return hasError;
	}

	public void addGenerationResult(GenerationResult info) {
		if (info.exception != null) {
			hasError = true;
		}

		infoList.add(info);
	}

	public void addResults(GenerationInformation results) {
		infoList.addAll(results.getInformations());
		if (results.hasError) {
			hasError = true;
		}
	}

	public List<GenerationResult> getInformations() {
		return infoList;
	}

	public List<Detail> makeDetails() {
		
		List<Detail> details = new ArrayList<Detail>();
		
		for (GenerationResult info : infoList) {
			Detail detail = new Detail();
			detail.setSuffix(info.suiteName + "/" + info.caseName);			
			detail.setPriority(info.getPriority());
			detail.setMsg(info.getMessage());

			details.add(detail);
		}
		
		return details;
	}

}
