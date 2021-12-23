package org.wlcp.wlcptranspiler.transpiler.transition;

import java.util.List;
import java.util.Map;

import org.wlcp.wlcptranspiler.dto.GameDto;
import org.wlcp.wlcptranspiler.dto.GameDto.Connection;
import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.Transition;

public interface ITransitionType {
	
	public String GenerateTranstion(String scope, Map<Connection, Transition> connectionTransitions, List<OutputState> outputStates, GameDto game);
	
}
