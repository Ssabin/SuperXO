import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

import javax.swing.*;
public class XOPre extends JFrame {
	private static final long serialVersionUID = 1L;
	int size=10;//Board size 10X10
	int m=5;//Series Victory
	private static int gameType=1;//game type( p VS p, p VS comp )
	private static int gameDiffuculty=-1;//game diffuculty( easy, medium ,hard )
	private static final int  human =1;//1 Xplayer Human
	private static final int  computer=2;// 2 OPlayer Computer
	private static final int human2=2;// 2 OPlayer Human2
	private static int startPlayer=1;//Starts player in every game that the window will open is Human
	private int bestRow;//for computer  calculations
	private int bestCol;//for computer  calculations
	private int depth;//for alpha beta  calculations
	private int turn;//Which player needs to play now
	private static boolean flag=false;
	private Stack<Point> mainStack = new Stack<Point>();//all the steps that have been made since the start of the game
	private Stack<Point> undoStack = new Stack<Point>();//all the steps that have been Removed since the start of the game	
	private MyButton [][] gBoard;//Graphic Board
	private int [][] lBoard;  //0 free 1 Xplayer  2 OPlayer
	private int [][] scoreBoard;//Score board with grades for each cell
//Creates the window and the menu bar	
public XOPre(){
		
		JMenuBar menuBar;
		// Create & set the menu bar.
		menuBar = new JMenuBar();
		// Sets menuBar to JFrame
		setJMenuBar(menuBar);
		
		//Create a menu.
		JMenu menu = new JMenu("File");
		// set shortcut using setMnemonic method, uses the ALT mask
		menu.setMnemonic(KeyEvent.VK_F);// Load with Alt+F
		// add menu to menuBar
		menuBar.add(menu);

		/* Create some JMenuItems */
		
		JMenu menuItem;
		JMenuItem vsPl;
		JMenu vsComp;
		// create a menu item
		menuItem = new JMenu("New");
		vsPl=new JMenuItem("Player VS Player");
		vsComp=new JMenu("Player VS Computer");
		// set keyboard shortcut using setAccelerator method
		// use with Control+N
//		menuItem.setAccelerator(KeyStroke.getKeyStroke(
//				KeyEvent.VK_N, ActionEvent.CTRL_MASK));
			
		// add ac0tion listener to respond to
		// menuItem event
		vsPl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				gameType=1;
				endStartGame();
				
			}
		});
		// add to menu
		
		menu.add(menuItem);
		menuItem.add(vsPl);
		menuItem.addSeparator();
		menuItem.add(vsComp);
		JMenuItem easy=new JMenuItem("Easy");
		JMenuItem medium=new JMenuItem("Medium");
		JMenuItem hard=new JMenuItem("Hard");
		vsComp.add(easy);
		vsComp.addSeparator();
		vsComp.add(medium);
		vsComp.addSeparator();
		vsComp.add(hard);
		// add a separator
		menu.addSeparator();

		easy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				gameType=2;
				gameDiffuculty=1;
				endStartGame();
			}
		});

		medium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				gameType=2;
				gameDiffuculty=2;
				endStartGame();
			}
		});

		hard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				gameType=2;
				gameDiffuculty=3;
				endStartGame();
			}
		});
		// create another menu item
		// combines creation and adding shortcut
		/* Use Alt+F+E to exit - Windows style */
		JMenuItem menuItem1 = new JMenuItem("Exit", KeyEvent.VK_E);
		menuItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);
			}
		});
		// add to menu
		menu.add(menuItem1);
		JMenu Actions=new JMenu("Actions");
		// Create two menu items for First menu
		JMenuItem aaa=new JMenuItem("Undo");
		aaa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(gameType==2)
				Undo();
			}
		});
		JMenuItem Redo=new JMenuItem("Redo");
		Redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(gameType==2)
				Redo();
			}
		});
		JMenuItem bbb=new JMenuItem("Hint");
		bbb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(gameType==2)
					Hint(turn);
			}
		});
		bbb.addActionListener(new AL1());
		
		
		// Add to First menu
		Actions.add(aaa);
		Actions.addSeparator(); //add a separator
		Actions.add(Redo);
		Actions.addSeparator();
		Actions.add(bbb);
		//Add to Second menu
		// add Firstt and Second menus to menu bar
		menuBar.add(Actions);

		setTitle("XOPre");
		initBoard();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(300,300);
		setVisible(true);
	}
	class AL1 implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			JMenuItem item=(JMenuItem)ae.getSource();
			
		}
	}
