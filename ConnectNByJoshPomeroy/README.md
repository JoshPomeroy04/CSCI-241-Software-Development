ConnectN Game by Josh Pomeroy
\
\
\
For versions 1.0 and 1.1 if you get a file error change lines 34 and 35 of GameBoard.java:

URL filePath = GameBoard.class.getResource("CurrentGameBoard.txt");  
File currentBoard = new File(filePath.getFile());

to:
File currentBoard = new File("Your/File/Path/To/CurrentGameBoard.txt");
\
\
\
lines 102 and 103 of GameBoard.java:

URL getBoard = GameBoard.class.getResource(Main.getGameMode()+"GameBoard.txt");\
File defaultBoard = new File(getBoard.getFile());

to:
File defaultBoard = new File("Your/Directory/Path/To/src/edu/wm/cs/cs301/connectn/"+Main.getGameMode()+"GameBoard.txt");
\
\
\
lines 127 and 128 of GameBoard.java:

File curBoard = new File(filePath.getFile());\
boardScanner = new Scanner(curBoard);

to:
boardScanner = new Scanner(currentBoard);
\
\
\
lines 144 and 145 of GameBoard.java:

File curBoard = new File(filePath.getFile());\
boardScanner = new Scanner(curBoard);

to:
boardScanner = new Scanner(currentBoard);
