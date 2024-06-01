package application;
import java.util.Scanner;
import javafx.scene.Node;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class SampleController {
	
	private static final int COLUMNS = 7;
    private static final int ROWS = 6;

    @FXML
    private AnchorPane anchorPane;
    
    Connect4 connect4board = new Connect4();
    int[] player_turn = {0};
    
    StringBuilder log = new StringBuilder();

    @FXML
    private void initialize() {
    	Platform.runLater(() -> {
            initializeBoard();
        });
    	PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));
        	pauseTransition.setOnFinished(event -> {
        		anchorPane.widthProperty().addListener(new ChangeListener<Number>() {

        			@Override
        			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
        				// TODO Auto-generated method stub
        				redrawBoard();
        				
        			}
                });
        		
        		
        });
        pauseTransition.play();
        
        
    }
    
    
    
    public void initializeBoard() {
    	double ANCHOR_WIDTH = anchorPane.getWidth();
        double ANCHOR_HEIGHT = anchorPane.getHeight() * 0.65;
        double width_radius = (ANCHOR_WIDTH/7)/2;
        double height_radius = (ANCHOR_HEIGHT/6)/2;
        
        double minRadius = Math.min(width_radius, height_radius);
        
        double anchorTop = anchorPane.getHeight()*0.25;
        double anchorLeft = (anchorPane.getWidth()-(2*minRadius*COLUMNS))/2;
        
        Button loadButton = new Button();
        loadButton.setMinSize(2*minRadius, 2*minRadius);
        loadButton.setMaxSize(2*minRadius, 2*minRadius);
        loadButton.setText("load");
        loadButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				load();
			}
        });
        
        Button saveButton = new Button();
        saveButton.setMinSize(2*minRadius, 2*minRadius);
        saveButton.setMaxSize(2*minRadius, 2*minRadius);
        saveButton.setText("save");
        saveButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				save();
			}
        });
        
        Button logButton = new Button();
        logButton.setMinSize(2*minRadius, 2*minRadius);
        logButton.setMaxSize(2*minRadius, 2*minRadius);
        logButton.setText("log");
        logButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				logFile();
			}
        });
        
        Button resetButton = new Button();
        resetButton.setMinSize(2*minRadius, 2*minRadius);
        resetButton.setMaxSize(2*minRadius, 2*minRadius);
        resetButton.setText("reset");
        resetButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				resetBoard();
			}
        });
        
        
        AnchorPane.setTopAnchor(loadButton, anchorPane.getHeight()*0.10);    // Set distance from the top edge
        AnchorPane.setLeftAnchor(loadButton, ANCHOR_WIDTH*0.5); 
        anchorPane.getChildren().add(loadButton);
        AnchorPane.setTopAnchor(saveButton, anchorPane.getHeight()*0.10);    // Set distance from the top edge
        AnchorPane.setLeftAnchor(saveButton, ANCHOR_WIDTH*0.5 + 2*minRadius);
        anchorPane.getChildren().add(saveButton);
        AnchorPane.setTopAnchor(logButton, anchorPane.getHeight()*0.10);    // Set distance from the top edge
        AnchorPane.setLeftAnchor(logButton, ANCHOR_WIDTH*0.5 + 4*minRadius);
        anchorPane.getChildren().add(logButton);
        AnchorPane.setTopAnchor(resetButton, anchorPane.getHeight()*0.10);    // Set distance from the top edge
        AnchorPane.setLeftAnchor(resetButton, ANCHOR_WIDTH*0.15);
        anchorPane.getChildren().add(resetButton);
        
        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS; row++) {
            	Button cellButton = new Button();
            	cellButton.setMinSize(1.5*minRadius, 1.5*minRadius);
            	cellButton.setMaxSize(1.5*minRadius, 1.5*minRadius);
            	
                Circle cell = new Circle(minRadius);            
                cell.setFill(null);
                cell.setStrokeWidth(1.0);
                cell.setStroke(javafx.scene.paint.Color.BLACK);
                
                
                cellButton.setGraphic(cell);
                cellListener(cellButton);

                
                AnchorPane.setTopAnchor(cellButton, anchorTop + minRadius*2*row);    // Set distance from the top edge
                AnchorPane.setLeftAnchor(cellButton, anchorLeft + minRadius*2*col); 
                anchorPane.getChildren().add(cellButton);
                connect4board.Buttons.get(row).add(cellButton);
                connect4board.Circles.get(row).add(cell);
                connect4board.Data.get(row).add(2);
            }
        }
    }
    
    
    /////////////// ////
    /// REDRAW BOARD////
    ////////////////////
    public void redrawBoard() {
    	//get the width and height of the pane  
    	ObservableList<Node> elements = anchorPane.getChildren();
    	List<Node> circlesToRemove = new ArrayList<>();

    	for (Node element : elements) {
    	    if (element instanceof Button) {
    	        circlesToRemove.add(element);
    	    }
    	}
    	// Remove buttons from the pane 
    	anchorPane.getChildren().removeAll(circlesToRemove);
    	
        double ANCHOR_WIDTH = anchorPane.getWidth();
        double ANCHOR_HEIGHT = anchorPane.getHeight() * 0.65;
        double width_radius = (ANCHOR_WIDTH/7)/2;
        double height_radius = (ANCHOR_HEIGHT/6)/2;
        
        double minRadius = Math.min(width_radius, height_radius);
        
        double anchorTop = anchorPane.getHeight()*0.25;
        double anchorLeft = (anchorPane.getWidth()-(2*minRadius*COLUMNS))/2;
        
        
        Button loadButton = new Button();
        loadButton.setMinSize(2*minRadius, 2*minRadius);
        loadButton.setMaxSize(2*minRadius, 2*minRadius);
        loadButton.setText("load");
        loadButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				load();
			}
        });
        
        Button saveButton = new Button();
        saveButton.setMinSize(2*minRadius, 2*minRadius);
        saveButton.setMaxSize(2*minRadius, 2*minRadius);
        saveButton.setText("save");
        saveButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				save();
			}
        });
        
        Button logButton = new Button();
        logButton.setMinSize(2*minRadius, 2*minRadius);
        logButton.setMaxSize(2*minRadius, 2*minRadius);
        logButton.setText("log");
        logButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				logFile();
			}
        });
        
        Button resetButton = new Button();
        resetButton.setMinSize(2*minRadius, 2*minRadius);
        resetButton.setMaxSize(2*minRadius, 2*minRadius);
        resetButton.setText("reset");
        resetButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				resetBoard();
			}
        });
        
        
        AnchorPane.setTopAnchor(loadButton, anchorPane.getHeight()*0.10);    // Set distance from the top edge
        AnchorPane.setLeftAnchor(loadButton, ANCHOR_WIDTH*0.5); 
        anchorPane.getChildren().add(loadButton);
        AnchorPane.setTopAnchor(saveButton, anchorPane.getHeight()*0.10);    // Set distance from the top edge
        AnchorPane.setLeftAnchor(saveButton, ANCHOR_WIDTH*0.5 + 2*minRadius);
        anchorPane.getChildren().add(saveButton);
        AnchorPane.setTopAnchor(logButton, anchorPane.getHeight()*0.10);    // Set distance from the top edge
        AnchorPane.setLeftAnchor(logButton, ANCHOR_WIDTH*0.5 + 4*minRadius);
        anchorPane.getChildren().add(logButton);
        AnchorPane.setTopAnchor(resetButton, anchorPane.getHeight()*0.10);    // Set distance from the top edge
        AnchorPane.setLeftAnchor(resetButton, ANCHOR_WIDTH*0.15);
        anchorPane.getChildren().add(resetButton);
        
        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS; row++) {
            	Button cellButton = new Button();
            	cellButton.setMinSize(1.5*minRadius, 1.5*minRadius);
            	cellButton.setMaxSize(1.5*minRadius, 1.5*minRadius);
            	
                Circle cell = new Circle(minRadius);
                Paint fill;
                if(connect4board.Data.get(row).get(col) == 0) {
                	fill = Color.RED;
                }
                else if(connect4board.Data.get(row).get(col) == 1) {
                	fill = Color.YELLOW;
                }
                else {
                	fill = null;
                }
                cell.setFill(fill);
                cell.setStrokeWidth(1.0);
                cell.setStroke(javafx.scene.paint.Color.BLACK);
                
                
                cellButton.setGraphic(cell);
                cellListener(cellButton);
                
                // Add the cell to the GridPane
                AnchorPane.setTopAnchor(cellButton, anchorTop + minRadius*2*row);    // Set distance from the top edge
                AnchorPane.setLeftAnchor(cellButton, anchorLeft + minRadius*2*col); 
                anchorPane.getChildren().add(cellButton);
                connect4board.Buttons.get(row).set(col, cellButton);
                connect4board.Circles.get(row).set(col, cell);
                
            }
        }
        
        
    }
    
    public void playerMove(int[] turn, int row, int col) {
    	if(turn[0] == 0) {
    		for(row = 5; row >=0; --row) {
    			if(connect4board.Data.get(row).get(col) == 2) {
    				connect4board.Data.get(row).set(col, 0);
    	    		connect4board.Circles.get(row).get(col).setFill(Color.RED);
    	    		turn[0] = 1;
    	    		break;
    			}
    		}
    		
    		int[] m_turn = new int[1];
    		m_turn[0] = turn[0];
    		int[] next_move = connect4board.minimax(connect4board.Data, 3, m_turn);
    		System.out.println(next_move[0] + " " + next_move[1]);
    		for(row = 5; row >=0; --row) {
    			if(connect4board.Data.get(row).get(next_move[0]) == 2) {
    				connect4board.Data.get(row).set(next_move[0], 1);
    	    		connect4board.Circles.get(row).get(next_move[0]).setFill(Color.YELLOW);
    	    		turn[0] = 0;
    	    		break;
    			}
    		}
    	}
  
    	for(int rows =0; rows < ROWS; ++rows) {
    		for(int cols =0; cols < COLUMNS; ++cols) {
    			log.append(String.valueOf(connect4board.Data.get(rows).get(cols)));
    		}
    		log.append("\n");
    	}
    	log.append("\n");
    	System.out.println();
    	System.out.println();
    }
    

    
    public int checkWinner() {
        return connect4board.checkWinner(connect4board.Data);
    }
    
    
    
    public void resetBoard() {
    	for(int row =0; row <ROWS; ++row) {
    		for(int col =0; col<COLUMNS; ++col) {
    			connect4board.Data.get(row).set(col, 2);
    			connect4board.Circles.get(row).get(col).setFill(null);
    		}
    	}
    }
    
    public void cellListener(Button cellButton) {
    	cellButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				Button curr_button = (Button) event.getSource();
				int possible_winner =2;
				outerloop:
				for(int row = 0; row < ROWS; ++row) {
					for(int col = 0; col < COLUMNS; ++col) {
						if(curr_button == connect4board.Buttons.get(row).get(col)) {
							playerMove(player_turn, row, col);
							possible_winner = checkWinner();
							break outerloop;
						}
					}
				}
				if(possible_winner != 2) {
					showWinnerAnnouncement(possible_winner);
					pauseBeforeReset();
					log.setLength(0);
				}
				
			}
		});
    	
    }
    private void pauseBeforeReset() {
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(5));
        pauseTransition.setOnFinished(event -> resetBoard());
        pauseTransition.play();
    }

    private void showWinnerAnnouncement(int winner) {
        WinnerAnnouncement winnerAnnouncement = new WinnerAnnouncement();
        winnerAnnouncement.showWinnerMessage(winner);
    }
    
	public void save() {
		TextInputDialog dialog = new TextInputDialog();
	    dialog.setTitle("Save Game");
	    dialog.setHeaderText(null);
	    dialog.setContentText("Enter filename:");

	    dialog.showAndWait().ifPresent(filename -> {
	    	connect4board.saveGameToFile(filename);
	        
	    });
	}
	public void load() {
	    TextInputDialog dialog = new TextInputDialog();
	    dialog.setTitle("Load Game");
	    dialog.setHeaderText(null);
	    dialog.setContentText("Enter filename:");

	    dialog.showAndWait().ifPresent(filename -> {
	        connect4board.loadGameFromFile(filename);
	        redrawBoard();
	    });
	    int player0count =0; 
	    int player1count =0;
	    for(int row=0; row < ROWS; ++row) {
	    	for(int col =0; col< COLUMNS; ++col) {
	    		if(connect4board.Data.get(row).get(col) == 0) {
	    			++player0count;
	    		}
	    		else if(connect4board.Data.get(row).get(col) == 1) {
	    			++player1count;
	    		}
	    	}
	    }
	    if(player0count == player1count) {
	    	player_turn[0] = 0;
	    }
	    else {
	    	player_turn[0] = 1;
	    }
	}
	public void logFile() {
		TextInputDialog dialog = new TextInputDialog();
	    dialog.setTitle("Log Game");
	    dialog.setHeaderText(null);
	    dialog.setContentText("Enter filename:");
	    
	    dialog.showAndWait().ifPresent(filename -> {
	    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
	    		String logcontent = log.toString();
	    		writer.write(logcontent);
	            System.out.println("Game saved to file successfully.");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    	
	        redrawBoard();
	    });
	}
	
	public void getbestmove() {
		int[] turn = new int[1];
		turn[0] = player_turn[0];
		int[] next_move = connect4board.minimax(connect4board.Data, 2, turn);
		System.out.println(next_move[0] + " " + next_move[1]);
	}
	
	
}
