package org.wlcp.wlcptranspiler.transpiler.steps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wlcp.wlcptranspiler.dto.GameDto;
import org.wlcp.wlcptranspiler.dto.GameDto.Connection;
import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.StartState;
import org.wlcp.wlcptranspiler.dto.GameDto.State;
import org.wlcp.wlcptranspiler.dto.GameDto.Transition;
import org.wlcp.wlcptranspiler.transpiler.helper.TranspilerHelpers;
import org.wlcp.wlcptranspiler.transpiler.state.DisplayPhotoStateType;
import org.wlcp.wlcptranspiler.transpiler.state.DisplayTextStateType;
import org.wlcp.wlcptranspiler.transpiler.state.IStateType;
import org.wlcp.wlcptranspiler.transpiler.state.NoStateType;
import org.wlcp.wlcptranspiler.transpiler.state.PlaySoundStateType;
import org.wlcp.wlcptranspiler.transpiler.state.PlayVideoStateType;
import org.wlcp.wlcptranspiler.transpiler.state.StateType;
import org.wlcp.wlcptranspiler.transpiler.transition.ITransitionType;
import org.wlcp.wlcptranspiler.transpiler.transition.KeyboardInputTransitionType;
import org.wlcp.wlcptranspiler.transpiler.transition.NoTransitionType;
import org.wlcp.wlcptranspiler.transpiler.transition.SequenceButtonPressTransitionType;
import org.wlcp.wlcptranspiler.transpiler.transition.SingleButtonPressTransitionType;
import org.wlcp.wlcptranspiler.transpiler.transition.TimerDurationTransitionType;

public class GenerateStateMachineFunctionsStep implements ITranspilerStep {
	
	private StringBuilder stringBuilder = new StringBuilder();
	private StartState startState;
	private GameDto game;
	private List<OutputState> outputStates;
	private List<Connection> connections;
	private List<Transition> transitions;
	private List<IStateType> stateTypes = new ArrayList<IStateType>();
	private List<ITransitionType> transitionTypes = new ArrayList<ITransitionType>();
	
	public GenerateStateMachineFunctionsStep(GameDto game, StartState startState, List<OutputState> outputStates, List<Connection> connections, List<Transition> transitions) {
		this.game = game;
		this.startState = startState;
		this.outputStates = outputStates;
		this.connections = connections;
		this.transitions = transitions;
	}

	@Override
	public String PerformStep() {
		
		//Setup the state and transition types
		SetupTypes();
		
		//Loop through every state in the machine
		for(int stateCount = 0; stateCount < outputStates.size() + 1; stateCount++) {
			
			//Special case
			if(stateCount == 0) {
				GenerateFunctions(startState);
			} else {
				GenerateFunctions(outputStates.get(stateCount - 1));
			}
		}
		stringBuilder.append("};\n\n");
		return stringBuilder.toString();
	}
	
	private void SetupTypes() {
		stateTypes.add(new NoStateType());
		stateTypes.add(new DisplayTextStateType());
		stateTypes.add(new DisplayPhotoStateType());
		stateTypes.add(new PlaySoundStateType());
		stateTypes.add(new PlayVideoStateType());
		transitionTypes.add(new NoTransitionType());
		transitionTypes.add(new SingleButtonPressTransitionType());
		transitionTypes.add(new SequenceButtonPressTransitionType());
		transitionTypes.add(new KeyboardInputTransitionType());
		transitionTypes.add(new TimerDurationTransitionType());
	}
	
