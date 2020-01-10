package org.wlcp.wlcptranspiler.transpiler.state;

import org.wlcp.wlcptranspiler.dto.GameDto.State;

public interface IStateType {

	public String GenerateState(String scope, State state);
}