//Creates and reboot the ScoreBoard
	public void initScoreBoard()
	{
		int minScore=1,multi=1,i,j;
		for(i=0;i<(size-i);i++)
			for(j=0;j<(size-j);j++)
			{
				scoreBoard[i][size-j-1]=(i+j+minScore+multi);
				scoreBoard[i][j]=(i+j+minScore+multi);
				scoreBoard[size-i-1][j]=(i+j+minScore+multi);
				scoreBoard[size-i-1][size-j-1]=(i+j+minScore+multi);
			}
	}
//Prints the ScoreBoard
	public void printScoreBoard()
	{
		for(int i=0;i<size;i++)
		{
			for(int j=0;j<size;j++)
				System.out.printf("%2d ",scoreBoard[i][j]);
			System.out.println(" ");
		}
	}
//Creates and reboot the Logic Board and the Graphic Board,starts ths game with the StartPlayer
	public void initBoard(){
		gBoard = new MyButton[size][size];
		lBoard = new int[size][size];
		scoreBoard=new int[size][size];
		initScoreBoard();
		setLayout(new GridLayout(size,size));
		printScoreBoard();
		for( int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				lBoard[i][j]=0;
				ImageIcon icon = new ImageIcon("free.gif");
				Image img = icon.getImage();
				gBoard[i][j]= new MyButton(img)	;
				gBoard[i][j].addActionListener(new AL(i,j));
				add(gBoard[i][j]);	
			}	
		}
		turn=startPlayer;
		
		if( gameType==2 && startPlayer==computer)
		{
			flag=true;
			computerSmart();
			flag=false;
		}	
	}
//Action listner for all the buttons,for human player
	class AL implements ActionListener{
		private int row;
		private int col;
		
		public AL(int row, int col){
			this.row=row;
			this.col=col;
		}
		public void actionPerformed(ActionEvent e){
			ImageIcon icon;
			Image img ;
			MyButton b=(MyButton)e.getSource();
			if(!flag&&turn==human){
		                if( lBoard[row][col]==0){ // free cell
						lBoard[row][col]=human;
						icon=new ImageIcon("cross.gif");
						img=icon.getImage();
						b.setImg(img);
						b.repaint();
						mainStack.push(new Point(row,col));
					    turn=3-turn;
					    gBoard[row][col].setEnabled(false);
					    if(gameType==2)
					    	flag=true;
						if(isThereWinner(human,row,col))
						{
							  XOPre.this.setVisible(false); // Clears game frame
							   //Change start player every game
							   startPlayer=3-startPlayer;
							   new XOPre();// Creates new game
						}           
						else
						if( isBoardFull())
						    endStartGame();
		                }
				}
			if(!flag&&gameType==1 && turn==human2){
                if( lBoard[row][col]==0){ // free cell
				lBoard[row][col]=human2;
				icon=new ImageIcon("not.gif");
				img=icon.getImage();
				b.setImg(img);
				b.repaint();
				mainStack.push(new Point(row,col));
			    turn=3-turn;
			    gBoard[row][col].setEnabled(false);
			    if(gameType==2)
			    	flag=true;
				if(isThereWinner(human2,row,col))
				{
					  XOPre.this.setVisible(false); // Clears game frame
					   //Change start player every game
					   startPlayer=3-startPlayer;
					   new XOPre();// Creates new game
				}           
				else
				if( isBoardFull())
				    endStartGame();
                }
		}
			if(flag&&gameType==2)
			{
				new Thread(new Runnable(){
					public void run(){
						try{
							Thread.sleep(1000);
							computerSmart();
							flag=false;
						}catch(InterruptedException ex){}
					}
				}).start();
			}
			while(!(undoStack.isEmpty()))
				undoStack.pop();
			}
	}
//Checks if board is full
	public boolean isBoardFull(){
		for( int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				if( lBoard[i][j]==0)
					return false;
			}
		}
		return true;
	}
