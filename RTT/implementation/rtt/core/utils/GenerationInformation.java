package rtt.core.utils;

import java.util.ArrayList;
import java.util.List;

import rtt.core.archive.logging.Detail;

public class GenerationInformation {
	
	public enum GenerationType {
		TEST_DATA("Test data"), REFERENCE_DATA("Reference data");
		
		public String text;
		
		private GenerationType(String text) {
			this.text = text;
		}
	}

	public static class GenerationResult {
		public boolean noError = false;
		public boolean hasReplaced = false;
		public Throwable exception = null;

		public String suiteName;
		public String caseName;
		public GenerationType type;

		public GenerationResult(String suiteName, String caseName) {
			this.suiteName = suiteName;
			this.caseName = caseName;
		}

		public String getMessage() {
			String message = type.text + " for test [" + suiteName + "/"
					+ caseName + "]";

			if (noError) {
				if (hasReplaced) {
					message += " has been generated.";
				} else {
					message += " has not changed.";
				}
				
			} else {
				message = "Error during generation of " + type.text;
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

	private GenerationType type;
	private boolean hasError = false;
	private List<GenerationResult> resultList;

	public GenerationInformation(GenerationType type) {
		this.type = type;
		resultList = new ArrayList<GenerationInformation.GenerationResult>();
	}

	public boolean hasErrors() {
		return hasError;
	}

	public void addResult(GenerationResult info) {
		info.type = this.type;
		if (info.exception != null) {
			hasError = true;
		}

		resultList.add(info);
	}

	public void concat(GenerationInformation genInfo) {
		resultList.addAll(genInfo.getResults(true));
		if (genInfo.hasError) {
			hasError = true;
		}
	}

	public List<GenerationResult> getResults(boolean returnAll) {
		List<GenerationResult> returnList = new ArrayList<GenerationInformation.GenerationResult>();
		if (returnAll) {
			returnList.addAll(resultList);
		} else {
			for (GenerationResult result : resultList) {
				if (result.exception != null) {
					returnList.add(result);
				}
			}
		}
		
		return returnList;
	}
	
	public GenerationType getType() {
		return type;
	}

	public List<Detail> makeDetails(boolean returnAll) {
		
		List<Detail> details = new ArrayList<Detail>();
		
		for (GenerationResult info : getResults(returnAll)) {
			Detail detail = new Detail();
			detail.setSuffix(info.suiteName + "/" + info.caseName);			
			detail.setPriority(info.getPriority());
			detail.setMsg(info.getMessage());

			details.add(detail);		
		}
		
		return details;
	}

}
