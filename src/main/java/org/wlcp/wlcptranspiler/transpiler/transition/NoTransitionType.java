package org.wlcp.wlcptranspiler.transpiler.transition;

import java.util.Map;
import java.util.Map.Entry;

import org.wlcp.wlcptranspiler.dto.GameDto.Connection;
import org.wlcp.wlcptranspiler.dto.GameDto.Transition;
import org.wlcp.wlcptranspiler.transpiler.helper.TranspilerHelpers;
import org.wlcp.wlcptranspiler.transpiler.state.StateType;

public class NoTransitionType extends TransitionType implements ITransitionType {

	@Override
	public String GenerateTranstion(String scope, Map<Connection, Transition> connectionTransitions) {
		StringBuilder stringBuilder = new StringBuilder();	
		for (Entry<Connection, Transition> entry : connectionTransitions.entrySet()) {
			if(TranspilerHelpers.transitionConatainsNoScopes(entry.getValue()) && scope == "Game Wide") {
				stringBuilder.append("      " + "this.playerVM.NoTransition();\n");
			}
	 		if (!TranspilerHelpers.transitionContainsScope("Game Wide", entry.getValue()) && !TranspilerHelpers.transitionConatainsNoScopes(entry.getValue()) && scope != "Game Wide") {
				if (!TranspilerHelpers.transitionContainsScope(scope, entry.getValue()) && scope.contains("Team") && scope.contains("Player") && !TranspilerHelpers.transitionContainsScope(scope.split(" ")[0] + " " + scope.split(" ")[1], entry.getValue())) {
					stringBuilder.append(StateType.GenerateStateConditional(scope));
					stringBuilder.append("         " + "this.playerVM.NoTransition();\n");
					stringBuilder.append(StateType.GenerateEndStateConditional(scope));
				}
			}
		}
		return stringBuilder.toString();
	}

}