//The function that contain all the algorithms that the computer player can play with
	public void computerSmart()
	{
		if(flag)
		{
		int i,j,max=Integer.MIN_VALUE,g;
		boolean done=false;
		ImageIcon icon;
		Image img ;
		switch(gameDiffuculty)
		{
		case 1://depth=1;
		//max=alphaBeta(depth, computer, Integer.MIN_VALUE, Integer.MAX_VALUE);
					for(i=0;i<size;i++)
					{
						for(j=0;j<size;j++)
						{
							if(lBoard[i][j]==0)
							{
								lBoard[i][j]=2;
								g=gradeBoard();
								if(max<g)
								{
									max=g;
									bestRow=i;
									bestCol=j;
								}
								lBoard[i][j]=0;
							}
						}
					}
					break;
		case 2:depth=3;
			max=negaMax(depth,computer);
				break;
		case 3:depth=3;
			max=alphaBeta(depth, computer, Integer.MIN_VALUE, Integer.MAX_VALUE);
				break;
		}
		if(lBoard[bestRow][bestCol]==0){
			lBoard[bestRow][bestCol]=computer;
			icon=new ImageIcon("not.gif");
			img=icon.getImage();
			gBoard[bestRow][bestCol].setImg(img);
			gBoard[bestRow][bestCol].repaint();
			gBoard[bestRow][bestCol].setEnabled(false);
			mainStack.push(new Point(bestRow,bestCol));
			turn=3-turn;
			done=true;
		}
		if(isThereWinner(computer,bestRow,bestCol))
		{
			  XOPre.this.setVisible(false); // Clears game frame
			   //Change start player every game
			   startPlayer=3-startPlayer;
			   new XOPre();// Creates new game
		}           
		else
		if( isBoardFull())
		    endStartGame();
		}
	}
//Start the game in a new board
	public void endStartGame (){
		   if(isBoardFull())
		   {
			   JOptionPane.showMessageDialog(XOPre.this,"Full Board");
			   startPlayer=3-startPlayer;
		   }
		   else
		   {
			   JOptionPane.showMessageDialog(XOPre.this,"new Board");
			   startPlayer=turn;
		   		
		   }
		   		XOPre.this.setVisible(false); // Clears game frame
		   		
		   new XOPre();// Creates new game
	}
//A function that opreates all the checking functions of the game is there a winner
	public boolean isThereWinner(int p,int row,int col)
	{
		if (!CheckLines(p,row,col)&&!CheckRows(p,row,col)&&!CheckAlchson1(p,row,col)&&!CheckAlchson2(p,row,col))
			return false;
		return true;
	}
//Check line victory series
	public  boolean CheckLines (int player, int r, int c){
		int i=1, counter=1,j;
		Point p1,p2;
		ImageIcon icon;
		Image img ;
		for(; i<m &&
      		 c+i<size && lBoard[r][c+i]==player; i++)
		{
				counter++;
				
		}
		p1=new Point(r,c+i);
		for(i=1; i<m && counter<m &&
				 c-i>=0 && lBoard[r][c-i]==player; i++)
		{
				counter++;
		}
		p2=new Point(r,c-i);
		if(counter==m)
		{
			switch(player)
			{
			case 1:for(j=p2.y+1;j<p1.y;j++)
					{
						icon=new ImageIcon("cross1.gif");
						img=icon.getImage();
						gBoard[r][j].setImg(img);
						gBoard[r][j].repaint();
					}
					JOptionPane.showMessageDialog(XOPre.this,"player1 won!");
					break;
			case 2:
					for(j=p2.y+1;j<p1.y;j++)
					{
						icon=new ImageIcon("not1.gif");
						img=icon.getImage();
						gBoard[r][j].setImg(img);
						gBoard[r][j].repaint();
					}
					JOptionPane.showMessageDialog(XOPre.this,"player2 won!");
					break;
			}
		 return true;
		}
		return false;
	
	}
//Check row victory series	  	
	 public  boolean CheckRows (int player, int r, int c){
		int i=1, counter=1,j;
		Point p1,p2;
		ImageIcon icon;
		Image img ;
		for( i=1; i< m && 
      		  r+i< size && lBoard[r+i][c]==player; i++)

				counter++;
		p1=new Point(r+i,c);
		for( i=1; i< m &&  counter<m &&
		  r-i >=0 && lBoard[r-i][c]==player; i++)

				counter++;
		p2=new Point(r-i,c);
		if(counter==m)
		{
			switch(player)
			{
			case 1:for(j=p2.x+1;j<p1.x;j++)
					{
						icon=new ImageIcon("cross1.gif");
						img=icon.getImage();
						gBoard[j][c].setImg(img);
						gBoard[j][c].repaint();
					}
					JOptionPane.showMessageDialog(XOPre.this,"player1 won!");
					break;
			case 2:
					for(j=p2.x+1;j<p1.x;j++)
					{
						icon=new ImageIcon("not1.gif");
						img=icon.getImage();
						gBoard[j][c].setImg(img);
						gBoard[j][c].repaint();
					}
					JOptionPane.showMessageDialog(XOPre.this,"player2 won!");
					break;
			}
		 return true;
		}
		return false;
		
	}
