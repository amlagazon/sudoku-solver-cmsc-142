/*
	LAGAZON, Aaron Louie
	TOLENTINO, Caroline
	MESINA, Clouie
	SUDOKU PUZZLE SOLVER
*/

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFileChooser;
import java.io.File;
import javax.swing.border.Border;

class Sudoku {
	//DECLARATIONS OF VARIABLES USED FOR THE INTERFACE
	int no_puzzles = 0;			
	ArrayList<Board> puzzles = new ArrayList<Board>();
	ArrayList<Board> solution_per_puzzle = new ArrayList<Board>();
	ArrayList<Board> original = new ArrayList<Board>();
	JLabel no_puzzles_label;
	JLabel what_puzzle_label;
	int index_puzzle =0;
	int index_solved = 0;
	JPanel center_panel = new JPanel();
	JPanel solved = new JPanel();
	JPanel prev_sol_panel = new JPanel();
	JButton prev_sol = new JButton("<");
	JPanel number_sol_panel = new JPanel();
	JLabel current_index = new JLabel();
	JPanel next_sol_panel = new JPanel();
	JButton next_sol = new JButton(">");
	JPanel south_boarder = new JPanel();
	JFrame frame = new JFrame("Sudoku Solver");
	JPanel canvas = new JPanel();
	JPanel north_panel = new JPanel();
	JPanel south_panel = new JPanel();
	JPanel east_panel = new JPanel();
	JPanel west_panel = new JPanel();
	JPanel open_file_panel = new JPanel();
	JButton open_file = new JButton("Load file");
	JPanel no_puzzles_panel = new JPanel();
	JLabel label1 = new JLabel("Puzzles: ");
	JPanel what_puzzle_panel = new JPanel();
	JLabel what_puzzle = new JLabel("/");
	JButton solve = new JButton("SOLVE");
	JButton next = new JButton(">");
	JButton prev = new JButton("<");
	JLabel type = new JLabel("Type: ");
	JLabel what_type = new JLabel("?");
	JLabel current_page = new JLabel("0");
	JLabel total_pages = new JLabel("/0");
	JLabel x_label = new JLabel("X:");
	JLabel x_counter = new JLabel("0 ");
	JLabel y_label = new JLabel("Y:");
	JLabel y_counter = new JLabel("0 ");
	JLabel xy_label = new JLabel("XY:");
	JLabel xy_counter = new JLabel("0 ");
	JButton check = new JButton("Check");
	JButton reset = new JButton("Reset");
	int player_x;
	int player_y;
	Board play_board;
	String string_input;

