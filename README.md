# CSCI 136 Final Project: Massachusetts Rideshare Data Heatmap
By Hiroshi Tokunaga and Michael Dai for Professor Katie Keith's CSCI 136 class at Williams College

# Overview
We designed a specialized point region quadtree to assist in generating a heatmap representation of Massachusetts rideshare pickup locations. Our program processes data from the following [dataset](https://www.mass.gov/info-details/2023-rideshare-data-report), which, among other attributes, stores the number of rideshare pickups per city. To allow data to be stored in our quadtree, we converted the city name to geographical coordinates. We chose a quadtree for its space-efficient storage of 2d points, especially in large, sparsely populated 2d planes, which characterizes our dataset. Using a quadtree allows for greater savings for future applications, for example, if we decide to input higher-resolution data.

## Functions
Upon opening the program, users are prompted to set the depth of the heatmap, which determines the size which the heatmap cells render. 

Users can click on tiles to display more information on the number of rides and towns contained in the tile.

![Alt text](https://media.discordapp.net/attachments/511018252181176330/1374138015747674222/HeatmapExample.png?ex=682cf541&is=682ba3c1&hm=24f7b0134a175a2200f9bf01c37f96d3a2dce58672bddec5bfffe14412f205f4&=&format=webp&quality=lossless&width=3200&height=1460)

Tip: feel free to try out all the different render depths from 3 to 6. 

### To run the program

Create bin directory to store compiled files:

`mkdir bin`

Compile using the following command:

`javac -d bin *.java`

Then run HeatMap.java:

`java -cp bin DaiToku.HeatMap`

## Limitations
The quadtree is designed only to take rideshare data stored in a RidePt (ride point), which stores the coordinates of a ride location and the number of rides at that pickup spot. In addition, since data collection was not our primary objective, we did not allocate significant time to researching datasets. 

The dataset only provides data at the municipal level, meaning we have inherently lower resolution compared to data at the precise location level. Thus, the data structure and methods we implemented has the potential to be even more useful when we have precise location data, which is what we plan to do as next steps in developing a rideshare app.

## Program overview:

### RidePt.java

Stores ride location data and the number of rides at a pickup location

### Region.java

Represents regions/nodes in a quadtree.  

### Quadtree.java

Creates and manipulates region objects to build and manage a quadtree. 

### QuadtreeAdapter.java

Takes coordinates and ride data from rides.csv and converts it into a quadtree.

### Heatmap.java

Creates heatmap using data stored in quadtree. This quadtree is created using QuadtreeAdapter.


