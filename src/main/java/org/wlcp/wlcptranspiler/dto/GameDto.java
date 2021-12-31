package org.wlcp.wlcptranspiler.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "gameId")
public class GameDto {
	public GameDto() { }
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
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "stateId")
	public static class State {
		public State() { }
		public String stateId;
		public StateType stateType;
		
		public String getStateId() {
			return stateId;
		}
		
	}
	
	public static class StartState extends State {
		public StartState() { }
	}
	
	public static class OutputState extends State {
		public OutputState() { }
		public String description;
		public Map<String, String> displayText;
		public Map<String, PictureOutput> pictureOutputs;
		public Map<String, SoundOutput> soundOutputs;
		public Map<String, VideoOutput> videoOutputs;
		
		public Map<String, String> getDisplayText() {
			return displayText;
		}
		public Map<String, PictureOutput> getPictureOutputs() {
			return pictureOutputs;
		}
		public Map<String, SoundOutput> getSoundOutputs() {
			return soundOutputs;
		}
		public Map<String, VideoOutput> getVideoOutputs() {
			return videoOutputs;
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
	
	public static class SoundOutput {
		public String url;
		
		public String getUrl() {
			return url;
		}
	}
	
	public static class VideoOutput {
		public String url;
		
		public String getUrl() {
			return url;
		}
	}
	
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "connectionId")
	public static class Connection {
		public Connection() { }
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
	
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "transitionId")
	public static class Transition {
		public String transitionId;
		public Connection connection;
		public Map<String, String> activeTransitions;
		public Map<String, SingleButtonPress> singleButtonPresses;
		public Map<String, SequenceButtonPress> sequenceButtonPresses;
		public Map<String, KeyboardInput> keyboardInputs;
		public Map<String, TimerDuration> timerDurations;
		public Map<String, Randoms> randoms;
		
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
		public Map<String, TimerDuration> getTimerDurations() {
			return timerDurations;
		}
		public Map<String, Randoms> getRandoms() {
			return randoms;
		}
		
	}
	
	public static class SingleButtonPress {
		public Boolean button1;
		public String button1Label;
		public Boolean button2;
		public String button2Label;
		public Boolean button3;
		public String button3Label;
		public Boolean button4;
		public String button4Label;
		
		public Boolean getButton1() {
			return button1;
		}
		public void setButton1(Boolean button1) {
			this.button1 = button1;
		}
		public String getButton1Label() {
			return button1Label;
		}
		public void setButton1Label(String button1Label) {
			this.button1Label = button1Label;
		}
		public Boolean getButton2() {
			return button2;
		}
		public void setButton2(Boolean button2) {
			this.button2 = button2;
		}
		public String getButton2Label() {
			return button2Label;
		}
		public void setButton2Label(String button2Label) {
			this.button2Label = button2Label;
		}
		public Boolean getButton3() {
			return button3;
		}
		public void setButton3(Boolean button3) {
			this.button3 = button3;
		}
		public String getButton3Label() {
			return button3Label;
		}
		public void setButton3Label(String button3Label) {
			this.button3Label = button3Label;
		}
		public Boolean getButton4() {
			return button4;
		}
		public void setButton4(Boolean button4) {
			this.button4 = button4;
		}
		public String getButton4Label() {
			return button4Label;
		}
		public void setButton4Label(String button4Label) {
			this.button4Label = button4Label;
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
	
	public static class TimerDuration {
		public Integer duration;
		
		public Integer getDuration() {
			return duration;
		}
	}
	
	public static class Randoms {
		public boolean randomEnabled;
		
		public boolean getRandomEnabled() {
			return randomEnabled;
		}
	}
	
}
