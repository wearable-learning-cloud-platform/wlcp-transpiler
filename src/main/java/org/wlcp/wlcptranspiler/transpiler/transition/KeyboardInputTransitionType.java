package org.wlcp.wlcptranspiler.transpiler.transition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.wlcp.wlcptranspiler.dto.GameDto;
import org.wlcp.wlcptranspiler.dto.GameDto.Connection;
import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.Transition;
import org.wlcp.wlcptranspiler.transpiler.helper.TranspilerHelpers;

public class KeyboardInputTransitionType extends TransitionType implements ITransitionType {
	
	@Override
	public String GenerateTranstion(String scope, Map<Connection, Transition> connectionTransitions, List<OutputState> outputStates, GameDto game) {
		StringBuilder stringBuilder = new StringBuilder();
		Map<String, String> keyboardInputMap = GenerateKeyboardInputMap(scope, connectionTransitions, outputStates);
		if(keyboardInputMap.size() > 0) {
			stringBuilder.append(GenerateTransitionConditional(scope));
			stringBuilder.append(GenerateTransitionKeyboardInput(scope, keyboardInputMap));
			stringBuilder.append(GenerateTransitionEndConditional(scope));
		}
		return stringBuilder.toString();
	}
	
	public Map<String, String> GenerateKeyboardInputMap(String scope, Map<Connection, Transition> connectionTransitions, List<OutputState> outputStates) {
		Map<String, String> keyboardInputMap = new HashMap<String, String>();
		for(Entry<Connection, Transition> entry : connectionTransitions.entrySet()) {
			if(entry.getValue().getKeyboardInputs().containsKey(scope)) {
				for(String keyboardInput : entry.getValue().getKeyboardInputs().get(scope).getKeyboardInputs()) {
					keyboardInputMap.put(keyboardInput, entry.getKey().getConnectionTo().stateType.name() + "_" + (outputStates.indexOf(entry.getKey().getConnectionTo()) + 1));
				}
			}
		}
		return keyboardInputMap;
	}
	
	private String GenerateTransitionKeyboardInput(String scope, Map<String, String> keyboardInputMap) {
		StringBuilder stringBuilder = new StringBuilder();
		if(scope.equals("Game Wide")) {
			stringBuilder.append("      this.state = this.playerVM.KeyboardInput(" + GenerateArrays(keyboardInputMap) + ");\n");
		} else {
			stringBuilder.append("         this.state = this.playerVM.KeyboardInput(" + GenerateArrays(keyboardInputMap) + ");\n");
		}
		return stringBuilder.toString();
	}
	

	public String GenerateArrays(Map<String, String> keyboardInputMap) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		int count = 0;
		for(Entry<String, String> entry : keyboardInputMap.entrySet()) {
			if(count == keyboardInputMap.size() - 1) {
				stringBuilder.append("\"" + TranspilerHelpers.ReplaceEscapeSequences(entry.getKey()) + "\"], [");
			} else {
				stringBuilder.append("\"" + TranspilerHelpers.ReplaceEscapeSequences(entry.getKey()) + "\", ");
			}
			count++;
		}
		count = 0;
		for(Entry<String, String> entry : keyboardInputMap.entrySet()) {
			if(count == keyboardInputMap.size() - 1) {
				stringBuilder.append("states." + entry.getValue() + "]");
			} else {
				stringBuilder.append("states." + entry.getValue() + ", ");
			}
			count++;
		}
		return stringBuilder.toString();
	}
}
