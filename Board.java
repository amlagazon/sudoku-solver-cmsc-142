/*
	LAGAZON, Aaron Louie
	TOLENTINO, Caroline
	MESINA, Clouie
	SUDOKU PUZZLE SOLVER

This class contains functions for solving of the puzzle, initializing 
and generating stacks.
*/
import java.util.ArrayList;
import java.util.Stack;

class Board{
	//DECLARATION OF VARIABLES
	int size;
	int[][] board;
	boolean isX;
	boolean isY;
	boolean isXY;
	int x_solutions = 0;
	int y_solutions = 0;
	int xy_solutions = 0;
	public ArrayList<Board> solutions = new ArrayList<Board>();
	
	//CONSTRUCTOR
	public Board(int[][] board, int size){
		this.board = board;
		this.size = size;
		this.isX = false;
		this.isY = false;
		this.isXY = false;
	}
	//PRINTS THE BOARD IN THE CONSOLE
	public void print_board(){
		for(int i = 0; i < size*size; i++){
			if(i%this.size == 0) System.out.println(); //just for divider
			for(int j = 0; j < size*size; j++){
				if(j%this.size == 0) System.out.print(" "); //just for divider
				System.out.print(board[i][j]+" ");
			}
			System.out.println();
		}
	}

	//<------CHECKS IF THE NUMBER EXISTS IN THE ROW,COLUMN OR BOX IN THE PUZZLE
	public boolean isInRow(int row, int num) {
		for (int k=0; k<size*size; k++) {
			if (this.board[row][k]==num) return true;
		}
		return false;
	}

	public boolean isInColumn(int col, int num) {
		for (int k=0; k<size*size; k++) {
			if (this.board[k][col]==num) return true;
		}
		return false;
	}

	public boolean isInBox(int row, int col, int num) {
		int k = (row/size)*size;
		int l = (col/size)*size;
		for (int i=k; i<k+size;i++){
			for (int j=l; j<l+size;j++) {
				if (this.board[i][j]==num) return true;
			}
		}
		return false;
	}//------------------------------------------>

	// <-------RETURNS STACK OF CANDIDATE VALUES FOR THE CURRENT CELL
	public Stack<Integer> get_available_values(int row, int col) {
		Stack<Integer> candidates = new Stack<Integer>();
		int num;
		boolean inRow, inCol, inBox;
		int nopts = 0;

		for (num=size*size; num>0; num--) {
			inRow = isInRow(row,num);
			inCol = isInColumn(col,num);
			inBox = isInBox(row,col,num);

			if (!inRow && !inCol && !inBox) {
				candidates.push(num);
				nopts++;
			}
		}

		return candidates;
	}//----------------------------------------->

	//CHECKS IF THE NUMBER IN THE CURRENT CELL EXISTS IN THE ORIGINAL BOARD
	public boolean isConstant(int[][] board, int row, int col, int num) {
		if (board[row][col]==num) return true;
		else return false;
	}
	// SAVES A COPY OF THE ORIGINAL BOARD
	public int[][] copy_board(int[][] board, int size) {
		int[][] copy = new int[size*size][size*size];
		for (int i=0; i<size*size; i++) {
			for (int j=0; j<size*size; j++) {
				copy[i][j] = board[i][j];
			}
		}
		return copy;
	}

	//<------------------CHECKS IF THE CURRENT SOLUTION IS AN X , Y OR XY SOLUTION
	public boolean isXsolution() {
		for (int k=1; k<size*size; k++) {
			int count1 = 0;
			int count2 = 0;
			for (int l=0; l<size*size; l++) {
				if (board[l][l]==k) count1++;
				if (board[l][size*size-l-1]==k) count2++;
			}
			if (count1!=1 || count2!=1) return false;
		}
		return true;
	}

	public boolean isRegular(){
		for(int i = 0; i < size*size; i++){
			for (int k=1; k<size*size; k++) {
			int count1 = 0;

			for (int l=0; l<size*size; l++) {
				if (board[i][l]==k) count1++;
				if(!(board[i][l]>0 && board[i][l]<=size*size)) return false;
			}
			if (count1!=1) return false;
			}	
		}
		
		for(int i = 0; i < size*size; i++){
			for (int k=1; k<size*size; k++) {
			int count1 = 0;

			for (int l=0; l<size*size; l++) {
				if (board[l][i]==k) count1++;
				if(!(board[l][i]>0 && board[l][i]<=size*size)) return false;
			}
			if (count1!=1) return false;
			}	
		}
		return true;
	}
	public boolean isYsolution() {
		int m=((this.size*this.size)/2);
		for (int k=1; k<size*size; k++) {
			int count1 = 0;
			int count2 = 0;
			int l=0;
			while (l<size*size) {
				if (l<m) {
					if (board[l][l]==k) count1++;
					if (board[l][size*size-l-1]==k) count2++;
				} else {
					if (board[l][m]==k) {
						count1++;
						count2++;
					}
				}
				l++;
			}
			if (count1!=1 || count2!=1) return false;
		}
		return true;
	}

