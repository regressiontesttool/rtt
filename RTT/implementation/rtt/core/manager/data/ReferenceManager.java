package rtt.core.manager.data;

import java.util.Calendar;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.history.Version;
import rtt.core.archive.output.LexerOutput;
import rtt.core.archive.output.ParserOutput;
import rtt.core.archive.input.Input;
import rtt.core.loader.ArchiveLoader;
import rtt.core.testing.generation.DataGenerator;
import rtt.core.testing.generation.LexerExecuter;
import rtt.core.testing.generation.ParserExecuter;

public class ReferenceManager extends AbstractTestDataManager {

	private InputManager inputManager;
	
	public ReferenceManager(ArchiveLoader loader, String suiteName,
			String caseName, Configuration config) {
		super(loader, suiteName, caseName, config, "ref");
		inputManager = new InputManager(loader, suiteName, caseName);
		
		this.suiteName = suiteName;
		this.caseName = caseName;
	}

	@Override
	public GenerationInfo createData(LexerExecuter lexer, ParserExecuter parser) throws Exception {

		GenerationInfo info = new GenerationInfo();

		LexerOutput newLexOut = null;
		ParserOutput newParOut = null;

		newLexOut = DataGenerator.generateOutput(inputManager.loadData(), lexer);
		newParOut = DataGenerator.generateOutput(inputManager.loadData(), parser);

		info.somethingDone = true;

		boolean replace = false;

		if (lastNr == null) {
			// previous data are not available, write new one
			replace = true;
		} else {
			// previous data is available
			Reference ref = dataMap.get(lastNr);

			// replace if input version of old reference data is out-dated.
			if (ref.refVersion < inputManager.getCurrentNr()) {
				replace = true;
				info.lexerVersioned = true;
				info.parserVersioned = true;
			} else {
				// input version is up-to-date
				LexerOutput oldLexOut = ref.lexOut;
				ParserOutput oldParOut = ref.parOut;

				// replace, if new data differ from old data
				if (LexerOutputManager.dataEqual(oldLexOut, newLexOut) == false
						|| ParserOutputManager.dataEqual(oldParOut, newParOut) == false) {
					info.lexerVersioned = true;
					info.parserVersioned = true;
					replace = true;
				}
			}
		}

		if (replace) {
			Version newVersion = new Version();
			newVersion.setDate(Calendar.getInstance());

			info.lexerReplaced = true;
			info.parserReplaced = true;

			newVersion.setNr(data.getCurrent() + 1);
			newVersion.setReference(inputManager.getCurrentNr());

			data.getVersion().add(newVersion);
			data.setCurrent(newVersion.getNr());

			dataMap.put(newVersion.getNr(), new Reference(newLexOut, newParOut,
					true));
		}

		return info;
	}

	@Override
	public boolean isUpToDate() {
		if (getCurrentNr() == 0) {
			return false;
		}
		
		return inputManager.getCurrentNr() == dataMap.get(getCurrentNr()).refVersion;
	}

	public Input getInputFor(Integer version) {
		return inputManager.loadData(dataMap.get(version).refVersion);
	}
	
	public Input getInput() {
		return getInputFor(getCurrentNr());
	}
}
