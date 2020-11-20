package org.wlcp.wlcptranspiler.transpiler.state;

import java.util.Map;

import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.SoundOutput;
import org.wlcp.wlcptranspiler.dto.GameDto.State;

public class PlaySoundStateType extends StateType implements IStateType {
	
	@Override
	public String GenerateState(String scope, State state) {
		StringBuilder stringBuilder = new StringBuilder();
		Map<String, SoundOutput> soundOutputs = ((OutputState) state).getSoundOutputs();
		if(soundOutputs.containsKey(scope)) {
			stringBuilder.append(StateType.GenerateStateConditional(scope));
			if(scope.equals("Game Wide")) {
				stringBuilder.append("      " + "this.playerVM.PlaySound(" + "\"" + soundOutputs.get(scope).getUrl() + "\"" + ");\n");
			} else {
				stringBuilder.append("         " + "this.playerVM.PlaySound(" + "\"" + soundOutputs.get(scope).getUrl() + "\"" +");\n");
			}
			stringBuilder.append(StateType.GenerateEndStateConditional(scope));
			return stringBuilder.toString();
		}
		return stringBuilder.toString();
	}
}
