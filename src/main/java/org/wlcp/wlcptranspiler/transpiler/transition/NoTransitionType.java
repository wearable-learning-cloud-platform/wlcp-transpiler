package org.wlcp.wlcptranspiler.transpiler.transition;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.wlcp.wlcptranspiler.dto.GameDto;
import org.wlcp.wlcptranspiler.dto.GameDto.Connection;
import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.Transition;
import org.wlcp.wlcptranspiler.transpiler.helper.TranspilerHelpers;
import org.wlcp.wlcptranspiler.transpiler.state.StateType;

public class NoTransitionType extends TransitionType implements ITransitionType {
	
	//1. Nothing is active
	//2. Game wide active
	//3. Team active
	//4. Player active
	
	@Override
	public String GenerateTranstion(String scope, Map<Connection, Transition> connectionTransitions, List<OutputState> outputStates, GameDto game) {
		StringBuilder stringBuilder = new StringBuilder();
		
		boolean emptyTransition = true;
		for (Entry<Connection, Transition> entry : connectionTransitions.entrySet()) {
			emptyTransition &= TranspilerHelpers.transitionConatainsNoScopes(entry.getValue());
		}
		boolean containsGameWide = false;
		for (Entry<Connection, Transition> entry : connectionTransitions.entrySet()) {
			containsGameWide |= TranspilerHelpers.transitionContainsScope("Game Wide", entry.getValue());
		}
		boolean containsScopes = false;
		for (Entry<Connection, Transition> entry : connectionTransitions.entrySet()) {
			containsScopes |= TranspilerHelpers.transitionContainsScope(scope, entry.getValue());
		}
		
		if(scope.equals("Game Wide")) {
			if(emptyTransition && !containsGameWide) {
				stringBuilder.append("      " + "this.playerVM.NoTransition();\n");
			}
		} else if(scope.contains("Team") && !scope.contains("Player") && !emptyTransition && !containsGameWide && !containsScopes) {					
			noTransition(stringBuilder, scope, !containsScopes(game, scope, connectionTransitions, false));
		} else if(scope.contains("Team") && scope.contains("Player") && !emptyTransition && !containsGameWide && !containsScopes) {
			boolean activeTransition = false;
			for (Entry<Connection, Transition> entry : connectionTransitions.entrySet()) {
				activeTransition |= TranspilerHelpers.transitionContainsScope(scope, entry.getValue());
			}
			noTransition(stringBuilder, scope, !activeTransition && containsScopes(game, scope, connectionTransitions, true));
		}
		
		return stringBuilder.toString();
	}
	
	private boolean containsScopes(GameDto game, String scope, Map<Connection, Transition> connectionTransitions, boolean condition) {
		boolean containsPlayerScope = false;
		List<String> players = TranspilerHelpers.GenerateScope(game.getTeamCount(), game.getPlayersPerTeam());
		Iterator<String> it = players.iterator();
		while (it.hasNext()) {
			String s = it.next();
			if(condition) {
				if(s.contains(scope.split(" ")[0] + " " + scope.split(" ")[1]) && s.contains("Player") && !s.equals(scope)) {
					
				} else {
					it.remove();
				}
			} else {
				if(s.contains(scope) && s.contains("Player")) {
					
				} else {
					it.remove();
				}
			}
		}
		for(String s : players) {
			for (Entry<Connection, Transition> entry : connectionTransitions.entrySet()) {
				containsPlayerScope |= TranspilerHelpers.transitionContainsScope(s, entry.getValue());
			}
		}
		return containsPlayerScope;
	}
	
	private void noTransition(StringBuilder stringBuilder, String scope, boolean condition) {
		if(condition) {
			stringBuilder.append(StateType.GenerateStateConditional(scope));
			stringBuilder.append("         " + "this.playerVM.NoTransition();\n");
			stringBuilder.append(StateType.GenerateEndStateConditional(scope));
		}
	}
}
