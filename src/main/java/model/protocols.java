package model;

public class protocols {
    public static final String EXIT = "EXIT";
    public static final String REFILL_HAND = "REFILL_HAND";
    public static final String GET_HAND = "GET_HAND";
    public static final String CONFIRM = "CONFIRM" ;

    public static final String CHALLENGE =  "CHALLENGE";
    public static final String QUERY = "QUERY";
    public static final String ADD_WORD = "ADD_WORD";
    public static final String GET_BOARD = "GET_BOARD";
    public static final String GET_SCORE = "GET_SCORE";
    public static final String START_GAME = "START_GAME";
    public static final String GET_TURN =  "GET_TURN";
    public static final String END_GAME =  "END_GAME";

    // protocols that playerHandler sends to the model
    public static final String BOARD_CHANGED = "BOARD_CHANGED" ;
    public static final String HAND_CHANGED = "HAND_CHANGED" ;
    public static final String NEW_GAME = "NEW_GAME" ;
    public static final String GET_CURRENT_PLAYER =  "GET_CURRENT_PLAYER";
    public static final String SERVER_SEND_MSG =  "SERVER_SEND_MSG";
    public static final String PLACE_WORD = "PLACE_WORD";
    public static final String PASS = "PASS";
    public static final String GET_LAST_SCORE = "GET_LAST_SCORE";
    public static final String UPDATE_SCORE =  "UPDATE_SCORE";
    public static final String UPDATE_TURN =  "UPDATE_TURN";
    public static final String NEW_PLAYER =  "NEW_PLAYER";
    public static final String GET_BONUS = "GET_BONUS" ;
    public static final String SAVE_GAME = "SAVE_GAME" ;
    public static final String LOAD_GAME = "LOAD_GAME" ;
}
