package org.wlcp.wlcptranspiler.transpiler.transition;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.wlcp.wlcptranspiler.dto.GameDto;
import org.wlcp.wlcptranspiler.dto.GameDto.Connection;
import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.Transition;

public class SingleButtonPressTransitionType extends TransitionType implements ITransitionType {

	@Override
	public String GenerateTranstion(String scope, Map<Connection, Transition> connectionTransitions, List<OutputState> outputStates, GameDto game) {
		StringBuilder stringBuilder = new StringBuilder();
		Map<String, String> buttonMap = GenerateButtonMap(scope, connectionTransitions, outputStates);
		Map<String, String> labelMap = GenerateLabelMap(scope, connectionTransitions);
		if(buttonMap.size() > 0) {
			stringBuilder.append(GenerateTransitionConditional(scope));
			stringBuilder.append(GenerateTransitionSingleButtonPress(scope, buttonMap, labelMap));
			stringBuilder.append(GenerateTransitionEndConditional(scope));
		}
		return stringBuilder.toString();
	}
	
	public Map<String, String> GenerateLabelMap(String scope, Map<Connection, Transition> connectionTransitions) { 
		Map<String, String> labelMap = new LinkedHashMap<String, String>();
		for(Entry<Connection, Transition> entry : connectionTransitions.entrySet()) {
			if(entry.getValue().getSingleButtonPresses().containsKey(scope)) {
				labelMap.put("label1", entry.getValue().getSingleButtonPresses().get(scope).getButton1Label());
				labelMap.put("label2", entry.getValue().getSingleButtonPresses().get(scope).getButton2Label());
				labelMap.put("label3", entry.getValue().getSingleButtonPresses().get(scope).getButton3Label());
				labelMap.put("label4", entry.getValue().getSingleButtonPresses().get(scope).getButton4Label());
			}
		}
		return labelMap;
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
	
	public String GenerateArrays(Map<String, String> buttonMap, Map<String, String> labelMap) {
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
				stringBuilder.append("states." + entry.getValue() + "], [");
			} else {
				stringBuilder.append("states." + entry.getValue() + ", ");
			}
			count++;
		}
		count = 0;
		for(Entry<String, String> entry : labelMap.entrySet()) {
			if(count == labelMap.size() - 1) {
				stringBuilder.append("\"" + entry.getValue() + "\"]");
			} else {
				stringBuilder.append("\"" + entry.getValue() + "\", ");
			}
			count++;
		}
		return stringBuilder.toString();
	}
	
	private String GenerateTransitionSingleButtonPress(String scope, Map<String, String> buttonMap, Map<String, String> labelMap) {
		StringBuilder stringBuilder = new StringBuilder();
		GenerateArrays(buttonMap, labelMap);
		if(scope.equals("Game Wide")) {
			stringBuilder.append("      this.state = this.playerVM.SingleButtonPress(" + GenerateArrays(buttonMap, labelMap) + ");\n");
		} else {
			stringBuilder.append("         this.state = this.playerVM.SingleButtonPress(" + GenerateArrays(buttonMap, labelMap) + ");\n");
		}
		return stringBuilder.toString();
	}
	
}
