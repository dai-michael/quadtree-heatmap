# CSCI 136 Final Project: Massachusetts Rideshare Data Heatmap
By Hiroshi Tokunaga and Michael Dai for Professor Katie Keith's CSCI 136 class

# Overview
We designed a specialized point region quadtree to assist in generating a heatmap representation of Massachusetts rideshare pickup locations. Our program processes data from the following [dataset]([url](https://www.mass.gov/info-details/2023-rideshare-data-report)), which, among other factors, stores the number of rideshare pickups per city. To allow data to be stored in our quadtree, we converted the city name to geographical coordinates. We chose a quadtree for its space-efficient storage of 2d points, especially in large, sparsely populated 2d planes, which characterizes our dataset. Using a quadtree allows for greater savings for future applications, for example, if we decide to input higher-resolution data.

Upon opening the program, users are prompted to set the depth of the heatmap, which determines the size which the heatmap cells render. Users can click on tiles to display more information on the number of rides and cities contained in the tile.

### To run the program

Create bin directory to store compiled files:

`mkdir bin`

Compile using the following command:

`javac -d bin *.java`

Then run HeatMap.java:

`java -cp bin DaiToku.HeatMap`

## Limitations
Due to time limitations, our quadtree is designed only to take rideshare data stored in a RidePt (ride point), which stores the coordinates of a ride location and the number of rides at that pickup spot. In addition, since data collection was not our primary objective, we did not allocate significant time to researching datasets. Our dataset only provides data at the municipal level, meaning we do not benefit as much from a quadtree as we would from a more precise dataset. 


<<<<<<< HEAD
explain design here

and limitations
=======
>>>>>>> fe565b96337adbe3d56b9ea4d6a6258755888511