	private void GenerateFunctions(State state) {
		
		//Get a list of connections for that state
		List<Connection> fromConnections = TranspilerHelpers.GetFromConnections(connections, state);
		
		//If there are no connections
		if(fromConnections.size() == 0) {
			GenerateNoConnections(state);
		} else if(fromConnections.size() == 1) { //If there is only one connection...
			
			Transition transition;
			
			//And no transition, go right there
			if((transition = TranspilerHelpers.GetConnectionTransition(transitions, fromConnections.get(0))) == null) {
				GenerateSingleConnectionNoTransition(state, fromConnections.get(0));
			} else {
				//Else generate the transition conditional
				GenerateSingleConnectionSingleTransition(state, fromConnections.get(0), transition);
			}
			
		} else if(fromConnections.size() > 1) { //If there is more than one connection
			
			Map<Connection, Transition> connectionTransitions = new HashMap<Connection, Transition>();
			List<Connection> connectionsWithoutTransitions = new ArrayList<Connection>();
			
			//Loop through the connections and determine which have transitions and which don't
			for(Connection connection : fromConnections) {
				Transition transition = TranspilerHelpers.GetConnectionTransition(transitions, connection);
				if(transition != null) {
					connectionTransitions.put(connection, transition);
				} else {
					connectionsWithoutTransitions.add(connection);
				}
			}
			
			GenerateMethodSignature(state);
			GenerateOutputState(state);
			
			//Generate connections with transitions
			GenerateMultipleConnectionsSingleTransition(connectionTransitions);
			
			//Generate connections with no transitions
			for(Connection connection : connectionsWithoutTransitions) {
				
				//If there are no transitions on the connection
				//We need to look ahead at the state
				GenerateMultipleConnectionsNoTransition(connection);
			}
			
			stringBuilder.append("   " + "},\n\n");
		}
	}
	
	private void GenerateNoConnections(State state) {
		GenerateMethodSignature(state);
		GenerateOutputState(state);
		GenerateTransition(TranspilerHelpers.createEmptyTransition());
		stringBuilder.append("      this.state = -1;\n");
		stringBuilder.append("   " + "},\n\n");
	}
	
	private void GenerateSingleConnectionNoTransition(State state, Connection connection) {
		GenerateMethodSignature(state);
		GenerateOutputState(state);
		stringBuilder.append("      " + "this.state = states." + connection.connectionTo.stateType.name() + "_" + (outputStates.indexOf(connection.connectionTo) + 1) + ";\n");
		stringBuilder.append("   " + "},\n\n");
	}
	
	private void GenerateSingleConnectionSingleTransition(State state, Connection connection, Transition transition) {
		GenerateMethodSignature(state);
		GenerateOutputState(state);
		Map<Connection, Transition> connectionTransitions = new HashMap<Connection, Transition>(); connectionTransitions.put(connection, transition);
		GenerateTransition(connectionTransitions);
		stringBuilder.append("   " + "},\n\n");
	}
	
	private void GenerateMultipleConnectionsNoTransition(Connection connection) {
		OutputState nextState = (OutputState) TranspilerHelpers.GetToState(outputStates, connection);
		for(String s : TranspilerHelpers.GenerateScope(game.getTeamCount(), game.getPlayersPerTeam())) {
			if(TranspilerHelpers.stateContainsScope(s, nextState)) {
				stringBuilder.append(StateType.GenerateStateConditional(s));
				stringBuilder.append("         " + "this.state = states." + nextState.stateType.name() + "_" + (outputStates.indexOf(nextState) + 1) + ";\n");
				stringBuilder.append(StateType.GenerateEndStateConditional(s));
			}
		}
	}
	
	private void GenerateMultipleConnectionsSingleTransition(Map<Connection, Transition> connectionTransitions) {
		GenerateTransition(connectionTransitions);
	}
	
	private void GenerateOutputState(State state) {
		if(state instanceof StartState) {return;}
		for(String s : TranspilerHelpers.GenerateScope(game.getTeamCount(), game.getPlayersPerTeam())) {
			for(IStateType stateType : stateTypes) {
				stringBuilder.append(stateType.GenerateState(s, state));
			}
		}
	}
	
	private void GenerateTransition(Map<Connection, Transition> connectionTransitions) {
		for(String s : TranspilerHelpers.GenerateScope(game.getTeamCount(), game.getPlayersPerTeam())) {
			for(ITransitionType transitionType : transitionTypes) {
				stringBuilder.append(transitionType.GenerateTranstion(s, connectionTransitions, outputStates));
			}
		}
	}
	
	private void GenerateMethodSignature(State state) {
		if(state instanceof StartState) {
			stringBuilder.append("   " + state.stateType.name() + "_0" + " : function() {\n");
		} else {
			stringBuilder.append("   " + state.stateType.name() + "_" + (outputStates.indexOf(state) + 1) + " : function() {\n");
		}
	}

}
