import javax.swing.*;//new window for painting
import java.awt.event.*;//Keyboard input
import java.io.*;
import java.awt.*;//Color
import java.util.Scanner;
import java.util.ArrayList;

public class MazeProject extends JPanel implements KeyListener
{
	String[][] maze;
	JFrame frame;
	int dim = 15;
	int hRow, hCol, endRow, endCol;
	char hDir = 'E';
	boolean draw3D = false;
	ArrayList<Wall> walls;

	public MazeProject()
	{
		frame = new JFrame("Maze Program");
		frame.setSize(1200, 900);
		frame.add(this);
		frame.addKeyListener(this);

		loadMaze();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, frame.getWidth(), frame.getHeight());

		if(!draw3D)
		{
			for(int r = 0; r < maze.length; r++)
			{
				for(int c = 0; c < maze[r].length; c++)
				{
					if(maze[r][c].equals("#"))
					{
						g2.setColor(Color.RED);
						g2.fillRect(c*dim, r*dim, dim, dim);
					}
					if(maze[r][c].equals("E"))
					{
						g2.setColor(Color.WHITE);
						g2.fillRect(c*dim, r*dim, dim, dim);
					}
					if(maze[r][c].equals("X"))
					{
						g2.setColor(Color.GREEN);
						g2.fillRect(c*dim, r*dim, dim, dim);
					}
					g2.setColor(Color.BLACK);
					g2.drawRect(c*dim, r*dim, dim, dim);
				}
			}
			g2.setColor(Color.MAGENTA);
			g2.fill(new Rectangle(hCol*dim, hRow*dim, dim, dim));
			g2.setStroke(new BasicStroke(5));
			g2.setColor(Color.GREEN);
			g2.draw(new Rectangle(hCol*dim, hRow*dim, dim, dim));
		}
		else
		{
			System.out.println(walls.size());
			for(Wall wall: walls)
			{
				g2.setColor(wall.getColor());
				g2.fillPolygon(wall.getPoly());
				g2.setColor(Color.BLACK);
				g2.drawPolygon(wall.getPoly());
			}
			g.drawString(""+hDir,600,400);
		}
	}

	public void keyPressed(KeyEvent e)
	{
		if(draw3D)
		{
			move3D(e.getKeyCode());
		}
		else
		{
			move2D(e.getKeyCode());
		}

		if(e.getKeyCode() == 32)
		{
			draw3D = !draw3D;
		}
		if(draw3D)
			createWalls();

		repaint();
	}
	public void createWalls()
	{
		walls = new ArrayList<Wall>();
		for(int x = 0; x<5; x++)
		{
			switch(hDir)
			{
				case 'E':
					try
					{
						if(maze[hRow-1][hCol+x].equals("#"))
							walls.add(getLeft(x));
						if(maze[hRow+1][hCol+x].equals("#"))
							walls.add(getRight(x));


					}catch(ArrayIndexOutOfBoundsException e){}
					break;
				case 'S':
					try
					{
						if(maze[hRow+x][hCol+1].equals("#"))
							walls.add(getLeft(x));
						if(maze[hRow+x][hCol-1].equals("#"))
							walls.add(getRight(x));

						//else x=5;
					}catch(ArrayIndexOutOfBoundsException e){}
					break;
				case 'W':
					try
					{

						if(maze[hRow+1][hCol-x].equals("#"))
							walls.add(getLeft(x));
						if(maze[hRow-1][hCol-x].equals("#"))
							walls.add(getRight(x));

					}catch(ArrayIndexOutOfBoundsException e){}
					break;
				case 'N':
					try
					{

						if(maze[hRow-x][hCol-1].equals("#"))
							walls.add(getLeft(x));
						if(maze[hRow-x][hCol+1].equals("#"))
							walls.add(getRight(x));

					}catch(ArrayIndexOutOfBoundsException e){}
					break;

			}
		}

		for(int i = 0; i < 5; i++)
		{
			switch(hDir)
			{
				case 'E':
					try
					{
						if(maze[hRow][hCol+i].equals("#"))
						{
							walls.add(getFront(i));
							i = 5;
						}

					}catch(ArrayIndexOutOfBoundsException e){}
					break;
				case 'S':
					try
					{
						if(maze[hRow+i][hCol].equals("#"))
						{
							walls.add(getFront(i));
							i = 5;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					break;
				case 'W':
					try
					{

						if(maze[hRow][hCol-i].equals("#"))
						{
							walls.add(getFront(i));
							i = 5;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					break;
				case 'N':
					try
					{

						if(maze[hRow-i][hCol].equals("#"))
						{
							walls.add(getFront(i));
							i = 5;
						}
					}catch(ArrayIndexOutOfBoundsException e){}
					break;
			}
		}
	}

	public Wall getFront(int x)
	{
		int[] xPoints = {100+50*x, 1000-50*x, 1000-50*x, 100+50*x};
		int[] yPoints = {100+50*x, 100+50*x, 800-50*x, 800-50*x};
		return new Wall(xPoints, yPoints, 255, 255, 255, "Front", 100, Color.GRAY);
	}

	public Wall getLeft(int x)
	{
		int[] xPoints = {100+50*x, 150+50*x, 150+50*x, 100+50*x};
		int[] yPoints = {100+50*x, 150+50*x, 750-50*x, 800-50*x};
		return new Wall(xPoints, yPoints, 255, 255, 255, "Left", 100, Color.GRAY);
	}
	public Wall getRight(int x)
	{
			int[] xPoints = {1000-50*x, 950-50*x, 950-50*x, 1000-50*x};
			int[] yPoints = { 100+50*x, 150+50*x, 750-50*x, 800-50*x};
			return new Wall(xPoints, yPoints, 255, 255, 255, "Right", 100, Color.GRAY);
	}

	public void keyReleased(KeyEvent e)
	{

	}
	public void keyTyped(KeyEvent e)
	{

	}

	public void move3D(int key)
	{
		if(key == 38)
		{
			try
			{
				switch(hDir)
				{
					case 'N':
						if(maze[hRow-1][hCol].equals(" "))
							hRow--;
						break;
					case 'E':
						if(maze[hRow][hCol+1].equals(" "))
							hCol++;
						break;
					case 'S':
						if(maze[hRow+1][hCol].equals(" "))
							hRow++;
						break;
					case 'W':
						if(maze[hRow][hCol-1].equals(" "))
							hCol--;
						break;
				}
			}catch(ArrayIndexOutOfBoundsException e)
			{

			}
		}
		if(key == 37)
		{
			switch(hDir)
			{
				case 'N':
					hDir = 'W';
					break;
				case 'E':
					hDir = 'N';
					break;
				case 'S':
					hDir = 'E';
					break;
				case 'W':
					hDir = 'S';
					break;
			}
		}
		if(key == 39)
		{
			switch(hDir)
			{
				case 'N':
					hDir = 'E';
					break;
				case 'E':
					hDir = 'S';
					break;
				case 'S':
					hDir = 'W';
					break;
				case 'W':
					hDir = 'N';
					break;
			}
		}
	}
		public void move2D(int key)
		{
			if(key == 38)
			{
				try
				{
					if(maze[hRow-1][hCol].equals(" "))
						hRow--;
				}catch(ArrayIndexOutOfBoundsException e)
				{

				}
			}
			if(key == 40)
			{
				try
				{
					if(maze[hRow+1][hCol].equals(" "))
						hRow++;
				}catch(ArrayIndexOutOfBoundsException e)
				{

				}
			}
			if(key == 39)
			{
				try
				{
					if(maze[hRow][hCol+1].equals(" "))
						hCol++;
				}catch(ArrayIndexOutOfBoundsException e)
				{

				}
			}
			if(key == 37)
			{
				try
				{
					if(maze[hRow][hCol-1].equals(" "))
						hCol--;
				}catch(ArrayIndexOutOfBoundsException e)
				{

				}
			}
	}

	public void loadMaze()
	{
		File file=new File("MazeProject.txt");

		try{
			BufferedReader input=new BufferedReader(new FileReader(file));
			String text;
			int row = 0;

			maze = new String[36][];

			while((text=input.readLine())!=null)
			{
				System.out.print(text);
				String[] line = text.split("");
				maze[row] = line;

				if(text.contains("E"))
				{
					hRow = row;
					hCol = text.indexOf("E");
				}
				if(text.contains("X"))
				{
					endRow = row;
					endCol = text.indexOf("X");
				}

				row++;
			}
			for(int  r= 0; r < maze.length; r++)
			{
				for(int c = 0; c < maze[r].length; c++)
				{
					System.out.print(maze[r][c]);
				}
				System.out.println();
			}
		}catch(IOException ee){

		}
	}

	public class Wall
	{
		private int[] y;
		private int[] x;
		private int r;
		private int g;
		private int b;
		private String type;
		private int dist;
		private Color color;

		public Wall(int[] x, int[] y, int r, int g, int b, String type, int dist, Color color)
		{
			this.x = x;
			this.y = y;
			this.r = r;
			this.g = g;
			this.b = b;
			this.type = type;
			this.dist = dist;
			this.color = color;
		}

		public void setColor(Color color)
		{
			this.color = color;
		}
		public Color getBreadCrumb()
		{
			return color;
		}
		public void setType(String type)
		{
			this.type = type;
		}
		public String getType()
		{
			return type;
		}
		public Polygon getPoly()
		{
			return new Polygon(x, y, x.length);
		}
		public int getX()
		{
			return x[0];
		}
		public int getY()
		{
			return y[0];
		}
		public Color getColor()
		{
			return color;
		}
	}


	public static void main(String[]args)
	{
		MazeProject app = new MazeProject();
	}
}