//Check Main Diagonal victory series
	 public boolean CheckAlchson1(int p,int row,int col)
	{
		int i=1, cnt1=1,j;
		Point p1,p2;
		ImageIcon icon;
		Image img ;
		
		for(i=1;i<m && row+i<size && col+i<size && lBoard[row+i][col+i]==p;i++)
			cnt1++;
		p1=new Point(row+i,col+i);
		for(i=1;i<m && cnt1<m && row-i>=0&&col-i>=0 && lBoard[row-i][col-i]==p;i++)
			cnt1++;
		p2=new Point(row-i,col-i);
		if(cnt1==m)
		{
			switch(p)
			{
			case 1:
					for(i=p2.x+1,j=p2.y+1;i<p1.x && j<p1.y;i++,j++)
					{
						icon=new ImageIcon("cross1.gif");
						img=icon.getImage();
						gBoard[i][j].setImg(img);
						gBoard[i][j].repaint();
					}
					JOptionPane.showMessageDialog(XOPre.this,"player1 won!");
					break;
			case 2:
				for(i=p2.x+1,j=p2.y+1;i<p1.x && j<p1.y;i++,j++)
				{
					icon=new ImageIcon("not1.gif");
					img=icon.getImage();
					gBoard[i][j].setImg(img);
					gBoard[i][j].repaint();
				}
				JOptionPane.showMessageDialog(XOPre.this,"player2 won!");
				break;
			}
		 return true;
		}
		return false;
	}
//Check Secondary Diagonal victory series
	public boolean CheckAlchson2(int p,int row,int col)
	{
		int i=1, cnt1=1,j;
		Point p1,p2;
		ImageIcon icon;
		Image img ;
		for(i=1;i<m && row+i<size && col-i>=0 && lBoard[row+i][col-i]==p;i++)
			cnt1++;
		p1=new Point(row+i,col-i);
		for(i=1;i<m && cnt1<m && row-i>=0&&col+i<size && lBoard[row-i][col+i]==p;i++)
			cnt1++;
		p2=new Point(row-i,col+i);
		if(cnt1==m)
		{
			switch(p)
			{
			case 1:
				for(i=p2.x+1,j=p2.y-1;i<p1.x && j>p1.y;i++,j--)
				{
					icon=new ImageIcon("cross1.gif");
					img=icon.getImage();
					gBoard[i][j].setImg(img);
					gBoard[i][j].repaint();
				}
					JOptionPane.showMessageDialog(XOPre.this,"player1 won!");
					break;
			case 2:
					for(i=p2.x+1,j=p2.y-1;i<p1.x && j>p1.y;i++,j--)
					{
						icon=new ImageIcon("not1.gif");
						img=icon.getImage();
						gBoard[i][j].setImg(img);
						gBoard[i][j].repaint();
					}
					JOptionPane.showMessageDialog(XOPre.this,"player2 won!");
					break;
			}
		 return true;
		}
		return false;
	}
//Gives a grade to a cell in position r,c
	int gradeCell(int r,int c)
	{
		int stepCount,mark1=0,mark2=0,i=0,j=0,xdir,ydir;
		int monim[]=new int [3];
		for(xdir=-1;xdir<2;xdir++)
		{
			for(ydir=-1;ydir<2;ydir++)
			{
				stepCount=0;
				monim[0]=monim[1]=monim[2]=0;
				i=r;
				j=c;
				if(!(xdir==0&&ydir==0))
				{
					while(i>=0 && i<size && j>=0 && j<size && stepCount<m)
					{
						monim[lBoard[i][j]]++;
						stepCount++;
						i=r+stepCount*ydir;
						j=c+stepCount*xdir;
					}
					
					if(stepCount==m)
					{
						if(monim[1]>0&&monim[2]==0)
						{
							mark1=mark1+(int)Math.pow(10,monim[1])*scoreBoard[r][c];
							if(monim[1]==m-2){
								if(i<size && j<size && i>=0 && j>=0 && lBoard[r][c]==0 && lBoard[i][j]==0)
									mark1+=(int)Math.pow(10, m);
							}
							if(monim[1]==m-1){
								if(i<size&&j<size&&i>=0&&j>=0&& lBoard[r][c]==0 )
									mark1+=((int)Math.pow(10, m+1));
							}
						}
						if( monim[2]>0 && monim[1]==0){  
							mark2=mark2+(int)Math.pow(10,monim[2])*scoreBoard[r][c];
						
						 //31-3-2013
						 // If computer can win, then win!!
						    if( monim[2]==m)// Ojo 
							mark2+=(int)Math.pow(10,m+2);
						    // 31-3-2013
						    // Mind the gap
						    // en lugar de monim[2]==winSize-3
						    // this makes alpha beta better...
						    if( monim[2]==m-3){
							  if( lBoard[r][c]==0 &&
								i>=0 && i<size &&
							    j>=0 && j<size  &&
							    lBoard[i][j]==0 )
									
								mark2+=(int)Math.pow(10,m-1)/2;		
							}
						 }
					}
				}
			}
		}
		return(mark2-mark1);
	}
