package org.wlcp.wlcptranspiler.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class GameDto {
	
	public String gameId;
	public Integer teamCount;
	public Integer playersPerTeam;
	public Username username;
	public Boolean visibility;
	public Integer stateIdCount;
	public Integer transitionIdCount;
	public Integer connectionIdCount;
	public Boolean dataLog;
	public List<State> states;
	public List<Connection> connections;
	public List<Transition> transitions;
	
	public Integer getTeamCount() {
		return teamCount;
	}

	public Integer getPlayersPerTeam() {
		return playersPerTeam;
	}

	public static class Username {
		public String usernameId;
	}
	
	public enum StateType {
		START_STATE,
		OUTPUT_STATE
	}
	
	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property="stateType", defaultImpl=State.class)
	@JsonSubTypes({@Type(value = StartState.class, name="START_STATE"), @Type(value = OutputState.class, name = "OUTPUT_STATE")})
	public static class State {
		public State() { }
		public String stateId;
		public StateType stateType;
		
		public String getStateId() {
			return stateId;
		}
		
	}
	
	public static class StartState extends State {
		
	}
	
	public static class OutputState extends State {
		public String description;
		public Map<String, String> displayText;
		public Map<String, PictureOutput> pictureOutputs;
		
		public Map<String, String> getDisplayText() {
			return displayText;
		}
		public Map<String, PictureOutput> getPictureOutputs() {
			return pictureOutputs;
		}
	}
	
	public static class PictureOutput {
		public String url;
		public Integer scale;
		
		public String getUrl() {
			return url;
		}
		public Integer getScale() {
			return scale;
		}
	}
	
	public static class Connection {
		public String connectionId;
		public State connectionFrom;
		public State connectionTo;
		public Transition transition;
		
		public String getConnectionId() {
			return connectionId;
		}
		public State getConnectionFrom() {
			return connectionFrom;
		}
		public State getConnectionTo() {
			return connectionTo;
		}
	}
	
	public static class Transition {
		public String transitionId;
		public Connection connection;
		public Map<String, String> activeTransitions;
		public Map<String, SingleButtonPress> singleButtonPresses;
		public Map<String, SequenceButtonPress> sequenceButtonPresses;
		public Map<String, KeyboardInput> keyboardInputs;
		
		public Connection getConnection() {
			return connection;
		}
		public Map<String, SingleButtonPress> getSingleButtonPresses() {
			return singleButtonPresses;
		}

		public Map<String, SequenceButtonPress> getSequenceButtonPresses() {
			return sequenceButtonPresses;
		}

		public Map<String, KeyboardInput> getKeyboardInputs() {
			return keyboardInputs;
		}
	}
	
	public static class SingleButtonPress {
		public Boolean button1;
		public Boolean button2;
		public Boolean button3;
		public Boolean button4;
		
		public Boolean getButton1() {
			return button1;
		}
		public Boolean getButton2() {
			return button2;
		}
		public Boolean getButton3() {
			return button3;
		}
		public Boolean getButton4() {
			return button4;
		}
	}
	
	public static class SequenceButtonPress {
		public String scope;
		public List<String> sequences;
		
		public List<String> getSequences() {
			return sequences;
		}
	}
	
	public static class KeyboardInput {
		public String scope;
		public List<String> keyboardInputs;
		
		public List<String> getKeyboardInputs() {
			return keyboardInputs;
		}
	}
	
}
