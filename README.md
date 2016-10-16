# SuperXO
Final project in Java for Program technician degree at Ort Rehovot College in 2013.

Game Description:

Super XO is a strategy game against the computer.
The game board is 10 X 10.
When you start the game the board will be empty, and as you proceed in the game it will fill in Xs and Os .

Game purpose:

Creating a row of 5 Xs/Os vertically, horizontally or diagonally.

Game process:
Each player chooses a square on the board and place his on it.
Each player will try to block his enemy from creating a 5 in a row and will focus on creating one himself.
Game rules
Each player place his sign in his turn.
You can place a sign only on an empty square.
The game ends when one of the players gets a 5 in arrow of his sign.
Game algorithms 
Scan the board for winning.
Scoring the board from the PC perspective for a result.
Minimax algorithm for choosing the ideal move of the PC and calculating few steps ahead.
Alpha-beta algorithm for choosing the ideal move of the PC and calculating few steps ahead.

Data structures 
Logic board game – matrix of numbers that each presents the status of the square.
Graphic board game – matrix of buttons that each will change to the sign of the player(X OR O) when clicked.
Stack – for saving game moves that were made.
Stack - for saving game moves that were UNDO.

