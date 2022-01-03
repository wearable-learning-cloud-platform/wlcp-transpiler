package org.wlcp.wlcptranspiler.serivce.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.wlcp.wlcptranspiler.dto.GameDto;
import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.StartState;
import org.wlcp.wlcptranspiler.dto.GameDto.State;
import org.wlcp.wlcptranspiler.dto.GameDto.StateType;
import org.wlcp.wlcptranspiler.service.TranspilerService;
import org.wlcp.wlcptranspiler.transpiler.steps.GenerateGlobalVariablesStep;
import org.wlcp.wlcptranspiler.transpiler.steps.GenerateNameSpaceAndVariablesStep;
import org.wlcp.wlcptranspiler.transpiler.steps.GenerateSetGameVariablesStep;
import org.wlcp.wlcptranspiler.transpiler.steps.GenerateStartFunctionStep;
import org.wlcp.wlcptranspiler.transpiler.steps.GenerateStateEnumStep;
import org.wlcp.wlcptranspiler.transpiler.steps.GenerateStateMachineFunctionsStep;
import org.wlcp.wlcptranspiler.transpiler.steps.GenerateStateMachineStep;
import org.wlcp.wlcptranspiler.transpiler.steps.ITranspilerStep;

@Service
public class JavaScriptTranspilerServiceImpl implements TranspilerService {
	
	private StringBuilder stringBuilder;
	private List<ITranspilerStep> transpilerSteps;

	@Override
	public String transpile(GameDto gameDto) {
		
		//Clear the step list
		transpilerSteps = new ArrayList<ITranspilerStep>();
		
		//Create a new string builder
		stringBuilder = new StringBuilder();
		
		//Setup the transpiler steps
		setupTranspilerSteps(gameDto);
		
		//Loop through the steps
		for(ITranspilerStep step : transpilerSteps) {
			stringBuilder.append(step.PerformStep());
		}
		
		return stringBuilder.toString();
	}
	
	private void setupTranspilerSteps(GameDto gameDto) {
		
		StartState startState = null;
		List<OutputState> outputStates = new ArrayList<OutputState>();
		
		for(State state : gameDto.states) {
			if(state.stateType.equals(StateType.START_STATE)) {
				startState = (StartState) state;
			} else {
				outputStates.add((OutputState) state);
			}
		}
		
		transpilerSteps.add(new GenerateStateEnumStep(startState, outputStates));
		transpilerSteps.add(new GenerateNameSpaceAndVariablesStep(startState));
		transpilerSteps.add(new GenerateGlobalVariablesStep(startState));
		transpilerSteps.add(new GenerateStartFunctionStep());
		transpilerSteps.add(new GenerateStateMachineStep(startState, outputStates));
		transpilerSteps.add(new GenerateStateMachineFunctionsStep(gameDto, startState, outputStates, gameDto.connections, gameDto.transitions));
		transpilerSteps.add(new GenerateSetGameVariablesStep());
	}

}
