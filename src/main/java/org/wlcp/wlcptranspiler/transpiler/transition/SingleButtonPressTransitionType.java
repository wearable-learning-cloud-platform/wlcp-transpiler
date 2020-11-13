package org.wlcp.wlcptranspiler.transpiler.transition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.wlcp.wlcptranspiler.dto.GameDto.Connection;
import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.Transition;

public class SingleButtonPressTransitionType extends TransitionType implements ITransitionType {

	@Override
	public String GenerateTranstion(String scope, Map<Connection, Transition> connectionTransitions, List<OutputState> outputStates) {
		StringBuilder stringBuilder = new StringBuilder();
		Map<String, String> buttonMap = GenerateButtonMap(scope, connectionTransitions, outputStates);
		if(buttonMap.size() > 0) {
			stringBuilder.append(GenerateTransitionConditional(scope));
			stringBuilder.append(GenerateTransitionSingleButtonPress(scope, buttonMap));
			stringBuilder.append(GenerateTransitionEndConditional(scope));
		}
		return stringBuilder.toString();
	}
	
	public Map<String, String> GenerateButtonMap(String scope, Map<Connection, Transition> connectionTransitions, List<OutputState> outputStates) {
		Map<String, String> buttonMap = new HashMap<String, String>();
		for(Entry<Connection, Transition> entry : connectionTransitions.entrySet()) {
			if(entry.getValue().getSingleButtonPresses().containsKey(scope)) {
				if(entry.getValue().getSingleButtonPresses().get(scope).getButton1()) {
					buttonMap.put("1", entry.getKey().getConnectionTo().stateType.name() + "_" + (outputStates.indexOf(entry.getKey().getConnectionTo()) + 1));
				}
				if(entry.getValue().getSingleButtonPresses().get(scope).getButton2()) {
					buttonMap.put("2", entry.getKey().getConnectionTo().stateType.name() + "_" + (outputStates.indexOf(entry.getKey().getConnectionTo()) + 1));
				}
				if(entry.getValue().getSingleButtonPresses().get(scope).getButton3()) {
					buttonMap.put("3", entry.getKey().getConnectionTo().stateType.name() + "_" + (outputStates.indexOf(entry.getKey().getConnectionTo()) + 1));
				}
				if(entry.getValue().getSingleButtonPresses().get(scope).getButton4()) {
					buttonMap.put("4", entry.getKey().getConnectionTo().stateType.name() + "_" + (outputStates.indexOf(entry.getKey().getConnectionTo()) + 1));
				}
			}
		}
		return buttonMap;
	}
	
	public String GenerateArrays(Map<String, String> buttonMap) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		int count = 0;
		for(Entry<String, String> entry : buttonMap.entrySet()) {
			if(count == buttonMap.size() - 1) {
				stringBuilder.append("\"" + entry.getKey() + "\"], [");
			} else {
				stringBuilder.append("\"" + entry.getKey() + "\", ");
			}
			count++;
		}
		count = 0;
		for(Entry<String, String> entry : buttonMap.entrySet()) {
			if(count == buttonMap.size() - 1) {
				stringBuilder.append("states." + entry.getValue() + "]");
			} else {
				stringBuilder.append("states." + entry.getValue() + ", ");
			}
			count++;
		}
		return stringBuilder.toString();
	}
	
	private String GenerateTransitionSingleButtonPress(String scope, Map<String, String> buttonMap) {
		StringBuilder stringBuilder = new StringBuilder();
		GenerateArrays(buttonMap);
		if(scope.equals("Game Wide")) {
			stringBuilder.append("      this.state = this.playerVM.SingleButtonPress(" + GenerateArrays(buttonMap) + ");\n");
		} else {
			stringBuilder.append("         this.state = this.playerVM.SingleButtonPress(" + GenerateArrays(buttonMap) + ");\n");
		}
		return stringBuilder.toString();
	}
	
}
