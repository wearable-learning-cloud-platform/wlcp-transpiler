package org.wlcp.wlcptranspiler.transpiler.transition;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.wlcp.wlcptranspiler.dto.GameDto.Connection;
import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.Transition;

public class TimerDurationTransitionType extends TransitionType implements ITransitionType {

	@Override
	public String GenerateTranstion(String scope, Map<Connection, Transition> connectionTransitions, List<OutputState> outputStates) {
		StringBuilder stringBuilder = new StringBuilder();
		Entry<Integer, String> timerDuration = getTimerDuration(scope, connectionTransitions, outputStates);
		if(timerDuration != null) {
			stringBuilder.append(GenerateTransitionConditional(scope));
			stringBuilder.append(GenerateTransitionTimerDuration(scope, timerDuration));
			stringBuilder.append(GenerateTransitionEndConditional(scope));
		}
		return stringBuilder.toString();
	}
	
	private Entry<Integer, String> getTimerDuration(String scope, Map<Connection, Transition> connectionTransitions, List<OutputState> outputStates) {
		for(Entry<Connection, Transition> entry : connectionTransitions.entrySet()) {
			if(entry.getValue().getTimerDurations().containsKey(scope)) {
				return new AbstractMap.SimpleEntry<Integer, String>(entry.getValue().getTimerDurations().get(scope).getDuration(), "states." + entry.getKey().getConnectionTo().stateType.name() + "_" + (outputStates.indexOf(entry.getKey().getConnectionTo()) + 1));
			}
		}
		return null;
	}

	private String GenerateTransitionTimerDuration(String scope, Entry<Integer, String> timerDuration) {
		StringBuilder stringBuilder = new StringBuilder();
		if(scope.equals("Game Wide")) {
			stringBuilder.append("      this.playerVM.Delay(" + timerDuration.getKey() + ");\n");
			stringBuilder.append("      this.state = " + timerDuration.getValue() + ";\n");
		} else {
			stringBuilder.append("         this.playerVM.Delay(" + timerDuration.getKey() + ");\n");
			stringBuilder.append("         this.state = " + timerDuration.getValue() + ";\n");
		}
		return stringBuilder.toString();
	}
	
}
