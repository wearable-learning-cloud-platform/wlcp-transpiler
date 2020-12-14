package org.wlcp.wlcptranspiler.transpiler.state;

import org.wlcp.wlcptranspiler.dto.GameDto.GlobalVariable;
import org.wlcp.wlcptranspiler.dto.GameDto.GlobalVariableOutput;
import org.wlcp.wlcptranspiler.dto.GameDto.GlobalVariableOutputModifier;
import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.StartState;
import org.wlcp.wlcptranspiler.dto.GameDto.State;

public class GlobalVariableStateType extends StateType implements IStateType {
	
	private StartState startState;
	
	public GlobalVariableStateType(StartState startState) {
		this.startState = startState;
	}
	
	@Override
	public String GenerateState(String scope, State state) {
		StringBuilder stringBuilder = new StringBuilder();
		if(state instanceof OutputState) {
			OutputState outputState = (OutputState) state;
			GlobalVariableOutput g = outputState.globalVariables.get(scope);
			if(g != null) {
				for(GlobalVariableOutputModifier variableModifier : g.variableModifiers) {
					String e = "";
					if(!variableModifier.variableName.equals("expression")) {
						e = new String(variableModifier.variableName + " " + variableModifier.operator + " " + variableModifier.value);
					} else {
						e = new String(variableModifier.value);
					}
					for(GlobalVariable globalVariable : startState.globalVariables) {
						e = e.replaceAll("\\b" + globalVariable.getName() + "\\b", "FSMGame." + globalVariable.getName());
					}
					if(outputState.getGlobalVariables().containsKey(scope)) {
						stringBuilder.append(StateType.GenerateStateConditional(scope));
						if(scope.equals("Game Wide")) {
							stringBuilder.append("      this.playerVM.setGlobalVariable(\"");
						} else {
							stringBuilder.append("         this.playerVM.setGlobalVariable(\"");
						}
						stringBuilder.append(e);
						stringBuilder.append("\");\n");
						stringBuilder.append(StateType.GenerateEndStateConditional(scope));
					}
				}
			}
		}
		return stringBuilder.toString();
	}
}
