package org.wlcp.wlcptranspiler.transpiler.state;

import java.util.Map;

import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.State;
import org.wlcp.wlcptranspiler.transpiler.helper.TranspilerHelpers;

public class DisplayTextStateType extends StateType implements IStateType {

	@Override
	public String GenerateState(String scope, State state) {
		StringBuilder stringBuilder = new StringBuilder();
		Map<String, String> displayText = ((OutputState) state).getDisplayText();
		if(displayText.containsKey(scope)) {
			stringBuilder.append(StateType.GenerateStateConditional(scope));
			if(scope.equals("Game Wide")) {
				stringBuilder.append("      " + "this.playerVM.DisplayText(" + "\"" + TranspilerHelpers.ReplaceEscapeSequences(displayText.get(scope)) + "\"" + ");\n");
			} else {
				stringBuilder.append("         " + "this.playerVM.DisplayText(" + "\"" + TranspilerHelpers.ReplaceEscapeSequences(displayText.get(scope)) + "\"" + ");\n");
			}
			stringBuilder.append(StateType.GenerateEndStateConditional(scope));
			return stringBuilder.toString();
		}
		return stringBuilder.toString();
	}

}