	public boolean isXYsolution(boolean isX, boolean isY) {
		return isX && isY;
	}//------------------------------------------------------>

	//PRINTS STACK FOR THE CURRENT ROW IN THE CONSOLE USED IN BACKTRACKING
	public void print_stacks(ArrayList<ArrayList<Stack<Integer>>> stacks) {
		for (int k=0; k<size*size; k++) {
			System.out.println("----------------------------------------\n\tRow "+k+"'s Possible Values\n----------------------------------------");
			for (int l=0; l<size*size; l++) {
				System.out.print("col "+l+" |");
				if (!stacks.get(k).get(l).empty()) System.out.println(stacks.get(k).get(l));
				else System.out.println();	
			}
		}
	}

	//INITIALIZATION OF STACKS FOR CANDIDATE ANSWER OF EACH CELL
	public void initialize_stacks(ArrayList<ArrayList<Stack<Integer>>> stacks) {
		for (int i=0; i<size*size; i++) {
			stacks.add(new ArrayList<Stack<Integer>>());
			for (int j=0; j<size*size; j++) {
				stacks.get(i).add(new Stack<Integer>());
			}
		}
	}
	// <--------SOLVING THE PUZZLE---
	public void solve_puzzle(int index){
		// x_solutions = y_solutions = xy_solutions = 0;
		int length = this.size * this.size;
		int num_solutions = 0;


		ArrayList<ArrayList<Stack<Integer>>> stacks = new ArrayList<ArrayList<Stack<Integer>>>();
		initialize_stacks(stacks);

		index++;

		print_board();

		// for checking of constants
		int[][] original_board = copy_board(this.board,this.size);

		// solving puzzle
		int row=0, col, start=0;
		// boolean isX=false, isY=false, isXY=false;
		while (row!=-1){  // <-- fix loop condition
			while (row<length) {
				col = start;
				if(row==-1) break;
				while (col<length) {
					if (this.board[row][col]==0) {		// if cell is empty
						//get possible values for current cell
						Stack<Integer> candidates = get_available_values(row,col);
						stacks.get(row).set(col,candidates);
						// System.out.println("stack: "+stacks.get(row).get(col));		
						if (!stacks.get(row).get(col).empty()) {
							this.board[row][col] = (int)stacks.get(row).get(col).peek();
							col++;
							if(col==length) start=0;
						}
						//NO CANDIDATE FOUND FOR THE CURRENT CELL-TRIGGERS BACKTRACKING
						else {
							if (col>0) col--;
							else {
								col = length-1;
								row--;
							}

							if (row==-1) break; //BREAK THE LOOP IF STACK TRAVERSAL IS ALREADY DONE

							//
							while (stacks.get(row).get(col).size()==1) {
								//IF THE NUMBER IS IN THE ORIGINAL BOARD,NUMBER WILL NOT BE POPPED IN THE STACK AND WILL NOT BE ERASED IN THE BOARD
								if (!isConstant(original_board,row,col,this.board[row][col])){
									this.board[row][col]=0;
								}
								
								if (col>0) col--;
								else {
									col = length-1;
									row--;
								}
								if (row==-1) break;
							}

							if (row!=-1){
								stacks.get(row).get(col).pop();
								this.board[row][col]=(int)stacks.get(row).get(col).peek();
								if (col==length-1) {
									col=0;
									row++;
								}else col++;
								start = col;

							}
						}
					} else {
						if(stacks.get(row).get(col).empty()) stacks.get(row).get(col).push(this.board[row][col]);
						col++;
						if(col==length) start=0;
					}
					if (row==-1) break;
				}
				if (row==-1) break;
				
				row++;
				

				int[][] sol = copy_board(this.board,this.size);
				Board solution = new Board(sol,this.size);
				// A solution has been found
				if (row==length) {
					num_solutions++;
					// System.out.print("\n----------------------------------------\n\t\tSolution "+num_solutions+"\n----------------------------------------\n");
					solution.isX = isXsolution();
					if (solution.isX) this.x_solutions++;

					// is length is even, no Y and XY solutions
					if (length%2!=0) {
						solution.isY = isYsolution();
						if (solution.isY) this.y_solutions++;
						solution.isXY = isXYsolution(solution.isX, solution.isY);
						if (solution.isXY) this.xy_solutions++;
					}

					// saving found solution
					solutions.add(solution);

					// finding next solution

					row--;
					col--;

					if (row==-1) break;

					// BACKTRACKING FOR FINDING THE NEXT SOLUTION OF THE PUZZLE

					while (stacks.get(row).get(col).size()==1) {
						if (!isConstant(original_board,row,col,this.board[row][col])){
							this.board[row][col]=0;
						}
						if (col>0) col--;
						else {
							col = length-1;
							row--;
						}
						if (row==-1) break;
					}

					if (row!=-1){
						stacks.get(row).get(col).pop();
						this.board[row][col]=(int)stacks.get(row).get(col).peek();
						if (col==length-1) {
							col=0;
							row++;
						}else col++;
						start = col;
					}
				}
			}
		}

	}


}