//Gives a grade to a cell in position r,c(for alpha beta algorithm)
	int  gradeCell2 ( int r, int c) {

	     	int stepCount, mark1=0, mark2=0, i , j;
	     	int [] monim = new int[3];//index 0 free, 
		                              //index 1 player1(human)
									  //index 2 player2(computer)

	     	for( int xdir=-1; xdir<2; xdir++){
				for( int ydir=-1; ydir<2; ydir++){
					stepCount=0;
					monim[0]=monim[1]=monim[2]=0;
					
					i=r;
					j=c;
					
					if( !(xdir==0 && ydir==0) ){
					
					 while( i>=0 && i<size &&
							   j>=0 && j<size && 
							   stepCount<m ) {
						 
						 monim[lBoard[i][j]]++;
						 stepCount++;
						 i=r+stepCount*ydir;
						 j=c+stepCount*xdir;	 
					 }
					 
				  
					// Can be completed to a win sequence to player1
					if( stepCount==m){
					 if( monim[1]>0 && monim[2]==0){  
						mark1=mark1+(int)Math.pow(10,monim[1])*scoreBoard[r][c];
						
						
						
					     //19-3-2013
					 	// If human can win block him
					     if(  monim[1]==m-1){
					    	 mark1+=(int)Math.pow(10,m+1);
					    	 
					     }
					     
					     
						// Free both extremes can assure can give human victory 
					    // in two turns
						// like   -XXX--
					     
						if( monim[1]==m-2){
							if( lBoard[r][c]==0 &&
								i>=0 && i<size &&
							    j>=0 && j<size  &&
							    lBoard[i][j]==0 )
									
								   mark1+=(int)Math.pow(10,m);		
							}
							
						  }
					   
					     
					    
					 	
					  }
					// Can be completed to a win sequence to player2
					if( stepCount==m){
						if( monim[2]>0 && monim[1]==0){  
							mark2=mark2+(int)Math.pow(10,monim[2])*scoreBoard[r][c];
						
						 //31-3-2013
						 // If computer can win, then win!!
						    if( monim[2]==m)// Ojo 
							mark2+=(int)Math.pow(10,m+2);
						    // 31-3-2013
						    // Mind the gap
						    // en lugar de monim[2]==winSize-3
						    // this makes alpha beta better...
						    if( monim[2]==m-3){
							  if( lBoard[r][c]==0 &&
								i>=0 && i<size &&
							    j>=0 && j<size  &&
							    lBoard[i][j]==0 )
									
								mark2+=(int)Math.pow(10,m-1)/2;		
							}
						 }
					  }
					}
				}
	     	}
	      
	     	return( mark2-mark1);
		 }
//Gives grade to the entire board	
	public int gradeBoard()
	{
		int i,j,mark=0;
		for(i=0;i<size;i++)
			for(j=0;j<size;j++)
				mark+=gradeCell(i,j);
		return mark;
	}
//Gives grade to the entire board(for alpha beta algorithm)	
	public int gradeBoard2()
	{
		int i,j,mark=0;
		for(i=0;i<size;i++)
			for(j=0;j<size;j++)
				mark+=gradeCell2(i,j);
		return mark;
	}
