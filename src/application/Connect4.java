package application;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Connect4 {
	private static final int COLUMNS = 7;
    private static final int ROWS = 6;
	
	ArrayList<ArrayList<Integer>> Data = new ArrayList<>();
	ArrayList<ArrayList<Button>> Buttons = new ArrayList<>();
	ArrayList<ArrayList<Circle>> Circles = new ArrayList<>();
    public Connect4() {
        // Initialize Data
        for (int i1 = 0; i1 < ROWS; i1++) {
            ArrayList<Integer> row = new ArrayList<>();
            Data.add(row);
        }

        // Initialize Buttons
        for (int i2 = 0; i2 < ROWS; i2++) {
            ArrayList<Button> row = new ArrayList<>();
            Buttons.add(row);
        }

        // Initialize Circles
        for (int i3 = 0; i3 < ROWS; i3++) {
            ArrayList<Circle> row = new ArrayList<>();
            Circles.add(row);
        }
    }
    
    public int evalWindow(int[] window) {
    	int score =0;
    	int AIPiece = 1;
    	int opponentPiece = 0; 
    	
    	int AIcount =0;
    	int OPPcount =0;
    	int Emptycount =0;
    	
    	for(int number: window) {
    		if(number == AIPiece) {
    			++AIcount;
    		}
    		else if(number == opponentPiece) {
    			++OPPcount;
    		}
    		else {
    			++Emptycount;
    		}
    	}
    	
    	if(AIcount == 4) {
    		score += 100;
    	}
    	else if(AIcount == 3 && Emptycount == 1) {
    		score += 10;
    	}
    	else if (AIcount == 2 && Emptycount == 2) {
    		score += 2;
    	}
    	
    	
    	if(OPPcount == 3 && Emptycount ==1) {
    		score -= 5;
    	}
    	else if(OPPcount == 4) {
    		score = -60;
    	}
    	
    	
    	return score;
    }
    
    public int getscore_drop_incol(ArrayList<ArrayList<Integer>> Board) {
    	int score =0;
    	
    	// check the center array
    	int AIcentercount =0;
    	for(int row =0; row < ROWS; ++row) {
    		if(Board.get(row).get(3) == 1) {
    			++AIcentercount;
    		}
    	}
    	score += 4*AIcentercount;
    	// Check horizontal locations
        for (int col = 0; col < COLUMNS - 3; ++col) {
            for (int row = 0; row < ROWS; ++row) {
            	int[] window = {Board.get(row).get(col),Board.get(row).get(col + 1), Board.get(row).get(col + 2),Board.get(row).get(col + 3)};
                score += evalWindow(window);
            }
        }

        // Check vertical locations
        for (int col = 0; col < COLUMNS; ++col) {
            for (int row = 0; row < ROWS - 3; ++row) {
            	
            	
            	int[] window = {Board.get(row).get(col),Board.get(row + 1).get(col), Board.get(row + 2).get(col),Board.get(row+3).get(col)};
//            	if(col ==1 && row == 2) {
//            		for(int number:window) {
//            			System.out.print(number);
//            		}
//            		System.out.print(" ");
//            	}
            	score += evalWindow(window);
                
            }
        }

        // Check negative diagonals
        for (int col = 0; col < COLUMNS - 3; ++col) {
            for (int row = 0; row < ROWS - 3; ++row) {
            	
            	int[] window = {Board.get(row).get(col),Board.get(row + 1).get(col +1), Board.get(row + 2).get(col +2),Board.get(row+3).get(col +3)};
            	score += evalWindow(window);
            	
            	
                
            }
        }

        // Check positive diagonals
        for (int col = 0; col < COLUMNS - 3; ++col) {
            for (int row = 3; row < ROWS; ++row) {
            	int[] window = {Board.get(row).get(col),Board.get(row - 1).get(col +1), Board.get(row - 2).get(col +2),Board.get(row-3).get(col +3)};
            	score += evalWindow(window);
            }
        }
        
        System.out.println(score);
        System.out.println("");
        return score;
    }
    
    public int[] getbestmove(ArrayList<ArrayList<Integer>> Board) {
    	int bestScore = -100000;
    	int currScore =0;
    	int bestCol =0;
    	for(int col =0; col < COLUMNS; ++col) {
    		ArrayList<ArrayList<Integer>> newBoard = boardCopy(Board);
    		//dropping in the chip
    		for(int row = 5; row >=0; --row) {
    			if(newBoard.get(row).get(col) == 2) {
    				newBoard.get(row).set(col, 1); // only AI piece for now    				
    				
    				currScore = getscore_drop_incol(newBoard);
    				
    	    		break;
    			}
    		}
    		if(currScore > bestScore) {
    			bestScore = currScore;
    			bestCol = col;
    		}
    		
    	}
    	
    	return new int[]{bestCol, bestScore};
    	
    }
    
    public int[] getworstmove(ArrayList<ArrayList<Integer>> Board) {
    	int worstScore =100000;
    	int currScore =0;
    	int worstCol =0;
    	for(int col =0; col < COLUMNS; ++col) {
    		ArrayList<ArrayList<Integer>> newBoard = boardCopy(Board);
    		//dropping in the chip
    		for(int row = 5; row >=0; --row) {
    			if(newBoard.get(row).get(col) == 2) {
    				newBoard.get(row).set(col, 0); // OPP piece			
    				
    				currScore = getscore_drop_incol(newBoard);
    				
    	    		break;
    			}
    		}
    		if(currScore < worstScore) {
    			worstScore = currScore;
    			worstCol = col;
    		}
    		
    	}
    	
    	return new int[]{worstCol, worstScore};
    	
    }
    
    public int[] minimax(ArrayList<ArrayList<Integer>> Board, int Depth, int[]turn) {
    	
    	if(Depth == 0 || checkWinner(Board) != 2) {
    		
    		if(turn[0] == 1) {//AI turn 
    			
    			return getbestmove(Board);
    		}
    		else {
    			return getworstmove(Board);
    		}
	    	
    	}
    	else if(turn[0] == 1) {
    		
    		int bestScore = -100000;
    		int bestCol =0;	
    		turn[0] = 0;
    		//trying each different state
    		for(int col =0; col < COLUMNS; ++col) {
    			turn[0] = 0;
    			if(colFull(Board, col)) {
    				continue;
    			}
    			ArrayList<ArrayList<Integer>> newBoard = boardCopy(Board);
    			
    			// dropping in the chip
    			for(int row = 5; row >=0; --row) {
        			if(newBoard.get(row).get(col) == 2) {
        				newBoard.get(row).set(col, 1); // AI piece
        	    		break;
        			}
        		}
    			if(checkWinner(newBoard) ==1) {
    				return new int[] {col, 10000};
    			}
    			
    			int curr_state_score = minimax(newBoard, Depth-1, turn)[1];
    			if(curr_state_score > bestScore) {
    				bestScore = curr_state_score;
    				bestCol = col;
    			}
    			
    		}
    		return new int[]{bestCol, bestScore};
    	}
    	else {
    		int worstScore = 100000;
    		int worstCol =0;	
    		//trying each different state
    		for(int col =0; col < COLUMNS; ++col) {
    			turn[0] = 1;
    			if(colFull(Board, col)) {
    				continue;
    			}
    			ArrayList<ArrayList<Integer>> newBoard = boardCopy(Board);
    			// dropping in the chip
    			for(int row = 5; row >=0; --row) {
        			if(newBoard.get(row).get(col) == 2) {
        				newBoard.get(row).set(col, 0); // OPP piece
        	    		break;
        			}
        		}
    			if(checkWinner(newBoard) ==0) {
    				return new int[] {col, -10000};
    			}
    			
    			int curr_state_score = minimax(newBoard, Depth-1, turn)[1];
    			if(curr_state_score < worstScore) {
    				worstScore = curr_state_score;
    				worstCol = col;
    			}
    			
    		}
    		return new int[]{worstCol, worstScore};
    	}
    }
    
    
    
    public boolean colFull(ArrayList<ArrayList<Integer>> Board, int col) {
    	return Board.get(0).get(col) != 2;
    	
    }
    
    public int checkWinner(ArrayList<ArrayList<Integer>> Board) {
    	// Check horizontal locations
        for (int col = 0; col < COLUMNS - 3; ++col) {
            for (int row = 0; row < ROWS; ++row) {
                if (Board.get(row).get(col) != 2 &&
                    Board.get(row).get(col) == Board.get(row).get(col + 1) &&
                    Board.get(row).get(col + 1) == Board.get(row).get(col + 2) &&
                    Board.get(row).get(col + 2) == Board.get(row).get(col + 3)) {
                    return Board.get(row).get(col);
                }
            }
        }

        // Check vertical locations
        for (int col = 0; col < COLUMNS; ++col) {
            for (int row = 0; row < ROWS - 3; ++row) {
                if (Board.get(row).get(col) != 2 &&
                    Board.get(row).get(col) == Board.get(row + 1).get(col) &&
                    Board.get(row + 1).get(col) == Board.get(row + 2).get(col) &&
                    Board.get(row + 2).get(col) == Board.get(row + 3).get(col)) {
                    return Board.get(row).get(col);
                }
            }
        }

        // Check negative diagonals
        for (int col = 0; col < COLUMNS - 3; ++col) {
            for (int row = 0; row < ROWS - 3; ++row) {
                if (Board.get(row).get(col) != 2 &&
                    Board.get(row).get(col) == Board.get(row + 1).get(col + 1) &&
                    Board.get(row + 1).get(col + 1) == Board.get(row + 2).get(col + 2) &&
                    Board.get(row + 2).get(col + 2) == Board.get(row + 3).get(col + 3)) {
                    return Board.get(row).get(col);
                }
            }
        }

        // Check positive diagonals
        for (int col = 0; col < COLUMNS - 3; ++col) {
            for (int row = 3; row < ROWS; ++row) {
                if (Board.get(row).get(col) != 2 &&
                    Board.get(row).get(col) == Board.get(row - 1).get(col + 1) &&
                    Board.get(row - 1).get(col + 1) == Board.get(row - 2).get(col + 2) &&
                    Board.get(row - 2).get(col + 2) == Board.get(row - 3).get(col + 3)) {
                    return Board.get(row).get(col);
                }
            }
        }

        return 2;
    }
    
    public ArrayList<ArrayList<Integer>> boardCopy(ArrayList<ArrayList<Integer>> Board){
    	ArrayList<ArrayList<Integer>> dataCopy = new ArrayList<>();
    	for(int rows =0; rows < ROWS; ++rows) {
    		ArrayList<Integer> row = new ArrayList<>();
    		dataCopy.add(row);
    		for(int cols =0; cols < COLUMNS; ++cols) {
    			dataCopy.get(rows).add(Board.get(rows).get(cols));
    		}
    	}
    	return dataCopy;
    	
    }
    
    public void saveGameToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (ArrayList<Integer> row : Data) {
                for (int value : row) {
                    writer.write(value + " ");
                }
                writer.newLine();  // Move to the next line after each row
            }
            System.out.println("Game saved to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGameFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
        	
            String line;
            int row = 0;

            while ((line = reader.readLine()) != null && row < ROWS) {
            	
            	
                StringTokenizer tokenizer = new StringTokenizer(line, " ");
                int col = 0;

                while (tokenizer.hasMoreTokens() && col < COLUMNS) {
                    int value = Integer.parseInt(tokenizer.nextToken());
                    Data.get(row).set(col, value);
                    col++;
                }

                row++;
            }
            
            

            System.out.println("Game loaded from file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void printBoard(ArrayList<ArrayList<Integer>> Board) {
    	for(int row =0; row < ROWS; ++row) {
    		for(int col =0; col < COLUMNS; ++col) {
    			System.out.print(Board.get(row).get(col));
    		}
    		System.out.println(" ");
    	}
    }
}
