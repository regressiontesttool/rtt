package rtt.core.manager.data;

import java.util.Calendar;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.history.Version;
import rtt.core.archive.output.LexerOutput;
import rtt.core.archive.output.ParserOutput;
import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;
import rtt.core.loader.ArchiveLoader;
import rtt.core.testing.generation.DataGenerator;
import rtt.core.testing.generation.LexerExecuter;
import rtt.core.testing.generation.ParserExecuter;

public class TestManager extends AbstractTestDataManager {

	ReferenceManager refManager;

	public TestManager(ArchiveLoader loader, String suiteName, String caseName,
			Configuration config) {
		super(loader, suiteName, caseName, config, "test");

		refManager = new ReferenceManager(loader, suiteName, caseName, config);
	}

	@Override
	public GenerationInfo createData(LexerExecuter lexer, ParserExecuter parser) throws RTTException {

		// CHRISTIAN unused
		GenerationInfo genInfo = new GenerationInfo();

		if (refManager.getCurrentNr() > 0) {
			LexerOutput newLexOut = null;
			ParserOutput newParOut = null;

			try {
				newLexOut = DataGenerator.generateOutput(
						refManager.getInput(), lexer);
				newParOut = DataGenerator.generateOutput(
						refManager.getInput(), parser);
			} catch (Exception e) {
				throw new RTTException(Type.ARCHIVE,
						"Could not generate test data.");
			}

			boolean replace = false;

			if (getCurrentNr() == 0) {
				replace = true;
			} else {
				Reference ref = dataMap.get(lastNr);
				if (ref.refVersion < refManager.getCurrentNr()) {
					replace = true;
				} else {
					LexerOutput oldLexOut = ref.lexOut;
					ParserOutput oldParOut = ref.parOut;

					if (LexerOutputManager.dataEqual(oldLexOut, newLexOut) == false
							|| ParserOutputManager.dataEqual(oldParOut,
									newParOut) == false) {
						replace = true;
					}
				}
			}

			if (replace) {
				Version newVersion = new Version();
				newVersion.setDate(Calendar.getInstance());

				newVersion.setNr(data.getCurrent() + 1);
				newVersion.setReference(refManager.getCurrentNr());

				data.getVersion().add(newVersion);
				data.setCurrent(newVersion.getNr());

				dataMap.put(newVersion.getNr(), new Reference(newLexOut,
						newParOut, true));
			}
		}

		return genInfo;
	}

	@Override
	public boolean isUpToDate() {
		if (getCurrentNr() == 0) {
			return false;
		}

		if (!refManager.isUpToDate()) {
			return false;
		}

		return refManager.getCurrentNr() == dataMap.get(getCurrentNr()).refVersion;
	}
}
