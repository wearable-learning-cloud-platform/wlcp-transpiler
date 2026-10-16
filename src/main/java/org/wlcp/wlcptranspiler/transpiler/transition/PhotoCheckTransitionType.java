package org.wlcp.wlcptranspiler.transpiler.transition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.wlcp.wlcptranspiler.dto.GameDto;
import org.wlcp.wlcptranspiler.dto.GameDto.Connection;
import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.PhotoCheck;
import org.wlcp.wlcptranspiler.dto.GameDto.Transition;
import org.wlcp.wlcptranspiler.transpiler.helper.TranspilerHelpers;

/**
 * Emits this.state = this.playerVM.PhotoCheck(modelUrl, [targetClasses], [thresholds], [states]);
 * for every photo check transition leaving a state in the given scope. Mirrors
 * KeyboardInputTransitionType: one call with parallel arrays, one entry per outgoing transition.
 * All photo checks leaving one state share the model of the first one found; the editor only
 * offers one link per state so this holds in practice.
 */
public class PhotoCheckTransitionType extends TransitionType implements ITransitionType {

	private static class Branch {
		String targetClass;
		double threshold;
		String state;
	}

	@Override
	public String GenerateTranstion(String scope, Map<Connection, Transition> connectionTransitions, List<OutputState> outputStates, GameDto game) {
		StringBuilder stringBuilder = new StringBuilder();
		String modelUrl = null;
		List<Branch> branches = new ArrayList<Branch>();
		for(Entry<Connection, Transition> entry : connectionTransitions.entrySet()) {
			if(entry.getValue().getPhotoChecks() != null && entry.getValue().getPhotoChecks().containsKey(scope)) {
				PhotoCheck photoCheck = entry.getValue().getPhotoChecks().get(scope);
				if(modelUrl == null && photoCheck.getModelUrl() != null && !photoCheck.getModelUrl().isEmpty()) {
					modelUrl = photoCheck.getModelUrl();
				}
				Branch branch = new Branch();
				branch.targetClass = photoCheck.getTargetClass() == null ? "" : photoCheck.getTargetClass();
				branch.threshold = photoCheck.getThreshold() == null ? 0.8 : photoCheck.getThreshold();
				branch.state = entry.getKey().getConnectionTo().stateType.name() + "_" + (outputStates.indexOf(entry.getKey().getConnectionTo()) + 1);
				branches.add(branch);
			}
		}
		if(branches.size() > 0 && modelUrl != null) {
			stringBuilder.append(GenerateTransitionConditional(scope));
			stringBuilder.append(scope.equals("Game Wide") ? "      " : "         ");
			stringBuilder.append("this.state = this.playerVM.PhotoCheck(\"" + TranspilerHelpers.ReplaceEscapeSequences(modelUrl) + "\", " + GenerateArrays(branches) + ");\n");
			stringBuilder.append(GenerateTransitionEndConditional(scope));
		}
		return stringBuilder.toString();
	}

	private String GenerateArrays(List<Branch> branches) {
		StringBuilder classes = new StringBuilder("[");
		StringBuilder thresholds = new StringBuilder("[");
		StringBuilder states = new StringBuilder("[");
		for(int i = 0; i < branches.size(); i++) {
			String sep = i == branches.size() - 1 ? "" : ", ";
			classes.append("\"" + TranspilerHelpers.ReplaceEscapeSequences(branches.get(i).targetClass) + "\"" + sep);
			thresholds.append(branches.get(i).threshold + sep);
			states.append("states." + branches.get(i).state + sep);
		}
		return classes.append("]").toString() + ", " + thresholds.append("]").toString() + ", " + states.append("]").toString();
	}
}
