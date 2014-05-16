Research
========

Research

-Abstract-
	This application is a testing environment for robotic navigation algorithms against one another in a specific environment while allowing the user to define the parameters of the experiment without the need to change hardcoded values.

-Environment Design-
	The application is a window with four tabs panes and three buttons. One pane contains a view of the randomly generated map that the experiment will utilize while the other three panes each contain a view of a different algorithm attempting to navigate the map. Each robotic agent is represented as a single red pixel and its sensors are represented as green wedges. 
	Obstacles in the environment are displayed as black and the viewed area of the map appears as white while the unviewed area is grey. The three buttons at the bottom are mostly self-explanatory, but there are a few features that are not obvious. The middle button will change the from 10 ms to 1ms and back again, while the stop button will pause the experiment and write an output file containing the experiment parameters and the data gathered.

-Random Movement-
	The random movement algorithm acts as the baseline for this experiment. At any given time, a robotic agent can perform one of three movements. It can turn left, turn right, or move forward. For every time step, every robot in the random movement algorithm will perform one of these movements chosen at random.

-Primary Movement- 
	In the primary movement algorithm, an agent will move forward as long at it can unveil more of the map in doing so. If none of the map can be unveiled, the agent will rotate 45 degrees counter clockwise and reevaluate. If a full 360 degree rotation is made and no single movement will unveil more of the map, the agent determines it is finished. It has no method of choosing a new goal or of finding a path to a goal.

-A*-
	A* is a derivation of Dykstra's least cost algorithm. It is a very mature and well tested algorithm. Given a starting point and a goal point, every acceptable point surrounding the current is assigned a series of costs based on the distance from current, distance to goal, and a heuristic value that calculates the 'cost' to get from current to goal. Each point is assigned a parent, which is the point was was traversed immediately prior. The least cost node is evaluated in the same manner. This process continues until the goal is assigned at parent. At this point a path has been created. To create the path, the parent of the goal is pushed onto a stack, then the parent of the parent of the goal is pushed onto the stack and so on until we reach the starting point. We can now pop the points off of the stack as we traverse the environment.

-Future Improvements-
	There are a number of known bugs with the application. It is my intention to correct them, but as none of the known bugs affect the performance of the algorithms, they are a low priority. 
	One of my major goals was to have every algorithm completely abstracted from the rest of the application. I was successful in this endeavor, but in an attempt to meet a deadline on data collection, some of the abstraction was lost in the displaying of results and report generation. This is my highest priority issue. 
	Also, the UI is created using static values for pixel widths and heights. I would like to implement the UI as ratios of total screen size, but this is a feature that is very low priority.

-Future Implementations-
	Upon inspection of early data collected, the primary movement algorithm out performs the A* algorithm in environments that contain no obstacles. I intend to implement a hybrid of primary movement and A* and compare it against its to parents. 
	Another observations is that the A* algorithm will miss small portions of the map occassionally. The result of this is that after the bulk of the map has been explored, the agents must backtrack and clean up the missed areas. I see to opportunities here for future research. One is the generate more effective goals that would minimize the percentage of the map that is missed on the initial pass. The other would be to implement a method of communication between the agents to allow them to 'call dibs' on a goal, essentially making that goal off limits to the other machines. 
	I would like to implement other navigation algorithms for test in this environment in the future. Some of the algorithms I would like to implement are, Jump Point search, D*, D* Lite, AP Lite.
