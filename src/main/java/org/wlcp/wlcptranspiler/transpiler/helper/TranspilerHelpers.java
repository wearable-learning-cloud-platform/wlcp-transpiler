package org.wlcp.wlcptranspiler.transpiler.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.wlcp.wlcptranspiler.dto.GameDto.Connection;
import org.wlcp.wlcptranspiler.dto.GameDto.KeyboardInput;
import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.SequenceButtonPress;
import org.wlcp.wlcptranspiler.dto.GameDto.SingleButtonPress;
import org.wlcp.wlcptranspiler.dto.GameDto.State;
import org.wlcp.wlcptranspiler.dto.GameDto.Transition;

public class TranspilerHelpers {
	
	public static List<Connection> GetFromConnections(List<Connection> connections, State state) {
		List<Connection> fromConnections = new ArrayList<Connection>();
		for(Connection connection : connections) {
			if(connection.getConnectionFrom().getStateId().equals(state.getStateId())) {
				fromConnections.add(connection);
			}
		}
		return fromConnections;
	}
	
	public static Transition GetConnectionTransition(List<Transition> transitions, Connection connection) {
		for(Transition transition : transitions) {
			if(transition.getConnection().getConnectionId().equals(connection.getConnectionId())) {
				return transition;
			}
		}
		return null;
	}
	
	public static State GetToState(List<OutputState> outputStates, Connection connection) {
		for(State state : outputStates) {
			if(state.getStateId().equals(connection.getConnectionTo().getStateId())) {
				return state;
			}
		}
		return null;
	}
	
	public static Map<Connection, Transition> createEmptyTransition() {
		Transition transition = new Transition();
		transition.singleButtonPresses = new HashMap<String, SingleButtonPress>();
		transition.sequenceButtonPresses = new HashMap<String, SequenceButtonPress>();
		transition.keyboardInputs = new HashMap<String, KeyboardInput>();
		HashMap<Connection, Transition> connectionTransitions = new HashMap<Connection, Transition>(); 
		connectionTransitions.put(new Connection(), transition);
		return connectionTransitions;
	}
	
	public static boolean stateContainsScope(String scope, OutputState state) {
		if(state.getDisplayText().containsKey(scope) || state.getPictureOutputs().containsKey(scope) || state.getSoundOutputs().containsKey(scope) || state.getVideoOutputs().containsKey(scope) || state.getGlobalVariables().containsKey(scope)) {
			return true;
		}
		return false;
	}
	
	public static boolean transitionContainsScope(String scope, Transition transition) {
		if(transition.getSingleButtonPresses().containsKey(scope) || transition.getSequenceButtonPresses().containsKey(scope) || transition.getKeyboardInputs().containsKey(scope)) {
			return true;
		}
		return false;
	}
	
	public static boolean stateContainsNoScopes(OutputState state) {
		if(state.getDisplayText().size() == 0 && state.getPictureOutputs().size() == 0 && state.getSoundOutputs().size() == 0 && state.getVideoOutputs().size() == 0 && state.getGlobalVariables().size() == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean transitionConatainsNoScopes(Transition transition) {
		if(transition.getSingleButtonPresses().size() == 0 && transition.getSequenceButtonPresses().size() == 0 && transition.getKeyboardInputs().size() == 0) {
			return true;
		}
		return false;
	}
	
	public static List<String> GenerateScope(int teams, int playersPerTeam) {
		
		List<String> scope = new ArrayList<String>();

		//Add game wide
		scope.add("Game Wide");
		
		//Add the teams
		for(int i = 0; i < teams; i++) {
			scope.add("Team " + (i + 1));
		}
		
		//Add the players
		for(int i = 0; i < teams; i++) {
			for(int n = 0; n < playersPerTeam; n++) {
				scope.add("Team " + (i + 1) + " Player " + (n + 1));
			}
		}
		return scope;
	}
	
	public static String ReplaceEscapeSequences(String input) {
		String returnString = input;
		returnString = returnString.replace("\\", "\\\\");
		returnString = returnString.replace("\"", "\\\"");
		returnString = returnString.replace("\'", "\\\'");
		returnString = returnString.replace("\n", "\\n");
		returnString = returnString.replace("\r", "\\r");
		return evaluate(returnString);
	}
	
	private static String evaluate(String input) {
		String[] evals = StringUtils.substringsBetween(input, "$eval(", ")");
		if(evals != null ) {
			for(int i = 0; i < evals.length; i++) {
				input = input.replace("$eval(" + evals[i] + ")", "\" + this.playerVM.getGlobalVariable(\"" + evals[i].split(" ")[0] +"\") + \"");
			}
		}
		return input;
	}
}
