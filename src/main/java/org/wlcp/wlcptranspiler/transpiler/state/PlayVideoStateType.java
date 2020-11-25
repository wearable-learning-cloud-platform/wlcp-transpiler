package org.wlcp.wlcptranspiler.transpiler.state;

import java.util.Map;

import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.State;
import org.wlcp.wlcptranspiler.dto.GameDto.VideoOutput;

public class PlayVideoStateType extends StateType implements IStateType {
	
	@Override
	public String GenerateState(String scope, State state) {
		StringBuilder stringBuilder = new StringBuilder();
		Map<String, VideoOutput> videoOutputs = ((OutputState) state).getVideoOutputs();
		if(videoOutputs.containsKey(scope)) {
			stringBuilder.append(StateType.GenerateStateConditional(scope));
			if(scope.equals("Game Wide")) {
				stringBuilder.append("      " + "this.playerVM.PlayVideo(" + "\"" + videoOutputs.get(scope).getUrl() + "\"" + ");\n");
			} else {
				stringBuilder.append("         " + "this.playerVM.PlayVideo(" + "\"" + videoOutputs.get(scope).getUrl() + "\"" +");\n");
			}
			stringBuilder.append(StateType.GenerateEndStateConditional(scope));
			return stringBuilder.toString();
		}
		return stringBuilder.toString();
	}
}