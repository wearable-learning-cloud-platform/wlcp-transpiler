package org.wlcp.wlcptranspiler.transpiler.state;

import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.State;
import org.wlcp.wlcptranspiler.transpiler.helper.TranspilerHelpers;

public class NoStateType extends StateType implements IStateType {

	@Override
	public String GenerateState(String scope, State state) {
		StringBuilder stringBuilder = new StringBuilder();
		if(TranspilerHelpers.stateContainsNoScopes((OutputState)state) && scope == "Game Wide") {
			stringBuilder.append("      " + "this.playerVM.NoState();\n");
		}
 		if (!TranspilerHelpers.stateContainsScope("Game Wide", (OutputState) state) && !TranspilerHelpers.stateContainsNoScopes((OutputState)state) && scope != "Game Wide") {
			if (!TranspilerHelpers.stateContainsScope(scope, (OutputState) state) && scope.contains("Team") && scope.contains("Player") && !TranspilerHelpers.stateContainsScope(scope.split(" ")[0] + " " + scope.split(" ")[1], (OutputState) state)) {
				stringBuilder.append(StateType.GenerateStateConditional(scope));
				stringBuilder.append("         " + "this.playerVM.NoState();\n");
				stringBuilder.append(StateType.GenerateEndStateConditional(scope));
			}
		}
		return stringBuilder.toString();
	}

}
