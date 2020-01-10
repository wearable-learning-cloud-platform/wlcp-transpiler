package org.wlcp.wlcptranspiler.transpiler.state;

import java.util.Map;

import org.wlcp.wlcptranspiler.dto.GameDto.OutputState;
import org.wlcp.wlcptranspiler.dto.GameDto.PictureOutput;
import org.wlcp.wlcptranspiler.dto.GameDto.State;

public class DisplayPhotoStateType extends StateType implements IStateType {
	@Override
	public String GenerateState(String scope, State state) {
		StringBuilder stringBuilder = new StringBuilder();
		Map<String, PictureOutput> pictureOutputs = ((OutputState) state).getPictureOutputs();
		if(pictureOutputs.containsKey(scope)) {
			stringBuilder.append(StateType.GenerateStateConditional(scope));
			if(scope.equals("Game Wide")) {
				stringBuilder.append("      " + "this.playerVM.DisplayPhoto(" + "\"" + pictureOutputs.get(scope).getUrl() + "\"" + "," + pictureOutputs.get(scope).getScale() + ");\n");
			} else {
				stringBuilder.append("         " + "this.playerVM.DisplayPhoto(" + "\"" + pictureOutputs.get(scope).getUrl() + "\"" + ", " + pictureOutputs.get(scope).getScale() +");\n");
			}
			stringBuilder.append(StateType.GenerateEndStateConditional(scope));
			return stringBuilder.toString();
		}
		return stringBuilder.toString();
	}

}
