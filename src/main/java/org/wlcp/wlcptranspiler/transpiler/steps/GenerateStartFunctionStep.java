package org.wlcp.wlcptranspiler.transpiler.steps;

public class GenerateStartFunctionStep implements ITranspilerStep {

	@Override
	public String PerformStep() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("   " + "start : function() {\n");
		stringBuilder.append("      " + "while(this.running) {\n");
		stringBuilder.append("         " + "if(this.state != -1) {\n");
		stringBuilder.append("            " + "this.oldState = this.state;\n");
		stringBuilder.append("            " + "this.stateMachine(this.state);\n");
		stringBuilder.append("         " + "} else {\n");
		stringBuilder.append("         " + "   this.running = false;\n");
		stringBuilder.append("         " + "}\n");
		stringBuilder.append("      " + "}\n");
		stringBuilder.append("   " + "},\n\n");
		return stringBuilder.toString();
	}

}