//negaMax/Mini max algorithm	
	public int negaMax(int depth,int turn)
	{
		int best=Integer.MIN_VALUE;
		int val;
		int i,j;
		if(depth==0)
			return -gradeBoard();
		for(i=0;i<size;i++)
		{
			for(j=0;j<size;j++)
			{
				if(lBoard[i][j]==0)
				{
					lBoard[i][j]=turn;
					val=-negaMax(depth-1,3-turn);
					lBoard[i][j]=0;
					if(val>best)
					{
						best=val;
						if(depth==3)
						{
							bestRow=i;
							bestCol=j;
							
						}
					}
				}
			}
		}
		return(best);
	}
//Alpha Beta algorithm	
	public int alphaBeta(int depth,int turn,int alpha,int beta)
	{
		int val;
		int i,j;
		if(depth==0)
			return -gradeBoard2();
		for(i=0;i<size;i++)
		{
			for(j=0;j<size;j++)
			{
				if(lBoard[i][j]==0)
				{
					lBoard[i][j]=turn;
					val=-alphaBeta(depth-1,3-turn,-beta,-alpha);
					lBoard[i][j]=0;
					if(val>=beta && depth<this.depth)
						return beta;
				if(val>alpha)
				{
					alpha=val;
					bestRow=i;
					bestCol=j;
				}
			}
		}
		}
		return alpha;
	}
//Undo function	
	public void Undo()
	{
		if(!(mainStack.isEmpty()))
				{
					Point p=mainStack.pop();
					if(!(mainStack.isEmpty()))
					{
						Point p1=mainStack.pop();
						undoStack.push(p);
						undoStack.push(p1);
						lBoard[p.x][p.y]=0;
						lBoard[p1.x][p1.y]=0;
						ImageIcon icon=new ImageIcon("free.gif");
						Image img=icon.getImage();
						gBoard[p.x][p.y].setImg(img);
						gBoard[p.x][p.y].repaint();
						gBoard[p.x][p.y].setEnabled(true);
						gBoard[p1.x][p1.y].setImg(img);
						gBoard[p1.x][p1.y].repaint();
						gBoard[p1.x][p1.y].setEnabled(true);
					}
				}
	}
//Re-do function	
	public void Redo()
	{
		ImageIcon icon;
		Image img=null;
		ImageIcon icon1;
		Image img1=null;
		if(!(undoStack.isEmpty()))
		{
			Point p=undoStack.pop();
			Point p1=undoStack.pop();
			lBoard[p.x][p.y]=turn;
			lBoard[p1.x][p1.x]=3-turn;
			switch(turn)
			{
					case 1: icon=new ImageIcon("cross.gif");
							img=icon.getImage();
							icon1=new ImageIcon("not.gif");
							img1=icon1.getImage();
							break;
					case 2: icon=new ImageIcon("not.gif");
							img=icon.getImage();
							icon1=new ImageIcon("cross.gif");
							img1=icon1.getImage();
							break;
			}
			gBoard[p.x][p.y].setImg(img);
			gBoard[p1.x][p1.y].setImg(img1);
			gBoard[p1.x][p1.y].repaint();
			gBoard[p1.x][p1.y].setEnabled(false);
			gBoard[p.x][p.y].setEnabled(false);
		}
	}
//Hint function
	public void Hint(int turn)
	{
		int i,j,max=Integer.MIN_VALUE,g;
		ImageIcon icon;
		Image img ;
		for(i=0;i<size;i++)
		{
			for(j=0;j<size;j++)
			{
				if(lBoard[i][j]==0)
				{
					lBoard[i][j]=2;
					g=gradeBoard();
					if(max<g)
					{
						max=g;
						bestRow=i;
						bestCol=j;
					}
					lBoard[i][j]=0;
				}
			}
		}
		
		if(lBoard[bestRow][bestCol]==0){
			icon=new ImageIcon("Banner.gif");
			img=icon.getImage();
			gBoard[bestRow][bestCol].setImg(img);
			gBoard[bestRow][bestCol].repaint();
			gBoard[bestRow][bestCol].setEnabled(false);
			new Thread(new Runnable(){
				public void run(){
					ImageIcon icon;
					Image img ;
					try{
						Thread.sleep(1500);
						icon=new ImageIcon("free.gif");
						img=icon.getImage();
						gBoard[bestRow][bestCol].setImg(img);
						gBoard[bestRow][bestCol].repaint();
						gBoard[bestRow][bestCol].setEnabled(true);
					}catch(InterruptedException ex){}
				}
			}).start();
		}
	}
	//Main function
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new XOPre();
	}
}
