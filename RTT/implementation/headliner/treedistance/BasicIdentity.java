package headliner.treedistance;

public class BasicIdentity extends TreeEditOperation {

	@Override
	public double getCost(int aNodeID, int bNodeID, TreeDefinition aTree,
			TreeDefinition bTree) {
		
		return 0;
	}

}
