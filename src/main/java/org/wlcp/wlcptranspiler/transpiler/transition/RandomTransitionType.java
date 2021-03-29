package org.wlcp.wlcptranspiler.transpiler.transition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.wlcp.wlcptranspiler.dto.GameDto.Connection;
import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.Transition;

public class RandomTransitionType extends TransitionType implements ITransitionType {
	
	@Override
	public String GenerateTranstion(String scope, Map<Connection, Transition> connectionTransitions, List<OutputState> outputStates) {
		StringBuilder stringBuilder = new StringBuilder();
		List<String> randomStates = GenerateRandomStateList(scope, connectionTransitions, outputStates);
		if(randomStates.size() > 0) {
			stringBuilder.append(GenerateTransitionConditional(scope));
			stringBuilder.append(GenerateRandomTransition(scope, randomStates));
			stringBuilder.append(GenerateTransitionEndConditional(scope));
		}
		return stringBuilder.toString();
	}
	
	public List<String> GenerateRandomStateList(String scope, Map<Connection, Transition> connectionTransitions, List<OutputState> outputStates) {
		List<String> randomStates = new ArrayList<String>();
		for(Entry<Connection, Transition> entry : connectionTransitions.entrySet()) {
			if(entry.getValue().getRandoms().containsKey(scope)) {
				if(entry.getValue().getRandoms().get(scope).randomEnabled) {
					randomStates.add(entry.getKey().getConnectionTo().stateType.name() + "_" + (outputStates.indexOf(entry.getKey().getConnectionTo()) + 1));
				}	
			}
		}
		return randomStates;
	}
	
	private String GenerateRandomTransition(String scope, List<String> randomStates) {
		StringBuilder stringBuilder = new StringBuilder();
		if(scope.equals("Game Wide")) {
			stringBuilder.append("      this.state = this.playerVM.RandomTransition(" + GenerateArrays(randomStates) + ");\n");
		} else {
			stringBuilder.append("         this.state = this.playerVM.RandomTransition(" + GenerateArrays(randomStates) + ");\n");
		}
		return stringBuilder.toString();
	}
	
	public String GenerateArrays(List<String> randomStates) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		int count = 0;
		for(String randomState : randomStates) {
			if(count == randomStates.size() - 1) {
				stringBuilder.append("states." + randomState + "]");
			} else {
				stringBuilder.append("states." + randomState + ", ");
			}
			count++;
		}
		return stringBuilder.toString();
	}

}