	public Sudoku() {
		set_components();


		//--------------------ACTION LISTENERS------------------------------


		//BUTTON TO THE NEXT PUZZLE TO BE SOLVED
		next.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(index_puzzle+1 < puzzles.size()){
					play_board = new Board(puzzles.get(index_puzzle+1).copy_board(puzzles.get(index_puzzle+1).board,puzzles.get(index_puzzle+1).size),puzzles.get(index_puzzle+1).size);
					set_what_puzzle(index_puzzle+1);
					if( puzzles.size() > 0&&puzzles.get(index_puzzle+1).solutions.size()>0){ //IF THE PUZZLE ALREADY HAS A SOLUTION
						index_puzzle++;
						center_panel.removeAll();
						center_panel.add(draw_puzzle(puzzles.get(index_puzzle).solutions.get(0)));
						Board instance = puzzles.get(index_puzzle).solutions.get(0);
						update_type_label(instance);
						update_counters(index_puzzle);
						center_panel.repaint();
						canvas.add(center_panel);
						current_page.setText("1");
						total_pages.setText("/"+String.valueOf(puzzles.get(index_puzzle).solutions.size()));

					}else{ //THEN SOLVE FOR THE SUDOKU 
						index_solved = 0;
						solution_per_puzzle = new ArrayList<Board>();
						center_panel.removeAll();
						center_panel.add(draw_puzzle(puzzles.get(++index_puzzle)));
						center_panel.revalidate();
						center_panel.repaint();
						canvas.add(center_panel);
						reset_labels();
						reset_counters();

					}
					
				}
			}
		});

		//BUTTON FOR THE PREVIOUS PUZZLE
		prev.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(!(index_puzzle-1 < 0)){
					play_board = new Board(puzzles.get(index_puzzle-1).copy_board(puzzles.get(index_puzzle-1).board,puzzles.get(index_puzzle-1).size),puzzles.get(index_puzzle-1).size);
					set_what_puzzle(index_puzzle-1);
					if( puzzles.size() > 0&&puzzles.get(index_puzzle-1).solutions.size() > 0){
						index_puzzle--;
						center_panel.removeAll();
						center_panel.add(draw_puzzle(puzzles.get(index_puzzle).solutions.get(0)));
						Board instance = puzzles.get(index_puzzle).solutions.get(0);
						update_type_label(instance);
						update_counters(index_puzzle);
						center_panel.repaint();
						canvas.add(center_panel);
						current_page.setText("1");
						total_pages.setText("/"+String.valueOf(puzzles.get(index_puzzle).solutions.size()));
					}else{
						index_solved = 0;
						solution_per_puzzle = new ArrayList<Board>();
						center_panel.removeAll();
						center_panel.add(draw_puzzle(puzzles.get(--index_puzzle)));
						center_panel.revalidate();
						center_panel.repaint();
						canvas.add(center_panel);
						reset_labels();
						reset_counters();
					}
					
				}
			}
		});
		
		//SOLVES THE PUZZLE
		solve.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
					
					if(puzzles.size() > 0) {
						if(puzzles.get(index_puzzle).solutions.size()==0)solve_puzzles();
						if(puzzles.get(index_puzzle).solutions.size() > 0){ //IF IT HAS SOLUTIONS
							center_panel.removeAll();
							center_panel.add(draw_puzzle(puzzles.get(index_puzzle).solutions.get(0)));
							Board instance = puzzles.get(index_puzzle).solutions.get(0);
							update_type_label(instance);
							update_counters(index_puzzle);
							center_panel.repaint();
							canvas.add(center_panel);
							current_page.setText("1");
							total_pages.setText("/"+String.valueOf(puzzles.get(index_puzzle).solutions.size()));
						}else{
							//OTHERWISE ERROR MESSAGE
							JOptionPane.showMessageDialog(null, 
	                              "No solutions found", 
	                              "Error", 
	                              JOptionPane.WARNING_MESSAGE);
							}
						}
						else{
							JOptionPane.showMessageDialog(null, 
	                              "Please load a file", 
	                              "Error", 
	                              JOptionPane.WARNING_MESSAGE);
						}
					
			}
		});


		//LOADS THE INPUT FILES
		open_file.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("."));
				int result = fileChooser.showOpenDialog(open_file);
				if(result == JFileChooser.APPROVE_OPTION){
					index_puzzle = 0;
					center_panel.removeAll();
					File file = fileChooser.getSelectedFile();
					if(file!= null) read_file(file.getAbsolutePath());
					set_no_puzzles(no_puzzles);
					set_what_puzzle(index_puzzle);
					// play_board = puzzles.get(0);
					play_board = new Board(puzzles.get(0).copy_board(puzzles.get(0).board,puzzles.get(0).size),puzzles.get(0).size);
					center_panel.add(draw_puzzle(puzzles.get(index_puzzle)));
					canvas.add(center_panel,BorderLayout.CENTER);
					south_boarder.setFocusable(true);
					center_panel.revalidate();
					center_panel.repaint();
					reset_labels();
					reset_counters();
				}
				
			}
		});

		//BUTTON TO TRAVERSE ALL SOLUTIONS
		prev_sol.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(!(index_solved-1<0) && puzzles.size() > 0 && puzzles.get(index_puzzle).solutions.size() > 0){
					center_panel.removeAll();
					center_panel.add(draw_puzzle(puzzles.get(index_puzzle).solutions.get(--index_solved)));
					Board instance = puzzles.get(index_puzzle).solutions.get(index_solved);
					update_type_label(instance);
					center_panel.revalidate();
					center_panel.repaint();
					canvas.add(center_panel,BorderLayout.CENTER);
					current_page.setText(String.valueOf(index_solved+1));
				}
				
			}
		});

		next_sol.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if( puzzles.size() > 0&&index_solved+1<puzzles.get(index_puzzle).solutions.size() && puzzles.get(index_puzzle).solutions.size()>0){
					//REPAINT...
					center_panel.removeAll();
					center_panel.add(draw_puzzle(puzzles.get(index_puzzle).solutions.get(++index_solved)));
					Board instance = puzzles.get(index_puzzle).solutions.get(index_solved);
					update_type_label(instance);
					center_panel.revalidate();
					center_panel.repaint();
					canvas.add(center_panel,BorderLayout.CENTER);
					current_page.setText(String.valueOf(index_solved+1));
				}
				
			}
		});

		check.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(play_board != null){
					if(puzzles.get(index_puzzle).solutions.size() > 0){
						JOptionPane.showMessageDialog(null, 
	                      "The current puzzle is already solved!", 
	                      "Oops", 
	                      JOptionPane.WARNING_MESSAGE);
					}else{
						System.out.println("Checking...");
						if(play_board.isRegular()) {
							JOptionPane.showMessageDialog(null, 
	                      "Your answers are correct. Good job!", 
	                      "Nice", 
	                      JOptionPane.PLAIN_MESSAGE);
							System.out.println("Correct!");
						}
						else{
							JOptionPane.showMessageDialog(null, 
	                      "Your answers are incorrect. Try again!", 
	                      "Error", 
	                      JOptionPane.WARNING_MESSAGE);
						}
					}
					

				}else {
					JOptionPane.showMessageDialog(null, 
	                  "Please load a file!", 
	                  "Error", 
	                  JOptionPane.WARNING_MESSAGE);
				}
				


			}
		});
		reset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(puzzles.size()>0){
					if(puzzles.get(index_puzzle).solutions.size() > 0){
						JOptionPane.showMessageDialog(null, 
	                  "The current puzzle is already solved!", 
	                  "Error", 
	                  JOptionPane.WARNING_MESSAGE);

					}else{
						System.out.println("Reset board");
						play_board = new Board(puzzles.get(index_puzzle).copy_board(puzzles.get(index_puzzle).board,puzzles.get(index_puzzle).size),puzzles.get(index_puzzle).size);
						center_panel.removeAll();
						center_panel.add(draw_puzzle(puzzles.get(index_puzzle)));
						center_panel.revalidate();
						center_panel.repaint();
					}
					
				}else {
					JOptionPane.showMessageDialog(null, 
	                  "Please load a file!", 
	                  "Error", 
	                  JOptionPane.WARNING_MESSAGE);
				}
					

			}
		});

		canvas.add(center_panel,BorderLayout.CENTER);
		canvas.add(north_panel,BorderLayout.NORTH);
		canvas.add(south_panel,BorderLayout.SOUTH);
		canvas.add(east_panel,BorderLayout.EAST);
		canvas.add(west_panel,BorderLayout.WEST);

		frame.setFocusable(true);
		frame.add(canvas);
		frame.setSize(600,630);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
	}

	//RESET LABELS
	public void reset_labels(){
		what_type.setText("?");
		total_pages.setText("/0");
		current_page.setText("0");
	}

	//UPDATE X, Y, XY SOLUTIONS COUNTER
	public void update_counters(int index){
		x_counter.setText(String.valueOf(puzzles.get(index).x_solutions));
		y_counter.setText(String.valueOf(puzzles.get(index).y_solutions));
		xy_counter.setText(String.valueOf(puzzles.get(index).xy_solutions));

	}

	//RESET X, Y, XY SOLUTIONS COUNTER
	public void reset_counters(){
		x_counter.setText("0");
		y_counter.setText("0");
		xy_counter.setText("0");

	}


	public void update_type_label(Board instance){
		if(instance.isX && instance.isY) what_type.setText("XY");
		else if(instance.isX) what_type.setText("X");
		else if(instance.isY) what_type.setText("Y");
		else what_type.setText("Regular");
		center_panel.revalidate();
	}

	//INTERFACE COMPONENTS
	public void set_components(){
		canvas.setLayout(new BorderLayout());
		south_panel.setBackground(new Color(45,45,45));
		south_panel.setLayout(new BorderLayout());
		center_panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		center_panel.setLayout(new BorderLayout());

		east_panel.setBorder(BorderFactory.createEmptyBorder(200,0,0,0));
		west_panel.setBorder(BorderFactory.createEmptyBorder(200,0,0,0));


		open_file_panel.setBackground(new Color(45,45,45));
		open_file.setForeground(Color.WHITE);
		open_file.setBorderPainted(false);
		open_file.setFocusPainted(false);
		open_file.setBackground(new Color(10,10,10));
		open_file_panel.add(open_file);
		
		
		

		north_panel.add(open_file_panel);


		no_puzzles_panel.setBackground(new Color(45,45,45));
		no_puzzles_label = new JLabel(String.valueOf(no_puzzles));
		label1.setForeground(Color.CYAN);
		this.no_puzzles_label.setForeground(Color.WHITE);
		no_puzzles_panel.add(label1);

		no_puzzles_panel.add(what_puzzle_panel);
		no_puzzles_panel.add(this.no_puzzles_label);
		// no_puzzles_panel.setBorder(BorderFactory.createEmptyBorder(0,0,0,20));


		what_puzzle_panel.setBackground(new Color(45,45,45));
		what_puzzle.setForeground(Color.CYAN);
		what_puzzle_label = new JLabel("0");
		what_puzzle_label.setForeground(Color.WHITE);
		what_puzzle_panel.add(what_puzzle_label);
		what_puzzle_panel.add(what_puzzle);

		north_panel.add(no_puzzles_panel);
		north_panel.setBackground(new Color(45,45,45));

		check.setForeground(Color.WHITE);
		check.setBorderPainted(false);
		check.setFocusPainted(false);
		check.setBackground(new Color(10,10,10));
		north_panel.add(check);

		reset.setForeground(Color.WHITE);
		reset.setBorderPainted(false);
		reset.setFocusPainted(false);
		reset.setBackground(new Color(10,10,10));
		north_panel.add(reset);

		solve.setForeground(Color.WHITE);
		solve.setBorderPainted(false);
		solve.setFocusPainted(false);
		solve.setBackground(new Color(10,10,10));

		next.setForeground(Color.WHITE);
		next.setBorderPainted(false);
		next.setFocusPainted(false);
		next.setBackground(new Color(10,10,10));

		east_panel.add(next);

		prev.setForeground(Color.WHITE);
		prev.setBorderPainted(false);
		prev.setFocusPainted(false);
		prev.setBackground(new Color(10,10,10));
		prev.setFont(new Font("Sans Serif", Font.PLAIN, 10));

		west_panel.add(prev);

		prev_sol = new JButton("<");
		prev_sol.setFont(new Font("Sans Serif", Font.PLAIN, 10));
		prev_sol.setBorderPainted(false);
		prev_sol.setFocusPainted(false);
		prev_sol.setBackground(new Color(124,124,124));

		prev_sol_panel.add(prev_sol);

		next_sol = new JButton(">");
		next_sol.setFont(new Font("Sans Serif", Font.PLAIN, 10));
		next_sol.setBorderPainted(false);
		next_sol.setFocusPainted(false);
		next_sol.setBackground(new Color(124,124,124));



		next_sol.setFont(new Font("Sans Serif", Font.PLAIN, 10));
		next_sol_panel.add(next_sol);

		JPanel page_panel = new JPanel();
		page_panel.setBackground(new Color(45,45,45));
		page_panel.add(current_page);
		page_panel.add(total_pages);

		JPanel counters = new JPanel();
		counters.setBackground(new Color(45,45,45));
		counters.add(x_label);
		counters.add(x_counter);
		counters.add(y_label);
		counters.add(y_counter);
		counters.add(xy_label);
		counters.add(xy_counter);

		x_label.setForeground(Color.CYAN);
		y_label.setForeground(Color.CYAN);
		xy_label.setForeground(Color.CYAN);
		x_counter.setForeground(Color.WHITE);
		y_counter.setForeground(Color.WHITE);
		xy_counter.setForeground(Color.WHITE);
		current_page.setForeground(Color.WHITE);
		total_pages.setForeground(Color.WHITE);
		

		JPanel southest = new JPanel();
		JPanel south_north = new JPanel();
		southest.add(prev_sol);
		southest.add(solve);
		southest.add(next_sol);

		type.setForeground(Color.CYAN);
		what_type.setForeground(Color.WHITE);

		south_north.add(counters);
		south_north.add(page_panel);
		south_north.add(type);
		south_north.add(what_type);

		south_north.setBackground(new Color(45,45,45));
		southest.setBackground(new Color(45,45,45));

		south_panel.add(southest, BorderLayout.SOUTH);
		south_panel.add(south_north, BorderLayout.NORTH);
	}

	//READ INPUT FILE
	public void read_file(String file){
		puzzles = new ArrayList<Board>();
		original = new ArrayList<Board>();
		String line;
		try{
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			line = bufferedReader.readLine();
			this.no_puzzles = Integer.parseInt(line);

			for(int i = 0; i < this.no_puzzles; i++){
				line = bufferedReader.readLine();
				int size = Integer.parseInt(line);
				int[][] single_board = new int[size*size][size*size];
				for(int j = 0; j < size*size; j++){
					line = bufferedReader.readLine();
					String[] array = line.split("\\s");
					int[] int_array = new int[array.length];
					for(int k = 0; k < array.length; k++){
						int_array[k] = Integer.parseInt(array[k]);
					}
					single_board[j] = int_array;

				}
				Board board_instance = new Board(single_board,size);
				this.puzzles.add(board_instance);
				this.original.add(board_instance); 
			}
			bufferedReader.close();
			fileReader.close();
		}catch(Exception e){}

		
	}


	//SOLVE SPECIFIC PUZZLE FROM THE ARRAYLIST
	public void solve_puzzles(){
		puzzles.get(index_puzzle).solve_puzzle(index_puzzle);
	}

	//DRAW THE BOARD
	public JPanel draw_puzzle(Board to_draw){
		Board original_board = original.get(index_puzzle);
		int size = to_draw.size;
		to_draw.print_board();
		JPanel canvas = new JPanel();
		JPanel board = new JPanel();
		GridLayout grid = new GridLayout(size*size,size*size);
		grid.setVgap(2);
		grid.setHgap(2);
		board.setLayout(grid);
		for(int i = 0; i < size*size; i++){
			for(int j = 0; j < size*size; j++){
				Color color;
				if((i/size)%2 == 0){
					if((j/size)%2 != 0){
						color = new Color(45,45,45);
					}else color = new Color(20,20,20);
				}else{
					 if((j/size)%2 == 0){
						color = new Color(45,45,45);
					}else color = new Color(20,20,20);
				}
				if(original_board.board[i][j] != 0){
					JPanel cell = new JPanel();
				    cell.setLayout(new GridBagLayout());
				    cell.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
					JLabel label = new JLabel(String.valueOf(to_draw.board[i][j]));
					label.setForeground(Color.CYAN);
					label.setHorizontalAlignment(SwingConstants.CENTER);
					label.setFont(new Font("Serif", Font.PLAIN,80/size));
					cell.setBorder(null);
					cell.setPreferredSize(new Dimension((430/size)/size,(430/size)/size));
					cell.setBackground(color);
					cell.add(label);
					board.add(cell);
				}else if(to_draw.board[i][j] == 0){
					JPanel cell = new JPanel();
					JButton button = new JButton();
					button.setName(i+","+j); //SETS THE NAME OF EACH BUTTON
					button.setPreferredSize(new Dimension(190,190));
					button.setBorderPainted(false);
					button.setFocusPainted(false);
					button.setBackground(color);
					button.setMargin(new Insets(0, 0, 0, 0));
					button.setFont(new Font("Serif", Font.PLAIN,80/size));
					button.setForeground(Color.WHITE);

					// label.setHorizontalAlignment(SwingConstants.CENTER);
					button.setPreferredSize(new Dimension((430/size)/size,(430/size)/size));
					cell.setPreferredSize(new Dimension((430/size)/size,(430/size)/size));
					cell.setBackground(color);
					board.add(button);
					button.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							string_input ="";
							String name = ((JComponent)e.getSource()).getName();
							String[] id = name.split(",");
							System.out.println("\nPressed "+id[0]+","+id[1]);
							player_x = Integer.valueOf(id[0]);
							player_y = Integer.valueOf(id[1]);
						}
					});
					button.addKeyListener(new KeyListener(){
						public void keyPressed(KeyEvent ke){
					      char c = ke.getKeyChar();
				          if ((c >= '0') && (c <= '9')) {
				          	string_input+=c;
				          	button.setText(string_input);
				            System.out.println(string_input+" > "+player_x+", "+player_y);
				            play_board.board[player_x][player_y] =Integer.valueOf(string_input);
				            System.out.println("Playing...");
				            play_board.print_board();
				          }
				          if(ke.getKeyCode() == KeyEvent.VK_BACK_SPACE){
				          	if(string_input.length() > 0) string_input = string_input.substring(0, string_input.length() - 1);
				          	button.setText(string_input);
				            if(string_input.length()>0) play_board.board[player_x][player_y] = Integer.valueOf(string_input);
				            else play_board.board[player_x][player_y] = 0;
				            play_board.print_board();

				          }
						}
						public void keyTyped(KeyEvent ke){}
						public void keyReleased(KeyEvent ke){}

				    });

					}else{
						JPanel cell = new JPanel();
						cell.setLayout(new GridBagLayout());
						cell.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
						JLabel label = new JLabel(String.valueOf(to_draw.board[i][j]));
						label.setForeground(Color.WHITE);
						label.setHorizontalAlignment(SwingConstants.CENTER);
						label.setFont(new Font("Serif", Font.PLAIN,80/size));
						cell.setPreferredSize(new Dimension((430/size)/size,(430/size)/size));
						cell.setBackground(color);
						cell.add(label);
						board.add(cell);
					}
			}
		}
		canvas.add(board);
		return canvas;

	}
	
	public void set_no_puzzles(int n){
		no_puzzles_label.setText(String.valueOf(n));
	}
	public void set_what_puzzle(int n){
		what_puzzle_label.setText(String.valueOf(n+1));
	}
	public void print_all_boards(){
		for(Board b: puzzles){
			b.print_board();
		}
	}
	

}
