package org.wlcp.wlcptranspiler.transpiler.transition;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.wlcp.wlcptranspiler.dto.GameDto.Connection;
import org.wlcp.wlcptranspiler.dto.GameDto.GlobalVariableInputModifier;
import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.Transition;

public class GlobalVariableTransitionType extends TransitionType implements ITransitionType {

	@Override
	public String GenerateTranstion(String scope, Map<Connection, Transition> connectionTransitions, List<OutputState> outputStates) {
		StringBuilder stringBuilder = new StringBuilder();
		for(Entry<Connection, Transition> entry : connectionTransitions.entrySet()) {
			if(entry.getValue().getGlobalVariables().containsKey(scope)) {
				if(entry.getValue().getGlobalVariables().get(scope).globalVariableInputModifiers.size() > 0) {
					stringBuilder.append(GenerateTransitionConditional(scope));
					stringBuilder.append("      if(");
					for(GlobalVariableInputModifier globalVariableInputModifier : entry.getValue().getGlobalVariables().get(scope).globalVariableInputModifiers) {
						stringBuilder.append("this.playerVM.getGlobalVariable(\"" + globalVariableInputModifier.variableName + "\")" + " " + globalVariableInputModifier.operator + " " + globalVariableInputModifier.value);
						if(!globalVariableInputModifier.logicalOperator.equals("")) {
							stringBuilder.append(" " + globalVariableInputModifier.logicalOperator + " ");
						}
					}
					stringBuilder.append(") { \n");
					stringBuilder.append("         this.state = states." + entry.getKey().connectionTo.stateType.name() + "_" + (outputStates.indexOf(entry.getKey().connectionTo) + 1) + ";\n");
					stringBuilder.append("      }\n");
					
					stringBuilder.append(GenerateTransitionEndConditional(scope));
				}
			}
		}
		return stringBuilder.toString();
	}
	
}
