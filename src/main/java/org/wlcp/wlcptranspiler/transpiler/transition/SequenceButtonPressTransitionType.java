package org.wlcp.wlcptranspiler.transpiler.transition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.wlcp.wlcptranspiler.dto.GameDto;
import org.wlcp.wlcptranspiler.dto.GameDto.Connection;
import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.Transition;

public class SequenceButtonPressTransitionType extends TransitionType implements ITransitionType {
	
	@Override
	public String GenerateTranstion(String scope, Map<Connection, Transition> connectionTransitions, List<OutputState> outputStates, GameDto game) {
		StringBuilder stringBuilder = new StringBuilder();
		Map<String, String> sequenceMap = GenerateSequenceMap(scope, connectionTransitions, outputStates);
		if(sequenceMap.size() > 0) {
			stringBuilder.append(GenerateTransitionConditional(scope));
			stringBuilder.append(GenerateTransitionSequenceButtonPress(scope, sequenceMap));
			stringBuilder.append(GenerateTransitionEndConditional(scope));
		}
		return stringBuilder.toString();
	}
	
	public Map<String, String> GenerateSequenceMap(String scope, Map<Connection, Transition> connectionTransitions, List<OutputState> outputStates) {
		Map<String, String> sequenceMap = new HashMap<String, String>();
		for(Entry<Connection, Transition> entry : connectionTransitions.entrySet()) {
			if(entry.getValue().getSequenceButtonPresses().containsKey(scope)) {
				for(String sequence : entry.getValue().getSequenceButtonPresses().get(scope).getSequences()) {
					sequenceMap.put(sequence, entry.getKey().getConnectionTo().stateType.name() + "_" + (outputStates.indexOf(entry.getKey().getConnectionTo()) + 1));
				}
			}
		}
		return sequenceMap;
	}
	
	private String GenerateTransitionSequenceButtonPress(String scope, Map<String, String> sequenceMap) {
		StringBuilder stringBuilder = new StringBuilder();
		if(scope.equals("Game Wide")) {
			stringBuilder.append("      this.state = this.playerVM.SequenceButtonPress(" + GenerateArrays(sequenceMap) + ");\n");
		} else {
			stringBuilder.append("         this.state = this.playerVM.SequenceButtonPress(" + GenerateArrays(sequenceMap) + ");\n");
		}
		return stringBuilder.toString();
	}
	
	public String GenerateArrays(Map<String, String> sequenceMap) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		int count = 0;
		for(Entry<String, String> entry : sequenceMap.entrySet()) {
			if(count == sequenceMap.size() - 1) {
				stringBuilder.append("\"" + entry.getKey() + "\"], [");
			} else {
				stringBuilder.append("\"" + entry.getKey() + "\", ");
			}
			count++;
		}
		count = 0;
		for(Entry<String, String> entry : sequenceMap.entrySet()) {
			if(count == sequenceMap.size() - 1) {
				stringBuilder.append("states." + entry.getValue() + "]");
			} else {
				stringBuilder.append("states." + entry.getValue() + ", ");
			}
			count++;
		}
		return stringBuilder.toString();
	}
}
