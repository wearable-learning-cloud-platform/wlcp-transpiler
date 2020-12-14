package org.wlcp.wlcptranspiler.transpiler.steps;

import org.wlcp.wlcptranspiler.dto.GameDto.GlobalVariable;
import org.wlcp.wlcptranspiler.dto.GameDto.StartState;

public class GenerateGlobalVariablesStep implements ITranspilerStep {
	
	private StartState startState;
	
	public GenerateGlobalVariablesStep(StartState startState) {
		this.startState = startState;
	}

	@Override
	public String PerformStep() {
		StringBuilder stringBuilder = new StringBuilder();
		for(GlobalVariable globalVariable : startState.globalVariables) {
			if(globalVariable.getType().equals("NUMBER") || globalVariable.getType().equals("BOOLEAN")) 
				stringBuilder.append("   " + globalVariable.getName() + " : " + globalVariable.getDefaultValue() + ",\n");
			else {
				stringBuilder.append("   " + globalVariable.getName() + " : " + "\"" + globalVariable.getDefaultValue() + "\"" + ",\n");
			}
			
		}
		stringBuilder.append("\n");
		return stringBuilder.toString();
	}

}
